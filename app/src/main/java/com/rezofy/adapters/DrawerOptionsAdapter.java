package com.rezofy.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rezofy.R;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.HomeActivity;
import com.rezofy.views.custom_views.IconTextView;
import com.rezofy.views.fragments.EnterFlightInfoFragment;
import com.rezofy.views.fragments.PopularPackagesFragment;
import com.rezofy.views.fragments.WebViewFragment;

import java.util.List;

/**
 * Created by linchpin11192 on 27-May-2016.
 */
public class DrawerOptionsAdapter extends RecyclerView.Adapter<DrawerOptionsAdapter.DrawerOptionsViewHolder> {
    private Context context;
    private DrawerLayout drawerLayout;
    private int itemBefore = 1;
    private List<String> listOptionIcons, listOptionURLs, listOptionNames;
    private FragmentManager fm;
    private boolean isInsertPackages;

    public DrawerOptionsAdapter(Context context, DrawerLayout drawerLayout) {
        this.context = context;
        this.drawerLayout = drawerLayout;
        isInsertPackages = Boolean.parseBoolean(context.getString(R.string.insertPackages));
        fm = ((HomeActivity) context).getSupportFragmentManager();
        listOptionIcons = UIUtils.getOptionIconsList(context);
        listOptionURLs = UIUtils.getOptionURLsList(context);
        listOptionNames = UIUtils.getOptionNamesList(context);
        if (listOptionIcons.size() != listOptionURLs.size() || listOptionIcons.size() != listOptionNames.size()) {
            Toast.makeText(context, R.string.text_option_mapping_faulty, Toast.LENGTH_LONG).show();
            ((Activity) context).finish();
        }
    }

    @Override
    public DrawerOptionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DrawerOptionsViewHolder(LayoutInflater.from(context).inflate(R.layout.drawer_options_item, parent, false));
    }

    @Override
    public void onBindViewHolder(DrawerOptionsViewHolder holder, int position) {
        if (position == 0) {
            holder.iTVIcon.setText(R.string.icon_text_o);
            holder.tvName.setText(R.string.flights_text);
            holder.llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    if (!fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equals(Utils.TAG_ENTER_FLIGHT_INFO_FRAGMENT)) {
                        EnterFlightInfoFragment enterFlightInfoFragment = new EnterFlightInfoFragment();
                        ((HomeActivity) context).changeFragment(enterFlightInfoFragment, null, Utils.TAG_ENTER_FLIGHT_INFO_FRAGMENT);
                    }
                }
            });

        }  else if(position == 1 && isInsertPackages) {
            holder.iTVIcon.setText(R.string.icon_text_7);
            holder.tvName.setText(R.string.packages_txt);
            holder.llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    if (!fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equals(Utils.TAG_POPULAT_PACKAGES_FRAGMENT)) {
                        PopularPackagesFragment popularPackagesFragment = new PopularPackagesFragment();
                        ((HomeActivity) context).changeFragment(popularPackagesFragment, null, Utils.TAG_POPULAT_PACKAGES_FRAGMENT);
                    }
                    }
                }

                );

            }else {
            if(listOptionNames.get(position - itemBefore).trim().equalsIgnoreCase("Cruise")) {
                holder.iTVIcon.setText(R.string.icon_text_cruise);
            } else {
                holder.iTVIcon.setText(listOptionIcons.get(position - itemBefore).trim());
            }
            holder.tvName.setText(listOptionNames.get(position - itemBefore).trim());
            holder.llRoot.setTag(position - itemBefore);
            holder.llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    if (!fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equals(listOptionNames.get((Integer) v.getTag()).trim())) {
                        WebViewFragment webViewFragment = new WebViewFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("title", listOptionNames.get((Integer) v.getTag()).trim());
                        bundle.putString("url", listOptionURLs.get((Integer) v.getTag()).trim());
                        ((HomeActivity) context).changeFragment(webViewFragment, bundle, listOptionNames.get((Integer) v.getTag()));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(isInsertPackages) {
            itemBefore = 2;
            return 2 + listOptionIcons.size();
        } else {
            return 1 + listOptionIcons.size();
        }
    }

    public class DrawerOptionsViewHolder extends RecyclerView.ViewHolder {
        public IconTextView iTVIcon;
        public TextView tvName;
        public LinearLayout llRoot;

        public DrawerOptionsViewHolder(View itemView) {
            super(itemView);
            llRoot = (LinearLayout) itemView;
            iTVIcon = (IconTextView) itemView.findViewById(R.id.icon);
            tvName = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
