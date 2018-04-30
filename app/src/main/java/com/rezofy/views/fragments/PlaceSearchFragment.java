package com.rezofy.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.rezofy.R;
import com.rezofy.adapters.PackagePlaceSearchAdapter;
import com.rezofy.models.response_models.PackagesCountry;
import com.rezofy.models.response_models.PackagesCountryList;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.custom_views.IconTextView;

import java.util.ArrayList;

/**
 * Created by anuj on 20/1/17.
 */
public class PlaceSearchFragment extends Fragment implements TextWatcher {

    private View fView;
    private Context context;
    private RecyclerView rvSearchList;
    private EditText etSearch;
    private PackagePlaceSearchAdapter packagePlaceSearchAdapter;
    private PackagesCountryList packagesCountryList;
    private ArrayList<String> countryNameList;
    private IconTextView iTVBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fView = inflater.inflate(R.layout.fragment_place_search, container, false);
        init();
        setProperties();

        return fView;
    }

    private void setProperties() {
        fView.findViewById(R.id.ll_header).setBackgroundColor(UIUtils.getThemeColor(context));
        iTVBack.setTextColor(UIUtils.getThemeContrastColor(context));
        etSearch.setHintTextColor(UIUtils.getThemeContrastColor(context));
        etSearch.setTextColor(UIUtils.getThemeContrastColor(context));
    }

    private void init() {
        context = getContext();

        iTVBack = (IconTextView) fView.findViewById(R.id.left_icon);
        iTVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                Utils.hideSoftKey(context);
            }
        });
        etSearch = (EditText) fView.findViewById(R.id.et_search);
        etSearch.addTextChangedListener(this);
        rvSearchList = (RecyclerView) fView.findViewById(R.id.rv_search_list);
        packagesCountryList = (PackagesCountryList) getArguments().getSerializable(Utils.TAG_PACKAGES_COUNTRY_LIST);
        countryNameList = new ArrayList<String>();
        ArrayList<PackagesCountry> packagesCountries = packagesCountryList.getCountries();
        for (int i = 0; i < packagesCountries.size(); i++)
            countryNameList.add(packagesCountries.get(i).getName());

        rvSearchList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        packagePlaceSearchAdapter = new PackagePlaceSearchAdapter(context, packagesCountryList, countryNameList);
        rvSearchList.setAdapter(packagePlaceSearchAdapter);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        ArrayList<String> countryNameList;
        if (s.toString().length() < 2) {

            countryNameList = this.countryNameList;
        }
        else {

            countryNameList = getCountryNameList(etSearch.getText().toString());

        }
        packagePlaceSearchAdapter.setCountryNameList(countryNameList);
        packagePlaceSearchAdapter.notifyDataSetChanged();
    }

    private ArrayList<String> getCountryNameList(String subString) {

        ArrayList<String> countryNameList = new ArrayList<String>();
        for (int i = 0; i < this.countryNameList.size(); i++) {
            if(this.countryNameList.get(i).contains(subString))
                countryNameList.add(this.countryNameList.get(i));
        }

        return countryNameList;
    }
}
