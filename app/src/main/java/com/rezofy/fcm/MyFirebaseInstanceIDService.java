package com.rezofy.fcm;

import android.util.Log;

import com.freshdesk.hotline.Hotline;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by anuj on 22/9/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService
{

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Hotline.getInstance(this).updateGcmRegistrationToken(refreshedToken);
        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "Refreshed token: 2" );
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}
