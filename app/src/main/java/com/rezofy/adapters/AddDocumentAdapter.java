package com.rezofy.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.models.DocumentTypes;
import com.rezofy.views.activities.AddDocumentActivity;
import com.rezofy.views.fragments.MyTripsFragment;

import java.util.List;








public class AddDocumentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private static final int ID_VIEW_TRIP =  1;
    private Context ctx;
    //TripList tripRecordsList;
    List<DocumentTypes> documentTypes;
    AddDocumentActivity activity;

    private RecyclerView recyclerView;
    private final int request_code_webView = 1;
    public AddDocumentAdapter(Context ctx, List<DocumentTypes> documentTypes, RecyclerView mRecyclerView) {
        setConstructorData(ctx, documentTypes, mRecyclerView);
        this.ctx =  ctx;
    }

    public AddDocumentAdapter(MyTripsFragment myTripsFragment, Context ctx, List<DocumentTypes> documentTypes, RecyclerView mRecyclerView) {
        setConstructorData(ctx, documentTypes, mRecyclerView);

    }

    private void setConstructorData(Context ctx, List<DocumentTypes> documentTypes, RecyclerView mRecyclerView) {
        this.ctx = ctx;
        activity = (AddDocumentActivity)ctx;
        this.documentTypes = documentTypes;
        recyclerView = mRecyclerView;


    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        // View view = LayoutInflater.from(ctx).inflate(R.layout.trip_item, parent, false);
        View view = LayoutInflater.from(ctx).inflate(R.layout.custom_item_document_types, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.llRoot.setTag(position);
            myViewHolder.llRoot.setOnClickListener(this);


                myViewHolder.ivType.setImageResource(documentTypes.get(position).getIcon());
                myViewHolder.tvType.setText(documentTypes.get(position).getType());
            if(documentTypes.get(position).isChecked())
            {
                myViewHolder.tvCheck.setVisibility(View.VISIBLE);

            }
            else
            {
                myViewHolder.tvCheck.setVisibility(View.GONE);
            }


        }


    }


    @Override
    public int getItemCount() {
        if ( documentTypes == null || documentTypes.isEmpty()) {

            recyclerView.setVisibility(View.GONE);

            return 0;
        } else {

            recyclerView.setVisibility(View.VISIBLE);

            return documentTypes.size();
        }
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.llRoot)
        {
            int position = (int) v.getTag();
            for(DocumentTypes types: documentTypes)
            {
                types.setChecked(false);
            }
            documentTypes.get(position).setChecked(true);
            activity.setSelectedType(documentTypes.get(position).getType(),documentTypes.get(position).getIcon() );
            notifyDataSetChanged();
        }

    }



    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivType;
        TextView tvType;
        LinearLayout llRoot;
        TextView tvCheck;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivType = (ImageView) itemView.findViewById(R.id.ivType);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            llRoot = (LinearLayout)itemView.findViewById(R.id.llRoot);
            tvCheck = (TextView)itemView.findViewById(R.id.tvCheck);
        }


    }




}