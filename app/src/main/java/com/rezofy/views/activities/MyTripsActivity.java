package com.rezofy.views.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rezofy.R;

import com.rezofy.adapters.PaymentMethodAdapter;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.interfaces.FragmentLifeCycle;
import com.rezofy.interfaces.OnLoadListener;
import com.rezofy.interfaces.SearchListener;
import com.rezofy.models.response_models.MyTrip;
import com.rezofy.models.response_models.MyTripResponse;
import com.rezofy.models.response_models.PaymentMethodsResponse;
import com.rezofy.models.response_models.TripList;
import com.rezofy.models.response_models.TripRecord;
import com.rezofy.models.response_models.ViewTicketResponse;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.requests.Requests;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.custom_views.IconTextView;

import com.rezofy.views.fragments.MyTripFragment;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by Linchpin66 on 26-01-2016.
 */
public class MyTripsActivity extends AppCompatActivity implements View.OnClickListener, NetworkTask.Result {

    private ViewPagerAdapter adapter;
    private IconTextView iTVMenu;
    private IconTextView iTVSearch;
    private EditText etSearch;
    private RecyclerView myTripRV;
    private int TRIP_RECORD = 1;
    private int PAYMENT_METHODS = 2;

    private int pageNo = 1;


    private TextView tvTitle;
    private ImageView btnFloating;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public String bookingRefNo;
    private TableRow tabBarBackground;
    private RecyclerView rvPaymentMethods;
    private RelativeLayout rlOverlay;
    private String[] tabIcons = {
            "?",//all icon
            "o",//flight icon
            "f",//hotel icon
            "g",// bus icon
            ":",//transfer icon
            ";",//train icon
            "7"//package icon

    };

    private String[] pageTitles = {
            "ALL",
            "FLIGHTS",
            "HOTELS",
            "BUSES",
            "TRANSFER",
            "TRAIN",
            "PACKAGE"

    };
    private String[] types = {
            "",
            "FLIGHT",
            "HOTEL",
            "BUS",
            "TRANSFER",
            "TRAIN",
            "PACKAGE"

    };
    View bottomSheet ;
    BottomSheetBehavior behavior;

    private final int request_code_webView = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        init();
        setProperties();
        getPaymentMethods();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_trip, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        MenuItem searchMenu = menu.findItem(R.id.search);

