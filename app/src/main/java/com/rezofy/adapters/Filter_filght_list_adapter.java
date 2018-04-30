package com.rezofy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.models.util_models.FilterListModel;
import com.rezofy.views.activities.FilterActivity;

import java.io.IOException;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by linchpin on 3/2/16.
 */
public class Filter_filght_list_adapter extends RecyclerView.Adapter<FlightHolder> {
    Context context;
    ArrayList<FilterListModel> flights;
    ArrayList<String> checkedAirlineNames = new ArrayList<>();

    public ArrayList<FilterListModel> getFlights() {
        return flights;
    }

    public void setFlights(ArrayList<FilterListModel> flights) {
        this.flights = flights;
    }

    FilterActivity filter;


    public Filter_filght_list_adapter(Context context, ArrayList<FilterListModel> flights) {
        this.context = context;
        filter = (FilterActivity) context;
        this.flights = flights;
        // this.checkedAirlineNames = checkedAirlineNames;
    }


    @Override
    public FlightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.filter_rview_item_view, parent, false);
        FlightHolder holder = new FlightHolder(view);
//        holder.checkbox.setOnClickListener(Filter_filght_list_adapter.this);
//
//        holder.checkbox.setTag(holder);

        return holder;
    }

    public void setCheckedAirlineNames(ArrayList<String> checkedAirlineNames) {
        this.checkedAirlineNames = checkedAirlineNames;
    }

    @Override
    public void onBindViewHolder(final FlightHolder holder, final int position) {

        try {
            holder.ivCarrierImage.setImageDrawable(new GifDrawable(context.getAssets(), new StringBuilder().append(context.getString(R.string.image_path_pre_string)).append(flights.get(position).getCarrier()).append(context.getString(R.string.image_path_post_string)).toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //holder.flight_name.setText(flights.get(position).getAirlLineName());
        holder.flight_name.setText(flights.get(position).getCarrier());
        holder.checkbox.setTag(position);

        // Log.d("Trip", "inside onbindview airline name is " + flights.get(position).getAirlLineName() + " ::: " + checkedAirlineNames.contains(flights.get(position).getAirlLineName()) + " :: " + checkedAirlineNames.size());
        if (checkedAirlineNames.contains(flights.get(position).getCarrier())) {  //getAirlLineName()
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);
        }


        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (int) ((CheckBox) buttonView).getTag();
                Log.d("Trip", "inside onchecked " + position);
                //int i = holder.getAdapterPosition();
                if (isChecked) {
                    if (!checkedAirlineNames.contains(flights.get(position).getCarrier())) {  //getAirlLineName()
                        checkedAirlineNames.add(flights.get(position).getCarrier());  //getAirlLineName
                    }

                } else {
                    if (checkedAirlineNames.contains(flights.get(position).getCarrier())) ;  //getAirlLineName
                    {
                        checkedAirlineNames.remove(flights.get(position).getCarrier());   //getAirlLineName
                    }

                }

                boolean flag = filter.checkForApplyButtion();
                filter.toggleBackButton(flag);


            }
        });


    }

    public ArrayList<String> getCheckedAirlineNames() {
        return checkedAirlineNames;
    }

    @Override
    public int getItemCount() {
        return flights.size();
    }


}


class FlightHolder extends RecyclerView.ViewHolder {
    public TextView flight_name;
    public CheckBox checkbox;
    GifImageView ivCarrierImage;

    public FlightHolder(View itemView) {
        super(itemView);
        flight_name = (TextView) itemView.findViewById(R.id.flight_name);
        checkbox = (CheckBox) itemView.findViewById(R.id.checkbox1);
        ivCarrierImage = (GifImageView) itemView.findViewById(R.id.image_flight);
    }


}

