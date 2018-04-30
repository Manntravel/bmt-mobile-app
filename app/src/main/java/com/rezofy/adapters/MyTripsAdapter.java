package com.rezofy.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.interfaces.OnLoadListener;
import com.rezofy.models.response_models.MyTrip;
import com.rezofy.models.response_models.TripList;
import com.rezofy.models.response_models.ViewTicketResponse;
import com.rezofy.requests.Requests;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.activities.MyTripsActivity;
import com.rezofy.views.activities.TripDetailsActivity;
import com.rezofy.views.fragments.MyTripsFragment;

import java.util.List;

/**
 * Created by Linchpin66 on 26-01-2016.
 */
public class MyTripsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, NetworkTask.Result {
    private static final int ID_VIEW_TRIP =  1;
    private Context ctx;
    //TripList tripRecordsList;
    List<MyTrip> tripList;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private MyTripsActivity myTripsActivity;
    private boolean isPrimaryApp;
    private MyTripsFragment myTripsFragment;

    public MyTripsAdapter(Context ctx, List<MyTrip> tripList, RecyclerView mRecyclerView) {
        setConstructorData(ctx, tripList, mRecyclerView);
        myTripsActivity = (MyTripsActivity) ctx;
    }

    public MyTripsAdapter(MyTripsFragment myTripsFragment, Context ctx, List<MyTrip> tripList, RecyclerView mRecyclerView) {
        setConstructorData(ctx, tripList, mRecyclerView);
        this.myTripsFragment = myTripsFragment;
    }

    private void setConstructorData(Context ctx, List<MyTrip> tripList, RecyclerView mRecyclerView) {
        this.ctx = ctx;
        if (ctx.getString(R.string.is_primary_app).equals(ctx.getString(R.string.true_string)))
            isPrimaryApp = true;
        else
            isPrimaryApp = false;
        this.tripList = tripList;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //Log.d("Trip","total itemcount is "+totalItemCount+"::::"+lastVisibleItem);
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return tripList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.trip_item, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.progress_load_more, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.llRoot.setTag(position);
            myViewHolder.llRoot.setOnClickListener(this);
            myViewHolder.tv_stations.setText(tripList.get(position).getOrigin() + " - " + tripList.get(position).getDestination());
            myViewHolder.tv_date.setText(tripList.get(position).getStartDate());
            if (tripList.get(position).getStatus().equalsIgnoreCase("Canceled")) {
                myViewHolder.tv_tktstatus.setBackgroundResource(R.drawable.circular_button);
            } else {
                myViewHolder.tv_tktstatus.setBackgroundColor(ctx.getResources().getColor(R.color.white));
            }

            myViewHolder.tv_tktstatus.setText(tripList.get(position).getStatus());
           /* if (tripList.get(position).getCancellationStatus() == null)
                myViewHolder.tv_refundStatus.setVisibility(View.GONE);
            else
                myViewHolder.tv_refundStatus.setText(tripList.get(position).getCancellationStatus());*/
            myViewHolder.tvBookingRefNo.setText(tripList.get(position).getBookingRefNo());

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


    }


    @Override
    public int getItemCount() {
        if ( tripList == null || tripList.isEmpty()) {
            /*if (isPrimaryApp)
                myTripsActivity.getMyTripRV().setVisibility(View.GONE);
            else
                myTripsFragment.getMyTripRV().setVisibility(View.GONE);*/
            return 0;
        } else {
            /*if (isPrimaryApp)
                myTripsActivity.getMyTripRV().setVisibility(View.VISIBLE);
            else
                myTripsFragment.getMyTripRV().setVisibility(View.VISIBLE);*/
            return tripList.size();
        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public void onClick(View v) {
        if (!Utils.isNetworkAvailable(ctx)) {
            Utils.showAlertDialog(ctx, ctx.getString(R.string.app_name), ctx.getString(R.string.network_error), ctx.getString(R.string.ok), null, null, null);

        } else {
            int position = (int) v.getTag();
            String url;
            NetworkTask networkTask = new NetworkTask(ctx, ID_VIEW_TRIP);
            networkTask.setDialogMessage(ctx.getString(R.string.please_wait));
            networkTask.exposePostExecute(this);
            url = UIUtils.getBaseUrl(ctx) + WebServiceConstants.urlViewTrip;
            String paramsArray[] = new String[]{url, Requests.viewTripRequest(tripList.get(position).getBookingRefNo())};
            networkTask.execute(paramsArray);
        }
    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {
        if (object == null || object.equals("")) {
            Utils.showAlertDialog(ctx, ctx.getString(R.string.app_name), ctx.getString(R.string.network_error), ctx.getString(R.string.ok), null, null, null);

        } else {
            Intent intent = new Intent(ctx, TripDetailsActivity.class);
            intent.putExtra("view_ticket_response", new Gson().fromJson(object, ViewTicketResponse.class));
            if (isPrimaryApp)
                myTripsActivity.startActivityForResult(intent, Utils.TRIP_ACTION_CODE);
            else
                myTripsFragment.startActivityForResult(intent, Utils.TRIP_ACTION_CODE);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_stations, tv_date, tv_tktstatus, tv_refundStatus, tvBookingRefNo, tvPnr1, tvAdd, tvPnr2;
        private LinearLayout llRoot;

        public MyViewHolder(View itemView) {
            super(itemView);
            llRoot = (LinearLayout) itemView.findViewById(R.id.root);
            tv_stations = (TextView) itemView.findViewById(R.id.tv_stations);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_tktstatus = (TextView) itemView.findViewById(R.id.tv_tktstatus);
            tv_refundStatus = (TextView) itemView.findViewById(R.id.tv_refundStatus);
            tv_refundStatus.setTextColor(UIUtils.getThemeColor(ctx));
            tvBookingRefNo = (TextView) itemView.findViewById(R.id.booking_ref_no);
            tvPnr1 = (TextView) itemView.findViewById(R.id.pnr_1);
            tvPnr2 = (TextView) itemView.findViewById(R.id.pnr_2);
            tvAdd = (TextView) itemView.findViewById(R.id.add);
        }


    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }


}
