package com.rezofy.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rezofy.R;
import com.rezofy.models.DocumentTypes;
import com.rezofy.views.activities.MyDocumentsActivity;
import com.rezofy.views.activities.MyDocumentTypeActivity;
import com.rezofy.views.fragments.MyTripsFragment;

import java.util.List;

/**
 * Created by linchpin on 8/2/17.
 */

public class MyDocumentTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private static final int ID_VIEW_TRIP =  1;
    private Context ctx;
    //TripList tripRecordsList;
    List<DocumentTypes> documentTypes;
    MyDocumentTypeActivity activity;

    private RecyclerView recyclerView;
    private final int request_code_webView = 1;
    public MyDocumentTypeAdapter(Context ctx, List<DocumentTypes> documentTypes, RecyclerView mRecyclerView) {
        setConstructorData(ctx, documentTypes, mRecyclerView);
        this.ctx =  ctx;
    }

    public MyDocumentTypeAdapter(MyTripsFragment myTripsFragment, Context ctx, List<DocumentTypes> documentTypes, RecyclerView mRecyclerView) {
        setConstructorData(ctx, documentTypes, mRecyclerView);

    }

    private void setConstructorData(Context ctx, List<DocumentTypes> documentTypes, RecyclerView mRecyclerView) {
        this.ctx = ctx;
        activity = (MyDocumentTypeActivity)ctx;
        this.documentTypes = documentTypes;
        recyclerView = mRecyclerView;


    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        // View view = LayoutInflater.from(ctx).inflate(R.layout.trip_item, parent, false);
        View view = LayoutInflater.from(ctx).inflate(R.layout.custom_my_document_type, parent, false);
        return new MyDocumentTypeAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyDocumentTypeAdapter.MyViewHolder) {
            MyDocumentTypeAdapter.MyViewHolder myViewHolder = (MyDocumentTypeAdapter.MyViewHolder) holder;
            myViewHolder.rlRoot.setTag(position);
            myViewHolder.rlRoot.setOnClickListener(this);
            myViewHolder.ivType.setImageResource(documentTypes.get(position).getIcon());
            myViewHolder.tvType.setText(documentTypes.get(position).getType());
            if(documentTypes.get(position).getCount()>0)
            {
                myViewHolder.tvCount.setVisibility(View.VISIBLE);
                myViewHolder.tvCount.setText(""+documentTypes.get(position).getCount());
            }
            else
            {
                myViewHolder.tvCount.setVisibility(View.GONE);
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

    public void setDocumentTypes(List<DocumentTypes> documentTypes) {
        this.documentTypes = documentTypes;
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rlRoot)
        {
            int position = (int) v.getTag();
            DocumentTypes t = documentTypes.get(position);
            if(t.getCount()>0) {
                Intent intent = new Intent(ctx, MyDocumentsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("type", t);
                intent.putExtras(bundle);
                ctx.startActivity(intent);
            }
            else
            {
                Toast.makeText(ctx,"No document is added",Toast.LENGTH_SHORT).show();
            }
            /*int position = (int) v.getTag();
            for(DocumentTypes types: documentTypes)
            {
                types.setChecked(false);
            }
            documentTypes.get(position).setChecked(true);
            activity.setSelectedType(documentTypes.get(position).getType(),documentTypes.get(position).getIcon() );
            notifyDataSetChanged();*/
        }

    }



    class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlRoot;
        ImageView ivType;
       TextView  tvType, tvCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            rlRoot = (RelativeLayout)itemView.findViewById(R.id.rlRoot);
            ivType = (ImageView)itemView.findViewById(R.id.ivType);
                      tvType = (TextView)itemView.findViewById(R.id.tvType);
            tvCount = (TextView)itemView.findViewById(R.id.tvCount);
        }

    }




}