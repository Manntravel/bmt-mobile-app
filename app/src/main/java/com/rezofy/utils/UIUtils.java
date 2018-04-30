package com.rezofy.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.models.response_models.FlightData;
import com.rezofy.views.activities.SplashActivity;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by linchpin11192 on 27-Jan-2016.
 */
public class UIUtils {

    public static Comparator<FlightData> priceIncComparator = new Comparator<FlightData>() {

        @Override
        public int compare(FlightData lhs, FlightData rhs) {
            int lhsPrice = Math.round(lhs.getFare().getSellingPrice());
            int rhsPrice = Math.round(rhs.getFare().getSellingPrice());
            return lhsPrice - rhsPrice;
        }
    };

    public static Comparator<FlightData> priceDecComparator = new Comparator<FlightData>() {

        @Override
        public int compare(FlightData lhs, FlightData rhs) {
            int lhsPrice = Math.round(lhs.getFare().getSellingPrice());
            int rhsPrice = Math.round(rhs.getFare().getSellingPrice());
            return rhsPrice - lhsPrice;
        }
    };

    public static Comparator<FlightData> durationIncComparator = new Comparator<FlightData>() {

        @Override
        public int compare(FlightData lhs, FlightData rhs) {
            int lhsDuration = Integer.parseInt(lhs.getSegments().get(0).getDuration());
            int rhsDuration = Integer.parseInt(rhs.getSegments().get(0).getDuration());
            return lhsDuration - rhsDuration;
        }
    };

    public static Comparator<FlightData> durationDecComparator = new Comparator<FlightData>() {

        @Override
        public int compare(FlightData lhs, FlightData rhs) {
            int lhsDuration = Integer.parseInt(lhs.getSegments().get(0).getDuration());
            int rhsDuration = Integer.parseInt(rhs.getSegments().get(0).getDuration());
            return rhsDuration - lhsDuration;
        }
    };

    public static Comparator<FlightData> timeIncComparator = new Comparator<FlightData>() {

        @Override
        public int compare(FlightData lhs, FlightData rhs) {
            int lhsTime = Integer.parseInt(lhs.getSegments().get(0).getLegs().get(0).getDepartureTime());
            int rhsTime = Integer.parseInt(rhs.getSegments().get(0).getLegs().get(0).getDepartureTime());
            return lhsTime - rhsTime;
        }
    };

    public static Comparator<FlightData> timeDecComparator = new Comparator<FlightData>() {

        @Override
        public int compare(FlightData lhs, FlightData rhs) {
            int lhsTime = Integer.parseInt(lhs.getSegments().get(0).getLegs().get(0).getDepartureTime());
            int rhsTime = Integer.parseInt(rhs.getSegments().get(0).getLegs().get(0).getDepartureTime());
            return rhsTime - lhsTime;
        }
    };

    public static String getTravellersText(Context context, int noOfAdults, int noOfChildren, int noOfInfants) {
        String text = "";
        text = text.concat(noOfAdults + " ");
        if (noOfAdults == 1)
            text = text.concat(context.getString(R.string.text_small_adult));
        else
            text = text.concat(context.getString(R.string.text_small_adults));
        if (noOfChildren != 0) {
            text = text.concat(", " + noOfChildren + " ");
            if (noOfChildren == 1)
                text = text.concat(context.getString(R.string.text_small_child));
            else
                text = text.concat(context.getString(R.string.text_small_children));
        }
        if (noOfInfants != 0) {
            text = text.concat(", " + noOfInfants + " ");
            if (noOfInfants == 1)
                text = text.concat(context.getString(R.string.text_small_infant));
            else
                text = text.concat(context.getString(R.string.text_small_infants));
        }
        return text;
    }

    public static String getFlightType(int noOfStops) {
        if (noOfStops == 0)
            return "Non Stop";
        else if (noOfStops == 1)
            return "1 Stop";
        else
            return noOfStops + " Stops";
    }

    public static String getTimeToDisplay(String time) {
        StringBuilder sb = new StringBuilder();
        sb.append(time.substring(0, 2));
        sb.append(":");
        sb.append(time.substring(2));
        return sb.toString();
    }

