package com.rezofy.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.adapters.DrawerOptionsAdapter;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.fragments.HomeFragment;
import com.rezofy.views.fragments.MyTripsFragment;

import org.json.JSONObject;
import org.w3c.dom.Text;

//1077859060800-97k2pbtss26n0gvr44g93ppfqaksns75.apps.googleusercontent.com // this is the client id for gplus login
public class HomeActivity extends BaseHomeActivity implements NetworkTask.Result, View.OnClickListener {


    private boolean isPrimaryApp;
    private TextView tvCashBalance, tvCreditBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPrimaryApp = Boolean.parseBoolean(getString(R.string.is_primary_app));
        setDrawerLayout();
        loadDesiredFragment();
        getCashAndCreditBalance(false);

        for(int i = 0; i < 3;){

            i++;
        }

    }

    void setDrawerLayout() {
        super.setDrawerLayout();
        tvCashBalance = (TextView) findViewById(R.id.tvCashBalance);
        tvCreditBalance = (TextView) findViewById(R.id.tvCreditBalance);
        if (!isPrimaryApp || (!AppPreferences.getInstance(HomeActivity.this).getToken().equals("") && !AppPreferences.getInstance(HomeActivity.this).getB2B())) {
            llMyBalance.setVisibility(View.GONE);
            rlBalances.setVisibility(View.GONE);

        } else
            llMyBalance.setVisibility(View.VISIBLE);

        DrawerOptionsAdapter drawerOptionsAdapter = new DrawerOptionsAdapter(this, drawerLayout);
        rcOptions.setAdapter(drawerOptionsAdapter);
    }

    private void loadDesiredFragment() {
        Fragment desiredFragment;
        String tag;
        if (isPrimaryApp) {
            desiredFragment = new HomeFragment();
            tag = Utils.TAG_HOME_FRAGMENT;
            changeFragment(desiredFragment, null, tag);

        } else {
//            desiredFragment = new MyTripsFragment();
//            tag = Utils.TAG_MY_TRIPS_FRAGMENT;
            Intent intent = new Intent(this, MyTripsActivity.class);
            startActivity(intent);
        }

    }

    //Change or Replace a Fragment
    public void changeFragment(Fragment fragment, Bundle data, String fragmentTag) {
        //getSupportFragmentManager().popBackStack(Utils.TAG_HOME_FRAGMENT, 0);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (data != null)
            fragment.setArguments(data);
        fragmentTransaction.replace(R.id.fragment_container, fragment, fragmentTag);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (drawerLayout.isDrawerOpen(Gravity.LEFT))
            drawerLayout.closeDrawer(Gravity.LEFT);
        else if (frag instanceof HomeFragment || frag instanceof MyTripsFragment)
            finish();
//        else if(frag instanceof CountryPackagesFragment)
//            getSupportFragmentManager().popBackStack(Utils.TAG_COUNTRY_PACKAGES_FRAGMENT, 0);
        else
            getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getCashAndCreditBalance(boolean showProgressDialog) {
        if (!AppPreferences.getInstance(HomeActivity.this).getToken().equals("") && AppPreferences.getInstance(HomeActivity.this).getB2B()) {
            if (!Utils.isNetworkAvailable(this))
                Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

            else {
                String url;
                NetworkTask networkTask = new NetworkTask(HomeActivity.this, Utils.CREDIT_BALANCE);
                networkTask.setProgressDialog(showProgressDialog);
                networkTask.setDialogMessage(getString(R.string.please_wait));

                networkTask.exposePostExecute(HomeActivity.this);
                String paramsArray[] = null;

                url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlCreditBalance;
                paramsArray = new String[]{url, null};
                networkTask.execute(paramsArray);
            }
        }
    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {
        try {
            if (object != null && !object.equals("")) {
                if (id == Utils.CREDIT_BALANCE) {
                    JSONObject obj = new JSONObject(object);
                    AppPreferences.getInstance(HomeActivity.this).setCashLimit(obj.getString("cashBalance"));
                    AppPreferences.getInstance(HomeActivity.this).setCreditLimit(obj.getString("creditBalance"));
                    AppPreferences.getInstance(this).setIsExpired(obj.getBoolean("isExprired"));
                    tvCashBalance.setText(UIUtils.getFareToDisplay(this, AppPreferences.getInstance(this).getCashLimit()));
                    StringBuilder sb = new StringBuilder();
                    sb.append(UIUtils.getFareToDisplay(this, AppPreferences.getInstance(this).getCreditLimit()));
                    if (AppPreferences.getInstance(this).getIsExpired())
                        sb.append(getString(R.string.validity_expired));
                    tvCreditBalance.setText(sb.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.home:
                drawerLayout.closeDrawer(Gravity.LEFT);
                getSupportFragmentManager().popBackStack(Utils.TAG_HOME_FRAGMENT, 0);
                break;
            case R.id.myTrip:
                drawerLayout.closeDrawer(Gravity.LEFT);
                if (isPrimaryApp) {
                    Intent intent = new Intent(HomeActivity.this, MyTripsActivity.class);
                    startActivity(intent);

                } else {
                    if (!getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(Utils.TAG_MY_TRIPS_FRAGMENT))
                        loadDesiredFragment();
                }
                break;

            case R.id.my_balance:
                if (rlBalances.getVisibility() == View.VISIBLE)
                    rlBalances.setVisibility(View.GONE);
                else
                    rlBalances.setVisibility(View.VISIBLE);
                break;

            case R.id.refresh:
                getCashAndCreditBalance(true);
                break;
        }
    }


}