package com.rezofy.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.utils.Utils;
import com.rezofy.views.fragments.DateClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_SPACE = 2;
    private static RecyclerView recyclerView;
    private String mNavTitles[];
    private String mIcons[];
    private String name;
    private int profile;
    private int endPositionGrid = -1;
    private int endPositionList = -1;
    private int startPositionGrid = 6;
    private int startPositionList = 0;

    private int spaceNo = 5;
    private Activity activity;
    private final String[] months = {"January", "February", "March",
            "April", "May", "June", "July", "August", "September",
            "October", "November", "December"};
    private String startDate, endDate, selectedDate;
    private int daysOfTrip = 0, check = 0;
    private boolean isDeparture = true;


    public CalendarAdapter(Activity mainActivity, boolean b, String s, String s1, int i) {
        activity = mainActivity;
        //recyclerView = r;

        startDate = s;
        endDate = s1;
        daysOfTrip = i;
        isDeparture = b;
        setStartDate();
    }

    private void setStartDate() {
        Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        if (startDate == null) {
            startDate = dd + "-" + mm + 1 + "-" + yy;
        }
        if (isDeparture) {
            if (endDate == null) {

            } else {

            }
        } else {

        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout rowLayout;
        int Holderid;
        TextView textView, tripDays;
        GridView gridView;
        CalendarItemAdapter adapter;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);

            if (ViewType == TYPE_ITEM) {
                // itemView.setOnClickListener(this);
                textView = (TextView) itemView.findViewById(R.id.tvMonth);
                tripDays = (TextView) itemView.findViewById(R.id.tvDaysNo);
                gridView = (GridView) itemView.findViewById(R.id.calendar);
                Holderid = 1;

            } else {
                Holderid = 2;
            }
        }


//        @Override
//        public void onClick(View v) {
//            int itemPosition = recyclerView.getChildPosition(v);
//            switch (itemPosition){
//                case 0:
//                    break;
//                case 1:
//                    HomeFragment fragment = new HomeFragment();
//                    messanger.onMessage(fragment, null, "HomeFragment", true);
//
//                    break;
//                case 2:
//                    break;
//                case 3:
//                    break;
//            }
//
//        }
    }


  /*  public CalendarAdapter(MainActivity screen, RecyclerView homeScreen, String Titles[], String Icons[], String Name, int Profile)
    {
      // MyAdapter Constructor with titles and icons parameter
        mNavTitles = Titles;
        mIcons = Icons;
        name = Name;
        //  messanger = screen;
        profile = Profile;
        recyclerView = homeScreen;
        spaceNo = mNavTitles.length - 3;
    }
*/

    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vhItem = null;
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_layout_item, parent, false); //Inflating the layout

            vhItem = new ViewHolder(v, viewType);

            return vhItem;

            //inflate your layout and pass it to view holder

