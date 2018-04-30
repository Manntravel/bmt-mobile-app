package com.rezofy.views.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.controllers.FacebookController;
import com.rezofy.controllers.GooglePlusController;
import com.rezofy.models.response_models.LoginResponse;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.requests.Requests;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.custom_views.IconTextView;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by linchpin on 23/2/16.
 */
public class RegistrationActivity extends SocialActivity implements FacebookController.FbLoginListener, FacebookController.GetProfileInfoListner, GooglePlusController.GoogleLoginListener,
        View.OnClickListener, NetworkTask.Result {

    private static final int MY_PERMISSIONS_REQUEST_ACCOUNT = 1;
    private LinearLayout btnFBLogin, btnGPlusLogin, contBtn, llRootLayout;
    private TextView gender_male, gender_female, tvContinue, tvLoginWith;
    private boolean isMaleSelected = true;
    private EditText first_name, last_name, email, password;
    private String validationMsg = "";
    private static final int ID_REGISTRATION = 102;
    private GraphRequest request;
    private FacebookController facebookController;
    private GooglePlusController googlePlusController;
    private ScrollView sVGradient;
    private IconTextView iTVFirstName, iTVLastName, iTVUsername, iTVPassword;
    private View loginSeparatorLeft, loginSeparatorRight;
    private GradientDrawable gDrawableSelected, gDrawableUnselected;
    private ProgressDialog pd = null;
    private ImageView btnFloating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
        setProperties();
    }

    private void init() {
        gDrawableSelected = (GradientDrawable) getResources().getDrawable(R.drawable.selected_btn_bg_round);
        gDrawableSelected.setStroke(2, UIUtils.getThemeContrastColor(this));
        gDrawableUnselected = (GradientDrawable) getResources().getDrawable(R.drawable.unselected_btn_bg_round);
        sVGradient = (ScrollView) findViewById(R.id.rl_childscrollview);
        llRootLayout = (LinearLayout) findViewById(R.id.root);
        btnFBLogin = (LinearLayout) findViewById(R.id.fbLogin);
        btnGPlusLogin = (LinearLayout) findViewById(R.id.gPlusLogin);
        contBtn = (LinearLayout) findViewById(R.id.ll_continue);
        gender_male = (TextView) findViewById(R.id.gender_seletor_male);
        gender_female = (TextView) findViewById(R.id.gender_seletor_female);
        iTVFirstName = (IconTextView) findViewById(R.id.itv_left_firstname);
        iTVLastName = (IconTextView) findViewById(R.id.itv_left_lastname);
        iTVUsername = (IconTextView) findViewById(R.id.itv_left_username);
        iTVPassword = (IconTextView) findViewById(R.id.tv_left_userpass);
        first_name = (EditText) findViewById(R.id.etfirstname);
        last_name = (EditText) findViewById(R.id.etlastname);
        email = (EditText) findViewById(R.id.etUserName);
        password = (EditText) findViewById(R.id.etPassword);
        gender_male.setBackgroundDrawable(gDrawableSelected);
        gender_female.setBackgroundDrawable(gDrawableUnselected);
        gender_male.setOnClickListener(this);
        gender_female.setOnClickListener(this);
        tvContinue = (TextView) findViewById(R.id.btn_continue);
        contBtn.setOnClickListener(this);
        loginSeparatorLeft = findViewById(R.id.login_separator_left);
        tvLoginWith = (TextView) findViewById(R.id.login_with);
        loginSeparatorRight = findViewById(R.id.login_separator_right);

        btnFBLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Check", "insie fb click");
                if (!getString(R.string.facebook_app_id).equalsIgnoreCase("not_available")) {
                    Utils.appendLog("fb button clicked");
                    pd = new ProgressDialog(RegistrationActivity.this);
                    pd.setMessage("Please wait ...");
                    pd.setCancelable(false);
                    pd.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            facebookController = FacebookController.getInstance();
                            facebookController.initializeFacebook(RegistrationActivity.this);
                            facebookController.loginToFacebook(RegistrationActivity.this, RegistrationActivity.this);

                        }
                    }).start();
                } else {
                    Toast.makeText(RegistrationActivity.this, "Feature Not Available", Toast.LENGTH_SHORT);
                }
            }
        });

        if(Boolean.parseBoolean(getString(R.string.hideGoogleSignIn))) {
            btnGPlusLogin.setVisibility(View.GONE);
        }

        btnGPlusLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Check", "insie google click");
                if (!getString(R.string.facebook_app_id).equalsIgnoreCase("not_available")) {

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        checkAccountPermission();
                    } else {
                        getAccountInfo();
                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, "Feature Not Available", Toast.LENGTH_SHORT);
                }


            }
        });

        btnFloating = (ImageView) findViewById(R.id.btn_flaoting);
        if(Boolean.parseBoolean(getString(R.string.hideChatIcon))) {
            btnFloating.setVisibility(View.GONE);
        }
        btnFloating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatingButtonMove floatingButtonMove = new FloatingButtonMove(getApplicationContext());
                floatingButtonMove.dragView(v, event);
                return true;
            }
        });
    }

    private void setProperties() {
        int themeContrastColor = UIUtils.getThemeContrastColor(this);
        UIUtils.setWindowBackground(this);
        UIUtils.setBackgroundGradient(sVGradient);
        iTVFirstName.setTextColor(themeContrastColor);
        iTVLastName.setTextColor(themeContrastColor);
        iTVUsername.setTextColor(themeContrastColor);
        iTVPassword.setTextColor(themeContrastColor);
        first_name.setHintTextColor(themeContrastColor);
        first_name.setTextColor(themeContrastColor);
        last_name.setHintTextColor(themeContrastColor);
        last_name.setTextColor(themeContrastColor);
        email.setHintTextColor(themeContrastColor);
        email.setTextColor(themeContrastColor);
        password.setHintTextColor(themeContrastColor);
        password.setTextColor(themeContrastColor);
        gender_male.setTextColor(themeContrastColor);
        gender_female.setTextColor(themeContrastColor);
        tvContinue.setTextColor(themeContrastColor);
        GradientDrawable gDrawableStarted = (GradientDrawable) getResources().getDrawable(R.drawable.getstart_tv_background);
        gDrawableStarted.setStroke(2, themeContrastColor);
        contBtn.setBackgroundDrawable(gDrawableStarted);
        loginSeparatorLeft.setBackgroundColor(themeContrastColor);
        tvLoginWith.setTextColor(themeContrastColor);
        loginSeparatorRight.setBackgroundColor(themeContrastColor);
    }

    private void getUserDetails(AccessToken accessToken) {
        Log.d("Trip", "inside getUserDetails");
        try {
            request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            Log.d("Trip", "inside getUserDetails response is " + object.toString());
                            // Application code
                        }
                    });
        } catch (Exception e) {

        }

    }


    @Override
    public void onFbLoginSuccessful(String accessToken) {
        Utils.appendLog("inside onFbLoginSuccessful");
        Toast.makeText(this, "login succesfull", Toast.LENGTH_SHORT).show();
        dismislProgressBar();
        facebookController.getUserProfileInfo(RegistrationActivity.this, RegistrationActivity.this);

    }

    private void dismislProgressBar() {
        if (pd.isShowing()) {
            pd.dismiss();
        }
    }


    @Override
    public void onFbLoginFailed(FacebookException e) {
        Utils.appendLog("inside onFbLoginFailed" + e);
        dismislProgressBar();
        Toast.makeText(this, "login faliled", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFbLoginCanceled() {
        Utils.appendLog("inside onFbLoginCanceled");
        dismislProgressBar();
        Toast.makeText(this, "login cancelled", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onGoogleLoginSuccessful(String email, Person currentPerson) {
        Utils.appendLog("inside onGoogleLoginSuccessful email is " + email + " : and current person is " + currentPerson.getName().getGivenName());
        Toast.makeText(this, "Gp login succesfull", Toast.LENGTH_SHORT).show();
        dismislProgressBar();
        String firstName, lastName, title;
        firstName = currentPerson.getName().getGivenName();
        lastName = currentPerson.getName().getFamilyName();
        if (currentPerson.getGender() == 0)
            title = Utils.TITLE_MR;
        else if (currentPerson.getGender() == 1)
            title = Utils.TITLE_MS;
        else
            title = Utils.TITLE_OTHERS;

        hitForRegistration(title, firstName, lastName, email, "", true);
    }

    @Override
    public void onGoogleLoginCanceled() {
        Utils.appendLog("inside onGoogleLoginCanceled");
        Toast.makeText(this, "Gp login cancelled", Toast.LENGTH_SHORT).show();
        dismislProgressBar();

    }

    @Override
    public void onGoogleLoginFailed(Exception e) {
        Log.d("Check", "inside onGoogleLoginFailed  error is " + e);
        dismislProgressBar();
        Toast.makeText(this, "Gp login failed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gender_seletor_male:
//                if (isMaleSelected) {
//                    gender_male.setBackgroundDrawable(gDrawableUnselected);
//                    gender_female.setBackgroundDrawable(gDrawableSelected);
//                    isMaleSelected = false;
//
//                } else {
                    gender_male.setBackgroundDrawable(gDrawableSelected);
                    gender_female.setBackgroundDrawable(gDrawableUnselected);
                    isMaleSelected = true;
//                }
                break;

            case R.id.gender_seletor_female:
//                if (!isMaleSelected) {
//                    gender_male.setBackgroundDrawable(gDrawableSelected);
//                    gender_female.setBackgroundDrawable(gDrawableUnselected);
//                    isMaleSelected = true;
//
//                } else {
                    gender_male.setBackgroundDrawable(gDrawableUnselected);
                    gender_female.setBackgroundDrawable(gDrawableSelected);
                    isMaleSelected = false;

//                }
                break;

            case R.id.ll_continue:
                String fName = first_name.getText().toString();
                String lName = last_name.getText().toString();
                String mailId = email.getText().toString();
                String pswd = password.getText().toString();
                String title = "";
                if (validate(fName, lName, mailId, pswd)) {
                    if (isMaleSelected) {
                        title = Utils.TITLE_MR;

                    } else {
                        title = Utils.TITLE_MS;
                    }
                    hitForRegistration(title, fName, lName, mailId, pswd, false);

                } else {

                    Toast.makeText(RegistrationActivity.this, validationMsg, Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }

    private void hitForRegistration(String title, String fName, String lName, String mailId, String pswd, boolean socialNetworkLogin) {
        Utils.appendLog("inside hitForRegistration");
        String url;
        NetworkTask networkTask = new NetworkTask(RegistrationActivity.this, ID_REGISTRATION);
        networkTask.setDialogMessage(getString(R.string.please_wait));
        networkTask.exposePostExecute(RegistrationActivity.this);
        String paramsArray[];

        url = UIUtils.getBaseUrl(this) + WebServiceConstants.registerApi;
        paramsArray = new String[]{url, Requests.registerRequest("", fName, "", lName, "", mailId, pswd, false, 0, Utils.CREATED_ON_SIGNUP, socialNetworkLogin)};
        networkTask.execute(paramsArray);
    }

    private boolean validate(String fName, String lName, String mailId, String pswd) {
        if (fName.equals("")) {
            validationMsg = "Please Enter the first Name";
            return false;
        } else if (lName.equals("")) {
            validationMsg = "Please Enter the last Name";
            return false;
        } else if (!Utils.isValidEmail(mailId)) {
            validationMsg = "Please Enter the correct e-Mail";
            return false;
        } else if (pswd.equals("")) {
            validationMsg = "Please Enter the password";
            return false;
        } else if (pswd.length()<6){
            validationMsg = "Please enter the password with minimum 6 characters";
            return false;
        } else if(!pswd.matches("^.*(?=.{4,10})(?=.*\\d)(?=.*[a-zA-Z]).*$")) {
            validationMsg = "Please Enter valid password containing atleast 1 alphabet and 1 digit";
            return false;
        }  else {
            return true;
        }
    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {
        Log.d("Trip", "response is " + object);
        Utils.appendLog("inside result from networking " + object);
        if (object == null || object.equals("")) {
            Utils.showAlertDialog(RegistrationActivity.this, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else {
            try {
                JSONObject obj = new JSONObject(object);
                if (obj.optString("status").equals("0")) {
                    Utils.showAlertDialog(RegistrationActivity.this, getString(R.string.app_name), obj.optString("message"), getString(R.string.ok), null, null, null);

                } else {
                    Gson gson = new Gson();
                    LoginResponse loginResponse = gson.fromJson(obj.getString("user"), LoginResponse.class);
                    AppPreferences.getInstance(this).setUserData(obj.getString("user"));
                    AppPreferences.getInstance(this).setToken(loginResponse.getToken());
                    AppPreferences.getInstance(this).isLoggedIn(true);
                    AppPreferences.getInstance(this).isB2B(loginResponse.isB2BUser());
                    setResult(RESULT_OK);
                    finish();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onProfileInfoFetched(JSONObject object, String imageUrl) {
        String firstName, lastName, email, title;
        Utils.appendLog("inside onProfileInfoFetched");
        try {
            firstName = object.getString("first_name");
            lastName = object.getString("last_name");
            email = object.optString("email");
            if (object.getString("gender").equals("male"))
                title = Utils.TITLE_MR;
            else
                title = Utils.TITLE_MS;
            Utils.appendLog("inside onProfileInfoFetched  first name is " + firstName + " :  lastName  is " + lastName);
            hitForRegistration(title, firstName, lastName, email, "", true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Toast.makeText(this, "login succesfull " + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        Toast.makeText(this, "login succesfull ", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Toast.makeText(this, "login succesfull " + i, Toast.LENGTH_SHORT).show();
//    }

    public boolean checkAccountPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.GET_ACCOUNTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.GET_ACCOUNTS},
                        MY_PERMISSIONS_REQUEST_ACCOUNT);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.GET_ACCOUNTS},
                        MY_PERMISSIONS_REQUEST_ACCOUNT);
            }
            return false;
        } else {
            getAccountInfo();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCOUNT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.GET_ACCOUNTS)
                            == PackageManager.PERMISSION_GRANTED) {

                        getAccountInfo();

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void getAccountInfo() {
        Utils.appendLog("gplus button clicked");
        pd = new ProgressDialog(RegistrationActivity.this);
        pd.setMessage("Please wait ...");
        pd.setCancelable(false);
        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
                googlePlusController = GooglePlusController.getInstance();
                googlePlusController.initialiseGPlus(RegistrationActivity.this);
                googlePlusController.loginToGP(RegistrationActivity.this, RegistrationActivity.this);
            }
        }).start();
    }
}

