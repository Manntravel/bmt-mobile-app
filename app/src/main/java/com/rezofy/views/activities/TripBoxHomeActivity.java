package com.rezofy.views.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;

import com.rezofy.R;
import com.rezofy.adapters.TripBoxMenuAdapter;
import com.rezofy.utils.Utils;
import com.rezofy.views.fragments.TripBoxFragment;

/**
 * Created by anuj on 6/2/17.
 */
public class TripBoxHomeActivity extends BaseHomeActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawerLayout();
        loadDesiredFragment();
    }

    void setDrawerLayout() {
        super.setDrawerLayout();
        myTrip.setVisibility(View.GONE);
        llMyBalance.setVisibility(View.GONE);
        TripBoxMenuAdapter tripBoxMenuAdapter = new TripBoxMenuAdapter(this, Utils.TAG_TRIPBOX_HOME_ACTIVITY, drawerLayout);
        rcOptions.setAdapter(tripBoxMenuAdapter);
        //rcOptions.scrollToPosition(0);
    }

    private void loadDesiredFragment() {
        Fragment desiredFragment;
        String tag;
            desiredFragment = new TripBoxFragment();
            tag = Utils.TAG_TRIPBOX_FRAGMENT;

        changeFragment(desiredFragment, null, tag);
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
//        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        if (drawerLayout.isDrawerOpen(Gravity.LEFT))
//            drawerLayout.closeDrawer(Gravity.LEFT);
//        else if (frag instanceof HomeFragment || frag instanceof MyTripsFragment)
//            finish();
////        else if(frag instanceof CountryPackagesFragment)
////            getSupportFragmentManager().popBackStack(Utils.TAG_COUNTRY_PACKAGES_FRAGMENT, 0);
//        else
        if(getSupportFragmentManager().getBackStackEntryCount()>1)
            getSupportFragmentManager().popBackStack();
        else
            finish();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.home:
                drawerLayout.closeDrawer(Gravity.LEFT);
                getSupportFragmentManager().popBackStack(Utils.TAG_TRIPBOX_FRAGMENT, 0);
                break;
        }
    }
}
