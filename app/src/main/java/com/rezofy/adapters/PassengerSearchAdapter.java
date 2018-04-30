package com.rezofy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.models.util_models.TravellerDBModel;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.PassengerSearchActivity;

import java.util.List;

/**
 * Created by linchpin on 17/2/16.
 */
public class PassengerSearchAdapter extends RecyclerView.Adapter<PassengerDataHolder> {
    private Context context;
    private PassengerSearchActivity pasSearchOb;
    private List<TravellerDBModel> travellerDBModelList;

    public PassengerSearchAdapter(Context context, List<TravellerDBModel> travellerDBModelList) {
        pasSearchOb = (PassengerSearchActivity) context;
        this.context = context;
        this.travellerDBModelList = travellerDBModelList;
    }


    @Override
    public PassengerDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PassengerDataHolder(LayoutInflater.from(context).inflate(R.layout.row_passeger_rview_search, parent, false));
    }


    @Override
    public void onBindViewHolder(final PassengerDataHolder holder, final int position) {
        holder.names.setText(new StringBuilder().append(travellerDBModelList.get(position).getFirstName()).append(" ").append(travellerDBModelList.get(position).getLastName()).toString());
        if (travellerDBModelList.get(position).getGender().equalsIgnoreCase("Mr")) {
            holder.gender.setText("Male");
        } else if (travellerDBModelList.get(position).getGender().equalsIgnoreCase("Ms")) {
            holder.gender.setText("Female");
        }
        if (travellerDBModelList.get(position).getDob().equals("")) {
            holder.dob.setVisibility(View.GONE);
        } else {
            holder.dob.setVisibility(View.VISIBLE);
            holder.dob.setText(Utils.changeDateFormat(travellerDBModelList.get(position).getDob(), Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_d_space_LLL_space_yyyy));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.putExtra("traveller_db_model", travellerDBModelList.get(position));
                pasSearchOb.setResult(Activity.RESULT_OK, intent);
                Utils.hideSoftKey(context);
                pasSearchOb.finish();
            }
        });

    }


    @Override
    public int getItemCount() {
        return travellerDBModelList.size();
    }

    public List<TravellerDBModel> getTravellerDBModelList() {
        return travellerDBModelList;
    }

    public void setTravellerDBModelList(List<TravellerDBModel> travellerDBModelList) {
        this.travellerDBModelList = travellerDBModelList;
    }
}


class PassengerDataHolder extends RecyclerView.ViewHolder {
    public TextView names, dob, gender;

    public PassengerDataHolder(View itemView) {
        super(itemView);
        names = (TextView) itemView.findViewById(R.id.pas_name);
        dob = (TextView) itemView.findViewById(R.id.dob);
        gender = (TextView) itemView.findViewById(R.id.gender);
    }
}


