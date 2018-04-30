package com.rezofy.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.rezofy.R;
import com.rezofy.utils.Utils;

import org.json.JSONObject;

import java.util.Arrays;



/**
 * Created by LinchPin on 11/16/2015.
 */
public class FacebookController {
    public static CallbackManager callbackManager;
    private String accessToken;
    private Context context;
    public LoginResult loginResult;
    private FbLoginListener listner;
    private GetProfileInfoListner profileListener;
    public static FacebookController facebookControllerInstance;

    private FacebookController()
    {

    }

    public static FacebookController getInstance()
    {
        synchronized (FacebookController.class)
        {
            if(facebookControllerInstance == null)
            {
                synchronized (FacebookController.class)
                {
                    facebookControllerInstance = new FacebookController();
                }
            }
        }
        return facebookControllerInstance;
    }

    public static void setCallbackManager(CallbackManager callbackManager) {
        FacebookController.callbackManager = callbackManager;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LoginResult getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(LoginResult loginResult) {
        this.loginResult = loginResult;
    }

    /**
     *
     * @param context context of activity
     */
    public void initializeFacebook(Context context)
    {

        this.context = context;
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
    }

    /**
     *
     * @return facebook callback manager
     */
    protected CallbackManager getCallbackManager()
    {
        if(callbackManager == null)
        {
            callbackManager = CallbackManager.Factory.create();
        }
        return callbackManager;
    }

    /**
     * method for login with facebook you must extend ur activity with Social Activity and call initializeFacebook() method before
     * @param context context of the activity from which method is called
     * @param listner listner whose callbacks will be called on successful login or failure
     */
    public void loginToFacebook(final Context context, final FbLoginListener listner)
    {
        this.context = context;
        this.listner = listner;
        if (Utils.isNetworkAvailable(context)) {
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


                @Override
                public void onSuccess(LoginResult loginResult) {

                    accessToken = loginResult.getAccessToken().getToken();

                    setLoginResult(loginResult);
                    setAccessToken(accessToken);
                    listner.onFbLoginSuccessful(accessToken);

                }




                @Override
                public void onCancel() {

                    listner.onFbLoginCanceled();
                    // write to log file
//                    writeToLog.write("*****Login Page***** \n facebook cancel");
                }

                @Override
                public void onError(FacebookException e) {
                    listner.onFbLoginFailed(e);
                    // write to log file
//                    writeToLog.write("*****Login Page***** \n facebook error, " + e.getMessage());
                }
            });
            LoginManager.getInstance().logInWithReadPermissions((Activity) this.context, Arrays.asList("public_profile", "user_friends", "email", "user_birthday", "user_location", "user_photos", "user_education_history"));

        } else {

            Toast.makeText(context, "Please check your internet Connection", Toast.LENGTH_SHORT).show();
            //DialogTask.getInstance().showDialog(context, context.getString(R.string.app_name), context.getString(R.string.network_error), context.getString(R.string.ok_text), 1, null);
//            // write to log file
//            writeToLog.write("*****Login Page***** \n connection error");
        }

    }

    public void getActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public interface FbLoginListener
    {
        void onFbLoginSuccessful(String accessToken);
       // void onFbLoginSuccessful(AccessToken accessToken);
        void onFbLoginFailed(FacebookException e);
        void onFbLoginCanceled();
    }

    public interface GetProfileInfoListner
    {
        void onProfileInfoFetched(JSONObject object, String imageUrl);
    }

    public void getUserProfileInfo(Context context, final GetProfileInfoListner profileListener)
    {
        Utils.appendLog("inside getUserProfileInfo");
        this.profileListener = profileListener;
        this.context = context;
        if(loginResult == null)
            Utils.appendLog("inside getUserProfileInfo loginresult is null");

        if(accessToken == null)
            Utils.appendLog("inside getUserProfileInfo acces token is null");
        if(loginResult == null || accessToken == null)
        {
            //throw my own exception
            return;
        }
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Utils.appendLog("inside onCompleted of graphrequest jsonobject is"+object);
                // Application code
                try {
                    String identifier = object.getString("id");
                    profileListener.onProfileInfoFetched(object, "https://graph.facebook.com/" + identifier + "/picture?type=large");
//                    name = object.getString("name");
//                    email = object.optString("email");
//                    birth_date = object.optString("birthday");
//                    identifier = object.getString("id");
//                    gender = object.getString("gender");
//                    URL imageURL = new URL("https://graph.facebook.com/" + identifier + "/picture?type=large");
//
//
//                    String params = SignUpController.prepareJsonString(LoginActivity.this, name, getLastSyncTimeToSend(), "", "", "", "", "",
//                            loginType, "", identifier, email, "", "", gender);
//                    storeInfo(RSAEncryption.ecryptData(params));
//                    System.out.println(object.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                    // write to log file
                    //writeToLog.write("*****Login Page***** \n exception, " + e.getMessage());
                }
            }
        });
        Bundle parameters = new Bundle();
//                    parameters.putString("fields", "id,name,email,education,birthday,address,link");
        parameters.putString("fields", "id,name,first_name,middle_name,last_name,email,birthday,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
