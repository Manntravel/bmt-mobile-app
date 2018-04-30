package com.rezofy.views.activities;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.rezofy.R;
import com.rezofy.adapters.PassengerSearchAdapter;
import com.rezofy.database.DbHelper;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.custom_views.IconTextView;

/**
 * Created by linchpin on 17/2/16.
 */
public class PassengerSearchActivity extends Activity implements TextWatcher, View.OnClickListener {
    private RecyclerView searchRView;
    private PassengerSearchAdapter adapter;
    private EditText etSearch;
    private IconTextView iTVBack;
    DbHelper dbHelper;
    SQLiteDatabase db;
    private ImageView btnFloating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_search);
        init();
        setProperties();
        dbHelper = new DbHelper(this);
        db = dbHelper.getReadableDatabase();
        String SELECT_QUERY = "Select * from " + DbHelper.PASSENGER_TABLE_NAME + " order By " + DbHelper.pas_timeStamp + " desc";
        adapter = new PassengerSearchAdapter(this, dbHelper.getTravellerData(db, SELECT_QUERY));
        searchRView.setLayoutManager(new LinearLayoutManager(this));
        searchRView.addItemDecoration(new SimpleDividerItemDecoration(this));
        searchRView.setAdapter(adapter);
    }


    private void init() {
        iTVBack = (IconTextView) findViewById(R.id.left_icon);
        iTVBack.setOnClickListener(this);
        etSearch = (EditText) findViewById(R.id.title);
        etSearch.addTextChangedListener(this);
        searchRView = (RecyclerView) findViewById(R.id.passenger_rView);
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

    private void setProperties() {
        findViewById(R.id.ll_root).setBackgroundColor(UIUtils.getThemeColor(this));
        iTVBack.setTextColor(UIUtils.getThemeContrastColor(this));
        etSearch.setHintTextColor(UIUtils.getThemeContrastColor(this));
        etSearch.setTextColor(UIUtils.getThemeContrastColor(this));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String partName = etSearch.getText().toString();
        String selQuery = "Select * from " + DbHelper.PASSENGER_TABLE_NAME + " where " + DbHelper.pas_first_name + " like '%" + partName + "%' or " + DbHelper.pas_last_name + " like '%" + partName + "%' order By " + DbHelper.pas_timeStamp + " desc";
        Log.d("Trip", "sel query is " + selQuery);
        adapter.setTravellerDBModelList(dbHelper.getTravellerData(db, selQuery));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_icon:
                setResult(Activity.RESULT_CANCELED);
                Utils.hideSoftKey(this);
                finish();
                break;
        }
    }

/*    public void finishActivity(Cursor cursor, int position) {
        try {
            if (cursor == null) {
                Log.d("Trip", "cursor is null");
            } else {
                Log.d("Trip", "cursor is not null");
            }
            cursor.moveToPosition(position);
            Intent i = new Intent();
            i.putExtra("firstname", cursor.getString(cursor.getColumnIndex(DbHelper.pas_first_name)));
            i.putExtra("lastname", cursor.getString(cursor.getColumnIndex(DbHelper.pas_last_name)));
            i.putExtra("gender", cursor.getString(cursor.getColumnIndex(DbHelper.pas_gender)));
            i.putExtra("meal", cursor.getString(cursor.getColumnIndex(DbHelper.pas_meal)));
            i.putExtra("dob", cursor.getString(cursor.getColumnIndex(DbHelper.pas_dob)));
            i.putExtra("passportNo", cursor.getString(cursor.getColumnIndex(DbHelper.pas_passport_no)));
            i.putExtra("issuingCountry", cursor.getString(cursor.getColumnIndex(DbHelper.pas_passport_issued_by)));
            i.putExtra("nationality", cursor.getString(cursor.getColumnIndex(DbHelper.pas_resident_country)));
            i.putExtra("passportExpiration", cursor.getString(cursor.getColumnIndex(DbHelper.pas_passport_expiration_date)));
            i.putExtra("travellerType", cursor.getString(cursor.getColumnIndex(DbHelper.pas_passenger_type)));

            setResult(RESULT_OK, i);
            cursor.close();
            Utils.hideSoftKey(this);
            finish();


        } catch (Exception e) {
            Log.d("Trip", "Eror in finishActivity " + e);
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        dbHelper.close();
    }
}

class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public SimpleDividerItemDecoration(Context context) {
        mDivider = context.getResources().getDrawable(R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }


}
