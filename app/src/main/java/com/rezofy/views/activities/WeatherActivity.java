package com.rezofy.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.views.custom_views.IconTextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private View header;
    private IconTextView iTVMenu, iTVSearch;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

//        final Handler handler = new Handler();
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    while (true) {
//                        getWeather();
//                        handler.post(this);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        thread.start();


        //getWeather();

//        initViews();
//        setProperties();
//        setListener();
//        setData();

    }

    public void getWeather() {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            // this twitter call returns json results.
            // see this page for more info: https://dev.twitter.com/docs/using-search
            // http://search.twitter.com/search.json?q=%40apple

            // Example URL 1: this yahoo weather call returns results as an rss (xml) feed
            //HttpGet httpGetRequest = new HttpGet("http://weather.yahooapis.com/forecastrss?p=80020&u=f");

            // Example URL 2: this twitter api call returns results in a JSON format
            HttpGet httpGetRequest = new HttpGet("http://search.twitter.com/search.json?q=%40apple");

            // Execute HTTP request
            HttpResponse httpResponse = httpClient.execute(httpGetRequest);

            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getStatusLine());
            System.out.println("----------------------------------------");

            // Get hold of the response entity
            HttpEntity entity = httpResponse.getEntity();

            // If the response does not enclose an entity, there is no need
            // to bother about connection release
            byte[] buffer = new byte[1024];
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                try {
                    int bytesRead = 0;
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        String chunk = new String(buffer, 0, bytesRead);
                        System.out.println(chunk);
                    }
                } catch (IOException ioException) {
                    // In case of an IOException the connection will be released
                    // back to the connection manager automatically
                    ioException.printStackTrace();
                } catch (RuntimeException runtimeException) {
                    // In case of an unexpected exception you may want to abort
                    // the HTTP request in order to shut down the underlying
                    // connection immediately.
                    httpGetRequest.abort();
                    runtimeException.printStackTrace();
                } finally {
                    // Closing the input stream will trigger connection release
                    try {
                        inputStream.close();
                    } catch (Exception ignore) {
                    }
                }
            }
        } catch (ClientProtocolException e) {
            // thrown by httpClient.execute(httpGetRequest)
            e.printStackTrace();
        } catch (IOException e) {
            // thrown by entity.getContent();
            e.printStackTrace();
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpClient.getConnectionManager().shutdown();
        }

    }

//    private void initViews() {
//        context = getApplicationContext();
//
//        header = findViewById(R.id.header);
//        if(header != null) {
//            iTVMenu = (IconTextView) header.findViewById(R.id.left_icon);
//            iTVMenu.setText(getString(R.string.icon_text_h));
//            iTVMenu.setOnClickListener(this);
//            iTVMenu.setTextSize(20);
//
//            tvTitle = (TextView) header.findViewById(R.id.title);
//            tvTitle.setText(getString(R.string.weather));
//
//            iTVSearch = (IconTextView) header.findViewById(R.id.right_icon3);
//            iTVSearch.setVisibility(View.VISIBLE);
//            iTVSearch.setOnClickListener(this);
//            iTVSearch.setText(getString(R.string.icon_text_search));
//            iTVSearch.setTextSize(20);
//        }
//    }
//
//    private void setProperties() {
//        int themeContrastColor = UIUtils.getThemeContrastColor(context);
//        header.setBackgroundColor(UIUtils.getThemeColor(context));
//        iTVMenu.setTextColor(themeContrastColor);
//        iTVSearch.setTextColor(themeContrastColor);
//        tvTitle.setTextColor(themeContrastColor);
//    }
//
//    private void setListener() {
//
//    }
//
//    private void setData() {
//
//    }
//
//
    @Override
    public void onClick(View v) {

    }
}