//        } else if (viewType == TYPE_HEADER) {
//
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_drawer, parent, false); //Inflating the layout
//            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view
//
//            return vhHeader; //returning the object created
//        } else if (viewType == TYPE_SPACE) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.space_in_list_of_drawer, parent, false); //Inflating the layout
//
//            ViewHolder vhHeader = new ViewHolder(v, viewType); //C
//            return vhHeader;
        }
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final CalendarAdapter.ViewHolder holder, int position) {
        final int rowNo = position;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, position);
        String month = (String) DateFormat.format("MMMM", cal.getTime());
        int year = cal.get(Calendar.YEAR);
        if (holder.Holderid == 1) {
            holder.textView.setText(month + " " + year);
            if ((daysOfTrip == 0) && (endDate != null))
                holder.tripDays.setText("same" + " day trip");
            else if (daysOfTrip > 1)
                holder.tripDays.setText(daysOfTrip + " days trip");
            else
                holder.tripDays.setText(daysOfTrip + " day trip");

            holder.adapter = new CalendarItemAdapter(activity, rowNo, startDate, endDate);
            holder.gridView.setAdapter(holder.adapter);
            //Log.e("", String.valueOf(holder.gridView.getId()));
            holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // GridView gv = (CalendarAdapter.ViewHolder)( recyclerView.getAdapter().getItemId(0)).gridView;
                    // holder.gridView.setAdapter(new CalendarItemAdapter(activity, departureDay, position));
                    CalendarItemAdapter adapter = new CalendarItemAdapter(activity, rowNo);
                    if (adapter.isClicable(position)) {
                        return;
                    }
                    selectedDate = adapter.getSelectedDate(position);

                    Date dtSelectedDate = Utils.getDateFromFormat(selectedDate, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy);
                    dtSelectedDate.setHours(1);
                    Date dtCurrentDate = new Date();
                    dtCurrentDate.setHours(0);
                    dtCurrentDate.setMinutes(0);
                    dtCurrentDate.setSeconds(0);

                    if (!dtSelectedDate.before(dtCurrentDate)) {
                        getSellection();

                        // daysOfTrip = noOfDays();
                        ((DateClickListener) activity).onClick(startDate, endDate, daysOfTrip);
                    }
                }
            });


        } else if (holder.Holderid == 0) {

//            holder.profile.setImageResource(profile);
//            holder.Name.setText(name);

        }
    }

    private int noOfDays() {
        int sDay, sMonth, sYear, eDay, eMonth, eYear, days = 0;
        try {
            String date[] = startDate.split("-");
            sDay = Integer.parseInt(date[0]);
            sMonth = Integer.parseInt(date[1]);
            sYear = Integer.parseInt(date[2]);

            String date1[] = endDate.split("-");
            eDay = Integer.parseInt(date1[0]);
            eMonth = Integer.parseInt(date1[1]);
            eYear = Integer.parseInt(date1[2]);
            if ((eYear - sYear) > 0) {
                eMonth += 12;
            }
            days = eDay;
            int diffMonth = (eMonth - sMonth);
            if (diffMonth > 0) {
                for (int x = 0; x < diffMonth; x++) {
                    days += CalendarItemAdapter.getNumberOfDaysOfMonth((sMonth + x) % 12);
                }
            }
            return days - sDay;

        } catch (Exception e) {

        }
        return days;
    }

    private void getSellection() {
        if (isDeparture) {
            if (endDate == null) {
                startDate = selectedDate;
            } else {
                if (compareDate(endDate, selectedDate) > 0) {
                    startDate = selectedDate;
                    daysOfTrip = noOfDays();
                } else {
                    startDate = selectedDate;
                    endDate = addDays(selectedDate, daysOfTrip, true);
                }
            }
        } else {
            if (endDate == null) {
                endDate = addDays(startDate, 1, true);
            } else {
                if (compareDate(startDate, selectedDate) <= 0) {
                    endDate = selectedDate;
                    daysOfTrip = noOfDays();
                } else {
                    endDate = selectedDate;
                    startDate = addDays(selectedDate, daysOfTrip, false);
                }
            }
        }
    }

    public String addDays(String selectedDate, int daysOfTrip, boolean b) {
        SimpleDateFormat format = new SimpleDateFormat(Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy);
        Date d1 = null;
        Date d2 = null;
        String date = null;
        long diff = daysOfTrip * 24 * 60 * 60 * 1000;
        try {
            d1 = format.parse(selectedDate);
            if (b) {
                d2 = new Date(diff + d1.getTime());
            } else {
                long x = d1.getTime() - diff;
                long d = new Date().getTime();
                if (x < d) {
                    d2 = new Date(d);
                } else {
                    d2 = new Date(x);
                }
            }
            date = format.format(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private int compareDate(String startTime, String endTime) {
        SimpleDateFormat format = new SimpleDateFormat(Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy);
        Date d1 = null;
        Date d2 = null;
        long diff = 0;
        try {
            d1 = format.parse(startTime);
            d2 = format.parse(endTime);

            diff = d1.getTime() - d2.getTime();
            if (diff < 0) {
                return -1;
            } else if (diff > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) diff;
    }

    public String getSelectedDate(int position) {

        String[] day_color = CalendarItemAdapter.list.get(position).split("-");
        String theday = day_color[0];
        String themonth = day_color[2];
        String theyear = day_color[3];
        return theday + "-" + themonth + "-" + theyear;
    }


    @Override
    public int getItemCount() {
        return 12;//mNavTitles.length;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDaysOfTrip() {
        return daysOfTrip;
    }

    public void setDaysOfTrip(int daysOfTrip) {
        this.daysOfTrip = daysOfTrip;
    }

    public boolean isDeparture() {
        return isDeparture;
    }

    public void setDeparture(boolean departure) {
        isDeparture = departure;
    }
}
