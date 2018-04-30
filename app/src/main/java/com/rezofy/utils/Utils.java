package com.rezofy.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.rezofy.R;
import com.rezofy.models.response_models.FlightData;
import com.rezofy.models.response_models.PackagesCountryList;
import com.rezofy.preferences.AppPreferences;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Created by linchpin11192 on 24-Dec-2015.
 */
public class Utils {

    public static final String TAG_HOME_ACTIVITY = "HomeActivity";
    public static final String TAG_TRIPBOX_HOME_ACTIVITY = "TripboxHomeActivity";
    public static final String TAG_ENTER_FLIGHT_INFO_FRAGMENT = "EnterFlightInfoFragment";
    public static final String TAG_HOME_FRAGMENT = "HomeFragment";
    public static final String TAG_MY_TRIPS_FRAGMENT = "MyTripsFragment";
    public static final String TAG_POPULAT_PACKAGES_FRAGMENT = "PopularPackagesFragment";
    public static final String TAG_COUNTRY_PACKAGES_FRAGMENT = "CountryPackagesFragment";
    public static final String TAG_PACKAGE_DETAIL_FRAGMENT = "PackageDetailFragment";
    public static final String TAG_PLACE_SEARCH_FRAGMENT = "PlaceSearchFragment";
    public static final String TAG_ENQUIRY_FRAGMENT = "EnquiryFragment";
    public static final String TAG_TRIPBOX_FRAGMENT = "TripBoxFragment";
    public static final String TAG_CUSTOM_WEBVIEW_FRAGMENT = "CustomWebViewFragment";
    public static final String TAG_WEB_CHECKIN_ACTIVITY = "WebCheckinActivity";
    public static final String TAG_BAGGAGE_INFORMATION_ACTIVITY = "BaggageInformationActivity";
    public static final String TRIP_TYPE_FLIGHT = "FLIGHT";
    public static final String TYPE_ROUND_TRIP = "ROUNDTRIP";
    public static final String SERVICE_CC_ECONOMY = "Y";
    public static final String SERVICE_CC_PREMIUM_ECONOMY = "W";
    public static final String SERVICE_CC_BUSINESS = "C";
    public static final String SERVICE_CC_FIRST = "F";
    public static final String CARRIER_TYPE_ANY = "ANY";
//    public static final String COUNTRY_CODE_IN = "IN";
//    public static final String CITY_CODE_DELHI = "DEL";
//    public static final String CITY_CODE_MUMBAI = "BOM";
//    public static final String CITY_NAME_DELHI = "New Delhi";
//    public static final String CITY_NAME_MUMBAI = "Mumbai";
//    public static final String AIRPORT_NAME_DELHI = "Indira Gandhi International Airport";
//    public static final String AIRPORT_NAME_MUMBAI = "Chhatrapati Shivaji International Airport";
//    public static final String COUNTRY_NAME_IN = "India";
    public static final String TYPE_ONE_WAY = "ONEWAY";
    public static final int PLACE_SEARCH_CODE = 101;
    public static final int DATE_SELECTOR_CODE = 102;
    public static final int TRIP_ACTION_CODE = 103;
    public static final int registerReqId = 103;
    public static final int CREDIT_BALANCE = 105;
    public static final int request_code_recentSearch = 10;
    public static final int request_code_passengerSearch = 11;
    public static final String LARGE_DATA_FILE_NAME = "large_data_file";
    public static final String Filtered_DATA_FILE_NAME = "filtered_data_file";
    public static final int SORT_INCREMENTAL = 1;
    public static final int SORT_DECREMENTAL = -1;
    public static final int SORT_PRICE = 1;
    public static final int SORT_TIME = -1;
    public static final int SORT_DURATION = 2;
    public static final String FARE_RESULTS_TYPE_REGULAR = "REGULAR";
    public static final String FARE_RESULTS_TYPE_SPECIAL = "SPECIAL";
    public static final String TAB_GDS = "GDS";
    public static final String TAB_REGULAR = "REGULAR";
    public static final int FLIGHT_DIRECTION_OUTBOUND = -1;
    public static final int FLIGHT_DIRECTION_INBOUND = 1;
    public static final String PASSENGER_TYPE_ADULT = "ADT";
    public static final String PASSENGER_TYPE_CHILD = "CHD";
    public static final String PASSENGER_TYPE_INFANT = "INF";
    public static final String OPTION_NP = "NP";
    public static final String MEAL_VEG = "Veg";
    public static final String MEAL_NON_VEG = "Non-Veg";
    public static final String TITLE_MR = "Mr";
    public static final String TITLE_MS = "Ms";
    public static final String TITLE_OTHERS = "Ors.";
    public static final String DATE_FORMAT_ddMMyy = "ddMMyy";
    public static final String DATE_FORMAT_dd_dash_mm_dash_yyyy = "dd-mm-yyyy";
    public static final String DATE_FORMAT_dd_dash_MM_dash_yyyy = "dd-MM-yyyy";
    public static final String DATE_FORMAT_yyyy_dash_MM_dash_dd = "yyyy-MM-dd";
    public static final String DATE_FORMAT_dd_slash_mm_slash_yyyy = "dd/MM/yyyy";
    public static final String DATE_FORMAT_EEE_space_comma_d_space_LLL = "EEE, d LLL";
    public static final String DATE_FORMAT_d_space_LLL = "d LLL";
    public static final String DATE_FORMAT_d_space_LLL_space_yyyy = "d LLL yyyy";
    public static final String DATE_FORMAT_dd = "dd";
    public static final String DATE_FORMAT_MMM = "MMM";
    public static final String DATE_FORMAT_EEEE = "EEEE";
    public static final String PAYMENT_TYPE_CREDIT_LIMIT = "credit";
    public static final String PAYMENT_TYPE_CREDIT_CARD = "card";
    public static final String PAYMENT_TYPE_CASH_CARD = "cash";
    public static final String CREATED_ON_SIGNUP = "SIGNUP";
    public static final String TICKET_STATUS_CANCEL_REQUESTED = "CANCELREQUESTED";
    public static final String TEXT_ERROR = "error";
    public static final int REQUEST_CODE_REVIEW_DATA = 104;
    public static final String TAG_SOURCE = "source";
    public static boolean show_saved_filters = false;
    public static boolean filter_hideMultiAirlines = false;
    public static String CURRENT_HOME_ACTIVITY = TAG_HOME_ACTIVITY;
    public static ArrayList<String> filter_checked_airlineNames = new ArrayList<>();
    public static ArrayList<String> filter_no_of_stop_list = new ArrayList<>();
    public static ArrayList<String> filter_dep_slot_list = new ArrayList<>();
    public static ArrayList<String> filter_arrival_slot_list = new ArrayList<>();
    public static Bitmap bitmap;
    public static final String KEY_PUBLISHED_PRICE_DIFF = "publishedPriceDiff";
    public static final String TICKET_STATUS_BOOKED = "BOOKED";
    public static final String TICKET_STATUS_TICKETED = "TICKETED";
    public static String CreditCardSuceesJson = "";
    public static String CreditCardFailureMsg = "";

