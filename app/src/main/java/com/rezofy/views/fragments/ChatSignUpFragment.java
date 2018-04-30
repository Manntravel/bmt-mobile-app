package com.rezofy.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rezofy.R;

/**
 * Created by linchpin on 23/8/16.
 */

public class ChatSignUpFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fView = inflater.inflate(R.layout.fragment_chat_signup, container, false);
        return fView;
    }
    @Override
    public void onClick(View v) {

    }
}
