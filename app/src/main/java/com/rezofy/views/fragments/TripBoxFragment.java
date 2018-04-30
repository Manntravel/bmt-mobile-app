package com.rezofy.views.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.adapters.TripBoxMenuAdapter;
import com.rezofy.models.response_models.LoginResponse;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.BaseHomeActivity;
import com.rezofy.views.custom_views.IconTextView;

/**
 * Created by anuj on 6/2/17.
 */
public class TripBoxFragment extends Fragment implements View.OnClickListener
{

    private View fView, header;
    private IconTextView iTVMenu, iTVSearch;
    private TextView tvUserName, tvTitle;
    private RecyclerView recyclerView;
    private TripBoxMenuAdapter tripBoxMenuAdapter;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fView = inflater.inflate(R.layout.fragment_tripbox, container, false);

        initViews();
        setProperties();
        setData();

        return fView;
    }

    private void initViews(){
        context = getActivity();

        header = fView.findViewById(R.id.header);
        if(header != null) {
            iTVMenu = (IconTextView) header.findViewById(R.id.left_icon);
            iTVMenu.setText(getString(R.string.icon_text_h));
            iTVMenu.setOnClickListener(this);
            iTVMenu.setTextSize(20);

            tvTitle = (TextView) header.findViewById(R.id.title);
            tvTitle.setText(getString(R.string.trip_box_text));

            iTVSearch = (IconTextView) header.findViewById(R.id.right_icon3);
            iTVSearch.setVisibility(View.GONE);
            iTVSearch.setOnClickListener(this);
            iTVSearch.setText(getString(R.string.icon_text_search));
            iTVSearch.setTextSize(20);
        }
        tvUserName = (TextView) fView.findViewById(R.id.tv_username);

        recyclerView = (RecyclerView) fView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tripBoxMenuAdapter = new TripBoxMenuAdapter(context, Utils.TAG_TRIPBOX_FRAGMENT);
        recyclerView.setAdapter(tripBoxMenuAdapter);
    }

    private void setProperties() {
        int themeContrastColor = UIUtils.getThemeContrastColor(context);
        //header.setBackgroundColor(UIUtils.getThemeColor(getContext()));
        iTVMenu.setTextColor(themeContrastColor);
        iTVSearch.setTextColor(themeContrastColor);
        tvTitle.setTextColor(themeContrastColor);
        UIUtils.setRoundedButtonProperties(recyclerView, getResources().getColor(R.color.white_op70), getResources().getColor(R.color.white_op70));
    }

    private void setData() {
        String userData = AppPreferences.getInstance(context).getUserData();
        if (!userData.isEmpty()) {
            LoginResponse loginResponse = new Gson().fromJson(userData, LoginResponse.class);
            if(loginResponse.getName().getFirst().contains("guest") || loginResponse.getName().getFirst().contains("Guest"))
            {
                tvUserName.setText(getString(R.string.guest));
            } else {
                tvUserName.setText(loginResponse.getName().getFirst() + " " + loginResponse.getName().getLast());
            }

        } else {
            tvUserName.setText(getString(R.string.guest));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.left_icon:
                ((BaseHomeActivity) getActivity()).openDrawer();
                break;
        }
    }
}
