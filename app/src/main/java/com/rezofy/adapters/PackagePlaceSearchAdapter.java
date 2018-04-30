package com.rezofy.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.models.response_models.PackagesCountryList;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.HomeActivity;
import com.rezofy.views.activities.TripBoxHomeActivity;
import com.rezofy.views.fragments.CountryPackagesFragment;

import java.util.ArrayList;

/**
 * Created by anuj on 20/1/17.
 */
public class PackagePlaceSearchAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private PackagesCountryList packagesCountryList;
    private ArrayList<String> countryNameList;

    public PackagePlaceSearchAdapter(Context context, PackagesCountryList packagesCountryList, ArrayList<String> countryNameList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.packagesCountryList = packagesCountryList;
        this.countryNameList = countryNameList;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.place_name_item, parent, false);
        return new PlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        PlaceHolder placeHolder = (PlaceHolder) holder;
        placeHolder.tvPlaceName.setText(countryNameList.get(position));
        placeHolder.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity)context).getSupportFragmentManager().popBackStack(Utils.TAG_POPULAT_PACKAGES_FRAGMENT, 0);
                CountryPackagesFragment countryPackagesFragment = new CountryPackagesFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Utils.TAG_COUNTRY_NAME, countryNameList.get(position));
                if(Utils.CURRENT_HOME_ACTIVITY.equals(Utils.TAG_HOME_ACTIVITY)) {
                    ((HomeActivity) context).changeFragment(countryPackagesFragment, bundle, Utils.TAG_COUNTRY_PACKAGES_FRAGMENT);
                } else {
                    ((TripBoxHomeActivity) context).changeFragment(countryPackagesFragment, bundle, Utils.TAG_COUNTRY_PACKAGES_FRAGMENT);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryNameList.size();
    }

    public void setCountryNameList(ArrayList<String> countryNameList) {
        this.countryNameList = countryNameList;
    }

    class PlaceHolder extends RecyclerView.ViewHolder{

        private LinearLayout llRoot;
        private TextView tvPlaceName;

        public PlaceHolder(View itemView) {
            super(itemView);
            llRoot = (LinearLayout) itemView.findViewById(R.id.ll_root);
            tvPlaceName = (TextView) itemView.findViewById(R.id.tv_place_name);
        }
    }
}
