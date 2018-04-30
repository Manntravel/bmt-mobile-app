package com.rezofy.views.custom_views;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by linchpin11192 on 04-May-2016.
 */
public class SelectorDialog extends Dialog {
    private SelectorDialog(Context context, int layoutResId, int featureFlag, boolean isCancelable) {
        super(context);
        requestWindowFeature(featureFlag);
        setContentView(layoutResId);
        setCancelable(isCancelable);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public static SelectorDialog getInstance(Context context, int layoutResId, int featureFlag, boolean isCancelable){
        return new SelectorDialog(context, layoutResId, featureFlag, isCancelable);
    }

    public interface SelectorRecyclerViewHandler {

        RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, int selectionId);

        void onBindViewHolder(RecyclerView.ViewHolder holder, final int position, int selectionId, int srcResId);

        int getItemCount(int selectionId);
    }

    public static class SelectorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private int selectionId;
        private int srcResId;
        private SelectorDialog.SelectorRecyclerViewHandler handler;

        public SelectorAdapter(int selectionId, int srcResId, SelectorDialog.SelectorRecyclerViewHandler handler) {
            this.selectionId = selectionId;
            this.srcResId = srcResId;
            this.handler = handler;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return handler.onCreateViewHolder(parent, viewType, selectionId);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            handler.onBindViewHolder(holder, position, selectionId, srcResId);
        }

        @Override
        public int getItemCount() {
            return handler.getItemCount(selectionId);
        }
    }

}
