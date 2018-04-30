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

import com.rezofy.R;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.models.MyDocument;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.MyDocumentsActivity;
import com.rezofy.views.activities.MyDocumentsDetailActivity;
import com.rezofy.views.fragments.MyTripsFragment;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * Created by linchpin on 6/2/17.
 */

public class MyDocumentsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, NetworkTask.Result {



    // Types used for identfy view create
    private final int VIEW_TYPE_ITEM = 1;


    private Context ctx;

    private List<MyDocument> documentList;
    private MyDocumentsActivity activity;


    private RecyclerView recyclerView;

    public MyDocumentsListAdapter(Context ctx, List<MyDocument> documentList, RecyclerView mRecyclerView) {
        setConstructorData(ctx, documentList, mRecyclerView);

    }

    public MyDocumentsListAdapter(MyTripsFragment myTripsFragment, Context ctx, List<MyDocument> documentList, RecyclerView mRecyclerView) {
        setConstructorData(ctx, documentList, mRecyclerView);

    }

    private void setConstructorData(Context ctx, List<MyDocument> documentList, RecyclerView mRecyclerView) {
        this.ctx = ctx;
        activity = (MyDocumentsActivity) ctx;

        this.documentList = documentList;
        recyclerView = mRecyclerView;

    }



    @Override
    public int getItemViewType(int position) {

        return  VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.custom_my_document_item, parent, false);
            return new MyDocumentsListAdapter.MyViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        //set Flight holder items
        if (holder instanceof MyDocumentsListAdapter.MyViewHolder) {
            MyDocumentsListAdapter.MyViewHolder myViewHolder = (MyDocumentsListAdapter.MyViewHolder) holder;
            myViewHolder.rlRoot.setTag(position);
            myViewHolder.rlRoot.setOnClickListener(this);
            String type = documentList.get(position).getType();
            int icon = ctx.getResources().obtainTypedArray(R.array.array_document_type).getResourceId(((ArrayUtils.indexOf(ctx.getResources().getStringArray(R.array.array_document_type), type))), R.drawable.passport);
            myViewHolder.ivType.setImageResource(icon);
            myViewHolder.tvDocumentName.setText(documentList.get(position).getName());
            myViewHolder.tvDate.setText(documentList.get(position).getCreatedOn());
            myViewHolder.tvType.setText(type);



        }


    }


    @Override
    public int getItemCount() {
        if ( documentList == null || documentList.isEmpty()) {

                recyclerView.setVisibility(View.GONE);

            return 0;
        } else {

                recyclerView.setVisibility(View.VISIBLE);

            return documentList.size();
        }
    }


    @Override
    public void onClick(View v) {

            int position = (int) v.getTag();
            Intent intent = new Intent(ctx, MyDocumentsDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("type", documentList.get(position));
            intent.putExtras(bundle);
            ctx.startActivity(intent);


    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {
        if (object == null || object.equals("")) {
            Utils.showAlertDialog(ctx, ctx.getString(R.string.app_name), ctx.getString(R.string.network_error), ctx.getString(R.string.ok), null, null, null);

        } else {

        }
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlRoot;
        ImageView ivType;
        TextView tvDocumentName, tvDate, tvType;

        public MyViewHolder(View itemView) {
            super(itemView);
            rlRoot = (RelativeLayout)itemView.findViewById(R.id.rlRoot);
            ivType = (ImageView)itemView.findViewById(R.id.ivType);
            tvDocumentName = (TextView)itemView.findViewById(R.id.tvDocumentName);
            tvDate = (TextView)itemView.findViewById(R.id.tvDate);
            tvType = (TextView)itemView.findViewById(R.id.tvType);
        }


    }




}