    public static final String TAG_COUNTRY_NAME = "country_name";
    public static final String TAG_PACKAGE_ID = "package_id";
    public static final String TAG_PACKAGE_NAME = "package_name";
    public static final String TAG_CITY_NAME = "city_name";
    public static final String TAG_STARTING_PRICE = "starting_price";
    public static final String TAG_THUMBNAIL = "thumbnail";
    public static final String TAG_PHONENUMBER = "phonenumber";
    public static final String TAG_FAVORITE = "favorite";
    public static final String TAG_DURATION = "duration";
    public static final String TAG_PACKAGES_COUNTRY_LIST = "package_country_list";
    public static final String TAG_WISH_LIST = "wish_list";
    public static final String TAG_WISHLIST = "wishlist";

    private static DisplayImageOptions options;
    private static ImageLoader imageLoader;
    private static ArrayList<String> packageCountryList;
    public static PackagesCountryList packagesCountryList;
    public static final int request_code_GstSearch = 12;

    public static void putObjectToJSON(String name, Object value, JSONObject obj) {
        try {
            obj.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void putBooleanToJSON(String name, boolean value, JSONObject obj) {
        try {
            obj.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void putIntToJSON(String name, int value, JSONObject obj) {
        try {
            obj.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void showAlertDialog(final Context context, String title, String message, String neutralButtonText,
                                       String positiveButtonText, String negativeButtonText,
                                       DialogInterface.OnClickListener onClickListener) {

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        alt_bld.setMessage(message).setCancelable(false);

        //Set neutral button
        if (neutralButtonText != null) {
            if (onClickListener != null)
                alt_bld.setNeutralButton(neutralButtonText, onClickListener);
            else
                alt_bld.setNeutralButton(neutralButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        }
        //Set positive button
        if (positiveButtonText != null && onClickListener != null) {
            alt_bld.setPositiveButton(positiveButtonText, onClickListener);
        }

        ////Set negative button
        if (negativeButtonText != null) {
            if (onClickListener != null)
                alt_bld.setNegativeButton(negativeButtonText, onClickListener);
            else
                alt_bld.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendEmail(context);
                    }
                });
        }
        /*if (negativeButtonText != null && onClickListener != null) {
            alt_bld.setNegativeButton(negativeButtonText, onClickListener);
        }*/
        AlertDialog alert = alt_bld.create();
        alert.setTitle(title);
        alert.show();
    }

    public static void sendEmail(Context context) {
        String filename = "logs.txt";
        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
        emailIntent.setType("vnd.android.cursor.dir/email");
        String to[] = {"rakhi@ecaretechlabs.com "};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
// the mail subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Logs");
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }


    public static void showInfo(Context context, String message) {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        alt_bld.setMessage(message).setCancelable(true);
        AlertDialog alert = alt_bld.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int[] dimensions = new int[2];
        dimensions[0] = size.x;
        dimensions[1] = size.y;
        return dimensions;
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }


    public static boolean isPassportNoValid(String passportNo) {
        CharSequence inputStr = passportNo.trim();
        Pattern pattern = Pattern.compile(new String("^[a-zA-Z0-9]{4,12}$"));
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static String httpPostRaw(String url, String jsonData, Context context) {
        String response = "";
        BufferedReader br = null;
        try {
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setReadTimeout(Integer.parseInt(context.getString(R.string.default_read_timeout)));
            if (url.contains("booking-initialize.do")||url.contains("trip-list.do"))
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            else
                conn.setRequestProperty("Content-Type", "application/json");


            if (!AppPreferences.getInstance(context).getToken().isEmpty() || !url.contains("login.do") || !url.contains("b2c-registration.do"))
                conn.setRequestProperty("token", AppPreferences.getInstance(context).getToken());

            Log.d("Token",AppPreferences.getInstance(context).getToken());
            // For POST only - BEGIN
            if (jsonData != null && !jsonData.equals("")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(jsonData);
                out.close();
                /*DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.write(jsonData.getBytes());
                out.close();*/
            }
            // For POST only - END
            // read the response

            System.out.println("Response Code: " + conn.getResponseCode());
            InputStream in = new BufferedInputStream(conn.getInputStream());
            StringBuilder sb = new StringBuilder();

            String line;

            br = new BufferedReader(new InputStreamReader(in));
            while ((line = br.readLine()) != null)
                sb.append(line);
            response = sb.toString();
            Log.d("URL:", url);
            if(jsonData!=null)
            Log.d("Request:", jsonData);
            Log.d("Response:", response);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    public static String httpPostRaw(String params, List<NameValuePair> namevalueList) {
        InputStream is = null;
        String result = "";
        try {
//            HttpClient httpclient = new DefaultHttpClient();
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params);
            httpPost.setEntity(new UrlEncodedFormEntity(namevalueList));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        // convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    // check if net is available or not
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr.getActiveNetworkInfo() != null
                && connMgr.getActiveNetworkInfo().isAvailable()
                && connMgr.getActiveNetworkInfo().isConnected())
            return true;

        else {
            return false;
        }
    }

    //third parameter filename is increased so that we can also pass the filename along with other parameters
    public static boolean writeToLargeDataFile(Context context, String data, String fileName) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getDurationInHrsAndMins(String duration) {
        return new StringBuilder().append(Integer.parseInt(duration) / 60).append("h ").append(Integer.parseInt(duration) % 60).append("m").toString();
    }

    public static String getFormattedDurationInHrsAndMins(String duration) {
        return new StringBuilder().append(getTwoDigitFormat(Integer.parseInt(duration) / 60)).append("h ").append(getTwoDigitFormat(Integer.parseInt(duration) % 60)).append("m").toString();
    }

    public static String getTwoDigitFormat(int number) {
        NumberFormat formatter = new DecimalFormat("00");
        return formatter.format(number);
    }

    ////third parameter filename is increased so that we can also pass the filename along with other parameters
    public static String getFromLargeDataFile(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            reader.close();
            return out.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // check validity of email
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    public static boolean isValidMobile(String mobileNo, String isdCode) {
        if(isdCode.equals("+61") || isdCode.equals("+64")){
            return android.util.Patterns.PHONE.matcher(mobileNo).matches() && (mobileNo.length() == 9);
        } else {
            return android.util.Patterns.PHONE.matcher(mobileNo).matches() && (mobileNo.length() == 10);
        }
    }

    public static void setSellingPriceInFlightDataList(List<FlightData> flightDataList) {
        for (FlightData flightData : flightDataList) {
            float tax = 0;
            if (flightData.getFare().getTaxes().get(KEY_PUBLISHED_PRICE_DIFF) != null)
                tax = Float.parseFloat(flightData.getFare().getTaxes().get(KEY_PUBLISHED_PRICE_DIFF).getPrice().getAmount());
            float offerPrice = Float.parseFloat(flightData.getFare().getTotal().getPrice().getAmount());
            flightData.getFare().setSellingPrice(offerPrice + tax);
        }
    }


    // Convert a date String from one format to another

    public static String changeDateFormat(String time, String fromFormat,
                                          String toFormat) {

        SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SimpleDateFormat sdf1 = new SimpleDateFormat(toFormat);
        String s = sdf1.format(date.getTime());
        return s;
    }

    public static Date getDateFromFormat(String dateString, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return date;
        }
    }

    public static Bitmap takeScreenshot(View view) {
        Log.d("Trip", "inside takescreenshot");
        // View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        int width = view.getWidth();
        int height = view.getHeight();
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width,
                height - statusBarHeight);
        view.destroyDrawingCache();
        return bitmap2;
    }

    public static void TakeScreenshot(View rootView) {
        //View rootView = (View) findViewById(R.id.ScrollView1);
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = rootView.getDrawingCache();

        File imagePath = new File(Environment.getExternalStorageDirectory() + "/MyScrnShot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Log.d("Trip", "file path is " + imagePath.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getTimeInDateformat(long millis) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

        // long now = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        System.out.println(millis + " = " + formatter.format(calendar.getTime()));
    }


    public static long getTimeInMilliSeconds(String day, String month, String year) {
        long millis = 0;
        try {


            String someDate = month + "." + day + "." + year;

            SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
            Date date = sdf.parse(someDate);
            millis = date.getTime();
            long currentMilis = System.currentTimeMillis();

        } catch (Exception e) {
            Log.d("Trip", "Eror in getTimeInMilliseconds " + e);
        } finally {
            return millis;
        }
    }

    public static void hideSoftKey(Context context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftKey(Dialog dialog) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getHTMLData(Context context) {
        StringBuilder html = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();

            InputStream input = assetManager.open("booking_initialize.html");
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String line;
            while ((line = br.readLine()) != null) {
                html.append(line);
            }
            br.close();
        } catch (Exception e) {
            //Handle the exception here
        }

        return html.toString();
    }
    public static String getUnpaidHTMLData(Context context) {
        StringBuilder html = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();

            InputStream input = assetManager.open("unpaid_booking_initialize.html");
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String line;
            while ((line = br.readLine()) != null) {
                html.append(line);
            }
            br.close();
        } catch (Exception e) {
            //Handle the exception here
        }

        return html.toString();
    }


    public static String getTripJsonFromCreditCardSuccesJson() {
        JSONObject mainTripOb = null;
        try {
            System.out.println("response of suceess::" + CreditCardSuceesJson);
            JSONObject mainOb = new JSONObject(CreditCardSuceesJson);
            JSONObject dataOb = mainOb.getJSONObject("data");
            JSONArray travellers = dataOb.getJSONArray("travellers");
            JSONObject selectedResult = dataOb.getJSONObject("selectedResult");
            JSONArray segments = selectedResult.getJSONArray("segments");
            JSONObject segmentOb = segments.getJSONObject(0);
            Iterator<String> keys = mainOb.keys();
            if (!segmentOb.has("airlinePnr")) {
                segmentOb.put("airlinePnr", "Pending");
            }

            //Now Create ViewTicketJson
            mainTripOb = new JSONObject();
            JSONObject tripOb = new JSONObject();
            tripOb.putOpt("travellers", travellers);

            JSONObject tripSelectedResult = new JSONObject();
            JSONArray segmentAr = new JSONArray();
            segmentAr.put(segmentOb);
            tripSelectedResult.put("segments", segmentAr);
            tripOb.put("selectedResult", tripSelectedResult);
            tripOb.put("totalPrice", mainOb.getString("totalPrice"));
            tripOb.put("status", mainOb.getString("status"));
            tripOb.put("bookingDate", dataOb.getString("bookingDate"));
            mainTripOb.put("trip", tripOb);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return mainTripOb.toString();
        }
    }


//    public static Bitmap getScreenshotFromRecyclerView(RecyclerView view) {
//        RecyclerView.Adapter adapter = view.getAdapter();
//        Bitmap bigBitmap = null;
//        if (adapter != null) {
//            int size = adapter.getItemCount();
//            int height = 0;
//            Paint paint = new Paint();
//            int iHeight = 0;
//            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//
//            // Use 1/8th of the available memory for this memory cache.
//            final int cacheSize = maxMemory / 8;
//            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
//            for (int i = 0; i < size; i++) {
//                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
//                adapter.onBindViewHolder(holder, i);
//                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
//                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
//                holder.itemView.setDrawingCacheEnabled(true);
//                holder.itemView.buildDrawingCache();
//                Bitmap drawingCache = holder.itemView.getDrawingCache();
//                if (drawingCache != null) {
//
//                    bitmaCache.put(String.valueOf(i), drawingCache);
//                }
////                holder.itemView.setDrawingCacheEnabled(false);
////                holder.itemView.destroyDrawingCache();
//                height += holder.itemView.getMeasuredHeight();
//            }
//
//            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
//            Canvas bigCanvas = new Canvas(bigBitmap);
//            bigCanvas.drawColor(Color.WHITE);
//
//            for (int i = 0; i < size; i++) {
//                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
//                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
//                iHeight += bitmap.getHeight();
//                bitmap.recycle();
//            }
//
//        }
//        return bigBitmap;
//    }

    public static Bitmap getRecyclerViewScreenshot(RecyclerView view) {
        int size = view.getAdapter().getItemCount();
        RecyclerView.ViewHolder holder = view.getAdapter().createViewHolder(view, 0);
        view.getAdapter().onBindViewHolder(holder, 0);
        holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
        final Bitmap bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), holder.itemView.getMeasuredHeight() * size,
                Bitmap.Config.ARGB_8888);
        Canvas bigCanvas = new Canvas(bigBitmap);
        bigCanvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        int iHeight = 0;
        holder.itemView.setDrawingCacheEnabled(true);
        holder.itemView.buildDrawingCache();
        bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
        holder.itemView.setDrawingCacheEnabled(false);
        holder.itemView.destroyDrawingCache();
        iHeight += holder.itemView.getMeasuredHeight();
        for (int i = 1; i < size; i++) {
            view.getAdapter().onBindViewHolder(holder, i);
            holder.itemView.setDrawingCacheEnabled(true);
            holder.itemView.buildDrawingCache();
            bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
            iHeight += holder.itemView.getMeasuredHeight();
            holder.itemView.setDrawingCacheEnabled(false);
            holder.itemView.destroyDrawingCache();
        }
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                saveBitMap(bigBitmap, Environment.getExternalStorageDirectory() + "/ticket.png");
                return null;
            }
        }.execute();
        return bigBitmap;
    }


    private static void saveBitMap(Bitmap bmp, String filename) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static ProgressDialog getProgressDialog(Context context, int stringResId, boolean isCancelable, DialogInterface.OnDismissListener onDismissListener) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        if (isCancelable)
            progressDialog.setOnDismissListener(onDismissListener);
        progressDialog.setMessage(context.getString(stringResId));
        progressDialog.setTitle(null);
        View v = View.inflate(context, R.layout.custom_progress_dialog, null);
        ((TextView) v.findViewById(R.id.lodingText)).setText(stringResId);
        progressDialog.setView(v);
        progressDialog.setCancelable(isCancelable);
        progressDialog.setIndeterminate(true);
        return progressDialog;
    }