        SearchView searchView =
                (SearchView) searchMenu.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        MenuItemCompat.setOnActionExpandListener(searchMenu,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        // Return true to allow the action view to expand
                        Log.d("action expand", "action expand");
                        SearchListener listener = getSearchListener();
                        if(listener!=null)
                            listener.searchOpened();
                        return true;
                    }
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        // When the action view is collapsed, reset the query
                        Log.d("action collapse", "action collapse");
                        SearchListener listener = getSearchListener();
                        if(listener!=null)
                            listener.searchClosed();
                        // Return true to allow the action view to collapse
                        return true;
                    }
                });
        return true;
    }


    @Override
    public void onBackPressed() {

        if(isPaymentMethodsShow())
            hidePaymentMethods();
        else
            super.onBackPressed();
    }

    private void setProperties() {
        findViewById(R.id.header).setBackgroundColor(UIUtils.getThemeColor(this));
        tabBarBackground.setBackgroundColor(UIUtils.getThemeColor(this));
        iTVMenu.setTextColor(UIUtils.getThemeContrastColor(this));
        tvTitle.setTextColor(UIUtils.getThemeContrastColor(this));
    }

    private void init() {
     /*CustomBottomSheetDialog bottomSheetDialog = new CustomBottomSheetDialog();
        bottomSheetDialog.show(getSupportFragmentManager(), "Custom Bottom Sheet");*/
        rlOverlay = (RelativeLayout) findViewById(R.id.rlOverlay);
        rlOverlay.setOnClickListener(this);
        rvPaymentMethods = (RecyclerView)findViewById(R.id.rvPaymentMethods);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        rvPaymentMethods.setLayoutManager(layoutManager);
        //rvPaymentMethods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvPaymentMethods.setHasFixedSize(true);

        rvPaymentMethods.setItemAnimator(new DefaultItemAnimator());

         bottomSheet = findViewById(R.id.design_bottom_sheet);
          behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        rlOverlay.setVisibility(View.VISIBLE);
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        rlOverlay.setVisibility(View.GONE);
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_HIDDEN");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
            }
        });



        tabBarBackground = (TableRow) findViewById(R.id.tabBarBackground);

       /* iTVSearch = (IconTextView)  findViewById(R.id.header).findViewById(R.id.search_icon);
        iTVSearch.setVisibility(View.GONE);
        iTVSearch.setOnClickListener(this);

        etSearch = (EditText) findViewById(R.id.etSearch);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //performSearch();
                    return true;
                }
                return false;
            }
        });*/

        iTVMenu = (IconTextView) findViewById(R.id.header).findViewById(R.id.left_icon);
        iTVMenu.setText("k");
        iTVMenu.setTextSize(20);
        iTVMenu.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.header).findViewById(R.id.title);
        tvTitle.setText(getString(R.string.my_trips));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(false); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
       // shape.setCornerRadii(new float[] { 8, 8, 8, 8, 0, 0, 0, 0 });
        shape.setColor(UIUtils.getThemeColor(this));
       // shape.setStroke(3, borderColor);
        ab.setBackgroundDrawable(shape);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);


        setupTabIcons();
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        for(int i=0;i<tabLayout.getTabCount();i++)
                        {

                            View view = tabLayout.getTabAt(i).getCustomView();
                            ((TextView)view.findViewById(R.id.tvIcon)).setTextColor(getResources().getColor(R.color.transparent_color));
                            ((TextView)view.findViewById(R.id.tab)).setTextColor(getResources().getColor(R.color.transparent_color));

                        }
                        View view= tab.getCustomView();
                        ((TextView)view.findViewById(R.id.tvIcon)).setTextColor(UIUtils.getThemeColor(MyTripsActivity.this));
                        ((TextView)view.findViewById(R.id.tab)).setTextColor(getResources().getColor(R.color.black));
                    }
                });

        btnFloating = (ImageView) findViewById(R.id.btn_flaoting);
        btnFloating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatingButtonMove floatingButtonMove = new FloatingButtonMove(getApplicationContext());
                floatingButtonMove.dragView(v, event);
                return true;
            }
        });
        if(Boolean.parseBoolean(getString(R.string.hideChatIcon))) {
            btnFloating.setVisibility(View.GONE);
        }

    }



    private void getPaymentMethods() {
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else {
            String url;

            NetworkTask networkTask = new NetworkTask(this, PAYMENT_METHODS);
            networkTask.setDialogMessage(getString(R.string.please_wait));
            networkTask.exposePostExecute(this);
            String paramsArray[] = null;
            //url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlTripRecord + pageNo;


            url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlPaymentMethods;
            paramsArray = new String[]{url, null};
            networkTask.execute(paramsArray);
        }

    }

    private void setupTabIcons() {

        for(int i=0;i<pageTitles.length;i++)
        {
            View tabOne =  LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            TextView tabOneText = (TextView)tabOne.findViewById(R.id.tab) ;
            tabOneText.setText(pageTitles[i]);
            if(i==0)
                tabOneText.setTextColor(getResources().getColor(R.color.black));
            else
                tabOneText.setTextColor(getResources().getColor(R.color.transparent_color));

            TextView tabOneIcon = (TextView)tabOne.findViewById(R.id.tvIcon) ;
            tabOneIcon.setText(tabIcons[i]);
            if(i==0)
                tabOneIcon.setTextColor(UIUtils.getThemeColor(MyTripsActivity.this));
            else
                tabOneIcon.setTextColor(getResources().getColor(R.color.transparent_color));

            tabLayout.getTabAt(i).setCustomView(tabOne);
        }


    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for(int i=0;i<types.length;i++)
        {
            MyTripFragment fragment = new MyTripFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", types[i]);
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, pageTitles[i]);
        }

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageChangeListener);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request_code_webView && resultCode == RESULT_OK) {
            String status = data.getStringExtra("STATUS");
            if (status.equals("true")) {
                //hit ticket request
                String tripJson = Utils.getTripJsonFromCreditCardSuccesJson();
                viewTicket(tripJson);
                // getTicket();
            } else {
                //show error dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage(Utils.CreditCardFailureMsg);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        }
    }

    private void viewTicket(String object) {
        ViewTicketResponse viewTicketResponse = new Gson().fromJson(object, ViewTicketResponse.class);
        Intent intent = new Intent(this, ViewTicketActivity.class);
        Bundle bundle = getIntent().getExtras();
        bundle.putSerializable("view_ticket_response", viewTicketResponse);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_icon:
                finish();
                break;
            /*case R.id.search_icon:
                etSearch.setVisibility(View.VISIBLE);
                etSearch.setFocusable(true);
                etSearch.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
                iTVSearch.setVisibility(View.GONE);
                break;*/


        }
    }

    public void showPaymentMethods(String bookingRefNo)
    {
        this.bookingRefNo = bookingRefNo;
        final int initState = BottomSheetBehavior.STATE_EXPANDED;
        bottomSheet.post(new Runnable() {
            @Override
            public void run() {
                behavior.setState(initState);
            }
        });

    }

    public boolean isPaymentMethodsShow()
    {
        if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
        {
            return  true;
        }
        else
        {
            return false;
        }
    }
    public void hidePaymentMethods()
    {
        this.bookingRefNo = "";
        final int initState = BottomSheetBehavior.STATE_COLLAPSED;
        bottomSheet.post(new Runnable() {
            @Override
            public void run() {
                behavior.setState(initState);
            }
        });
    }


    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {

        if(object != null && id == PAYMENT_METHODS)
        {
            PaymentMethodsResponse response = new Gson().fromJson(object,PaymentMethodsResponse.class);

           if(response!=null&& response.getGateways()!=null)
           {
               rvPaymentMethods.setAdapter(new PaymentMethodAdapter(this, response.getGateways(), rvPaymentMethods));
           }


        }

    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

            if(query!=null && query.length()>0) {
                SearchListener listener = getSearchListener();
                if(listener!=null)
                    listener.searchFromList(query);

            }
        }
    }

    public SearchListener getSearchListener()
    {
        int position = tabLayout.getSelectedTabPosition();
        Fragment fragment = adapter.getItem(position);
        if (fragment instanceof SearchListener) {
            SearchListener listener = (SearchListener)fragment;
            return listener;

        }
        else
        {
            return null;
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        int currentPosition = 0;

        @Override
        public void onPageSelected(int newPosition) {

            FragmentLifeCycle fragmentToShow = (FragmentLifeCycle)adapter.getItem(newPosition);
            fragmentToShow.onResumeFragment();

            FragmentLifeCycle fragmentToHide = (FragmentLifeCycle)adapter.getItem(currentPosition);
            fragmentToHide.onPauseFragment();

            currentPosition = newPosition;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) { }

        public void onPageScrollStateChanged(int arg0) { }
    };



    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }




}
