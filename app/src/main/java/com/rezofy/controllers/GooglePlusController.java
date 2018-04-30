package com.rezofy.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.rezofy.utils.Constants;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.SocialActivity;

import java.util.Calendar;


/**
 * Created by LinchPin on 11/16/2015.
 */
public class GooglePlusController implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private ConnectionResult mConnectionResult;
    private GoogleLoginListener googleLoginListner;
    private static GooglePlusController googleControllerInstance;

    public static GooglePlusController getInstance() {
        synchronized (GooglePlusController.class) {
            if (googleControllerInstance == null) {
                synchronized (GooglePlusController.class) {
                    googleControllerInstance = new GooglePlusController();
                }
            }
        }
        return googleControllerInstance;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Utils.appendLog("inside onConnected of gplus");
        Log.d("Trip","inside onConnected  ");
        getProfileInformation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Utils.appendLog("inside onConnectionSuspended of gplus");
        Log.d("Trip","inside onConnectionSuspended  "+i);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Utils.appendLog("inside onConnectionFailed of gplus"+connectionResult);
        Log.d("Trip","inside onConnection failed "+connectionResult);
        //Toast.makeText(context,"connection failed",Toast.LENGTH_SHORT).show();
        mConnectionResult = connectionResult;
        resolveSignInError();
    }

    public void initialiseGPlus(Context context) {
        Utils.appendLog("inside initialiseGPlus");
        this.context = context;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }



    public void loginToGP(Context context, GoogleLoginListener googleLoginListner) {
        Utils.appendLog("inside loginToGP ");
        this.googleLoginListner = googleLoginListner;
        if (!mGoogleApiClient.isConnected()) {
            if(mConnectionResult == null) {
                Log.d("Trip","mConnectionResult is null"+ Calendar.getInstance().getTime());
                mGoogleApiClient.connect();
            }
            else
            {
                Log.d("Trip","mConnectionResult is not null"+ Calendar.getInstance().getTime());
                try {
                    mConnectionResult.startResolutionForResult((Activity) context, Constants.Google_SIGN_IN);
                } catch (IntentSender.SendIntentException e) {
                    Log.d("Trip","inside catch "+e);
                    mConnectionResult = null;
                    mGoogleApiClient.connect();
                }
            }
        }

    }

    /**
     * Method to resolve any signin errors
     */
    public void resolveSignInError() {
        Log.d("Trip","inside resolveSignInError");
        if (mConnectionResult != null && mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult((Activity) context, Constants.Google_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();

            }
        }
    }

    public interface GoogleLoginListener {
        void onGoogleLoginSuccessful(String email, com.google.android.gms.plus.model.people.Person currentPerson);

        void onGoogleLoginCanceled();

        void onGoogleLoginFailed(Exception e);
    }

    /**
     * Fetching user's information name, email, profile pic
     */
    private void getProfileInformation() {
        try {
            Utils.appendLog("inside getProfileInformation of gplus");
            Log.d("Trip","inside getProfileInformation");
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                com.google.android.gms.plus.model.people.Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                googleLoginListner.onGoogleLoginSuccessful(email, currentPerson);
            } else {
                Toast.makeText(context, "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            googleLoginListner.onGoogleLoginFailed(e);
            e.printStackTrace();

        }
    }

    public void getActivityResultSucess(int requestCode, int resultCode, Intent data, Context context) {
        mIntentInProgress = false;
        mGoogleApiClient.connect();
        Log.d("Trip","inside getActivityResultSucess");
    }

    public void getActivityResultFailed(int requestCode, int resultCode, Intent data, SocialActivity socialActivity) {
        Log.d("Trip","inside getActivityResultFailed");
        //googleLoginListner.onGoogleLoginCanceled();
        mIntentInProgress = false;
        if (!mGoogleApiClient.isConnecting()) {
             mGoogleApiClient.connect();
        }
    }


}
