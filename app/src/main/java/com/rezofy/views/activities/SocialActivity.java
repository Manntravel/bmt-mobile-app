package com.rezofy.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rezofy.R;
import com.rezofy.controllers.FacebookController;
import com.rezofy.controllers.GooglePlusController;
import com.rezofy.utils.Constants;
import com.rezofy.utils.Utils;


public class SocialActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
//        new DownloadTwitterTask().execute("");

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_social, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.appendLog("inside onActivityResult of Socialactivity request code is "+requestCode+" and result code is "+resultCode);
        if (requestCode == Constants.FACEBOOK_CODE) {
            FacebookController.getInstance().getActivityResult(requestCode, resultCode, data);

        } else if (requestCode == Constants.Google_SIGN_IN) {
            if (resultCode == RESULT_OK)
                GooglePlusController.getInstance().getActivityResultSucess(requestCode, resultCode, data, SocialActivity.this);
//            else if(resultCode == RESULT_CANCELED)
//                GooglePlusController.getInstance().getActivityResultFailed(requestCode,resultCode,data,SocialActivity.this);
        }
    }


//    @Override
//    public void onTwitterLoginSuccessful(long userID, User user1) {
//        System.out.println("Twitter Login Done......");
//
//        new DownloadTwitterTask().execute("");
////        Twitter twitter = tf.getInstance();
////        try {
////            List<Status> statuses;
////            String user;
////            user = "happylungsindia";
////            statuses = twitter.getUserTimeline(user);
////            Log.i("Status Count", statuses.size() + " Feeds");
////            for (int i = 0; i < statuses.size(); i++) {
////                Status status = statuses.get(i);
////                Log.i("Tweet Count " + (i + 1), status.getText() + "\n\n");
////            }
////        } catch (TwitterException te) {
////            te.printStackTrace();
////        }
//    }
//
//    @Override
//    public void onTwitterLoginCanceled() {
//
//    }
//
//    @Override
//    public void onTwitterLoginFailed(Exception e) {
//
//    }
//
//
//    // Uses an AsyncTask to download a Twitter user's timeline
//    private class DownloadTwitterTask extends AsyncTask<String, Void, String> {
//        final String CONSUMER_KEY = "2W6vihUNhwZj51JWiX6yfU5Wx";
//        final String CONSUMER_SECRET = "x8yeKo8t0kd6M7Pfw31lZAngJ9fKV5ut2ffdK7EWSjF5pVrCV3";
//        final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
//        final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";
//
//        @Override
//        protected String doInBackground(String... screenNames) {
//            String result = null;
//
//            if (screenNames.length > 0) {
//                result = getTwitterStream(screenNames[0]);
//            }
//            return result;
//        }
//
//        // onPostExecute convert the JSON results into a Twitter object (which is an Array list of tweets
//        @Override
//        protected void onPostExecute(String result) {
//
//        }
//
//        private String getTwitterStream(String screenName) {
//            String results = null;
//
//            // Step 1: Encode consumer key and secret
//            try {
//                // URL encode the consumer key and secret
//                String urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
//                String urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");
//
//                // Concatenate the encoded consumer key, a colon character, and the
//                // encoded consumer secret
//                String combined = urlApiKey + ":" + urlApiSecret;
//
//                // Base64 encode the string
//                String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);
//
//                String token = getToken(base64Encoded);
//
//                twitterResponse(token);
//
//            } catch (UnsupportedEncodingException ex) {
//            } catch (IllegalStateException ex1) {
//            }
//            return results;
//        }
//    }
//
//    private String getToken(String base64EncodedString) {
//        //Use Http post to send request.
//        String receivedToken = "";
//        try {
//            HttpClient httpclient = new DefaultHttpClient();
////Append twitter api url here.
//            String uriString = "https://api.twitter.com/oauth2/token";
//            HttpPost httppost = new HttpPost(uriString);
//            HttpParams httpParams = httppost.getParams();
//            HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
//            HttpConnectionParams.setSoTimeout(httpParams, 15000);
////Append EncodedString to Authorization Header.
//            httppost.setHeader("Authorization", "Basic " + base64EncodedString);
////Set Content type here.
//            httppost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//            HttpResponse response = null;
//
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("grant_type", "client_credentials"));
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//            response = httpclient.execute(httppost);
//            String returnedJsonStr = EntityUtils.toString(response.getEntity());
//
//            int statusCode = response.getStatusLine().getStatusCode();
//            Log.v("response", returnedJsonStr);
//            Log.v("response", Integer.toString(statusCode));
//            JSONObject jsonObject = new JSONObject(returnedJsonStr);
////Receive Bearer token here.
//            receivedToken = jsonObject.getString("access_token");
//            Log.v("access_token", receivedToken);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return receivedToken;
//    }
//
//    private void twitterResponse(String receivedToken) {
//        HttpClient httpclient = new DefaultHttpClient();
////Append twitter tvFlightSearchDetails and screen name with api URL
//        HttpGet httpget = new HttpGet("https://api.twitter.com//1.1/statuses/user_timeline.json?count=3&screen_name=" + "ankansithole");
////Append bearer token here.
//        httpget.setHeader("Authorization", "Bearer " + receivedToken);
//        HttpResponse response;
//        try {
//            //Receive response here and make it .
//            response = httpclient.execute(httpget);
//            HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                InputStream instream = entity.getContent();
//                String twitter_response = convertStreamToString(instream);
//                System.out.println("TWITTER RESPONSE  " + twitter_response);
//                instream.close();
//
//            }
//        } catch (Exception e) {
//        }
//    }
//
//    private String convertStreamToString(java.io.InputStream is) {
//        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
//        return s.hasNext() ? s.next() : "";
//    }

}
