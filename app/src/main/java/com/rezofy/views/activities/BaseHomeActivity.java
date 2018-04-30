package com.rezofy.views.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.freshdesk.hotline.Hotline;
import com.freshdesk.hotline.HotlineConfig;
import com.freshdesk.hotline.HotlineUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.database.DbHelper;
import com.rezofy.fcm.MyFirebaseInstanceIDService;
import com.rezofy.models.response_models.LoginResponse;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.service.FileCheckerService;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.custom_views.IconTextView;
import com.rezofy.views.fragments.ChatLoginFragment;

import java.util.Calendar;

/**
 * Created by anuj on 9/2/17.
 */
public class BaseHomeActivity extends AppCompatActivity implements View.OnClickListener{

    DrawerLayout drawerLayout;
    LinearLayout llHome, myTrip, llMyBalance, setting, logout,login,chat;
    private IconTextView iTVProfileImage;
    private TextView tvUsername, tvEmail;
    RecyclerView rcOptions;
    RelativeLayout rlBalances;
    private ImageView btnFloating;

    private IconTextView iTVRefresh;

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 4329;

    float dx, dy;
    int lastAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        setDrawerLayout();
        setListener();
        setProperties();
        initHotline();
        setAlarmToDeleteLogs();
    }

    private void setAlarmToDeleteLogs() {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(BaseHomeActivity.this, FileCheckerService.class);
        PendingIntent pintent = PendingIntent.getService(BaseHomeActivity.this, 0, intent,
                0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                24 * 60 * 60 * 1000, pintent);
    }

    void setDrawerLayout() {
        llHome = (LinearLayout) findViewById(R.id.home);
        myTrip = (LinearLayout) findViewById(R.id.myTrip);
        llMyBalance = (LinearLayout) findViewById(R.id.my_balance);
        rlBalances = (RelativeLayout) findViewById(R.id.balances);
        setting = (LinearLayout) findViewById(R.id.setting);
        logout = (LinearLayout) findViewById(R.id.logout);
        login = (LinearLayout) findViewById(R.id.login);
        chat = (LinearLayout) findViewById(R.id.chat);
        tvUsername = (TextView) findViewById(R.id.tvUserName);
        iTVProfileImage = (IconTextView) findViewById(R.id.profile_image);
        tvEmail = (TextView) findViewById(R.id.email);
        iTVRefresh = (IconTextView) findViewById(R.id.refresh);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        rcOptions = (RecyclerView) findViewById(R.id.drawer_options);
        rcOptions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
       /* if (!isPrimaryApp)
            rcOptions.setVisibility(View.GONE);*/


        btnFloating = (ImageView) findViewById(R.id.btn_flaoting);
        if(Boolean.parseBoolean(getString(R.string.hideChatIcon))) {
            btnFloating.setVisibility(View.GONE);
        }
    }

    private void setListener() {
        llHome.setOnClickListener(this);
        llMyBalance.setOnClickListener(this);
        iTVRefresh.setOnClickListener(this);
        myTrip.setOnClickListener(this);
        setting.setOnClickListener(this);
        logout.setOnClickListener(this);
        login.setOnClickListener(this);
        chat.setOnClickListener(this);
        btnFloating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatingButtonMove floatingButtonMove = new FloatingButtonMove(getApplicationContext());
                floatingButtonMove.dragView(v, event);
                // dragView(v, event);
                return true;
            }
        });
    }

    private void setProperties() {
        findViewById(R.id.header_bg).setBackgroundColor(UIUtils.getThemeColor(this));
        iTVProfileImage.setTextColor(UIUtils.getThemeContrastColor(this));
        tvUsername.setTextColor(UIUtils.getThemeContrastColor(this));
        tvEmail.setTextColor(UIUtils.getThemeContrastColor(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        manageUser();
    }


    private void manageUser() {
        String userData = AppPreferences.getInstance(this).getUserData();
        if (!userData.isEmpty()) {
            //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            LoginResponse loginResponse = new Gson().fromJson(userData, LoginResponse.class);
            if (loginResponse.isB2BUser()) {
                tvUsername.setText(loginResponse.getCompany());
                iTVProfileImage.setText(getString(R.string.icon_text_3));
                tvEmail.setText(loginResponse.getEmail());
                login.setVisibility(View.GONE);
            } else {
                if(loginResponse.getName().getFirst().contains("guest") || loginResponse.getName().getFirst().contains("Guest"))
                {
                    myTrip.setVisibility(View.GONE);
                    tvUsername.setText("Guest");
                    iTVProfileImage.setText(getString(R.string.icon_text_2));
                    llMyBalance.setVisibility(View.GONE);
                    rlBalances.setVisibility(View.GONE);
                    logout.setVisibility(View.GONE);
                    tvEmail.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);

                }
                else {
                    tvUsername.setText(loginResponse.getName().getFirst() + " " + loginResponse.getName().getLast());
                    iTVProfileImage.setText(getString(R.string.icon_text_2));
                    tvEmail.setText(loginResponse.getEmail());
                    login.setVisibility(View.GONE);
                }
            }


        } else {
            myTrip.setVisibility(View.GONE);
            tvUsername.setText("Guest");
            iTVProfileImage.setText(getString(R.string.icon_text_2));
            llMyBalance.setVisibility(View.GONE);
            rlBalances.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            //  drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

    }

    public void openDrawer() {
        drawerLayout.openDrawer(Gravity.LEFT);
        drawerLayout.setBackgroundColor(getResources().getColor(R.color.white));
    }

    void logOutWork() {
        AppPreferences.getInstance(BaseHomeActivity.this).clearPreferences();
        //Hotline.clearUserData(getApplicationContext());
        new DbHelper(this).deleteDB(this);
        Intent intent = new Intent(BaseHomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void initHotline(){

//        //init
//        //HotlineConfig hotlineConfig=new HotlineConfig("a626ad76-1e87-46d7-8c6b-7657ff6f1e48", "ca33a0d5-5bf4-40b4-b0cc-0a10c2975893");
        HotlineConfig hotlineConfig=new HotlineConfig(getString(R.string.appid), getString(R.string.appkey));
        Hotline.getInstance(getApplicationContext()).init(hotlineConfig);

        //Update user information
        HotlineUser user = Hotline.getInstance(getApplicationContext()).getUser();
        String userData = AppPreferences.getInstance(this).getUserData();
        if (!userData.isEmpty()) {
            LoginResponse loginResponse = new Gson().fromJson(userData, LoginResponse.class);
            String userName = "";
            if(loginResponse.getName().getFirst().length() > 1 ){
                userName = loginResponse.getName().getFirst();
            }if(loginResponse.getName().getLast().length() > 1 ){
                userName = userName + " " + loginResponse.getName().getLast();
            }
            user.setName(userName);
            if(loginResponse.getEmail().length() > 1){
                user.setEmail(loginResponse.getEmail());
            } else {
                user.setName("Guest2").setPhone("001", "2542542541");
            }}
        Hotline.getInstance(getApplicationContext()).updateUser(user);

        if(checkPlayServices(this)) {
            Intent intent = new Intent(this, MyFirebaseInstanceIDService.class);  //MyGcmRegistrationService.class;
            startService(intent);
        }
    }

    private boolean checkPlayServices(Activity activityContext) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activityContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activityContext, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("demoapp", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void openChatFragment()
    {
        drawerLayout.closeDrawer(Gravity.LEFT);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatLoginFragment(), ChatLoginFragment.class.getSimpleName()).addToBackStack(ChatLoginFragment.class.getSimpleName()).commit();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.logout:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.confirm_logout), null, getString(R.string.ok), getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        logOutWork();
                                        break;
                                }
                            }
                        });

                break;
            case R.id.login:
                logOutWork();
                break;
            case R.id.chat:
                //openChatFragment();
                if(Boolean.parseBoolean(getString(R.string.isHotLineIntegrated))) {
                    Hotline.showConversations(BaseHomeActivity.this);
                } else {
                    Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
