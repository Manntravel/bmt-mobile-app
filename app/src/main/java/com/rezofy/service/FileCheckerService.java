package com.rezofy.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;

/**
 * Created by Rakhi Mittal on 12-Mar-18.
 */

public class FileCheckerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            try {
                File logFile = new File(Environment.getExternalStorageDirectory() + File.separator + "logs.txt");
                if (logFile.exists()) {
                    logFile.delete();
                }
            } catch (Exception e) {

            }


        return super.onStartCommand(intent, flags, startId);
    }
}
