package com.rezofy.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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
 * Created by linchpin11192 on 26-May-2016.
 */
public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder> {
    private Context context;
    private List<String> listOptionIcons, listOptionURLs, listOptionNames;
    private int itemBefore = 1;
    private boolean isInsertPackages;

    public OptionsAdapter(Context context) {
        this.context = context;

        isInsertPackages = Boolean.parseBoolean(context.getString(R.string.insertPackages));
        listOptionIcons = UIUtils.getOptionIconsList(context);
        listOptionURLs = UIUtils.getOptionURLsList(context);
        listOptionNames = UIUtils.getOptionNamesList(context);
        if (listOptionIcons.size() != listOptionURLs.size() || listOptionIcons.size() != listOptionNames.size()) {
            Toast.makeText(context, R.string.text_option_mapping_faulty, Toast.LENGTH_LONG).show();
            ((Activity) context).finish();
        }

    }

    @Override
    public OptionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OptionsViewHolder(LayoutInflater.from(context).inflate(R.layout.options_item, parent, false));
    }

    @Override
    public void onBindViewHolder(OptionsViewHolder holder, int position) {
        UIUtils.setThemeLightSelector(holder.rlRoot);
        holder.iTVIcon.setTextColor(UIUtils.getThemeContrastColor(context));
        holder.tvName.setTextColor(UIUtils.getThemeContrastColor(context));
        if (position == 0) {
            holder.iTVIcon.setText(R.string.icon_text_o);
            holder.tvName.setText(R.string.flights_text);
            holder.rlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EnterFlightInfoFragment enterFlightInfoFragment = new EnterFlightInfoFragment();
                    ((HomeActivity) context).changeFragment(enterFlightInfoFragment, null, Utils.TAG_ENTER_FLIGHT_INFO_FRAGMENT);
                }
            });


        } else if(position == 1 && isInsertPackages) {
            holder.iTVIcon.setText(R.string.icon_text_7);
            holder.tvName.setText(R.string.packages_txt);
            holder.rlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopularPackagesFragment popularPackagesFragment = new PopularPackagesFragment();
                            ((HomeActivity) context).changeFragment(popularPackagesFragment, null, Utils.TAG_POPULAT_PACKAGES_FRAGMENT);
//                    EnquiryFragment popularPackagesFragment = new EnquiryFragment();
//                    ((HomeActivity) context).changeFragment(popularPackagesFragment, null, Utils.TAG_ENQUIRY_FRAGMENT);
                }
            });
        } else {
            if(listOptionNames.get(position - itemBefore).trim().equalsIgnoreCase("Cruise")) {
                holder.iTVIcon.setText(R.string.icon_text_cruise);
            } else {
                holder.iTVIcon.setText(listOptionIcons.get(position - itemBefore).trim());
            }
            holder.tvName.setText(listOptionNames.get(position - itemBefore).trim());
            holder.rlRoot.setTag(position - itemBefore);
            holder.rlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if(!listOptionURLs.get((Integer) v.getTag()).trim().isEmpty()) {
                    WebViewFragment webViewFragment = new WebViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", listOptionNames.get((Integer) v.getTag()).trim());
                    bundle.putString("url", listOptionURLs.get((Integer) v.getTag()).trim());
                    ((HomeActivity) context).changeFragment(webViewFragment, bundle, listOptionNames.get((Integer) v.getTag()));
                    //}
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

    public class OptionsViewHolder extends RecyclerView.ViewHolder {
        public IconTextView iTVIcon;
        public TextView tvName;
        public RelativeLayout rlRoot;

        public OptionsViewHolder(View itemView) {
            super(itemView);
            rlRoot = (RelativeLayout) itemView;
            rlRoot.setLayoutParams(new ViewGroup.LayoutParams(Utils.getScreenSize(context)[0] / 4, ViewGroup.LayoutParams.MATCH_PARENT));
            iTVIcon = (IconTextView) itemView.findViewById(R.id.icon);
            tvName = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
