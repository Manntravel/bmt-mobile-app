package com.rezofy.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.WebCheckinActivity;
import com.rezofy.views.activities.MyDocumentTypeActivity;
import com.rezofy.views.activities.MyTripsActivity;
import com.rezofy.views.activities.PolicyInfoActivity;
import com.rezofy.views.activities.TripBoxHomeActivity;
import com.rezofy.views.activities.WeatherActivity;
import com.rezofy.views.custom_views.IconTextView;
import com.rezofy.views.activities.VisaActivity;
import com.rezofy.views.fragments.CurrencyConverterDialog;
import com.rezofy.views.fragments.CustomWebViewFragment;
import com.rezofy.views.fragments.FlightTrackerDialog;
import com.rezofy.views.fragments.PopularPackagesFragment;

/**
 * Created by anuj on 6/2/17.
 */
public class TripBoxMenuAdapter extends RecyclerView.Adapter {

    private final int VIEW_TYPE_TRIPITEM = 0;
    private final int VIEW_TYPE_FLIGHTCHECKIN = 1;
    private final int VIEW_TYPE_TRIPDRAWERITEM = 2;
    private Context context;
    private LayoutInflater layoutInflater;
    private DrawerLayout drawerLayout;
    private String tripboxItemName[] = {"My Trips", "Weather", "Currency Converter", "Web Check-in", "Flight Tracker", "My Documents", "Visa", "Policy",
                                            "Baggage Information", "Packages"};

    private int tripboxItemIcon[] = {R.mipmap.my_trip_logo, R.mipmap.weather_logo, R.mipmap.currency_converter_logo, R.mipmap.flight_checkin_logo, R.mipmap.flight_tracker_logo,
            R.mipmap.my_docs_logo, R.mipmap.visa_logo, R.mipmap.policy_logo, R.mipmap.baggage_info_logo, R.mipmap.packages_logo};

    private String flightName[] = {"Spicejet", "Jet airways", "IndiGo", "GoAir", "Air India", "Lufthansa", "Thai Airways", "Emirates"};
    private String flightCheckinUrl[] = {"https://book.spicejet.com/SearchWebCheckin.aspx",
                                          "https://https.jetairways.com/EN/US/planyourtravel/web-check-in.aspx",
                                           "https://book.goindigo.in/checkin/view",
                                            "https://www.goair.in/checkin",
                                             "http://www.airindia.in/web-check-in.htm",
                                              "https://www.lufthansa.com/in/en/Online-Check-in",
                                               "https://checkin.si.amadeus.net/1ASIHSSCWEBTG/sscwtg/checkin?ln=en&type=W&Redirected=true",
                                                "https://www.emirates.com/english/plan_book/check_in_online/online-check-in.aspx"};
    private String baggageInfoUrl[] = {"http://www.spicejet.com/AirTravelBaggageFaq.aspx",
            "http://www.airindia.com/checked-baggage-allowances.htm#fba-ind-usa",
            "http://www.jetairways.com/EN/IN/TravelInformation/baggage/partner-airlines.aspx",
            "https://www.goair.in/bottom-menu/extra-baggage",
            "https://www.goindigo.in/information/travel-information/baggage/bag-allowance.html",
            "http://www.lufthansa.com/online/portal/lh/us/local?nodeid=3373626#ancAbT6",
            "http://www.thaiairways.com/en/Terms_condition/baggage_policy.page",
            "https://www.emirates.com/in/english/help/faq/490107/weight-and-piece-concept"};
    private int flightCheckinIcon[] = {R.mipmap.spicejet_logo, R.mipmap.jetairways_logo, R.mipmap.indigo, R.mipmap.goair_logo, R.mipmap.air_india_logo,
                                         R.mipmap.lufthansa_logo, R.mipmap.thai_airways_logo, R.mipmap.emirates_logo};
    private boolean isInsertPackages;
    private String tagSource;

    public TripBoxMenuAdapter(Context context, String tagSource){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        isInsertPackages = Boolean.parseBoolean(context.getString(R.string.insertPackages));
        this.tagSource = tagSource;
    }

