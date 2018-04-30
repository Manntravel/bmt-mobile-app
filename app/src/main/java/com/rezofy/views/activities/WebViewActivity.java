package com.rezofy.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.views.custom_views.IconTextView;

/**
 * Created by linchpinub4 on 17/6/15.
 */
public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView webView;
    private IconTextView iTVMenu;
    private TextView tvTitle;
    private ImageView btnFloating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        init();
        setProperties();
    }

    private void init() {
        iTVMenu = (IconTextView) findViewById(R.id.header).findViewById(R.id.left_icon);
        iTVMenu.setText(getString(R.string.icon_text_k));
        iTVMenu.setTextSize(20);
        iTVMenu.setOnClickListener(this);
        tvTitle=(TextView) findViewById(R.id.header).findViewById(R.id.title);
        tvTitle.setText(getIntent().getStringExtra("title"));
        webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl(getIntent().getStringExtra("url"));   //now it will not fail here
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

    private void setProperties() {
        findViewById(R.id.header).setBackgroundColor(UIUtils.getThemeColor(this));
        iTVMenu.setTextColor(UIUtils.getThemeContrastColor(this));
        tvTitle.setTextColor(UIUtils.getThemeContrastColor(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_icon:
                finish();
                break;
        }

    }
}