    public static String getOriginalFareToDisplay(Context context, float fare) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(R.string.rupee_symbol));
        sb.append(NumberFormat.getInstance(Locale.getDefault()).format(fare));
        return sb.toString();
    }

    public static String getOriginalFareToDisplay(Context context, String fare) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(R.string.rupee_symbol));
        sb.append(NumberFormat.getInstance(Locale.getDefault()).format(Float.parseFloat(fare)));
        return sb.toString();
    }

    public static String getFareToDisplay(Context context, float fare) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(R.string.rupee_symbol));
        sb.append(NumberFormat.getInstance(Locale.getDefault()).format(Math.round(Math.ceil(fare))));
        return sb.toString();
    }

    public static String getFareToDisplay(Context context, String fare) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(R.string.rupee_symbol));
        sb.append(NumberFormat.getInstance(Locale.getDefault()).format(Math.round(Math.ceil(Float.parseFloat(fare)))));
        return sb.toString();
    }

    public static String setAmountOnly(Context context, String fare){
        StringBuilder sb = new StringBuilder();
        sb.append(fare);
        sb.append(context.getString(R.string.amount_only));
        return sb.toString();
    }

    public static String getFlightToDisplay(String carrier, String flightNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append(carrier);
        sb.append(" ");
        sb.append(flightNumber);
        return sb.toString();
    }

    public static void setFareProperties(View view) {
        Context context = view.getContext();
        view.setBackgroundColor(Color.parseColor(context.getString(R.string.default_fare_bg_color)));
        if (view instanceof TextView)
            ((TextView) view).setTextColor(Color.parseColor(context.getString(R.string.default_fare_text_color)));
    }

    public static void setRoundedButtonProperties(View view) {
        Context context = view.getContext();
//        StateListDrawable stateListDrawable = new StateListDrawable();
//
//        GradientDrawable gDrawablePressed = (GradientDrawable) context.getResources().getDrawable(R.drawable.bg_search_rounded_pressed);
//        gDrawablePressed.setColor(Color.parseColor(context.getString(R.string.default_button_pressed_color)));
//        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, gDrawablePressed);
//
//        GradientDrawable gDrawableNormal = (GradientDrawable) context.getResources().getDrawable(R.drawable.bg_search_rounded);
//        gDrawableNormal.setColor(Color.parseColor(context.getString(R.string.default_button_color)));
//        stateListDrawable.addState(new int[]{}, gDrawableNormal);
//        view.setBackgroundDrawable(stateListDrawable);
//        if (view instanceof TextView)
//            ((TextView) view).setTextColor(Color.parseColor(context.getString(R.string.default_button_text_color)));

        setRoundedButtonProperties(view, Color.parseColor(context.getString(R.string.default_button_pressed_color)),
                Color.parseColor(context.getString(R.string.default_button_color)));
    }

    public static void setRoundedButtonProperties(View view, int pressedColor, int color) {
        Context context = view.getContext();
        StateListDrawable stateListDrawable = new StateListDrawable();

        GradientDrawable gDrawablePressed = (GradientDrawable) context.getResources().getDrawable(R.drawable.bg_search_rounded_pressed);
        gDrawablePressed.setColor(pressedColor);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, gDrawablePressed);

        GradientDrawable gDrawableNormal = (GradientDrawable) context.getResources().getDrawable(R.drawable.bg_search_rounded);
        gDrawableNormal.setColor(color);
        stateListDrawable.addState(new int[]{}, gDrawableNormal);
        view.setBackgroundDrawable(stateListDrawable);
        if (view instanceof TextView)
            ((TextView) view).setTextColor(Color.parseColor(context.getString(R.string.default_button_text_color)));
    }

    public static void setThemeLightSelector(View view) {
        Context context = view.getContext();
        StateListDrawable stateListDrawable = new StateListDrawable();

        GradientDrawable gDrawablePressed = (GradientDrawable) context.getResources().getDrawable(R.drawable.bg_home_button);
        gDrawablePressed.setColor(Color.parseColor(context.getString(R.string.theme_light_color)));
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, gDrawablePressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, gDrawablePressed);
        view.setBackgroundDrawable(stateListDrawable);
    }

    public static void setNormalButtonProperties(View view) {
        Context context = view.getContext();
        StateListDrawable stateListDrawable = new StateListDrawable();

        GradientDrawable gDrawablePressed = (GradientDrawable) context.getResources().getDrawable(R.drawable.bg_search_normal_pressed);
        gDrawablePressed.setColor(Color.parseColor(context.getString(R.string.default_button_pressed_color)));
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, gDrawablePressed);

        GradientDrawable gDrawableNormal = (GradientDrawable) context.getResources().getDrawable(R.drawable.bg_search_normal);
        gDrawableNormal.setColor(Color.parseColor(context.getString(R.string.default_button_color)));
        stateListDrawable.addState(new int[]{}, gDrawableNormal);
        view.setBackgroundDrawable(stateListDrawable);
        if (view instanceof TextView)
            ((TextView) view).setTextColor(Color.parseColor(context.getString(R.string.default_button_text_color)));
    }

    public static void setWindowBackground(Activity activity) {
        if (activity instanceof SplashActivity && activity.getString(R.string.default_splash_image_status).equals(activity.getString(R.string.true_string)))
            activity.getWindow().setBackgroundDrawableResource(R.drawable.splashbackgroundimage);
        else if (activity.getString(R.string.default_image_status).equals(activity.getString(R.string.true_string)))
            activity.getWindow().setBackgroundDrawableResource(R.drawable.loginbackgroundimage);
    }

    public static void setBackgroundImage(View view) {
        Context context = view.getContext();
        if (context.getString(R.string.default_image_status).equals(context.getString(R.string.true_string)))
            view.setBackgroundResource(R.drawable.loginbackgroundimage);
    }

    public static void setBackgroundGradient(View view) {
        Context context = view.getContext();
        GradientDrawable vrGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor(context.getString(R.string.default_pri_color)),
                        Color.parseColor(context.getString(R.string.default_sec_color)),
                        Color.parseColor(context.getString(R.string.default_ter_color))});
        vrGradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        view.setBackgroundDrawable(vrGradientDrawable);
    }

    public static void setBackgroundGradientBottomToTop(View view) {
        Context context = view.getContext();
        GradientDrawable vrGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                new int[]{Color.parseColor(context.getString(R.string.default_pri_color)),
                        Color.parseColor(context.getString(R.string.default_sec_color)),
                        Color.parseColor(context.getString(R.string.default_ter_color))});
        vrGradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        view.setBackgroundDrawable(vrGradientDrawable);
    }

    public static int getThemeColor(Context context) {
        return Color.parseColor(context.getString(R.string.theme_color));
    }

    public static int getThemeColorWithOp65(Context context) {
        String opt_color = "#";
        String theme_color = context.getString(R.string.theme_color).substring(1);
        opt_color = opt_color.concat("A6").concat(theme_color);
        return Color.parseColor(opt_color);
    }

    public static int getThemeLightColor(Context context) {
        return Color.parseColor(context.getString(R.string.theme_light_color));
    }

    public static int getThemeContrastColor(Context context) {
        return Color.parseColor(context.getString(R.string.theme_contrast_color));
    }


    public static void setBackgroundToThemeColor(View view) {
        view.setBackgroundColor(getThemeColor(view.getContext()));
    }

    public static void setTextToThemeContrastColor(TextView textView) {
        int themeContrastColor = getThemeContrastColor(textView.getContext());
        textView.setTextColor(themeContrastColor);
        textView.setHintTextColor(themeContrastColor);
    }

    public static void setStrokeToThemeContrastColor(View view) {
        GradientDrawable gDrawable = (GradientDrawable) view.getBackground();
        gDrawable.setStroke(2, getThemeContrastColor(view.getContext()));
    }


    public static String getLogicalTravellersText(Context context, int noOfAdults, int noOfChildren, int noOfInfants) {
        if (noOfChildren == 0 && noOfInfants == 0 && noOfAdults > 1) {
            return context.getString(R.string.adults_text);

        } else if (noOfChildren == 0 && noOfInfants == 0 && noOfAdults == 1) {
            return context.getString(R.string.adult_text);

        } else if ((noOfChildren != 0 || noOfInfants != 0) && noOfAdults >= 1) {
            return context.getString(R.string.travellers_text);

        } else {
            return null;
        }
    }

    public static String getBaseUrl(Context context) {
        return context.getString(R.string.base_url);
    }

    public static String getBaseUrl2(Context context) {
        return context.getString(R.string.base_url2);
    }

    /*public static String getBaseUrl3(Context context) {
        return context.getString(R.string.base_url3);
    }*/

    public static List<String> getOptionIconsList(Context context) {
        return Arrays.asList(context.getString(R.string.text_option_icon_mapper).split(","));
    }

    public static List<String> getOptionURLsList(Context context) {
        return Arrays.asList(context.getString(R.string.text_option_url_mapper).split(","));
    }

    public static List<String> getOptionNamesList(Context context) {
        return Arrays.asList(context.getString(R.string.text_option_name_mapper).split(","));
    }
}
