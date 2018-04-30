package com.rezofy.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rezofy.R;

/**
 * Created by anuj on 8/2/17.
 */
public class CustomWebViewFragment extends WebViewFragment// implements View.OnClickListener
{

    private Context context;
    private View fView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fView = super.onCreateView(inflater, container, savedInstanceState);
        initViews();
        return fView;
    }

    private void initViews() {

        context = getActivity();
        iTVMenu.setText(getString(R.string.icon_text_k));
        iTVShareApp.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_icon:
                  getActivity().onBackPressed();
                break;
        }
    }
}
