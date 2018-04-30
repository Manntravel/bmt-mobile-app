package com.rezofy.views.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.models.response_models.LoginResponse;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.requests.Requests;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.custom_views.IconTextView;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, NetworkTask.Result {

    private static final int LOGIN_ID = 100, forgotPswdId = 101;
    private LinearLayout llGradient;
    private LinearLayout llGetStarted;
    private EditText etUsername, etPassword;
    private LoginResponse loginResponse;
    private IconTextView iTVLeftUserName, iTVLeftUserPass;
    EditText emailEditText;
    Button sendBtn, okBtn;
    AlertDialog forgotPswdDialog, responseDialog;
    private TextView response, tvGetStarted, tvSkip;
    TextView forgetpswd, signUp;
    private boolean isPrimaryApp, isFromBook;
    private TextView tvPowerdBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setProperties();
        Utils.checkPermission(this);
    }

    private void setProperties() {
        UIUtils.setWindowBackground(this);
        UIUtils.setBackgroundGradient(llGradient);
        UIUtils.setTextToThemeContrastColor(iTVLeftUserName);
        UIUtils.setTextToThemeContrastColor(iTVLeftUserPass);
        UIUtils.setTextToThemeContrastColor(etUsername);
        UIUtils.setTextToThemeContrastColor(etPassword);
        UIUtils.setTextToThemeContrastColor(tvGetStarted);
        UIUtils.setTextToThemeContrastColor(tvSkip);
        GradientDrawable gDrawableStarted = (GradientDrawable) getResources().getDrawable(R.drawable.getstart_tv_background);
        gDrawableStarted.setStroke(2, UIUtils.getThemeContrastColor(this));
        llGetStarted.setBackgroundDrawable(gDrawableStarted);
        UIUtils.setTextToThemeContrastColor(forgetpswd);
        UIUtils.setTextToThemeContrastColor(signUp);

        if(Boolean.parseBoolean(getApplicationContext().getString(R.string.tripbox)))
        {
            if(!Boolean.parseBoolean(getApplicationContext().getString(R.string.is_primary_app)))
                tvPowerdBy.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        isFromBook = getIntent().getBooleanExtra("isFromBook", false);
        isPrimaryApp = Boolean.parseBoolean(getString(R.string.is_primary_app));
        llGradient = (LinearLayout) findViewById(R.id.ll_childscrollview);
        llGetStarted = (LinearLayout) findViewById(R.id.ll_getstart);
        iTVLeftUserName = (IconTextView) findViewById(R.id.itv_left_username);
        iTVLeftUserPass = (IconTextView) findViewById(R.id.tv_left_userpass);
        etUsername = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        forgetpswd = (TextView) findViewById(R.id.tv_left_bottum_forgate_pass);
        signUp = (TextView) findViewById(R.id.tv_right_bottum_sign_up);
        tvGetStarted = (TextView) findViewById(R.id.btn_continue);
        llGetStarted.setOnClickListener(this);
        tvSkip = (TextView) findViewById(R.id.tv_skip);
        tvPowerdBy = (TextView)findViewById(R.id.tvPowerdBy);
        SpannableString content = new SpannableString(getString(R.string.text_skip));
        content.setSpan(new UnderlineSpan(), 0, getString(R.string.text_skip).length(), 0);
        tvSkip.setText(content);
        forgetpswd.setOnClickListener(this);
        signUp.setOnClickListener(this);
        tvSkip.setOnClickListener(this);
        if (!isPrimaryApp || isFromBook)
            tvSkip.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_getstart) {
            String errorMsg = validateUserNameAndPassword();
            if (!errorMsg.equals("")) {
                Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            } else if (!Utils.isNetworkAvailable(LoginActivity.this)) {
                Utils.showAlertDialog(LoginActivity.this, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);
            } else {
                String url;
                NetworkTask networkTask = new NetworkTask(LoginActivity.this, LOGIN_ID);
                networkTask.setDialogMessage(getString(R.string.please_wait));
                networkTask.exposePostExecute(LoginActivity.this);
                String paramsArray[];

                url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlLogin;
                paramsArray = new String[]{url, Requests.loginRequest(etUsername.getText().toString(), etPassword.getText().toString())};
                networkTask.execute(paramsArray);
            }

        } else if (v.getId() == R.id.sendBtn) {
            if (!Utils.isValidEmail(emailEditText.getText().toString())) {
                String errorMsg = getString(R.string.invalid_mail);
                Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            } else if (!Utils.isNetworkAvailable(LoginActivity.this)) {
                Utils.showAlertDialog(LoginActivity.this, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);
            } else {

                String url;
                NetworkTask networkTask = new NetworkTask(LoginActivity.this, forgotPswdId);
                networkTask.setDialogMessage(getString(R.string.please_wait));
                networkTask.exposePostExecute(LoginActivity.this);
                String paramsArray[];

                url = UIUtils.getBaseUrl(this) + WebServiceConstants.forgetPswdApi;
                paramsArray = new String[]{url, Requests.forgetPswdRequest(emailEditText.getText().toString())};
                networkTask.execute(paramsArray);


            }
        } else if (v.getId() == R.id.okBtn) {
            responseDialog.dismiss();

        } else if (v.getId() == R.id.tv_left_bottum_forgate_pass) {
            forgotPassword();

        } else if (v.getId() == R.id.tv_right_bottum_sign_up) {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivityForResult(intent, Utils.registerReqId);

        } else if (v.getId() == R.id.tv_skip) {
            Intent i;
            if(Boolean.parseBoolean(getApplicationContext().getString(R.string.tripbox))) {
                Utils.CURRENT_HOME_ACTIVITY = Utils.TAG_TRIPBOX_HOME_ACTIVITY;
            }

            if(Utils.CURRENT_HOME_ACTIVITY.equals(Utils.TAG_TRIPBOX_HOME_ACTIVITY)) {
                i = new Intent(LoginActivity.this, TripBoxHomeActivity.class);
            } else {
                i = new Intent(LoginActivity.this, HomeActivity.class);
            }
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.appendLog("inside onActivityResult of login activity");
        if (requestCode == Utils.registerReqId && resultCode == RESULT_OK)
            handleSignUpAndSignIn();
    }

    private void handleSignUpAndSignIn() {
        Utils.appendLog("inside handleSignUpAndSignIn of login activity");
        Intent i;
        if (!isFromBook) {
            if(Boolean.parseBoolean(getApplicationContext().getString(R.string.tripbox))) {
                Utils.CURRENT_HOME_ACTIVITY = Utils.TAG_TRIPBOX_HOME_ACTIVITY;
            }

            if(Utils.CURRENT_HOME_ACTIVITY.equals(Utils.TAG_TRIPBOX_HOME_ACTIVITY)) {
                i = new Intent(LoginActivity.this, TripBoxHomeActivity.class);
            } else {
                i = new Intent(LoginActivity.this, HomeActivity.class);
            }
        } else {
            i = new Intent(this, FlightDetailsActivity.class);
            i.putExtras(getIntent().getExtras());
        }
        startActivity(i);
        finish();
    }

    private String validateUserNameAndPassword() {
        String errorMsg = "";
        if (etUsername.getText().toString().equals("")) {
            errorMsg = getString(R.string.blank_username);
        } else if (etPassword.getText().toString().equals("")) {
            errorMsg = getString(R.string.blank_password);
        } else if (!Utils.isValidEmail(etUsername.getText().toString())) {
            errorMsg = getString(R.string.invalid_mail);
        }
        return errorMsg;
    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {

        if (id == LOGIN_ID) {
            if (object == null || object.equals("")) {
                Utils.showAlertDialog(LoginActivity.this, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);
            } else {
                if (object.contains(getString(R.string.invalid_username_or_password))) {
                    Utils.showAlertDialog(LoginActivity.this, getString(R.string.app_name), getString(R.string.invalid_username_or_password), getString(R.string.ok), null, null, null);

                } else {
                    try {
                        Gson gson = new Gson();
                        JSONObject obj = new JSONObject(object);
                        loginResponse = gson.fromJson(obj.getString("user"), LoginResponse.class);
                        AppPreferences.getInstance(LoginActivity.this).setUserData(obj.getString("user"));
                        AppPreferences.getInstance(LoginActivity.this).setToken(loginResponse.getToken());
                        AppPreferences.getInstance(LoginActivity.this).isLoggedIn(true);
                        AppPreferences.getInstance(LoginActivity.this).isB2B(loginResponse.isB2BUser());
                        handleSignUpAndSignIn();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } else if (id == forgotPswdId) {
            try {
                Log.d("Trip", "response is " + object);
                JSONObject obj = new JSONObject(object);
                if (obj.getString("Status").equalsIgnoreCase("success")) {
                    forgotPswdDialog.dismiss();
                    showMessage(obj.getString("Remarks"), true);
                } else if (obj.getString("Status").equalsIgnoreCase("Failed")) {
                    showMessage(obj.getString("message"), false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void showMessage(String msg, boolean status) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_pswd_reset_success, null);
        dialogBuilder.setView(dialogView);
        response = (TextView) dialogView.findViewById(R.id.responseText);
        response.setText(msg);
        okBtn = (Button) dialogView.findViewById(R.id.okBtn);
        okBtn.setOnClickListener(this);

        sendBtn.setOnClickListener(this);
        responseDialog = dialogBuilder.create();
        responseDialog.show();
    }

    private void forgotPassword() {
        try {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_forgot_password, null);
            dialogBuilder.setView(dialogView);

            emailEditText = (EditText) dialogView.findViewById(R.id.emailid);
            sendBtn = (Button) dialogView.findViewById(R.id.sendBtn);

            UIUtils.setRoundedButtonProperties(sendBtn);
            sendBtn.setOnClickListener(this);
            forgotPswdDialog = dialogBuilder.create();
            forgotPswdDialog.show();

        } catch (Exception e) {
            Log.d("Trip", "Eror in forgotPassword" + e);
        }
    }
}
