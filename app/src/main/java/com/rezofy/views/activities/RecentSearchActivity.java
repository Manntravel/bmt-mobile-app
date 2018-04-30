package com.rezofy.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.adapters.RecentSearchAdapter;
import com.rezofy.database.DbHelper;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.custom_views.IconTextView;

/**
 * Created by linchpin on 11/2/16.
 */
public class RecentSearchActivity extends Activity implements View.OnClickListener {

    RecentSearchAdapter adapter;
    DbHelper dbHelper;
    RecyclerView recent_rView;
    private IconTextView iTVMenu;
    SQLiteDatabase db;
    private TextView tvTitle;
    private ImageView btnFloating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_search);
        init();
        setProperties();
    }

    private void setProperties() {
        findViewById(R.id.header).setBackgroundColor(UIUtils.getThemeColor(this));
        iTVMenu.setTextColor(UIUtils.getThemeContrastColor(this));
        tvTitle.setTextColor(UIUtils.getThemeContrastColor(this));
        UIUtils.setBackgroundGradient(recent_rView);
    }

    private void init() {
        iTVMenu = (IconTextView) findViewById(R.id.header).findViewById(R.id.left_icon);
        iTVMenu.setText(getString(R.string.icon_text_k));
        iTVMenu.setTextSize(20);
        tvTitle=(TextView) findViewById(R.id.header).findViewById(R.id.title);
        tvTitle.setText(getString(R.string.recent_search));
        recent_rView = (RecyclerView) findViewById(R.id.rViewRecentSearch);
        dbHelper = new DbHelper(this);
        db = dbHelper.getReadableDatabase();
        String SELECT_QUERY = "Select * from " + DbHelper.SEARCH_TABLE_NAME + " order By " + DbHelper.timeStamp + " desc";
        Cursor cursor = dbHelper.getData(db, SELECT_QUERY);
        adapter = new RecentSearchAdapter(this, cursor);
        recent_rView.setLayoutManager(new LinearLayoutManager(this));
        recent_rView.setAdapter(adapter);
        iTVMenu.setOnClickListener(this);
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

    public void callFinish(Cursor cursor, int position) {
        try {
            cursor.moveToPosition(position);
            Intent i = new Intent();
            i.putExtra("originCityName", cursor.getString(cursor.getColumnIndex(DbHelper.originCityName)));
            i.putExtra("destCityName", cursor.getString(cursor.getColumnIndex(DbHelper.destCityName)));
            i.putExtra("originCityCode", cursor.getString(cursor.getColumnIndex(DbHelper.originCityCode)));
            i.putExtra("destCityCode", cursor.getString(cursor.getColumnIndex(DbHelper.destCityCode)));
            i.putExtra("departDate", cursor.getString(cursor.getColumnIndex(DbHelper.departDate)));
            if (!cursor.getString(cursor.getColumnIndex(DbHelper.retDate)).isEmpty())
                i.putExtra("retDate", cursor.getString(cursor.getColumnIndex(DbHelper.retDate)));
            i.putExtra("noOfAdults", cursor.getInt(cursor.getColumnIndex(DbHelper.noOfAdults)));
            i.putExtra("noOfChildren", cursor.getInt(cursor.getColumnIndex(DbHelper.noOfChildren)));
            i.putExtra("noOfInfants", cursor.getInt(cursor.getColumnIndex(DbHelper.noOfInfants)));
            i.putExtra("originAirportName", cursor.getString(cursor.getColumnIndex(DbHelper.originAirportName)));
            i.putExtra("destAirportName", cursor.getString(cursor.getColumnIndex(DbHelper.destAirportName)));
            i.putExtra("originCountryName", cursor.getString(cursor.getColumnIndex(DbHelper.originCountryName)));
            i.putExtra("destCountryName", cursor.getString(cursor.getColumnIndex(DbHelper.destCountryName)));
            i.putExtra("type", cursor.getString(cursor.getColumnIndex(DbHelper.searchType)));
            setResult(Utils.request_code_recentSearch, i);
            cursor.close();
            db.close();
            dbHelper.close();
            finish();

        } catch (Exception e) {
            Log.d("Trip", "eror in callFinish" + e);
        }

    }
}
