package com.rezofy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rezofy.R;

import java.util.List;

/**
 * Created by linchpin11192 on 15-Feb-2016.
 */
public class CountrySpinnerAdapter extends BaseAdapter {
    private Context context;
    List<String> countryList;

    public CountrySpinnerAdapter(Context context, List<String> countryList) {
        this.context = context;
        this.countryList = countryList;

    }

    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.spinner_row, viewGroup, false);
        TextView countryName = (TextView) view.findViewById(R.id.name_gender);
        countryName.setText(countryList.get(i));
        return view;
    }

    public List<String> getCountryList() {
        return countryList;
    }
}
