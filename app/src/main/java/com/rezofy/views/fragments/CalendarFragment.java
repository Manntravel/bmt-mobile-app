package com.rezofy.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.adapters.CalendarAdapter;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.custom_views.IconTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CalendarFragment extends Fragment implements View.OnClickListener {
    private View view;
    private RecyclerView rcViewCalander;
    private LinearLayoutManager mLayoutManager;
    private CalendarAdapter adapter;
    private LinearLayout llDeparture;
    private RelativeLayout rlReturn, rlReturnBase;
    private TextView tvDate, tvMonth, tvDay, tvDeparture, tvReturn;
    private TextView tvDateReturn, tvMonthReturn, tvDayReturn, tvSelectReturn;
    private View stripDeparture, stripReturn;
    private IconTextView ivCross;
    private boolean isDeparture;
    private String startDate, endDate, sourceTag;
    private int trip;
    private int scrollPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.calander_layout, container, false);
        init();
        startDate = getArguments().getString("isDatDeparture");
        endDate = getArguments().getString("isDatReturn");
        sourceTag = getArguments().getString("sourceTag");
        isDeparture = getArguments().getBoolean("isDep");
        setTabViews();
        setScrollPosition();

        if (isDeparture)
            trip = 0;
        else
            trip = 1;

        adapter = new CalendarAdapter(getActivity(), isDeparture, startDate, endDate, trip);
        rcViewCalander.setAdapter(adapter);

        mLayoutManager.scrollToPositionWithOffset(0, 2);
        setProperties();
        setSourceTagLayout();
        return view;
    }

    private void setProperties() {
        int themeContrastColor = UIUtils.getThemeContrastColor(getActivity());
        UIUtils.setBackgroundToThemeColor(view.findViewById(R.id.header));
        tvDeparture.setTextColor(themeContrastColor);
        tvDate.setTextColor(themeContrastColor);
        tvMonth.setTextColor(themeContrastColor);
        tvDay.setTextColor(themeContrastColor);
        tvSelectReturn.setTextColor(themeContrastColor);
        tvReturn.setTextColor(themeContrastColor);
        tvDateReturn.setTextColor(themeContrastColor);
        tvMonthReturn.setTextColor(themeContrastColor);
        tvDayReturn.setTextColor(themeContrastColor);
        ivCross.setTextColor(themeContrastColor);
        stripDeparture.setBackgroundColor(themeContrastColor);
        stripReturn.setBackgroundColor(themeContrastColor);
    }

    public void setTabViews() {
        if (isDeparture)
            makeStripGone(stripReturn);
        else {
            makeStripGone(stripDeparture);
            if (endDate == null)
                setNextDayAsEndDate();
        }
        setDates();
        handleReturnTab();
    }

    public void setNextDayAsEndDate() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat(Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy).parse(startDate));
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            endDate = (dd + 1) + "-" + (mm + 1) + "-" + yy;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDates() {
        try {
            tvDate.setText(Utils.changeDateFormat(startDate, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_dd));
            tvMonth.setText(Utils.changeDateFormat(startDate, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_MMM).toUpperCase());
            setDay(tvDay, startDate);

            if (endDate != null) {
                tvDateReturn.setText(Utils.changeDateFormat(endDate, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_dd));
                tvMonthReturn.setText(Utils.changeDateFormat(endDate, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_MMM).toUpperCase());
                setDay(tvDayReturn, endDate);
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

    private void makeStripGone(View strip) {
        if(!sourceTag.equals(Utils.TAG_ENQUIRY_FRAGMENT)) {
            stripDeparture.setVisibility(View.VISIBLE);
        }
        stripReturn.setVisibility(View.VISIBLE);
        strip.setVisibility(View.GONE);
    }

    private void handleReturnTab() {
        if (endDate != null) {
            ivCross.setVisibility(View.VISIBLE);
            rlReturn.setVisibility(View.VISIBLE);
            tvSelectReturn.setVisibility(View.GONE);


        } else {
            ivCross.setVisibility(View.GONE);
            rlReturn.setVisibility(View.GONE);
            tvSelectReturn.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        rcViewCalander = (RecyclerView) view.findViewById(R.id.rcViewCalander);
        rcViewCalander.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rcViewCalander.setLayoutManager(mLayoutManager);

        llDeparture = (LinearLayout) view.findViewById(R.id.layout_departure);
        rlReturn = (RelativeLayout) view.findViewById(R.id.layout_return);
        tvDeparture= (TextView) view.findViewById(R.id.tvdeparture);
        tvReturn=(TextView) view.findViewById(R.id.tvreturn);
        tvDate = (TextView) view.findViewById(R.id.tvdate);
        tvMonth = (TextView) view.findViewById(R.id.tvMonth);
        tvDay = (TextView) view.findViewById(R.id.tvday);
        stripDeparture = view.findViewById(R.id.tabStrip1);

        rlReturnBase = (RelativeLayout) view.findViewById(R.id.rl_return_base);
        tvDateReturn = (TextView) view.findViewById(R.id.tvdate_return);
        tvMonthReturn = (TextView) view.findViewById(R.id.tvMonth_return);
        tvDayReturn = (TextView) view.findViewById(R.id.tvday_return);
        stripReturn = view.findViewById(R.id.tabStrip2);
        ivCross = (IconTextView) view.findViewById(R.id.iv_cross_button);
        tvSelectReturn = (TextView) view.findViewById(R.id.tv_select_return);

        rlReturn.setOnClickListener(this);
        llDeparture.setOnClickListener(this);
        tvSelectReturn.setOnClickListener(this);
        ivCross.setOnClickListener(this);
    }

    private void setScrollPosition() {
        try {
            Calendar calCurrent = Calendar.getInstance();
            if (isDeparture) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new SimpleDateFormat(Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy).parse(startDate));
                scrollPosition = cal.get(Calendar.MONTH)-calCurrent.get(Calendar.MONTH);
            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new SimpleDateFormat(Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy).parse(endDate));
                scrollPosition = cal.get(Calendar.MONTH)-calCurrent.get(Calendar.MONTH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(String s, String e, int trip) {
        startDate = s;
        endDate = e;
        this.trip = trip;
        setTabViews();
        adapter.setDeparture(isDeparture);
        adapter.setStartDate(s);
        adapter.setEndDate(e);
        adapter.setDaysOfTrip(trip);
        adapter.notifyDataSetChanged();

        setScrollPosition();
        mLayoutManager.scrollToPositionWithOffset(scrollPosition, 2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_departure:
                isDeparture = true;
                setTabViews();
                break;

            case R.id.layout_return:
                isDeparture = false;
                setTabViews();
                break;

            case R.id.tv_select_return:
                ivCross.setVisibility(View.VISIBLE);
                rlReturn.setVisibility(View.VISIBLE);
                tvSelectReturn.setVisibility(View.GONE);
                stripDeparture.setVisibility(View.VISIBLE);
                stripReturn.setVisibility(View.GONE);
                isDeparture = false;
                endDate = adapter.addDays(startDate, 1, true);
                trip = 1;
                break;

            case R.id.iv_cross_button:
                ivCross.setVisibility(View.GONE);
                rlReturn.setVisibility(View.GONE);
                tvSelectReturn.setVisibility(View.VISIBLE);
                stripDeparture.setVisibility(View.GONE);
                stripReturn.setVisibility(View.VISIBLE);
                isDeparture = true;
                endDate = null;
                trip = 0;
                break;
        }
        update(startDate, endDate, trip);
        //setTabViews();
        //rcViewCalander.setAdapter(new CalendarAdapter(getActivity(), isDeparture, startDate, endDate, trip));
        //mLayoutManager.scrollToPositionWithOffset(scrollPosition, 2);
    }

    public String returnSelectedDepartureDate() {
        String returnDate = startDate;
        return returnDate;
    }


    public String returnSelectedReturnDate() {
        String returnDate = endDate;
        return returnDate;
    }

    private void setSourceTagLayout() {
        if(sourceTag.equals(Utils.TAG_ENQUIRY_FRAGMENT)) {
            tvDeparture.setVisibility(View.GONE);
            stripDeparture.setVisibility(View.GONE);
            rlReturnBase.setVisibility(View.GONE);
        }
    }
}