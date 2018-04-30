package com.rezofy.views.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.adapters.TripBoxMenuAdapter;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.custom_views.IconTextView;

/**
 * Created by anuj on 7/2/17.
 */
public class WebCheckinActivity extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    private View header;
    private RelativeLayout rlRoot;
    private IconTextView iTVMenu;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private TripBoxMenuAdapter tripBoxMenuAdapter;
    private Intent intent;
    private String tagSoruce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flight_checkin);
        initViews();
        setProperties();
        setListener();
        setData();

    }

        private void initViews() {
        context = this;
        intent = getIntent();
        tagSoruce = intent.getStringExtra(Utils.TAG_SOURCE);

        header = findViewById(R.id.header);
        if(header != null) {
            iTVMenu = (IconTextView) header.findViewById(R.id.left_icon);
            iTVMenu.setText(getString(R.string.icon_text_k));
            iTVMenu.setOnClickListener(this);
            iTVMenu.setTextSize(20);

            tvTitle = (TextView) header.findViewById(R.id.title);

            if(tagSoruce.equals(Utils.TAG_WEB_CHECKIN_ACTIVITY)) {
                tvTitle.setText(getString(R.string.web_checkin));
            } else {
                tvTitle.setText(getString(R.string.baggage_information));
            }

        }

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            tripBoxMenuAdapter = new TripBoxMenuAdapter(context, tagSoruce);
            recyclerView.setAdapter(tripBoxMenuAdapter);

            rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
    }

    private void setProperties() {
        int themeContrastColor = UIUtils.getThemeContrastColor(context);
        header.setBackgroundColor(UIUtils.getThemeColor(context));
        iTVMenu.setTextColor(themeContrastColor);
        tvTitle.setTextColor(themeContrastColor);
    }

    private void setListener() {

    }

    private void setData() {

    }

    public void changeFragment(Fragment fragment, Bundle data, String fragmentTag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(data != null)
            fragment.setArguments(data);
        fragmentTransaction.replace(R.id.rl_root, fragment, fragmentTag);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.left_icon:
                   onBackPressed();
                break;
        }

    }
}