    public TripBoxMenuAdapter(Context context, String tagSource, DrawerLayout drawerLayout) {
        this(context, tagSource);
        this.drawerLayout = drawerLayout;
    }

    @Override
    public int getItemViewType(int position) {
        if(tagSource.equals(Utils.TAG_TRIPBOX_FRAGMENT)) {
            return VIEW_TYPE_TRIPITEM;
        } else if(tagSource.equals(Utils.TAG_TRIPBOX_HOME_ACTIVITY)) {
            return VIEW_TYPE_TRIPDRAWERITEM;
        } else {
            return VIEW_TYPE_FLIGHTCHECKIN;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_TRIPITEM) {
            return new TripBoxMenuHolder(layoutInflater.inflate(R.layout.tripbox_menu_item, parent, false));
        } else if(viewType == VIEW_TYPE_TRIPDRAWERITEM) {
            return new TripBoxDrawerItemHolder(layoutInflater.inflate(R.layout.drawer_options_item, parent, false));
        } else {
            return new FlightCheckinMenuHolder(layoutInflater.inflate(R.layout.flight_checkin_menu_item, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof TripBoxMenuHolder) {
            TripBoxMenuHolder tripBoxMenuHolder = (TripBoxMenuHolder) holder;
            tripBoxMenuHolder.ivIcon.setImageResource(tripboxItemIcon[position]);
            tripBoxMenuHolder.tvName.setText(tripboxItemName[position]);
            if (position % 2 != 0) {
                tripBoxMenuHolder.view.setVisibility(View.GONE);
            }

            tripBoxMenuHolder.rlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      openActivity(position);
                }
            });

        } else if(holder instanceof TripBoxDrawerItemHolder) {
            TripBoxDrawerItemHolder tripBoxDrawerItemHolder = (TripBoxDrawerItemHolder) holder;
            if(position == 0) {
                tripBoxDrawerItemHolder.iTVIcon.setText(R.string.icon_text_mytrips);
            } else if(position == 1) {
                tripBoxDrawerItemHolder.iTVIcon.setText(R.string.icon_text_weather);
            } else if(position == 2) {
                tripBoxDrawerItemHolder.iTVIcon.setText(R.string.icon_text_currency_converter);
            } else if(position == 3) {
                tripBoxDrawerItemHolder.iTVIcon.setText(R.string.icon_text_flight_checkin);
            } else if(position == 4) {
                tripBoxDrawerItemHolder.iTVIcon.setText(R.string.icon_text_flight_tracker);
            } else if(position == 5) {
                tripBoxDrawerItemHolder.iTVIcon.setText(R.string.icon_text_mydocuments);
            } else if(position == 6) {
                tripBoxDrawerItemHolder.iTVIcon.setText(R.string.icon_text_visa);
            } else if(position == 7) {
                tripBoxDrawerItemHolder.iTVIcon.setText(R.string.icon_text_policy);
            } else if(position == 8) {
                tripBoxDrawerItemHolder.iTVIcon.setText(R.string.icon_text_baggage_info);
            } else if(position == 9) {
                tripBoxDrawerItemHolder.iTVIcon.setText(R.string.icon_text_package);
            }
            tripBoxDrawerItemHolder.tvName.setText(tripboxItemName[position]);
            tripBoxDrawerItemHolder.llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    openActivity(position);
                }
            });
        }


        else {
            FlightCheckinMenuHolder flightCheckinMenuHolder = (FlightCheckinMenuHolder) holder;
            flightCheckinMenuHolder.ivIcon.setImageResource(flightCheckinIcon[position]);
            flightCheckinMenuHolder.tvName.setText(flightName[position]);
            if (position % 2 != 0) {
                flightCheckinMenuHolder.view.setVisibility(View.GONE);
            }

            flightCheckinMenuHolder.rlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomWebViewFragment webViewFragment = new CustomWebViewFragment();
                    Bundle bundle = new Bundle();
                    if(tagSource.equals(Utils.TAG_WEB_CHECKIN_ACTIVITY)) {
                        bundle.putString("title", context.getString(R.string.web_checkin));
                        bundle.putString("url", flightCheckinUrl[position]);
                    } else {
                        bundle.putString("title", context.getString(R.string.baggage_information));
                        bundle.putString("url", baggageInfoUrl[position]);
                    }

                    ((WebCheckinActivity) context).changeFragment(webViewFragment, bundle, Utils.TAG_CUSTOM_WEBVIEW_FRAGMENT);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if(tagSource.equals(Utils.TAG_TRIPBOX_FRAGMENT) || tagSource.equals(Utils.TAG_TRIPBOX_HOME_ACTIVITY)) {
            if (isInsertPackages) {
                return tripboxItemName.length;
            } else {
                return tripboxItemName.length - 1;
            }
        } else {
            return flightName.length;
        }
    }

    private void openActivity(int position) {

        if(position == 0) {
            Intent intent = new Intent(context, MyTripsActivity.class);
            context.startActivity(intent);

        }
        else if(position == 1) {
            Intent intent = new Intent(context, WeatherActivity.class);
            context.startActivity(intent);
        }
        if(position == 2) {
            new CurrencyConverterDialog().show(((AppCompatActivity) context).getSupportFragmentManager(), "Currency Dialog");

        }
        if(position == 3) {
            Intent intent = new Intent(context, WebCheckinActivity.class);
            intent.putExtra(Utils.TAG_SOURCE, Utils.TAG_WEB_CHECKIN_ACTIVITY);
            context.startActivity(intent);
        }
        if(position == 4)
        {
            new FlightTrackerDialog().show(((AppCompatActivity) context).getSupportFragmentManager(), "Flight Tracker Dialog");
        }
        if(position == 5) {
            Intent intent = new Intent(context, MyDocumentTypeActivity.class);
            context.startActivity(intent);
        }
        if(position == 6) {
            Intent intent = new Intent(context, VisaActivity.class);
            context.startActivity(intent);
        }
        if(position == 7) {
            Intent intent = new Intent(context, PolicyInfoActivity.class);
            context.startActivity(intent);
        } else if(position == 8) {
            Intent intent = new Intent(context, WebCheckinActivity.class);
            intent.putExtra(Utils.TAG_SOURCE, Utils.TAG_BAGGAGE_INFORMATION_ACTIVITY);
            context.startActivity(intent);
        }
        if(position == 9) {
            PopularPackagesFragment popularPackagesFragment = new PopularPackagesFragment();
            ((TripBoxHomeActivity) context).changeFragment(popularPackagesFragment, null, Utils.TAG_POPULAT_PACKAGES_FRAGMENT);
        }

    }

    public class TripBoxMenuHolder extends RecyclerView.ViewHolder{

        private RelativeLayout rlRoot;
        private ImageView ivIcon;
        private TextView tvName;
        private View view;

        public TripBoxMenuHolder(View itemView) {
            super(itemView);

            rlRoot = (RelativeLayout) itemView.findViewById(R.id.rl_root);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            view = (View) itemView.findViewById(R.id.view);
        }
    }

    public class TripBoxDrawerItemHolder extends RecyclerView.ViewHolder{

        private LinearLayout llRoot;
        private IconTextView iTVIcon;
        private TextView tvName;
        public TripBoxDrawerItemHolder(View itemView) {
            super(itemView);

            llRoot = (LinearLayout) itemView.findViewById(R.id.root);
            iTVIcon = (IconTextView) itemView.findViewById(R.id.icon);
            tvName = (TextView) itemView.findViewById(R.id.name);
        }
    }

    public class FlightCheckinMenuHolder extends RecyclerView.ViewHolder{

        private RelativeLayout rlRoot;
        private ImageView ivIcon;
        private TextView tvName;
        private View view;

        public FlightCheckinMenuHolder(View itemView) {
            super(itemView);

            rlRoot = (RelativeLayout) itemView.findViewById(R.id.rl_root);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            view = (View) itemView.findViewById(R.id.view);
        }
    }
}
