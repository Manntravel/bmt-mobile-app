package com.rezofy.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.freshdesk.hotline.Hotline;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rezofy.R;
import com.rezofy.views.activities.SplashActivity;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

//import com.freshdesk.hotline.Hotline;

/**
 * Created by anuj on 22/9/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService
{

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Map<String , String> map = remoteMessage.getData();
        Set<Map.Entry<String, String>> set = map.entrySet();
        //Iterator<String> iterator = map.entrySet().iterator();
        Iterator<Map.Entry<String, String>> iterator = set.iterator();
        int i = 0;
        String notfication_string = "";
        while (iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            entry.getKey();
            if(i == 9)
                notfication_string = entry.getValue();
            i++;
        }



//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//
//        //Calling method to generate notification
//        sendNotification(remoteMessage.getNotification().getBody());               // working fine
 //       sendNotification(notfication_string);

        if (Hotline.isHotlineNotification(remoteMessage)) {                            //// editing
            Hotline.getInstance(this).handleFcmMessage(remoteMessage);
           // Toast.makeText(getApplication(), "if " + remoteMessage, Toast.LENGTH_LONG).show();
        } else {
            //Handle notifications with data payload for your app
            //Toast.makeText(getApplication(), "message "+remoteMessage, Toast.LENGTH_LONG).show();
        }


    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Firebase Push Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
