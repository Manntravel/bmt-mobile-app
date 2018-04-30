package com.rezofy.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.database.DbHelper;
import com.rezofy.models.response_models.CountryTours;
import com.rezofy.models.response_models.SelectedCountryTours;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.HomeActivity;
import com.rezofy.views.activities.TripBoxHomeActivity;
import com.rezofy.views.custom_views.IconTextView;

import java.util.ArrayList;

/**
 * Created by anuj on 13/1/17.
 */
public class PopularPackagesBaseFragment extends Fragment implements View.OnClickListener {

    private Context context;
    View fView, header;
    IconTextView iTVMenu, iTVSearch, iTVFav;
    TextView tvTitle, tvNoFav;
    RecyclerView recyclerView;
    private SelectedCountryTours selectedCountryTours;
    public String  tourIds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if (fView == null) {
            fView = inflater.inflate(R.layout.fragment_popular_packages, container, false);
            initViews();
            setProperties();
            setListener();
        }

        return fView;
    }

    private void initViews() {
        context = getContext();
        header = fView.findViewById(R.id.header);
        if(header != null) {
            iTVMenu = (IconTextView) header.findViewById(R.id.left_icon);
            iTVMenu.setText(getString(R.string.icon_text_h));
            iTVMenu.setOnClickListener(this);
            iTVMenu.setTextSize(20);

            tvTitle = (TextView) header.findViewById(R.id.title);
            tvTitle.setText(getString(R.string.packages_text));

            iTVSearch = (IconTextView) header.findViewById(R.id.right_icon3);
            iTVSearch.setVisibility(View.VISIBLE);
            iTVSearch.setOnClickListener(this);
            iTVSearch.setText(getString(R.string.icon_text_search));
            iTVSearch.setTextSize(20);

            iTVFav = (IconTextView) header.findViewById(R.id.right_icon);
            iTVFav.setVisibility(View.VISIBLE);
            iTVFav.setOnClickListener(this);
            iTVFav.setText(getString(R.string.icon_text_fav));
            iTVFav.setTextSize(20);

        }

        tvNoFav = (TextView) fView.findViewById(R.id.tv_no_fav);
        recyclerView = (RecyclerView) fView.findViewById(R.id.rv_popular_packages);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void setProperties() {
        int themeContrastColor = UIUtils.getThemeContrastColor(getContext());
        header.setBackgroundColor(UIUtils.getThemeColor(getContext()));
        iTVMenu.setTextColor(themeContrastColor);
        iTVFav.setTextColor(themeContrastColor);
        iTVSearch.setTextColor(themeContrastColor);
        tvTitle.setTextColor(themeContrastColor);
    }

    private void setListener() {
        iTVMenu.setOnClickListener(this);
        iTVSearch.setOnClickListener(this);
        iTVFav.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.right_icon3:
                if (Utils.packagesCountryList != null && Utils.packagesCountryList.getCountries().size() > 0) {
                    PlaceSearchFragment placeSearchFragment = new PlaceSearchFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Utils.TAG_PACKAGES_COUNTRY_LIST, Utils.packagesCountryList);
                    if(Utils.CURRENT_HOME_ACTIVITY.equals(Utils.TAG_HOME_ACTIVITY)) {
                        ((HomeActivity) context).changeFragment(placeSearchFragment, bundle, Utils.TAG_PLACE_SEARCH_FRAGMENT);
                    } else {
                        ((TripBoxHomeActivity) context).changeFragment(placeSearchFragment, bundle, Utils.TAG_PLACE_SEARCH_FRAGMENT);
                    }
                }
                break;
        }
    }

    public SelectedCountryTours getSelectedCountryToursFromDB() {
        DbHelper dbHelper = DbHelper.getInstance(getContext());
        selectedCountryTours = dbHelper.getFavPackages();
        setFavoriteTourId(selectedCountryTours);

        return selectedCountryTours;
    }

    private void setFavoriteTourId(SelectedCountryTours selectedCountryTours) {
        String tourIds =  "";
        ArrayList<CountryTours> countryTourses = selectedCountryTours.getCountryToursArrayList();
        if(countryTourses != null) {
            for (int i = 0; i < countryTourses.size(); i++)
                tourIds = tourIds.concat(countryTourses.get(i).getID()).concat(" ");
        }
        this.tourIds = tourIds;
    }

}
