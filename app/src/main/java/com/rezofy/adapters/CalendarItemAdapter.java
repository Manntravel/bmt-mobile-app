package com.rezofy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;


public class CalendarItemAdapter extends BaseAdapter {
    private static final int DAY_OFFSET = 1;
    private final Context context;
    private int index = 0;
    private int sDay, sMonth, sYear;
    private int eDay, eMonth, eYear;
    private String departureDate, returnDate;
    private int departureDay = 0, returnDay = 6;

    public static List<String> list;
    private final String[] weekdays = new String[]{"Su", "Mo", "Tu",
            "Wed", "Thu", "Fri", "Sat"};
    private final String[] months = {"January", "February", "March",
            "April", "May", "June", "July", "August", "September",
            "October", "November", "December"};
    private final static int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
            31, 30, 31};

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
            Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy);
    private String tag;
    private int daysInMonth;
    private int currentDayOfMonth;
    private int currentWeekDay;
    private int currentMonth, currentYear;
    private static int startMonth, startYear;
    private TextView tv_calendarDay;
    private RelativeLayout gridcell;

    // Days in Current Month
//    public CalendarItemAdapter(Context context, int date,
//                               int month, int year) {
//        super();
//        tag = context.getString(R.string.app_name);
//        this.context = context;
//
//        this.list = new ArrayList<String>();
//
//        Calendar calendar = Calendar.getInstance();
//
//        //setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
//        //setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
//        //setCurrentMonth(calendar.get(Calendar.MONTH));
//        Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
//        Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
//        Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());
//        currentDayOfMonth = date;
//        currentYear = year;
//        currentMonth = month - 1;
//        startMonth = month - 1;
//        startYear = year;
//        // Print Month
//        printMonth();
//
//    }

    public CalendarItemAdapter(Context activity, int index, String s, String e) {
        context = (Context) activity;
        this.index = index;

        Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("Trip","row no is "+index+" : "+dd+" : "+mm+" : "+yy);
        departureDay = dd;
        if (s == null) {
            departureDate = dd + "-" + mm + 1 + "-" + yy;
        } else {
            departureDate = s;
        }
        returnDate = e;
        returnDay = -1;
        currentDayOfMonth = dd;
        currentYear = yy+((index + mm) / 12);
        currentMonth = (index + mm) % 12;
        Log.d("Trip","yeaer "+currentYear+" month "+currentMonth);
        startMonth = mm;
//        startYear = yy;
        setDates();
        printMonth();
    }

    public CalendarItemAdapter(Context activity, int index) {
        context = (Context) activity;
        this.index = index;
        Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        currentDayOfMonth = dd;
        currentYear = yy+((index + mm) / 12);
        currentMonth = (index + mm) % 12;
        Log.d("Trip","yeaer "+currentYear+" month "+currentMonth);
//        startMonth = mm;
//        startYear = yy;
        //  setDates();
        printMonth();
    }

    private void setDates() {
        try {
            String date[] = departureDate.split("-");
            sDay = Integer.parseInt(date[0]);
            sMonth = Integer.parseInt(date[1]) - 1;
            sYear = Integer.parseInt(date[2]);

            String date1[] = returnDate.split("-");
            eDay = Integer.parseInt(date1[0]);
            eMonth = Integer.parseInt(date1[1]) - 1;
            eYear = Integer.parseInt(date1[2]);
        } catch (Exception e) {

        }
    }

    public List<String> getList() {
        return list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    class ViewHolder implements View.OnTouchListener {
//         gridcell;
//        TextView tv_calendarDay;
//
//        public ViewHolder(View v) {
//            this.buttonImage = (ImageView) v.findViewById(R.id.imageViewButton);
//            this.buttonText = (TextView) v.findViewById(R.id.textViewButton);
//            v.setOnTouchListener(this);
//        }
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if (MotionEvent.ACTION_DOWN == event.getAction()) {
//                // Here you can put animation for your image view
//                // this.buttonImage is the target image view.
//            }
//            return true;
//        }
//    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.layout_grid_cell, parent, false);
        }

        gridcell = (RelativeLayout) row.findViewById(R.id.calendar_day_gridcell);
        tv_calendarDay = (TextView) row.findViewById(R.id.tv_calendarDay);
        gridcell.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getId();
                return false;
            }
        });
        GradientDrawable dateSelectDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.date_select_drawable);
        dateSelectDrawable.setColor(Color.parseColor(context.getString(R.string.theme_color)));

        String[] day_color = list.get(position).split("-");
        String theday = day_color[0];
        String themonth = day_color[2];
        String theyear = day_color[3];
        try {
            currentDayOfMonth = Integer.parseInt(theday);
        } catch (Exception e) {

        }
