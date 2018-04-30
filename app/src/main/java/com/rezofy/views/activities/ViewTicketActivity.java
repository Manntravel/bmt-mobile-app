package com.rezofy.views.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rezofy.R;
import com.rezofy.adapters.TicketAdapter;
import com.rezofy.database.DbHelper;
import com.rezofy.models.response_models.ViewTicketResponse;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.custom_views.IconTextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * Created by Linchpin66 on 25-01-2016.
 */
public class ViewTicketActivity extends AppCompatActivity implements View.OnClickListener {

    protected IconTextView iTVMenu, iTVShareTicket;
    protected RecyclerView recyclerView;
    protected TicketAdapter ticketAdapter;
    protected ViewTicketResponse viewTicketResponse;
    protected TextView tvTitle;
    private ImageView btnFloating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        init();
        setProperties();

    }

    private void setProperties() {
        int themeColor = UIUtils.getThemeColor(this);
        int themeContrastColor = UIUtils.getThemeContrastColor(this);
        findViewById(R.id.header).setBackgroundColor(themeColor);
        iTVMenu.setTextColor(themeContrastColor);
        tvTitle.setTextColor(themeContrastColor);
        iTVShareTicket.setTextColor(themeContrastColor);

    }

    private void init() {
        Nammu.init(this);
        viewTicketResponse = (ViewTicketResponse) getIntent().getSerializableExtra("view_ticket_response");
        iTVMenu = (IconTextView) findViewById(R.id.header).findViewById(R.id.left_icon);
        iTVMenu.setTextSize(20);
        tvTitle = (TextView) findViewById(R.id.header).findViewById(R.id.title);
        iTVShareTicket = (IconTextView) findViewById(R.id.header).findViewById(R.id.right_icon);
        iTVShareTicket.setVisibility(View.VISIBLE);
        iTVShareTicket.setText(getString(R.string.icon_text_I));
        iTVShareTicket.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.viewTicketList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        specialInit();
        btnFloating = (ImageView) findViewById(R.id.btn_flaoting);
        btnFloating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatingButtonMove floatingButtonMove = new FloatingButtonMove(getApplicationContext());
                floatingButtonMove.dragView(v, event);
                return true;
            }
        });
        if(Boolean.parseBoolean(getString(R.string.hideChatIcon))) {
            btnFloating.setVisibility(View.GONE);
        }
    }

    protected void specialInit() {
        iTVMenu.setText(getString(R.string.icon_text_H));
        iTVMenu.setOnClickListener(this);
        tvTitle.setText(getString(R.string.text_view_ticket));
        ticketAdapter = new TicketAdapter(this, viewTicketResponse, false);
        recyclerView.setAdapter(ticketAdapter);
    }


    public Uri getRecyclerViewScreenshot() {
        View v = getWindow().getDecorView().getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        try {
            File file = new File(Environment
                    .getExternalStorageDirectory().toString(), "SCREEN"
                    + System.currentTimeMillis() + ".png");
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_icon:
                finishAllAndTakeToHome();
                break;

            case R.id.right_icon:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    Nammu.askForPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionCallback() {
                        @Override
                        public void permissionGranted() {
                            prepareForScreenShot();
                        }

                        @Override
                        public void permissionRefused() {
                            Toast.makeText(getBaseContext(), R.string.text_permisson_refused, Toast.LENGTH_LONG).show();
                        }
                    });
                else
                    prepareForScreenShot();
                break;
        }
    }

    private void prepareForScreenShot() {
        final ArrayList<Uri> uriList = new ArrayList<>();
        recyclerView.scrollToPosition(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                uriList.add(getRecyclerViewScreenshot());
                recyclerView.scrollToPosition(ticketAdapter.getItemCount() - 1);

            }
        }, 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                uriList.add(getRecyclerViewScreenshot());
                final Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                shareIntent.setType("image/png/jpeg");
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
                startActivityForResult(Intent.createChooser(shareIntent, "Sharing your ticket"), 12345);
            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideSoftKey(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAllAndTakeToHome();
    }

    private void finishAllAndTakeToHome() {
        if(AppPreferences.getInstance(ViewTicketActivity.this).isGuestLogin()) {
            AppPreferences.getInstance(ViewTicketActivity.this).clearPreferences();
            AppPreferences.getInstance(ViewTicketActivity.this).setGuestLogin(false);
        }
        new DbHelper(this).deleteDB(this);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}