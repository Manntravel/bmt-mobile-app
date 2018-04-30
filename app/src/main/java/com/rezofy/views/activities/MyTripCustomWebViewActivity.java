package com.rezofy.views.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.rezofy.R;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.custom_views.MyBrowser;

/**
 * Created by linchpin on 1/2/17.
 */

public class MyTripCustomWebViewActivity  extends Activity implements MyBrowser.WebViewListener{

    WebView webView;
    MyBrowser browser;
    private ImageView btnFloating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        init();
        String bookingRefNo = getIntent().getStringExtra("bookingRefNo");
        String paymentType = getIntent().getStringExtra("paymentType");
        String token = getIntent().getStringExtra("token");
        ProgressDialog pd;
        webView = (WebView)findViewById(R.id.webView);
        try {
            String template = Utils.getUnpaidHTMLData(this);
            Log.d("Trip","template is "+template);
            template =  template.replace("$ACTION$", UIUtils.getBaseUrl(this) + WebServiceConstants.urlUnpaidBookingInitialize);
           // template =  template.replace("$ACTION$", "http://2.ecareibe.com/" + WebServiceConstants.urlUnpaidBookingInitialize);
            template =  template.replace("$BOOKING_REF_NO$",bookingRefNo);
            template = template.replace("$PAYMENT_TYPE$", paymentType);
            template = template.replace("$TOKEN$", AppPreferences.getInstance(this).getToken());
            Log.d("Trip", "template is :: " + template);
            pd = new ProgressDialog(MyTripCustomWebViewActivity.this);
            pd.setMessage("Please wait Loading...");
            pd.show();
            browser = new MyBrowser(this,pd);
           webView.setWebViewClient(browser);

           /* webView.setWebViewClient(new WebViewClient(){

                    @Override
                    public void onReceivedError(WebView view, int errorCode,
                    String description, String failingUrl) {

                        Toast.makeText(MyTripCustomWebViewActivity.this, "Oh no! " + description,
                                Toast.LENGTH_SHORT).show();

                    }

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);

                    return true;
                }

                public void onPageFinished(WebView view, String url) {

                }

            });*/
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);


            webView.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");
            webView.loadData(template,"text/html", "UTF-8");

//            webView.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "utf-8", null);
         //webView.loadUrl("http://demo.rezofy.com/api/mytrips/unpaid-booking-initialize.do");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(){
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

    @Override
    public void taskDone(String status) {
        Log.d("Trip", "inside taskDone" + status);
        Intent intent = new Intent();
        intent.putExtra("STATUS",status);
        setResult(RESULT_OK, intent);
        finish();
    }


}


