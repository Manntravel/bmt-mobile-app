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

import com.rezofy.R;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.custom_views.MyBrowser;

/**
 * Created by linchpin on 25/2/16.
 */
public class CustomWebViewActivity extends Activity implements MyBrowser.WebViewListener{

    WebView webView;
    MyBrowser browser;
    private ImageView btnFloating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        init();
        String searchId = getIntent().getStringExtra("searchId");
        String token = getIntent().getStringExtra("token");
        ProgressDialog pd;
        webView = (WebView)findViewById(R.id.webView);
        try {
           String template = Utils.getHTMLData(this);

            Log.d("Trip","template is "+template);
            template =  template.replace("$ACTION$", UIUtils.getBaseUrl(this) + WebServiceConstants.urlBookingInitialize);
            template =  template.replace("$SEARCH_ID$",searchId);
            template = template.replace("$TOKEN$", AppPreferences.getInstance(this).getToken());
            Log.d("Trip", "template is :: " + template);
            pd = new ProgressDialog(CustomWebViewActivity.this);
            pd.setMessage("Please wait Loading...");
            pd.show();
            browser = new MyBrowser(this,pd);
            webView.setWebViewClient(browser);
                       WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            webView.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");
            webView.loadDataWithBaseURL(null,template,"text/html", "UTF-8",null);
//            webView.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "utf-8", null);
//            webView.loadUrl("http://google.com/");
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

class MyJavaScriptInterface {

    private Context ctx;

    MyJavaScriptInterface(Context ctx) {
        this.ctx = ctx;
    }

    @JavascriptInterface
    public void showHTMLSuccess(String html) {

        String json = html.substring(html.indexOf("{"),html.lastIndexOf("}")+1);
        Utils.CreditCardSuceesJson = json;


    }

    @JavascriptInterface
    public void showHTMLFailure(String html) {

        String bodyDiv  = html.substring(html.indexOf("<div id=\"paymentErrorMessage\">"));
        String mainBodyDiv = bodyDiv.substring(0,bodyDiv.indexOf("</div>")+1);
        String failureMsg = mainBodyDiv.substring(mainBodyDiv.indexOf("{")+1,mainBodyDiv.lastIndexOf("}"));
        Utils.CreditCardFailureMsg = failureMsg;


    }

}
