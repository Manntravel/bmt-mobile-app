package com.rezofy.views.activities;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
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
import com.rezofy.adapters.GstDetailsSearchAdapter;
import com.rezofy.database.DbHelper;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.custom_views.IconTextView;

public class GstDetailsSearchActivity extends Activity implements TextWatcher, View.OnClickListener {

    private RecyclerView searchRView;
    private GstDetailsSearchAdapter adapter;
    private EditText etSearch;
    private IconTextView iTVBack;
    DbHelper dbHelper;
    SQLiteDatabase db;
    private ImageView btnFloating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gst_details_search);
        init();
        setProperties();
        dbHelper = new DbHelper(this);
        db = dbHelper.getReadableDatabase();
        String SELECT_QUERY = "Select * from " + DbHelper.GST_DETAILS_TABLE + " order By " + DbHelper.gstTimeStamp + " desc";
        adapter = new GstDetailsSearchAdapter(this, this, dbHelper.getGstData(db, SELECT_QUERY));
        searchRView.setLayoutManager(new LinearLayoutManager(this));
        searchRView.addItemDecoration(new SimpleDividerItemDecoration(this));
        searchRView.setAdapter(adapter);
    }

    private void setProperties() {
        findViewById(R.id.ll_root).setBackgroundColor(UIUtils.getThemeColor(this));
        iTVBack.setTextColor(UIUtils.getThemeContrastColor(this));
        etSearch.setHintTextColor(UIUtils.getThemeContrastColor(this));
        etSearch.setTextColor(UIUtils.getThemeContrastColor(this));
    }

    private void init() {
        iTVBack = (IconTextView) findViewById(R.id.left_icon);
        iTVBack.setOnClickListener(this);
        etSearch = (EditText) findViewById(R.id.title);
        if(getIntent().getStringExtra("BasisForSearch").equalsIgnoreCase("companyName")) {
            etSearch.setHint(getResources().getString(R.string.search_gst_company_name));
        } else {
            etSearch.setHint(getResources().getString(R.string.search_gst_no));
        }
        etSearch.addTextChangedListener(this);
        searchRView = (RecyclerView) findViewById(R.id.gstDetails_rView);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String partName = etSearch.getText().toString();
        String selQuery = null;
        if(getIntent().getStringExtra("BasisForSearch").equalsIgnoreCase("companyName")) {
            selQuery = "Select * from " + DbHelper.GST_DETAILS_TABLE + " where " + DbHelper.gstCompanyName + " like '%" + partName + "%' order By " + DbHelper.pas_timeStamp + " desc";
        } else {
            selQuery = "Select * from " + DbHelper.GST_DETAILS_TABLE + " where " + DbHelper.gstNo + " like '%" + partName + "%' order By " + DbHelper.pas_timeStamp + " desc";
        }
        Log.d("Trip", "sel query is " + selQuery);
        adapter.setGstDBModelList(dbHelper.getGstData(db, selQuery));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        dbHelper.close();
    }
}
/*

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
}*/
