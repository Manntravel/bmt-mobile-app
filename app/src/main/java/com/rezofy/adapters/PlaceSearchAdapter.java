package com.rezofy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.models.request_models.ModelFlightSearch;
import com.rezofy.views.custom_views.IconTextView;

import java.util.List;

public class PlaceSearchAdapter extends BaseAdapter {

    private Context context;
    private List<ModelFlightSearch> placeList;

    public PlaceSearchAdapter(Context context, List<ModelFlightSearch> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public Object getItem(int position) {
        return placeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_placesearch, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.flightIcon = (IconTextView) convertView.findViewById(R.id.it_flight_icon);
            viewHolder.stateName = (TextView) convertView.findViewById(R.id.state_name);
            viewHolder.countryName = (TextView) convertView.findViewById(R.id.country_name);
            viewHolder.airport = (TextView) convertView.findViewById(R.id.airport);
            viewHolder.airportName = (TextView) convertView.findViewById(R.id.airport_name);
            convertView.setTag(viewHolder);

        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.flightIcon.setText(context.getString(R.string.icon_text_o));
        viewHolder.stateName.setText(placeList.get(position).getCityName() + ", ");
        viewHolder.countryName.setText(placeList.get(position).getCountryName());
        viewHolder.airport.setText(placeList.get(position).getAirportCode() + " - ");
        viewHolder.airportName.setText(placeList.get(position).getAirportName());

        return convertView;

    }

    static class ViewHolder {
        TextView stateName, countryName, airport, airportName;
        IconTextView flightIcon;
    }

    public List<ModelFlightSearch> getPlaceList() {
        return placeList;
    }

    public void setPlaceList(List<ModelFlightSearch> placeList) {
        this.placeList = placeList;
    }
}


