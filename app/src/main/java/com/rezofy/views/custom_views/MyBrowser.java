package com.rezofy.views.custom_views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by linchpin on 25/2/16.
 */
public class MyBrowser extends WebViewClient {
    WebViewListener listener;
    ProgressDialog pd;
    private boolean isRedirected;

    public MyBrowser(WebViewListener listener, ProgressDialog pd) {
        this.listener = listener;
        this.pd = pd;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);

        isRedirected = true;
        // Log.d("Trip","inside load url "+url);
        if (url.contains("air/air-confirmation")) {

            //String hitResult = "javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');";
            // view.getHitTestResult().toString();//document.getElementsByTagName('html')[0].innerHTML


            //Log.d("Trip","hit result is "+hitResult);

        }
        return true;
    }

    @Override
    public void onPageFinished(final WebView view, String url) {
        super.onPageFinished(view, url);
        if (pd.isShowing()) {
            pd.dismiss();
        }
        Log.d("Trip", "inside pagefinished " + url);
        if (isRedirected) {
            isRedirected = false;
        } else {
            if (url.contains("air/air-confirmation")) {
//            if (url.contains("paytm")) {
                view.loadUrl("javascript:HtmlViewer.showHTMLSuccess" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {

                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        listener.taskDone("true");
                        super.onPostExecute(aVoid);
                    }
                }.execute();


            } else if (url.contains("air/payment-failure")) {
//            } else if (url.contains("payment-failure")) {
                view.loadUrl("javascript:HtmlViewer.showHTMLFailure" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                listener.taskDone("false");

            }


        }

//        view.loadUrl("javascript:HtmlViewer.showHTML" +
//                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");


    }

    public interface WebViewListener {
        void taskDone(String message);

    }

    @Override
    public void onReceivedSslError(WebView view, final SslErrorHandler handler,
                                   SslError error) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(((Activity)listener));
        String message = "SSL Certificate error.";
        switch (error.getPrimaryError()) {
            case SslError.SSL_UNTRUSTED:
                message = "The certificate authority is not trusted.";
                break;
            case SslError.SSL_EXPIRED:
                message = "The certificate has expired.";
                break;
            case SslError.SSL_IDMISMATCH:
                message = "The certificate Hostname mismatch.";
                break;
            case SslError.SSL_NOTYETVALID:
                message = "The certificate is not yet valid.";
                break;
        }
        message += " Do you want to continue anyway?";

        builder.setTitle("SSL Certificate Error");
        builder.setMessage(message);
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

    }
}
