package com.rezofy.controllers;

import android.app.Application;

import com.rezofy.R;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formUri = "", // will not be used
        mailTo = "support@ecaretechlabs.com", // my email here
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text,
        resDialogOkToast = R.string.crash_ok_toast
)


/**
 * Created by linchpin11192 on 16-Mar-2016.
 */
public class Rezofy extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }
}
