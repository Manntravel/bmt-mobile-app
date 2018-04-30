package com.rezofy.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rezofy.R;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;

public class SplashActivity extends AppCompatActivity implements NetworkTask.DoInBackground {
    private ImageView ivGradientImage;
    private TextView tvPowerdBy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_splash);

        ivGradientImage = (ImageView) findViewById(R.id.gradient_image);
        tvPowerdBy = (TextView)findViewById(R.id.tvPowerdBy);
        setProperties();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
        }
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (!AppPreferences.getInstance(SplashActivity.this).getLogIn(SplashActivity.this)) // && Boolean.parseBoolean(getString(R.string.login_activity_show)))
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        else {
                            if (Boolean.parseBoolean(getApplicationContext().getString(R.string.tripbox))) {
                                Utils.CURRENT_HOME_ACTIVITY = Utils.TAG_TRIPBOX_HOME_ACTIVITY;
                            }

                            if (Utils.CURRENT_HOME_ACTIVITY.equals(Utils.TAG_TRIPBOX_HOME_ACTIVITY)) {
                                startActivity(new Intent(SplashActivity.this, TripBoxHomeActivity.class));
                            } else {
                                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                            }
                        /**/
                        }
                       /* startActivity(new Intent(SplashActivity.this, TrainVoucherActivity.class));*/
                        finish();
                    }
                }, 1000);

        //throw new RuntimeException();

    }

    private void setProperties() {
        UIUtils.setWindowBackground(this);
        UIUtils.setBackgroundGradient(ivGradientImage);
        if(Boolean.parseBoolean(getApplicationContext().getString(R.string.tripbox)))
        {
            if(!Boolean.parseBoolean(getApplicationContext().getString(R.string.is_primary_app)))
                tvPowerdBy.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public String doInBackground(Integer id, String... params) {
        return null;
    }
}