//        if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
//            if (eventsPerMonthMap.containsKey(theday)) {
//                num_events_per_day = (TextView) row
//                        .findViewById(R.id.num_events_per_day);
//                Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
//                num_events_per_day.setText(numEvents.toString());
//            }
//        }

        // Set the Day GridCell
        if (day_color[1].equals("GREY")) {
            //tv_calendarDay.setText(theday);
            //  tv_calendarDay.setTextColor(context.getResources().getColor(R.color.hours_background_color));
        } else if (day_color[1].equals("BLACK")) {
            tv_calendarDay.setText(theday);
            tv_calendarDay.setTextColor(context.getResources().getColor(R.color.blackLight));
        } else {
            tv_calendarDay.setText(theday);

            if (returnDate != null) {

                if (isReturnDay(position)) {
                    tv_calendarDay.setTextColor(context.getResources().getColor(R.color.white));
                    tv_calendarDay.setBackgroundDrawable(dateSelectDrawable);
                    gridcell.setBackgroundColor(context.getResources().getColor(R.color.colorbackground));
                    gridcell.setRotation(180.0f);
                    tv_calendarDay.setRotation(180.0f);
                } else if (isDepartureDay(position)) {
                    tv_calendarDay.setTextColor(context.getResources().getColor(R.color.white));
                    tv_calendarDay.setBackgroundDrawable(dateSelectDrawable);
                    gridcell.setBackgroundColor(context.getResources().getColor(R.color.colorbackground));
                } else if (isBetween(position)) {  //(position < returnDay && position > departureDay) {
                    gridcell.setBackgroundColor(context.getResources().getColor(R.color.colorbackground));
                    // tv_calendarDay.setTextColor(context.getResources().getColor(R.color.splash_background));
                }
            } else {
                if ((currentDayOfMonth == sDay) && (currentMonth == sMonth) && (currentYear == sYear)) {
                    tv_calendarDay.setTextColor(context.getResources().getColor(R.color.white));
                    tv_calendarDay.setBackgroundDrawable(dateSelectDrawable);
                }
            }
        }
        if (position % 7 == 0 && !day_color[1].equals("BLACK"))
            tv_calendarDay.setTextColor(context.getResources().getColor(R.color.red_first));
        return row;
    }

    private boolean isReturnDay(int position) {
        if (currentMonth == eMonth) {
            if (position == returnDay) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    private boolean isDepartureDay(int position) {

        if (currentMonth == sMonth) {
            if (position == departureDay) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    private boolean isBetween(int position) {
        if ((currentYear >= sYear) && (currentYear <= eYear) && (currentMonth >= sMonth) && (currentMonth <= eMonth)) {
            if ((position < departureDay) && (currentMonth == sMonth)) {
                return false;
            } else if ((position > returnDay) && (currentMonth == eMonth)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public void onClick(View view) {
        int position = (int) view.getTag();
        String[] day_color = list.get(position).split("-");

    }

    public String getMonthAsString(int i) {
        return months[i];
    }

    private String getWeekDayAsString(int i) {
        return weekdays[i];
    }

    public static int getNumberOfDaysOfMonth(int i) {
        return daysOfMonth[i];
    }

    public void printMonth() {
        Log.d("Trip", "==> printMonth: mm: " + (currentMonth + 1) + " " + "yy: " + currentYear);
        int trailingSpaces = 0;
        int daysInPrevMonth = 0;
        int prevMonth = 0;
        int prevYear = 0;
        int nextMonth = 0;
        int nextYear = 0;

        //int currentMonth = mm - 1;
        String currentMonthName = getMonthAsString(currentMonth);
        daysInMonth = getNumberOfDaysOfMonth(currentMonth);

        Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
                + daysInMonth + " days.");

        GregorianCalendar cal = new GregorianCalendar(currentYear, currentMonth, 1);
        Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

        if (currentMonth == 11) {
            prevMonth = currentMonth - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 0;
            prevYear = currentYear;
            nextYear = currentYear + 1;
        } else if (currentMonth == 0) {
            prevMonth = 11;
            prevYear = currentYear - 1;
            nextYear = currentYear;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 1;
        } else {
            prevMonth = currentMonth - 1;
            nextMonth = currentMonth + 1;
            nextYear = currentYear;
            prevYear = currentYear;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
        }

        int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        trailingSpaces = currentWeekDay;

        if (cal.isLeapYear(cal.get(Calendar.YEAR)))
            if (currentMonth == 1)
                ++daysInMonth;
            else if (currentMonth == 2)
                ++daysInPrevMonth;
        list = new ArrayList<String>();
// Trailing Month days
        for (int i = 0; i < trailingSpaces; i++) {
            list.add(String
                    .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                            + i)
                    + "-GREY"
                    + "-"
                    + prevMonth//getMonthAsString(prevMonth)
                    + "-"
                    + prevYear);
        }

// Current Month Days
        for (int j = 1; j <= daysInMonth; j++) {
            if (j < getCurrentDayOfMonth() && currentMonth == this.startMonth) {
                list.add(String.valueOf(j) + "-BLACK" + "-"
                        + getMonthAsString(currentMonth) + "-" + currentYear);

                // setWeek((list.size()-1)/7);
            } else if (j == getCurrentDayOfMonth() && currentMonth == this.startMonth) {
                list.add(String.valueOf(j) + "-RED" + "-"
                        + currentMonth   //getMonthAsString(currentMonth)
                        + "-" + currentYear);

            } else {
                list.add(String.valueOf(j) + "-WHITE" + "-"
                        + currentMonth   //getMonthAsString(currentMonth)
                        + "-" + currentYear);
            }
            if (j == sDay) {
                departureDay = list.size() - 1;
                if (returnDay == -1)
                    returnDay = departureDay + 1;
            }
            if (j == eDay) {
                returnDay = list.size() - 1;

            }
        }

// Leading Month days
        for (int i = 0; list.size() < 42; i++) {
            list.add(String.valueOf(i + 1) + "-GREY" + "-"
                    + nextMonth  //getMonthAsString(nextMonth)
                    + "-" + nextYear);
        }
    }

    public String getSelectedDate(int position) {

        String[] day_color = list.get(position).split("-");
        String theday = day_color[0];
        String themonth = day_color[2];
        String theyear = day_color[3];
        int month = 0;
        try {
            month = Integer.parseInt(themonth);
        } catch (Exception e) {
            Log.e("", "Exception in getSelectedDate in CalanderAdapterItem");
        }
        return theday + "-" + (month + 1) + "-" + theyear;
    }

    public boolean isClicable(int position) {
        String s[] = list.get(position).split("-");
        boolean b = false;
        try {
            b = s[1].equals("GREY");
            if (!b) {
                b = s[1].equals("BLACK");
            }
        } catch (Exception e) {

        }
        return b;
    }


    private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
                                                                int month) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        return map;
    }

    public int getCurrentDayOfMonth() {
        return currentDayOfMonth;
    }

//    private void setCurrentDayOfMonth(int currentDayOfMonth) {
//        this.currentDayOfMonth = currentDayOfMonth;
//    }
//
//    public int getCurrentWeekDay() {
//        return currentWeekDay;
//    }
//
//    public void setCurrentWeekDay(int currentWeekDay) {
//        this.currentWeekDay = currentWeekDay;
//    }
//
//    public int getCurrentMonth() {
//        return this.currentMonth;
//    }
//
//    public void setCurrentMonth(int currentMonth) {
//        this.currentMonth = currentMonth;
//    }
//
//    public int getCurrentYear() {
//        return currentYear;
//    }
//
//    public void setCurrentYear(int year) {
//        this.currentYear = year;
//    }
//
//
////    public void setSelectedDate(int position) {
////        this.selectedDayOfMonth = position;
////    }
//
//    public int getStartMonth() {
//        return startMonth;
//    }
//
//    public void setSelectedMonth() {
//        this.startMonth = this.currentMonth;
//    }
//
//    public int getStartYear() {
//        return startYear;
//    }
//
//    public void setSelectedYear() {
//        this.startYear = this.currentYear;
//    }
//
//    public String convertListToString(String[] dayArray) {
//        String day = "";
//        for (int i = 0; i < dayArray.length; i++) {
//            day = (String.valueOf(dayArray[0]) + "-" + dayArray[1] + "-"
//                    + dayArray[2] + "-" + dayArray[3]);
//        }
//        return day;
//    }
//
////
////    public void setWeek(int week) {
////        selectedWeek = week;
////        departureDay = week * 7;
////        returnDay = (week + 1) * 7 - 1;
////
////    }
//
//    public String getWeekText() {
//        String weekText = "";
//        String[] startDay = list.get(departureDay).split("-");
//        String[] endDay = list.get(returnDay).split("-");
//        weekText += startDay[2].substring(0, 3) + " " + startDay[0] + ", " + startDay[3] + " TO ";
//        weekText += endDay[2].substring(0, 3) + " " + endDay[0] + ", " + endDay[3];
//        return weekText;
//    }
//
//    public String getStartDate() {
//        int month = startMonth;
//        String[] startDay = list.get(departureDay).split("-");
//        for (int i = 0; i < 12; i++) {
//
//            if (months[i].equalsIgnoreCase(startDay[2]))
//                month = i;
//        }
//        String startDate = startDay[3] + "-" + (month + 1) + "-" + startDay[0];
//        return startDate;
//    }


}