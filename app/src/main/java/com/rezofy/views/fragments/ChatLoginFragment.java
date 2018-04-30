package com.rezofy.views.fragments;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.utils.UIUtils;
import com.rezofy.views.custom_views.IconTextView;

/**
 * Created by linchpin on 23/8/16.
 */

public class ChatLoginFragment extends Fragment implements View.OnClickListener {
    private TextView tv_right_bottum_sign_up;
    private LinearLayout llGradient;
    private LinearLayout llGetStarted;
    private EditText etUsername, etPassword;
    private IconTextView iTVLeftUserName, iTVLeftUserPass;
    private TextView  tvGetStarted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         View fView = inflater.inflate(R.layout.fragment_chat_login, container, false);
        tv_right_bottum_sign_up = (TextView)fView.findViewById(R.id.tv_right_bottum_sign_up);
        llGradient = (LinearLayout) fView.findViewById(R.id.ll_childscrollview);
        llGetStarted = (LinearLayout) fView.findViewById(R.id.ll_getstart);
        iTVLeftUserName = (IconTextView) fView.findViewById(R.id.itv_left_username);
        iTVLeftUserPass = (IconTextView) fView.findViewById(R.id.tv_left_userpass);
        etUsername = (EditText) fView.findViewById(R.id.etUserName);
        etPassword = (EditText) fView.findViewById(R.id.etPassword);
        tvGetStarted = (TextView) fView.findViewById(R.id.btn_continue);
        tv_right_bottum_sign_up.setOnClickListener(this);
        setProperties();
        return fView;

    }
    private void setProperties() {
        UIUtils.setWindowBackground(getActivity());
        UIUtils.setBackgroundGradient(llGradient);
        UIUtils.setTextToThemeContrastColor(iTVLeftUserName);
        UIUtils.setTextToThemeContrastColor(iTVLeftUserPass);
        UIUtils.setTextToThemeContrastColor(etUsername);
        UIUtils.setTextToThemeContrastColor(etPassword);
        UIUtils.setTextToThemeContrastColor(tvGetStarted);
        GradientDrawable gDrawableStarted = (GradientDrawable) getResources().getDrawable(R.drawable.getstart_tv_background);
        gDrawableStarted.setStroke(2, UIUtils.getThemeContrastColor(getActivity()));
        llGetStarted.setBackgroundDrawable(gDrawableStarted);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_right_bottum_sign_up:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatSignUpFragment(), ChatLoginFragment.class.getSimpleName()).addToBackStack(ChatSignUpFragment.class.getSimpleName()).commit();
                break;
        }

    }
}