    public static void appendLog(String text) {
        /*StringBuilder str = new StringBuilder(text);
        str.append("**" + Calendar.getInstance().getTime());
        str.append("-------");

        File logFile = new File(Environment.getExternalStorageDirectory() + File.separator + "logs.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("Trip","eror in create file"+e);
            }
        }
        //  Log.d("Trip","file pathe is "+logFile.getAbsolutePath());
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(str);

            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }

    private static ImageLoader getImageLoader(Context context) {
        if (imageLoader == null || options == null) {
            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisk(true)
                    .showImageForEmptyUri(R.drawable.package_default)
                    .showImageOnFail(R.drawable.package_default)
                    .showImageOnLoading(R.drawable.package_default).build();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(options).build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);
        }

        return imageLoader;
    }

    public static void setImage(Context context, String url, ImageView imageView) {

        getImageLoader(context).displayImage(url, imageView, options);
    }

    public static ArrayList<String> getPackageCountryList() {
        return packageCountryList;
    }

    public static void setPackageCountryList(ArrayList<String> packageCountryList) {
        Utils.packageCountryList = packageCountryList;
    }

    public static String getTomorrowDate() {
        Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        String departDate = (dd + 1) + "-" + (mm + 1) + "-" + yy;
        return departDate;
    }


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    //   @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }

}
