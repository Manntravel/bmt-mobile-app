package com.rezofy.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.rezofy.R;
import com.rezofy.adapters.PlaceSearchAdapter;
import com.rezofy.controllers.SearchAirportController;
import com.rezofy.models.request_models.ModelFlightSearch;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.custom_views.IconTextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, TextWatcher, View.OnClickListener {
    private ListView searchList;
    private PlaceSearchAdapter searchAdapter;
    private EditText etSearch;
    private IconTextView iTVBack;
    private ImageView btnFloating;
    private String searchFor;
    private String countryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placesearch);
        init();
        setProperties();
    }

    private void setProperties() {
        findViewById(R.id.ll_root).setBackgroundColor(UIUtils.getThemeColor(this));
        iTVBack.setTextColor(UIUtils.getThemeContrastColor(this));
        etSearch.setHintTextColor(UIUtils.getThemeContrastColor(this));
        etSearch.setTextColor(UIUtils.getThemeContrastColor(this));
    }

    private void init() {
        searchFor = getIntent().getStringExtra("tag");

        iTVBack = (IconTextView) findViewById(R.id.left_icon);
        iTVBack.setOnClickListener(this);
        etSearch = (EditText) findViewById(R.id.title);
        etSearch.addTextChangedListener(this);
        searchList = (ListView) findViewById(R.id.search_list);
        searchList.setOnItemClickListener(this);
        countryName = getString(R.string.countryName);
        if(searchFor.equals("from") && countryName.length() > 1){
            searchAdapter = new PlaceSearchAdapter(this, SearchAirportController.getCountryPlaceList(this, countryName));
        } else{
            searchAdapter = new PlaceSearchAdapter(this, SearchAirportController.getDefaultPlaceList(this));
        }
        searchList.setAdapter(searchAdapter);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        if (getIntent().getStringExtra("tag").equals("from"))
            intent.putExtra("tag", "from");
        else
            intent.putExtra("tag", "to");
        intent.putExtra("city_name", searchAdapter.getPlaceList().get(position).getCityName());
        intent.putExtra("airport_code", searchAdapter.getPlaceList().get(position).getAirportCode());
        intent.putExtra("airport_name", searchAdapter.getPlaceList().get(position).getAirportName());
        intent.putExtra("country_code", searchAdapter.getPlaceList().get(position).getCountryCode());
        intent.putExtra("country_name", searchAdapter.getPlaceList().get(position).getCountryName());
        setResult(Utils.PLACE_SEARCH_CODE, intent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        List<ModelFlightSearch> searchList;
        if (s.toString().length() < 2) {
            if(searchFor.equals("from") && countryName.length() > 1){
                searchList = SearchAirportController.getCountryPlaceList(this, countryName); //Australia
            } else {
                searchList = SearchAirportController.getDefaultPlaceList(this);
            }
        }
        else {
            if(searchFor.equals("from") && countryName.length() > 1){
                searchList = SearchAirportController.getCountrySearchedFlightList(this, etSearch.getText().toString(), countryName);
            } else {
                searchList = SearchAirportController.getSearchedFlightList(this, etSearch.getText().toString());
            }
        }
        searchAdapter.setPlaceList(searchList);
        searchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_icon:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }
    }
}
