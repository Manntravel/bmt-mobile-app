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
import com.rezofy.models.GstDBModel;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.GstDetailsSearchActivity;

import java.util.List;

/**
 * Created by Rakhi Mittal on 22-Sep-17.
 */

public class GstDetailsSearchAdapter extends RecyclerView.Adapter<GstDetailsDataHolder> {
    private Context context;
    private GstDetailsSearchActivity searchActivity;
    private List<GstDBModel> gstDBModelList;

    public GstDetailsSearchAdapter(Context context, GstDetailsSearchActivity searchActivity, List<GstDBModel> gstDBModelList) {
        this.context = context;
        this.searchActivity = searchActivity;
        this.gstDBModelList = gstDBModelList;
    }

    @Override
    public GstDetailsDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GstDetailsDataHolder(LayoutInflater.from(context).inflate(R.layout.row_gst_details_rview_search, parent, false));
    }

    @Override
    public int getItemCount() {
        return gstDBModelList.size();
    }

    @Override
    public void onBindViewHolder(GstDetailsDataHolder holder, final int position) {
        holder.companyName.setText(gstDBModelList.get(position).getCompanyName());
        holder.gstNo.setText(gstDBModelList.get(position).getGstNo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.putExtra("GstDetailsDbModel", gstDBModelList.get(position));
                searchActivity.setResult(Activity.RESULT_OK, intent);
                Utils.hideSoftKey(context);
                searchActivity.finish();
            }
        });
    }

    public List<GstDBModel> getGstDBModelList() {
        return gstDBModelList;
    }

    public void setGstDBModelList(List<GstDBModel> gstDBModelList) {
        this.gstDBModelList = gstDBModelList;
    }
}

class GstDetailsDataHolder extends RecyclerView.ViewHolder {

    TextView companyName, gstNo;

    public GstDetailsDataHolder(View itemView) {
        super(itemView);
        companyName = (TextView) itemView.findViewById(R.id.company_name);
        gstNo = (TextView) itemView.findViewById(R.id.gst_no);
    }
}
