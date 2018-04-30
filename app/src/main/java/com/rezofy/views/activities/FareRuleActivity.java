package com.rezofy.views.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.views.custom_views.IconTextView;
import com.rezofy.views.fragments.FareRulesFragment;
import com.rezofy.views.fragments.ServiceFeeFragment;

/**
 * Created by Linchpin66 on 27-01-2016.
 */
public class FareRuleActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private IconTextView iTVMenu;
    private TextView tvTitle;
    private ImageView btnFloating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_rules);
        init();
        setProperties();

    }

    private void setProperties() {
        int themeColor, themeContrastColor;
        themeColor=UIUtils.getThemeColor(this);
        themeContrastColor=UIUtils.getThemeContrastColor(this);
        findViewById(R.id.header).setBackgroundColor(themeColor);
        iTVMenu.setTextColor(themeContrastColor);
        tvTitle.setTextColor(themeContrastColor);
        tabLayout.setSelectedTabIndicatorColor(themeColor);
        tabLayout.setTabTextColors(UIUtils.getThemeLightColor(this), themeColor);
    }


    private void init() {
        iTVMenu = (IconTextView) findViewById(R.id.header).findViewById(R.id.left_icon);
        iTVMenu.setText("k");
        iTVMenu.setTextSize(20);
        tvTitle=(TextView) findViewById(R.id.header).findViewById(R.id.title);
        tvTitle.setText(getString(R.string.header_fare_rule_text));
        iTVMenu.setOnClickListener(this);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        btnFloating = (ImageView) findViewById(R.id.btn_flaoting);
        btnFloating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatingButtonMove floatingButtonMove = new FloatingButtonMove(getApplicationContext());
                floatingButtonMove.dragView(v, event);
                return true;
            }
        });
        if(Boolean.parseBoolean(getString(R.string.hideChatIcon))) {
            btnFloating.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_icon:
                finish();
                break;
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private String[] name = {"Fare Rules", "Service Fee"};

        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle bundle = new Bundle();
            bundle.putString("fare_rule_response", getIntent().getStringExtra("fare_rule_response"));
            switch (position) {
                case 0:
                    fragment = new FareRulesFragment();
                    break;

                case 1:
                    fragment = new ServiceFeeFragment();
                    break;
            }
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return name[position];
        }


        @Override
        public int getCount() {
            return name.length;
        }
    }
}
