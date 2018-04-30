package com.rezofy.views.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.database.DbHelper;
import com.rezofy.models.response_models.SearchResponse;
import com.rezofy.requests.Requests;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.activities.DateSelectorActivity;
import com.rezofy.views.activities.FlightsSearchDomesticTwowayActivity;
import com.rezofy.views.activities.FlightsSearchInternationalTwowayActivity;
import com.rezofy.views.activities.FlightsSearchOnewayActivity;
import com.rezofy.views.activities.HomeActivity;
import com.rezofy.views.activities.PlaceSearchActivity;
import com.rezofy.views.activities.RecentSearchActivity;
import com.rezofy.views.custom_views.IconTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnterFlightInfoFragment extends Fragment implements View.OnClickListener, NetworkTask.Result {
    private static final String TEXT_ERROR_MESSAGES = "errorMessages";
    private final int ID_SEARCH_FLIGHTS = 1;
    TextView cityNames, tvFlightSearchDetails;
    private View fView;
    private ScrollView sVGradient;
    private RelativeLayout rlRootLayout, travelling_class;
    private IconTextView iTVMenu, iTVClass, iTVShareApp;
    private TextView tvClassName, tvTitle, tvFrom, tvTo, tvDeparture, tvReturn;
    private View viewFromDivider, viewToDivider, viewULDeparture, viewULReturn, viewULTravellers, viewULClass;
    private LinearLayout linearLayoutFromName;
    private LinearLayout linearLayoutToName;
    private IconTextView iTVChangeIcon;
    private TextView tvToPlaceName;
    private TextView tvToAirportName;
    private TextView tvFromPlaceName;
    private TextView tvFromAirportName;
    private int noOfAdults = 1;
    private int noOfChildren = 0;
    private int noOfInfants = 0;
    private RelativeLayout rlTravellers;
    private TextView tvTravellersNos;
    private TextView tvTravellersInfo;
    private RelativeLayout rtDeparturing;
    private RelativeLayout rtReturning;
    private TextView tvSearch;
    private IconTextView iTVTravellerIcon, iTVTripType;
    private String originCityCode;
    private String destCityCode;
    private String originCountryCode;
    private String destCountryCode;
    private String originCityName;
    private String destCityName;
    private String originAirportName;
    private String destAirportName;
    private String originCountryName;
    private String destCountryName;
    private String departDate;
    private String retDate;
    private TextView tvDepartureDay;
    private TextView tvDepartureMonth;
    private TextView tvDepartureDate;
    private LinearLayout llChangingLayoutReturn;
    private IconTextView tvCrossIcon;
    private TextView tvReturnMsg;
    private TextView tvReturnDate;
    private TextView tvReturnDay;
    private TextView tvReturnMonth;
    private String serviceClassCode = Utils.SERVICE_CC_ECONOMY;
    private String type = Utils.TYPE_ONE_WAY;
    private RelativeLayout showButton;
    private LinearLayout resentSearchLayout;
    private View justView;
    private boolean isReversed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (fView == null) {
            fView = inflater.inflate(R.layout.fragment_enter_flight_info, container, false);
            init();
            setProperties();
            setListener();
        }
        return fView;
    }

    private String call() {
        System.out.println("This is a method of rnddddddddddddddddddddddddd");
        return "hello";
    }

    private void setProperties() {
        int themeContrastColor = UIUtils.getThemeContrastColor(getContext());
        UIUtils.setBackgroundImage(rlRootLayout);
        UIUtils.setBackgroundGradient(sVGradient);
        fView.findViewById(R.id.header).setBackgroundColor(UIUtils.getThemeColor(getContext()));
        iTVMenu.setTextColor(themeContrastColor);
        iTVShareApp.setTextColor(themeContrastColor);
        UIUtils.setThemeLightSelector(linearLayoutFromName);
        UIUtils.setThemeLightSelector(linearLayoutToName);
        tvTitle.setTextColor(themeContrastColor);
        tvFrom.setTextColor(themeContrastColor);
        tvTo.setTextColor(themeContrastColor);
        tvFromPlaceName.setTextColor(themeContrastColor);
        tvToPlaceName.setTextColor(themeContrastColor);
        tvFromAirportName.setTextColor(themeContrastColor);
        tvToAirportName.setTextColor(themeContrastColor);
        iTVChangeIcon.setTextColor(themeContrastColor);
        UIUtils.setThemeLightSelector(iTVChangeIcon);
        viewFromDivider.setBackgroundColor(themeContrastColor);
        viewToDivider.setBackgroundColor(themeContrastColor);
        UIUtils.setThemeLightSelector(rtDeparturing);
        UIUtils.setThemeLightSelector(rtReturning);
        tvDeparture.setTextColor(themeContrastColor);
        tvDepartureDate.setTextColor(themeContrastColor);
        tvDepartureMonth.setTextColor(themeContrastColor);
        tvDepartureDay.setTextColor(themeContrastColor);
        viewULDeparture.setBackgroundColor(themeContrastColor);
        tvReturn.setTextColor(themeContrastColor);
        tvReturnMsg.setTextColor(themeContrastColor);
        tvReturnDate.setTextColor(themeContrastColor);
        tvReturnMonth.setTextColor(themeContrastColor);
        tvReturnDay.setTextColor(themeContrastColor);
        tvCrossIcon.setTextColor(themeContrastColor);
        viewULReturn.setBackgroundColor(themeContrastColor);
        UIUtils.setThemeLightSelector(rlTravellers);
        UIUtils.setThemeLightSelector(travelling_class);
        iTVTravellerIcon.setTextColor(themeContrastColor);
        tvTravellersNos.setTextColor(themeContrastColor);
        tvTravellersInfo.setTextColor(themeContrastColor);
        viewULTravellers.setBackgroundColor(themeContrastColor);
        iTVClass.setTextColor(themeContrastColor);
        tvClassName.setTextColor(themeContrastColor);
        viewULClass.setBackgroundColor(themeContrastColor);
        UIUtils.setRoundedButtonProperties(tvSearch);
    }

    @Override
    public void onStart() {
        super.onStart();
      /*  if (AppPreferences.getInstance(getContext()).getUserData().isEmpty())
            iTVMenu.setVisibility(View.GONE);
        else
            iTVMenu.setVisibility(View.VISIBLE);*/
    }

    private String init() {
        iTVMenu = (IconTextView) fView.findViewById(R.id.header).findViewById(R.id.left_icon);
        iTVMenu.setText(getString(R.string.icon_text_h));
        iTVMenu.setOnClickListener(this);
        iTVMenu.setTextSize(20);

        iTVShareApp = (IconTextView) fView.findViewById(R.id.header).findViewById(R.id.right_icon);
        iTVShareApp.setVisibility(View.VISIBLE);
        iTVShareApp.setOnClickListener(this);
        iTVShareApp.setText(getString(R.string.icon_text_I));
        iTVShareApp.setTextSize(20);
        tvTitle = (TextView) fView.findViewById(R.id.header).findViewById(R.id.title);
        tvTitle.setText(getString(R.string.flights_text));

        originCityCode = getString(R.string.originCityCode);//Utils.CITY_CODE_DELHI;
        destCityCode = getString(R.string.destCityCode); // = Utils.CITY_CODE_MUMBAI;
        originCountryCode = getString(R.string.originCountryCode); // = Utils.COUNTRY_CODE_IN;
        destCountryCode = getString(R.string.originCountryCode); // = Utils.COUNTRY_CODE_IN;
        originCityName = getString(R.string.originCityName); // = Utils.CITY_NAME_DELHI;
        destCityName = getString(R.string.destCityName); // = Utils.CITY_NAME_MUMBAI;
        originAirportName = getString(R.string.originAirportName); // = Utils.AIRPORT_NAME_DELHI;
        destAirportName = getString(R.string.destAirportName); // = Utils.AIRPORT_NAME_MUMBAI;
        originCountryName = getString(R.string.originCountryName); // = Utils.COUNTRY_NAME_IN;
        destCountryName = getString(R.string.originCountryName); // = Utils.COUNTRY_NAME_IN;


        rlRootLayout = (RelativeLayout) fView.findViewById(R.id.root);
        sVGradient = (ScrollView) fView.findViewById(R.id.rl_childscrollview);
        travelling_class = (RelativeLayout) fView.findViewById(R.id.travelling_class);
        tvClassName = (TextView) fView.findViewById(R.id.class_name);
        linearLayoutFromName = (LinearLayout) fView.findViewById(R.id.from_name);
        linearLayoutToName = (LinearLayout) fView.findViewById(R.id.to_name);
        tvFrom = (TextView) fView.findViewById(R.id.from);
        tvFromPlaceName = (TextView) fView.findViewById(R.id.from_place_name);
        tvFromPlaceName.setText(originCityName);
        tvFromAirportName = (TextView) fView.findViewById(R.id.from_airport_name);
        tvFromAirportName.setSelected(true);
        tvFromAirportName.setText(originCityCode + " - " + originAirportName);
        tvTo = (TextView) fView.findViewById(R.id.to);
        viewFromDivider = fView.findViewById(R.id.from_divider);
        viewToDivider = fView.findViewById(R.id.to_divider);
        tvToPlaceName = (TextView) fView.findViewById(R.id.to_place_name);
        tvToAirportName = (TextView) fView.findViewById(R.id.to_airport_name);
        tvToAirportName.setSelected(true);
        tvToPlaceName.setText(destCityName);
        tvToAirportName.setText(destCityCode + " - " + destAirportName);
        tvSearch = (TextView) fView.findViewById(R.id.search);
        iTVChangeIcon = (IconTextView) fView.findViewById(R.id.change_icon);
        iTVChangeIcon.setOnClickListener(this);
        rlTravellers = (RelativeLayout) fView.findViewById(R.id.travellers);
        rlTravellers.setOnClickListener(this);
        tvTravellersNos = (TextView) fView.findViewById(R.id.travellers_nos);
        rtDeparturing = (RelativeLayout) fView.findViewById(R.id.departuring);
        rtReturning = (RelativeLayout) fView.findViewById(R.id.returning);
        tvTravellersInfo = (TextView) fView.findViewById(R.id.travellers_info);
        iTVTravellerIcon = (IconTextView) fView.findViewById(R.id.traveller_icon);
        viewULDeparture = fView.findViewById(R.id.ul_departure);
        viewULReturn = fView.findViewById(R.id.ul_return);
        viewULTravellers = fView.findViewById(R.id.ul_travellers);
        viewULClass = fView.findViewById(R.id.ul_class);
        iTVClass = (IconTextView) fView.findViewById(R.id.class_icon);

        tvDeparture = (TextView) fView.findViewById(R.id.departure);
        tvReturn = (TextView) fView.findViewById(R.id.return_text);
        tvDepartureDay = (TextView) fView.findViewById(R.id.departure_day);
        tvDepartureMonth = (TextView) fView.findViewById(R.id.departure_month);
        tvDepartureDate = (TextView) fView.findViewById(R.id.departure_date_first_way);
        setDates();
        llChangingLayoutReturn = (LinearLayout) fView.findViewById(R.id.changing_layout);
        tvCrossIcon = (IconTextView) fView.findViewById(R.id.cross_icon);
        tvReturnMsg = (TextView) fView.findViewById(R.id.return_msg);
        tvReturnDate = (TextView) fView.findViewById(R.id.return_date);
        tvReturnDay = (TextView) fView.findViewById(R.id.return_day);
        tvReturnMonth = (TextView) fView.findViewById(R.id.return_month);
        showButton = (RelativeLayout) fView.findViewById(R.id.showRecentSearch);
        cityNames = (TextView) fView.findViewById(R.id.CityNames);
        tvFlightSearchDetails = (TextView) fView.findViewById(R.id.counts);
        resentSearchLayout = (LinearLayout) fView.findViewById(R.id.recetnSearchLayout);
        iTVTripType = (IconTextView) fView.findViewById(R.id.icon_departure_way);
        justView = (View) fView.findViewById(R.id.justView);
        return "DEL";
    }

    private void setDates() {
        try {
            if (departDate == null)
                setTomorrowAsDepartDate();
            tvDepartureDate.setText(Utils.changeDateFormat(departDate, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_dd));
            tvDepartureMonth.setText(Utils.changeDateFormat(departDate, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_MMM).toUpperCase());
            setDay(tvDepartureDay, departDate);

            if (retDate != null) {
                tvReturnDate.setText(Utils.changeDateFormat(retDate, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_dd));
                tvReturnMonth.setText(Utils.changeDateFormat(retDate, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_MMM).toUpperCase());
                setDay(tvReturnDay, retDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDay(TextView tvTarget, String date) {
        try {
            Calendar calendarCurrent = Calendar.getInstance();
            Calendar calendarTargetDate = Calendar.getInstance();
            calendarTargetDate.setTime(new SimpleDateFormat(Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy).parse(date));
            if (calendarTargetDate.get(Calendar.YEAR) == calendarCurrent.get(Calendar.YEAR)
                    && calendarTargetDate.get(Calendar.DAY_OF_YEAR) == calendarCurrent.get(Calendar.DAY_OF_YEAR))
                tvTarget.setText(getString(R.string.today_text));
            else if (calendarTargetDate.get(Calendar.YEAR) == calendarCurrent.get(Calendar.YEAR)
                    && calendarTargetDate.get(Calendar.DAY_OF_YEAR) - 1 == calendarCurrent.get(Calendar.DAY_OF_YEAR))
                tvTarget.setText(getString(R.string.tomorrow_text));
            else
                tvTarget.setText(Utils.changeDateFormat(date, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_EEEE).toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setTomorrowAsDepartDate() {
        Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        departDate = (dd + 1) + "-" + (mm + 1) + "-" + yy;
    }


    private void setListener() {
        travelling_class.setOnClickListener(this);
        linearLayoutFromName.setOnClickListener(this);
        linearLayoutToName.setOnClickListener(this);
        rtDeparturing.setOnClickListener(this);
        rtReturning.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        iTVChangeIcon.setOnClickListener(this);
        rlTravellers.setOnClickListener(this);
        iTVMenu.setOnClickListener(this);
        tvCrossIcon.setOnClickListener(this);
        showButton.setOnClickListener(this);
    }

    private void setClass() {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom);

        TextView text1 = (TextView) dialog.findViewById(R.id.text);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvClassName.setText(getString(R.string.economy_class_text));
                serviceClassCode = Utils.SERVICE_CC_ECONOMY;
                dialog.dismiss();
            }
        });
        dialog.show();


        TextView text2 = (TextView) dialog.findViewById(R.id.text1);
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvClassName.setText(getString(R.string.premium_economy_text));
                serviceClassCode = Utils.SERVICE_CC_PREMIUM_ECONOMY;
                dialog.dismiss();
            }
        });
        dialog.show();

        TextView text3 = (TextView) dialog.findViewById(R.id.text2);
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvClassName.setText(getString(R.string.business_class_text));
                serviceClassCode = Utils.SERVICE_CC_BUSINESS;
                dialog.dismiss();
            }
        });
        dialog.show();

        TextView text4 = (TextView) dialog.findViewById(R.id.text3);
        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvClassName.setText(getString(R.string.first_class_text));
                serviceClassCode = Utils.SERVICE_CC_FIRST;
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Utils.PLACE_SEARCH_CODE) {
            if (!isReversed) {
                if (data.getStringExtra("tag").equals("from")) {
                    originCityCode = data.getStringExtra("airport_code");
                    originCountryCode = data.getStringExtra("country_code");
                    originCityName = data.getStringExtra("city_name");
                    originAirportName = data.getStringExtra("airport_name");
                    originCountryName = data.getStringExtra("country_name");

                    tvFromPlaceName.setText(originCityName);
                    tvFromAirportName.setText(originCityCode + " - " + data.getStringExtra("airport_name"));


                } else if (data.getStringExtra("tag").equals("to")) {
                    destCityCode = data.getStringExtra("airport_code");
                    destCountryCode = data.getStringExtra("country_code");
                    destAirportName = data.getStringExtra("airport_name");
                    destCityName = data.getStringExtra("city_name");
                    destCountryName = data.getStringExtra("country_name");
                    tvToPlaceName.setText(destCityName);
                    tvToAirportName.setText(destCityCode + " - " + data.getStringExtra("airport_name"));
                }

            } else {
                if (data.getStringExtra("tag").equals("to")) {
                    originCityCode = data.getStringExtra("airport_code");
                    originCountryCode = data.getStringExtra("country_code");
                    originCityName = data.getStringExtra("city_name");
                    originAirportName = data.getStringExtra("airport_name");
                    originCountryName = data.getStringExtra("country_name");
                    tvToPlaceName.setText(originCityName);
                    tvToAirportName.setText(originCityCode + " - " + data.getStringExtra("airport_name"));


                } else if (data.getStringExtra("tag").equals("from")) {
                    destCityCode = data.getStringExtra("airport_code");
                    destCountryCode = data.getStringExtra("country_code");
                    destAirportName = data.getStringExtra("airport_name");
                    destCityName = data.getStringExtra("city_name");
                    destCountryName = data.getStringExtra("country_name");
                    tvFromPlaceName.setText(destCityName);
                    tvFromAirportName.setText(destCityCode + " - " + data.getStringExtra("airport_name"));
                }
            }

        } else if (resultCode == Utils.DATE_SELECTOR_CODE) {
            departDate = data.getStringExtra("returnDepartureDate");
            retDate = data.getStringExtra("returnReturnDate");
            if (retDate != null) {
                type = Utils.TYPE_ROUND_TRIP;
                llChangingLayoutReturn.setVisibility(View.VISIBLE);
                tvReturnMsg.setVisibility(View.INVISIBLE);
                tvReturnDate.setVisibility(View.VISIBLE);
                tvCrossIcon.setVisibility(View.VISIBLE);

            } else {
                type = Utils.TYPE_ONE_WAY;
                llChangingLayoutReturn.setVisibility(View.INVISIBLE);
                tvReturnMsg.setVisibility(View.VISIBLE);
                tvReturnDate.setVisibility(View.INVISIBLE);
                tvCrossIcon.setVisibility(View.INVISIBLE);
            }
            setDates();
        } else if (resultCode == Utils.request_code_recentSearch) {
            searchFlights(data);

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        DbHelper dbHelper = new DbHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String SELECT_QUERY = "Select * from " + DbHelper.SEARCH_TABLE_NAME + " order By " + DbHelper.timeStamp + " desc";
        Cursor cursor = dbHelper.getData(db, SELECT_QUERY);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            if (cursor.getString(cursor.getColumnIndex(DbHelper.searchType)).equals(Utils.TYPE_ONE_WAY))
                iTVTripType.setText(getString(R.string.icon_text_N));
            else
                iTVTripType.setText(getString(R.string.icon_text_U));
            Log.d("Trip", "id is " + cursor.getInt(cursor.getColumnIndex(DbHelper._id)));
            int noOfAdults = cursor.getInt(cursor.getColumnIndex(DbHelper.noOfAdults));
            int noOfChildren = cursor.getInt(cursor.getColumnIndex(DbHelper.noOfChildren));
            int noOfInfants = cursor.getInt(cursor.getColumnIndex(DbHelper.noOfInfants));
            StringBuilder sb = new StringBuilder();
            sb.append(Utils.changeDateFormat(cursor.getString(cursor.getColumnIndex(DbHelper.departDate)), Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_d_space_LLL));
            if (cursor.getString(cursor.getColumnIndex(DbHelper.searchType)).equals(Utils.TYPE_ROUND_TRIP)) {
                sb.append(" - ");
                sb.append(Utils.changeDateFormat(cursor.getString(cursor.getColumnIndex(DbHelper.retDate)), Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_d_space_LLL));
            }
            sb.append("  |  ");
            sb.append(String.valueOf(noOfAdults + noOfChildren + noOfInfants));
            sb.append(" ");
            sb.append(UIUtils.getLogicalTravellersText(getContext(), noOfAdults, noOfChildren, noOfInfants));
            cityNames.setText(cursor.getString(cursor.getColumnIndex(DbHelper.originCityCode)) + "  -  " + cursor.getString(cursor.getColumnIndex(DbHelper.destCityCode)));
            tvFlightSearchDetails.setText(sb.toString());
            resentSearchLayout.setVisibility(View.VISIBLE);
            justView.setVisibility(View.VISIBLE);


        } else {
            resentSearchLayout.setVisibility(View.GONE);
            justView.setVisibility(View.GONE);
        }
        db.close();
        dbHelper.close();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_icon:
                swapFromAndTo();
                break;

            case R.id.travellers:
                createTravellersDialog();
                break;

            case R.id.travelling_class:
                // Toast.makeText(getActivity(), "this is class ", Toast.LENGTH_SHORT).show();
                setClass();
                break;

            case R.id.left_icon:
                ((HomeActivity) getActivity()).openDrawer();
                break;

            case R.id.right_icon:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_share_app));
                startActivity(Intent.createChooser(shareIntent, getString(R.string.text_share)));
                break;

            case R.id.from_name:
                // Toast.makeText(getActivity(), "this is class from_name", Toast.LENGTH_SHORT).show();
                Intent placeSearch = new Intent(getActivity(), PlaceSearchActivity.class);
                placeSearch.putExtra("tag", "from");
                startActivityForResult(placeSearch, Utils.PLACE_SEARCH_CODE);
                break;

            case R.id.to_name:
                // Toast.makeText(getActivity(), "this is class to_name", Toast.LENGTH_SHORT).show();
                Intent placeSearchIntent = new Intent(getActivity(), PlaceSearchActivity.class);
                placeSearchIntent.putExtra("tag", "to");
                startActivityForResult(placeSearchIntent, Utils.PLACE_SEARCH_CODE);
                break;

            case R.id.departuring:
                // Toast.makeText(getActivity(), "this is class to_name date", Toast.LENGTH_SHORT).show();
                Intent dateIntent = new Intent(getActivity(), DateSelectorActivity.class);
                dateIntent.putExtra("isDeparture", true);
                dateIntent.putExtra("isDateDeparture", departDate);
                dateIntent.putExtra("sourceTag", Utils.TAG_ENTER_FLIGHT_INFO_FRAGMENT);
                if (retDate != null)
                    dateIntent.putExtra("isDateReturn", retDate);
                startActivityForResult(dateIntent, Utils.DATE_SELECTOR_CODE);
                break;

            case R.id.returning:
                //Toast.makeText(getActivity(), "this is class to_name date", Toast.LENGTH_SHORT).show();
                Intent dateReturnIntent = new Intent(getActivity(), DateSelectorActivity.class);
                dateReturnIntent.putExtra("isDeparture", false);
                dateReturnIntent.putExtra("isDateDeparture", departDate);
                dateReturnIntent.putExtra("sourceTag", Utils.TAG_ENTER_FLIGHT_INFO_FRAGMENT);
                if (retDate != null)
                    dateReturnIntent.putExtra("isDateReturn", retDate);

                startActivityForResult(dateReturnIntent, Utils.DATE_SELECTOR_CODE);
                break;

            case R.id.cross_icon:
                type = Utils.TYPE_ONE_WAY;
                retDate = null;
                tvReturnMsg.setVisibility(View.VISIBLE);
                llChangingLayoutReturn.setVisibility(View.INVISIBLE);
                tvReturnDate.setVisibility(View.INVISIBLE);
                tvCrossIcon.setVisibility(View.INVISIBLE);
                tvCrossIcon.setVisibility(View.GONE);
                break;

            case R.id.showRecentSearch:
                Intent i = new Intent(getActivity(), RecentSearchActivity.class);
                startActivityForResult(i, Utils.request_code_recentSearch);
                break;

            case R.id.search:
                if (!originCityCode.equals(destCityCode)) {
                    Log.d("Trip", " type is " + type);
                    DbHelper dbHelper = new DbHelper(getActivity());
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    String timeStamp = Long.toString(System.currentTimeMillis());
                    dbHelper.insertOrUpdateData(originCityName, destCityName, originCityCode, destCityCode, departDate,
                            retDate, noOfAdults, noOfChildren, noOfInfants, originAirportName, destAirportName,
                            originCountryName, destCountryName, type, timeStamp, db);
                    db.close();
                    dbHelper.close();
                    if (!Utils.isNetworkAvailable(getActivity())) {
                        Utils.showAlertDialog(getActivity(), getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

                    } else {
                        String url;
                        NetworkTask networkTask = new NetworkTask(getActivity(), ID_SEARCH_FLIGHTS);
                        //networkTask.setDialogAndTaskCancellable(true);
                        networkTask.setDialogMessage(getString(R.string.please_wait));
                        networkTask.exposePostExecute(this);
                        String paramsArray[];
                        if (retDate == null) {
                            url = UIUtils.getBaseUrl(getContext()) + WebServiceConstants.urlOneWaySearch;
                            paramsArray = new String[]{url, Requests.oneWaySearchRequest(originCityCode, destCityCode, departDate, noOfAdults, noOfChildren, noOfInfants, Utils.TRIP_TYPE_FLIGHT, Utils.TYPE_ONE_WAY, serviceClassCode, Utils.CARRIER_TYPE_ANY)};

                        } else {
                            url = UIUtils.getBaseUrl(getContext()) + WebServiceConstants.urlRoundTripSearch;
                            paramsArray = new String[]{url, Requests.roundTripSearchRequest(originCityCode, destCityCode, departDate, retDate, noOfAdults, noOfChildren, noOfInfants, Utils.TRIP_TYPE_FLIGHT, Utils.TYPE_ROUND_TRIP, serviceClassCode, Utils.CARRIER_TYPE_ANY)};
                        }
                        networkTask.execute(paramsArray);
                    }

                } else
                    Toast.makeText(getContext(), getString(R.string.arr_dep_same), Toast.LENGTH_SHORT).show();

                break;

        }
    }

    private void searchFlights(Intent intent) {
        try {
            originCityCode = intent.getStringExtra("originCityCode");
            destCityCode = intent.getStringExtra("destCityCode");
            originCityName = intent.getStringExtra("originCityName");
            destCityName = intent.getStringExtra("destCityName");
            departDate = intent.getStringExtra("departDate");
            retDate = intent.getStringExtra("retDate");
            noOfAdults = intent.getIntExtra("noOfAdults", 0);
            noOfChildren = intent.getIntExtra("noOfChildren", 0);
            noOfInfants = intent.getIntExtra("noOfInfants", 0);
            originAirportName = intent.getStringExtra("originAirportName");
            destAirportName = intent.getStringExtra("destAirportName");
            originCountryName = intent.getStringExtra("originCountryName");
            destCountryName = intent.getStringExtra("destCountryName");
            type = intent.getStringExtra("type");
            Log.d("Trip", "inside searchfilghts dept date is " + departDate + " and ret date is " + retDate);
            if (updateUi()) {
                Log.d("Trip", " type is " + type);
                DbHelper dbHelper = new DbHelper(getActivity());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String timeStamp = Long.toString(System.currentTimeMillis());
                dbHelper.updateTimeStamp(originCityName, destCityName, type, timeStamp, db);
                db.close();
                dbHelper.close();

                if (!Utils.isNetworkAvailable(getActivity())) {
                    Utils.showAlertDialog(getActivity(), getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

                } else {
                    String url;
                    NetworkTask networkTask = new NetworkTask(getActivity(), ID_SEARCH_FLIGHTS);
                    networkTask.setDialogMessage(getString(R.string.please_wait));
                    networkTask.exposePostExecute(this);
                    String paramsArray[];
                    if (retDate == null) {
                        url = UIUtils.getBaseUrl(getContext()) + WebServiceConstants.urlOneWaySearch;
                        paramsArray = new String[]{url, Requests.oneWaySearchRequest(originCityCode, destCityCode, departDate, noOfAdults, noOfChildren, noOfInfants, Utils.TRIP_TYPE_FLIGHT, Utils.TYPE_ONE_WAY, serviceClassCode, Utils.CARRIER_TYPE_ANY)};

                    } else {
                        url = UIUtils.getBaseUrl(getContext()) + WebServiceConstants.urlRoundTripSearch;
                        paramsArray = new String[]{url, Requests.roundTripSearchRequest(originCityCode, destCityCode, departDate, retDate, noOfAdults, noOfChildren, noOfInfants, Utils.TRIP_TYPE_FLIGHT, Utils.TYPE_ROUND_TRIP, serviceClassCode, Utils.CARRIER_TYPE_ANY)};
                    }
                    networkTask.execute(paramsArray);
                }
            }


        } catch (Exception e) {
            Log.d("Trip", "Eror in searchFlights " + e);
        }

    }

    private boolean updateUi() {
        boolean proceed = false;
        try {
            if (!isReversed) {
                tvFromPlaceName.setText(originCityName);
                tvFromAirportName.setText(originCityCode + " - " + originAirportName);
                tvToPlaceName.setText(destCityName);
                tvToAirportName.setText(destCityCode + " - " + destAirportName);

            } else {
                tvFromPlaceName.setText(destCityName);
                tvFromAirportName.setText(destCityCode + " - " + destAirportName);
                tvToPlaceName.setText(originCityName);
                tvToAirportName.setText(originCityCode + " - " + originAirportName);
            }
            tvTravellersNos.setText(String.valueOf(noOfAdults + noOfChildren + noOfInfants));
            String travellersText = UIUtils.getLogicalTravellersText(getContext(), noOfAdults, noOfChildren, noOfInfants);
            tvTravellersInfo.setText(travellersText);

            if (travellersText.equals(getString(R.string.adults_text)) || travellersText.equals(getString(R.string.adult_text)))
                iTVTravellerIcon.setText(getString(R.string.icon_text_b));

            else if (travellersText.equals(getString(R.string.travellers_text)) && noOfAdults > 1)
                iTVTravellerIcon.setText(getString(R.string.icon_text_j));

            if (retDate == null) {
                llChangingLayoutReturn.setVisibility(View.INVISIBLE);
                tvReturnMsg.setVisibility(View.VISIBLE);
                tvReturnDate.setVisibility(View.INVISIBLE);
                tvCrossIcon.setVisibility(View.INVISIBLE);
            } else {
                llChangingLayoutReturn.setVisibility(View.VISIBLE);
                tvReturnMsg.setVisibility(View.INVISIBLE);
                tvReturnDate.setVisibility(View.VISIBLE);
                tvCrossIcon.setVisibility(View.VISIBLE);
            }
            String dateAr[] = departDate.split("-");
            long oldMillis = Utils.getTimeInMilliSeconds(dateAr[0], dateAr[1], dateAr[2]);
            Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH) + 1;
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            Log.d("Trip", "::" + dd + " : " + mm + " : " + yy);
            long currentMillis = Utils.getTimeInMilliSeconds("" + dd, "" + mm, "" + yy);
            if (oldMillis >= currentMillis) {
                proceed = true;
                setDates();
            } else {
                proceed = false;
                setTomorrowAsDepartDate();
                setDates();
            }


        } catch (Exception e) {
            Log.d("Trp", "Eror in updateUi " + e);
        } finally {
            return proceed;
        }
    }


    private void swapFromAndTo() {

        iTVChangeIcon.setEnabled(false);
        int[] from = new int[2];
        linearLayoutFromName.getLocationOnScreen(from);
        int[] to = new int[2];
        linearLayoutToName.getLocationOnScreen(to);
        final ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(iTVChangeIcon, "rotation", 0f, 360f);
        Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                rotationAnimator.start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                iTVChangeIcon.setEnabled(true);
                rotationAnimator.cancel();
                interchangeOriginAndDestination();
                isReversed = !isReversed;

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
        linearLayoutFromName.animate().yBy(to[1] - from[1]);
        linearLayoutToName.animate().yBy(from[1] - to[1]).setListener(animatorListener);
    }

    private void interchangeOriginAndDestination() {


        String tempCityCode = originCityCode;
        String tempCountryCode = originCountryCode;
        String tempCityName = originCityName;
        String tempAirportName = originAirportName;
        String tempCountryName = originCountryName;

        originCityCode = destCityCode;
        originCountryCode = destCountryCode;
        originCityName = destCityName;
        originAirportName = destAirportName;
        originCountryName = destCountryName;

        destCityCode = tempCityCode;
        destCountryCode = tempCountryCode;
        destAirportName = tempAirportName;
        destCityName = tempCityName;
        destCountryName = tempCountryName;


    }

    private void createTravellersDialog() {
        Dialog travellersDialog = new Dialog(getActivity());
        travellersDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        travellersDialog.setContentView(R.layout.dialog_traveller_info);
        travellersDialog.setCancelable(true);
        travellersDialog.getWindow().setLayout(Utils.getScreenSize(getActivity())[0] - 60, ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout llTravellerIcons = (LinearLayout) travellersDialog.findViewById(R.id.travellers_icons);
        View.OnClickListener clickListener = getDialogClicksListener(travellersDialog);
        TextView tvAdultDecrement = (TextView) travellersDialog.findViewById(R.id.adult_decrement);
        tvAdultDecrement.setOnClickListener(clickListener);
        TextView tvAdultIncrement = (TextView) travellersDialog.findViewById(R.id.adult_increment);
        tvAdultIncrement.setOnClickListener(clickListener);
        TextView tvChildrenDecrement = (TextView) travellersDialog.findViewById(R.id.children_decrement);
        tvChildrenDecrement.setOnClickListener(clickListener);
        TextView tvChildrenIncrement = (TextView) travellersDialog.findViewById(R.id.children_increment);
        tvChildrenIncrement.setOnClickListener(clickListener);
        TextView tvInfantsDecrement = (TextView) travellersDialog.findViewById(R.id.infant_decrement);
        tvInfantsDecrement.setOnClickListener(clickListener);
        TextView tvInfantsIncrement = (TextView) travellersDialog.findViewById(R.id.infant_increment);
        tvInfantsIncrement.setOnClickListener(clickListener);
        TextView tvOk = (TextView) travellersDialog.findViewById(R.id.ok);
        tvOk.setOnClickListener(clickListener);
        TextView tvAdultNos = (TextView) travellersDialog.findViewById(R.id.adult_no);
        tvAdultNos.setText(String.valueOf(noOfAdults));
        TextView tvChildrenNos = (TextView) travellersDialog.findViewById(R.id.children_no);
        tvChildrenNos.setText(String.valueOf(noOfChildren));
        TextView tvInfantNos = (TextView) travellersDialog.findViewById(R.id.infant_no);
        tvInfantNos.setText(String.valueOf(noOfInfants));
        manageLLTravellerIcons(llTravellerIcons, noOfAdults, noOfChildren);
        travellersDialog.show();

    }

    private View.OnClickListener getDialogClicksListener(final Dialog travellersDialog) {
        final LinearLayout llTravellerIcons = (LinearLayout) travellersDialog.findViewById(R.id.travellers_icons);
        final TextView tvAdultNos = (TextView) travellersDialog.findViewById(R.id.adult_no);
        tvAdultNos.setTextColor(UIUtils.getThemeColor(getContext()));
        final TextView tvChildrenNos = (TextView) travellersDialog.findViewById(R.id.children_no);
        tvChildrenNos.setTextColor(UIUtils.getThemeColor(getContext()));
        final TextView tvInfantNos = (TextView) travellersDialog.findViewById(R.id.infant_no);
        tvInfantNos.setTextColor(UIUtils.getThemeColor(getContext()));
        return new View.OnClickListener() {
            int dialogNoOfAdults = noOfAdults;
            int dialogNoOfChildren = noOfChildren;
            int dialogNoOfInfants = noOfInfants;

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.adult_decrement:
                        if (dialogNoOfAdults > 1 && dialogNoOfAdults > dialogNoOfInfants) {
                            tvAdultNos.setText(String.valueOf(--dialogNoOfAdults));
                            manageLLTravellerIcons(llTravellerIcons, dialogNoOfAdults, dialogNoOfChildren);

                        } else if (dialogNoOfAdults == dialogNoOfInfants) {
                            Toast.makeText(getContext(), getString(R.string.max_infants), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.adult_increment:
                        if ((dialogNoOfAdults + dialogNoOfChildren) < 9) {
                            tvAdultNos.setText(String.valueOf(++dialogNoOfAdults));
                            manageLLTravellerIcons(llTravellerIcons, dialogNoOfAdults, dialogNoOfChildren);
                            if ((dialogNoOfAdults + dialogNoOfChildren) == 9)
                                Toast.makeText(getContext(), getString(R.string.upto_max_passenger), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.max_passenger), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.children_decrement:
                        if (dialogNoOfChildren > 0) {
                            tvChildrenNos.setText(String.valueOf(--dialogNoOfChildren));
                            manageLLTravellerIcons(llTravellerIcons, dialogNoOfAdults, dialogNoOfChildren);
                        }
                        break;

                    case R.id.children_increment:
                        if ((dialogNoOfAdults + dialogNoOfChildren) < 9) {
                            tvChildrenNos.setText(String.valueOf(++dialogNoOfChildren));
                            manageLLTravellerIcons(llTravellerIcons, dialogNoOfAdults, dialogNoOfChildren);
                            if ((dialogNoOfAdults + dialogNoOfChildren) == 9)
                                Toast.makeText(getContext(), getString(R.string.upto_max_passenger), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.max_passenger), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.infant_decrement:
                        if (dialogNoOfInfants > 0) {
                            tvInfantNos.setText(String.valueOf(--dialogNoOfInfants));
                            manageLLTravellerIcons(llTravellerIcons, dialogNoOfAdults, dialogNoOfChildren);
                        }
                        break;

                    case R.id.infant_increment:
                        if (dialogNoOfInfants < dialogNoOfAdults) {
                            tvInfantNos.setText(String.valueOf(++dialogNoOfInfants));
                            manageLLTravellerIcons(llTravellerIcons, dialogNoOfAdults, dialogNoOfChildren);
                        } else {
                            Toast.makeText(getContext(), getString(R.string.max_infants), Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case R.id.ok:
                        noOfAdults = dialogNoOfAdults;
                        noOfChildren = dialogNoOfChildren;
                        noOfInfants = dialogNoOfInfants;
                        tvTravellersNos.setText(String.valueOf(noOfAdults + noOfChildren + noOfInfants));
                        if (noOfChildren == 0 && noOfInfants == 0 && noOfAdults > 1) {
                            iTVTravellerIcon.setText(getString(R.string.icon_text_b));
                            tvTravellersInfo.setText(getString(R.string.adults_text));

                        } else if (noOfChildren == 0 && noOfInfants == 0 && noOfAdults == 1) {
                            iTVTravellerIcon.setText(getString(R.string.icon_text_b));
                            tvTravellersInfo.setText(getString(R.string.adult_text));

                        } else if ((noOfChildren != 0 || noOfInfants != 0) && noOfAdults >= 1) {
                            if (noOfAdults > 1)
                                iTVTravellerIcon.setText(getString(R.string.icon_text_j));
                            tvTravellersInfo.setText(getString(R.string.travellers_text));
                        }
                        travellersDialog.dismiss();
                        break;

                }
            }
        };
    }

    private void manageLLTravellerIcons(final LinearLayout llTravellerIcons, int dialogNoOfAdults, int dialogNoOfChildren) {
        llTravellerIcons.removeAllViews();
        for (int i = 1; i <= dialogNoOfAdults; i++) {
            IconTextView iTVAdult = new IconTextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(-10, 0, -10, 0);
            iTVAdult.setLayoutParams(params);
            iTVAdult.setGravity(Gravity.CENTER);
            iTVAdult.setText("b");
            iTVAdult.setTextColor(getResources().getColor(R.color.grey_first));
            iTVAdult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
            llTravellerIcons.addView(iTVAdult);
        }
        for (int i = 0; i < dialogNoOfChildren; i++) {
            IconTextView iTVAdult = new IconTextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(-5, 0, -5, 0);
            iTVAdult.setLayoutParams(params);
            iTVAdult.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            iTVAdult.setText("b");
            iTVAdult.setTextColor(getResources().getColor(R.color.grey_first));
            iTVAdult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            llTravellerIcons.addView(iTVAdult);
        }
    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {
        if (object == null || object.equals("")) {
            Utils.showAlertDialog(getActivity(), getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else {
            Log.d("Trip", "flight data is " + object);
            if (object.contains(TEXT_ERROR_MESSAGES)) {
                SearchResponse searchResponse = new Gson().fromJson(object, SearchResponse.class);
                StringBuilder sb = new StringBuilder();
                sb.append(searchResponse.getErrorMessages().get(0));

                if (searchResponse.getData() != null && searchResponse.getData().getSearchId() != null) {
                    sb.append("\n\nReference Id : ");
                    sb.append(searchResponse.getData().getSearchId());
                }
                Utils.showAlertDialog(getContext(), getString(R.string.app_name), sb.toString(), getString(R.string.ok_text), null, null, null);

            } else if (object.contains(Utils.TEXT_ERROR)) {
                Utils.showAlertDialog(getContext(), getString(R.string.app_name), getString(R.string.error_msg), getString(R.string.ok_text), null, null, null);

            } else if (Utils.writeToLargeDataFile(getContext(), object, Utils.LARGE_DATA_FILE_NAME)) {
                Intent i;
                if (!originCountryCode.equals(getString(R.string.originCountryCode)) || !destCountryCode.equals(getString(R.string.originCountryCode))) {
                    if (type.equals(Utils.TYPE_ROUND_TRIP)) {
                        i = new Intent(getActivity(), FlightsSearchInternationalTwowayActivity.class);

                    } else {
                        i = new Intent(getActivity(), FlightsSearchOnewayActivity.class);//Will be used for Domenstic and International Oneway Searches
                    }

                } else {
                    if (type.equals(Utils.TYPE_ROUND_TRIP)) {
                        i = new Intent(getActivity(), FlightsSearchDomesticTwowayActivity.class);
                    } else {
                        i = new Intent(getActivity(), FlightsSearchOnewayActivity.class);
                    }
                }
                i.putExtra("originCityName", originCityName);
                i.putExtra("destCityName", destCityName);
                i.putExtra("originCityCode", originCityCode);
                i.putExtra("destCityCode", destCityCode);
                i.putExtra("departDate", departDate);
                i.putExtra("retDate", retDate);
                i.putExtra("noOfAdults", noOfAdults);
                i.putExtra("noOfChildren", noOfChildren);
                i.putExtra("noOfInfants", noOfInfants);
                i.putExtra("originAirportName", originAirportName);
                i.putExtra("destAirportName", destAirportName);
                i.putExtra("originCountryName", originCountryName);
                i.putExtra("destCountryName", destCountryName);
                i.putExtra("originCountryCode", originCountryCode);
                i.putExtra("destCountryCode", destCountryCode);
                Utils.show_saved_filters = false;
                startActivity(i);

            } else {
                Utils.showAlertDialog(getContext(), getString(R.string.app_name), getString(R.string.memory_full), getString(R.string.ok_text), null, null, null);
            }
        }
    }
}