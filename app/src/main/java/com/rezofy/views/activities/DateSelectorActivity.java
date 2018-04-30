package com.rezofy.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.custom_views.IconTextView;
import com.rezofy.views.fragments.CalendarFragment;
import com.rezofy.views.fragments.DateClickListener;

public class DateSelectorActivity extends AppCompatActivity implements DateClickListener, View.OnClickListener {

    private IconTextView iTVMenu;
    private TextView tvOk, tvTitle;
    CalendarFragment objCalendarFragment;
    Bundle bundle;
    Boolean isDep;
    String isDatDeparture, sourceTag;
    String isDatreturn;
    private ImageView btnFloating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        init();
        setProperties();
        setListener();
        getFragmentManager().beginTransaction().add(R.id.fragment_cantainer, objCalendarFragment, "CalendarFragment").commit();
    }

    private void setProperties() {
        int themeContrastColor = UIUtils.getThemeContrastColor(this);
        findViewById(R.id.header).setBackgroundColor(UIUtils.getThemeColor(this));
        iTVMenu.setTextColor(themeContrastColor);
        tvTitle.setTextColor(themeContrastColor);
        UIUtils.setNormalButtonProperties(tvOk);

    }

    private void setListener() {
        iTVMenu.setOnClickListener(this);
        tvOk.setOnClickListener(this);
    }

    private void init() {

        isDep = getIntent().getExtras().getBoolean("isDeparture");
        isDatDeparture = getIntent().getExtras().getString("isDateDeparture");
        sourceTag = getIntent().getExtras().getString("sourceTag");
        isDatreturn = getIntent().getExtras().getString("isDateReturn");

        iTVMenu = (IconTextView) findViewById(R.id.header).findViewById(R.id.left_icon);
        iTVMenu.setText(getString(R.string.icon_text_d));
        iTVMenu.setTextSize(15);
        tvTitle = (TextView) findViewById(R.id.header).findViewById(R.id.title);
        if(sourceTag.equals(Utils.TAG_ENQUIRY_FRAGMENT)){
            tvTitle.setText(getString(R.string.check_in_date));
        } else {
            tvTitle.setText(getString(R.string.select_date));
        }
        tvTitle.setTextSize(20);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        objCalendarFragment = new CalendarFragment();

        bundle = new Bundle();

        bundle.putBoolean("isDep", isDep);
        bundle.putString("isDatDeparture", isDatDeparture);
        bundle.putString("sourceTag", sourceTag);
        bundle.putString("isDatReturn", isDatreturn);

        objCalendarFragment.setArguments(bundle);

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
    public void onClick(String s, String e, int i) {

        ((CalendarFragment) getFragmentManager().findFragmentById(R.id.fragment_cantainer)).update(s, e, i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_icon:
                finish();
                break;

            case R.id.tv_ok:
                Intent intent = new Intent();
                intent.putExtra("returnDepartureDate", ((CalendarFragment) getFragmentManager().findFragmentById(R.id.fragment_cantainer)).returnSelectedDepartureDate());
                intent.putExtra("returnReturnDate", objCalendarFragment.returnSelectedReturnDate());
                setResult(Utils.DATE_SELECTOR_CODE, intent);
                finish();
                break;
        }
    }


}
