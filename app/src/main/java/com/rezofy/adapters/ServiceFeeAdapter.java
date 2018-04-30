package com.rezofy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rezofy.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by linchpin11192 on 19-Feb-2016.
 */
public class ServiceFeeAdapter extends RecyclerView.Adapter<ServiceFeeAdapter.ViewHolder> {
    private Context context;
    private JSONArray serviceFeeJSONArray;

    public ServiceFeeAdapter(Context context, JSONArray serviceFeeJSONArray) {
        this.context = context;
        this.serviceFeeJSONArray = serviceFeeJSONArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_service_fee, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.tvServiceFee.setText(serviceFeeJSONArray.getString(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return serviceFeeJSONArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvServiceFee;

        public ViewHolder(View itemView) {
            super(itemView);
            tvServiceFee = (TextView) itemView.findViewById(R.id.service_fee);
        }
    }
}
