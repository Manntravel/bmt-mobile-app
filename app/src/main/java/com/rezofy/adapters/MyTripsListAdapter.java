package com.rezofy.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.rezofy.BR;
import com.rezofy.R;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.databinding.MyTripItemTransferBinding;
import com.rezofy.databinding.MytripItemBusBinding;
import com.rezofy.databinding.MytripItemFlightBinding;
import com.rezofy.databinding.MytripItemHotelBinding;
import com.rezofy.databinding.MytripItemPackageBinding;
import com.rezofy.databinding.MytripItemTrainBinding;
import com.rezofy.interfaces.OnLoadListener;
import com.rezofy.models.response_models.BusTicketResponse;
import com.rezofy.models.response_models.HotelTicketResponse;
import com.rezofy.models.response_models.MyTrip;
import com.rezofy.models.response_models.TrainTicketResponse;
import com.rezofy.models.response_models.TransferTicketResponse;
import com.rezofy.models.response_models.ViewTicketResponse;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.requests.Requests;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.activities.BusVoucherActivity;
import com.rezofy.views.activities.MyTripHotelDetailActivity;
import com.rezofy.views.activities.MyTripsActivity;
import com.rezofy.views.activities.TrainVoucherActivity;
import com.rezofy.views.activities.TransferVoucherActivity;
import com.rezofy.views.activities.TripDetailsActivity;
import com.rezofy.views.fragments.MyTripsFragment;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by linchpin on 3/2/17.
 */

public class MyTripsListAdapter   extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, NetworkTask.Result {

    // ID Used for indentify the result of item type clicked
    private static final int ID_VIEW_TRIP_FLIGHT =  1;
    private static final int ID_VIEW_TRIP_HOTEL =  2;
    private static final int ID_VIEW_TRIP_BUS =  3;
    private static final int ID_VIEW_TRIP_TRANSFER =  4;
    private static final int ID_VIEW_TRIP_TRAIN =  5;
    private static final int ID_VIEW_TRIP_PACKAGE =  6;

    // Types used for identfy view create
    private static final int VIEW_TYPE_LOADING = 1;
    private static final int VIEW_TYPE_FLIGHT = 2;
    private static final int VIEW_TYPE_BUS = 3;
    private static final int VIEW_TYPE_HOTEL = 4;
    private static final int VIEW_TYPE_TRANSFER = 5;
    private static final int VIEW_TYPE_TRAIN = 6;
    private static final int VIEW_TYPE_PACKAGE = 7;


    private Context ctx;

    List<MyTrip> tripList;
    private OnLoadListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private MyTripsActivity myTripsActivity;
    private boolean isPrimaryApp;
    private MyTripsFragment myTripsFragment;
    private RecyclerView recyclerView;

    public MyTripsListAdapter(Context ctx, List<MyTrip> tripList, RecyclerView mRecyclerView) {
        setConstructorData(ctx, tripList, mRecyclerView);
        myTripsActivity = (MyTripsActivity) ctx;
    }

    public MyTripsListAdapter(MyTripsFragment myTripsFragment, Context ctx, List<MyTrip> tripList, RecyclerView mRecyclerView) {
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
        recyclerView = mRecyclerView;
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

        if(tripList.get(position) == null)
            return VIEW_TYPE_LOADING;
        else if(tripList.get(position).getType().equalsIgnoreCase("FLIGHT"))
            return VIEW_TYPE_FLIGHT;
        else if(tripList.get(position).getType().equalsIgnoreCase("BUS"))
            return VIEW_TYPE_BUS;
        else if(tripList.get(position).getType().equalsIgnoreCase("HOTEL"))
            return VIEW_TYPE_HOTEL;
        else if(tripList.get(position).getType().equalsIgnoreCase("TRANSFER"))
            return VIEW_TYPE_TRANSFER;
        else if(tripList.get(position).getType().equalsIgnoreCase("TRAIN"))
            return VIEW_TYPE_TRAIN;
        else if(tripList.get(position).getType().equalsIgnoreCase("PACKAGE"))
            return VIEW_TYPE_PACKAGE;
        return tripList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_FLIGHT;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            switch (viewType) {

                case VIEW_TYPE_FLIGHT:
                    return new MyTripsListAdapter.MyViewFlightHolder(MytripItemFlightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
                case VIEW_TYPE_BUS:
                    return new MyTripsListAdapter.MyViewBusHolder(MytripItemBusBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
                case VIEW_TYPE_HOTEL:
                    return new MyTripsListAdapter.MyViewHotelHolder(MytripItemHotelBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
                case VIEW_TYPE_TRANSFER:
                    return new MyTripsListAdapter.MyViewTransferHolder(MyTripItemTransferBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
                case VIEW_TYPE_TRAIN:
                    return new MyTripsListAdapter.MyViewTrainHolder(MytripItemTrainBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
                case VIEW_TYPE_PACKAGE:
                    return new MyTripsListAdapter.MyViewPackageHolder(MytripItemPackageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
                case VIEW_TYPE_LOADING:
                    return new MyTripsListAdapter.LoadingViewHolder(LayoutInflater.from(ctx).inflate(R.layout.progress_load_more, parent, false));
                default:
                    return null;


            }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        //set Flight holder items
        if (holder instanceof MyTripsListAdapter.MyViewFlightHolder) {
            MyTripsListAdapter.MyViewFlightHolder myViewHolder = (MyTripsListAdapter.MyViewFlightHolder) holder;
            myViewHolder.binding.root.setTag(position);
            myViewHolder.binding.root.setOnClickListener(this);

            //show random images

            myViewHolder.binding.ivBg.setImageResource(ItemBgImage.getInstance(ctx).getBgResourceId(VIEW_TYPE_FLIGHT));


            myViewHolder.binding.tvStations.setText(tripList.get(position).getOrigin() + " - " + tripList.get(position).getDestination());
            myViewHolder.binding.tvDate.setText(tripList.get(position).getStartDate());
            if (tripList.get(position).getStatus().equalsIgnoreCase("Canceled")) {
                myViewHolder.binding.tvTktstatus.setBackgroundResource(R.drawable.circular_button);
            } else {
                myViewHolder.binding.tvTktstatus.setBackgroundColor(ctx.getResources().getColor(R.color.white));
            }

            myViewHolder.binding.tvTktstatus.setText(tripList.get(position).getStatus());
            if(tripList.get(position).getStatus().equalsIgnoreCase("UNPAID")) {
                myViewHolder.binding.tvpayBt.setText("PAY NOW");
                myViewHolder.binding.tvpayBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ((MyTripsActivity)ctx).showPaymentMethods(tripList.get(position).getBookingRefNo());

                    }
                });
                myViewHolder.binding.tvpayBt.setBackgroundResource(R.drawable.bg_mytrip_bt_pay);
            }
            else {
                myViewHolder.binding.tvpayBt.setText("PAID");
                myViewHolder.binding.tvpayBt.setBackgroundResource(R.drawable.bg_mytrip_bt_paid);
            }


            myViewHolder.binding.bookingRefNo.setText(tripList.get(position).getBookingRefNo());
            myViewHolder.binding.tvStatusWithDate.setVisibility(View.GONE);
            myViewHolder.binding.totalPriceTV.setText(tripList.get(position).getTotalPrice()+"/-");

        }
        else if (holder instanceof MyTripsListAdapter.MyViewTransferHolder) {
            MyTripsListAdapter.MyViewTransferHolder myViewHolder = (MyTripsListAdapter.MyViewTransferHolder) holder;
            myViewHolder.binding.root.setTag(position);
            myViewHolder.binding.root.setOnClickListener(this);

            //show random images

            myViewHolder.binding.ivBg.setImageResource(ItemBgImage.getInstance(ctx).getBgResourceId(VIEW_TYPE_TRANSFER));


            myViewHolder.binding.tvStations.setText(tripList.get(position).getOrigin() + " - " + tripList.get(position).getDestination());
            myViewHolder.binding.tvDate.setText(tripList.get(position).getStartDate());
            if (tripList.get(position).getStatus().equalsIgnoreCase("Canceled")) {
                myViewHolder.binding.tvTktstatus.setBackgroundResource(R.drawable.circular_button);
            } else {
                myViewHolder.binding.tvTktstatus.setBackgroundColor(ctx.getResources().getColor(R.color.white));
            }

            myViewHolder.binding.tvTktstatus.setText(tripList.get(position).getStatus());
            if(tripList.get(position).getStatus().equalsIgnoreCase("UNPAID")) {
                myViewHolder.binding.tvpayBt.setText("PAY NOW");
                myViewHolder.binding.tvpayBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ((MyTripsActivity)ctx).showPaymentMethods(tripList.get(position).getBookingRefNo());

                    }
                });
                myViewHolder.binding.tvpayBt.setBackgroundResource(R.drawable.bg_mytrip_bt_pay);
            }
            else {
                myViewHolder.binding.tvpayBt.setText("PAID");
                myViewHolder.binding.tvpayBt.setBackgroundResource(R.drawable.bg_mytrip_bt_paid);
            }


            myViewHolder.binding.bookingRefNo.setText(tripList.get(position).getBookingRefNo());
            myViewHolder.binding.tvStatusWithDate.setVisibility(View.GONE);
            myViewHolder.binding.totalPriceTV.setText(tripList.get(position).getTotalPrice()+"/-");

        }
         //set Hotel holder items
        else if (holder instanceof MyTripsListAdapter.MyViewHotelHolder) {
            MyTripsListAdapter.MyViewHotelHolder myViewHolder = ( MyTripsListAdapter.MyViewHotelHolder) holder;
            myViewHolder.binding.root.setTag(position);
            myViewHolder.binding.root.setOnClickListener(this);


            myViewHolder.binding.ivBg.setImageResource(ItemBgImage.getInstance(ctx).getBgResourceId(VIEW_TYPE_HOTEL));
            myViewHolder.binding.tvStations.setText(tripList.get(position).getServiceProvider());
            myViewHolder.binding.tvDate.setText(tripList.get(position).getDestination());
            if (tripList.get(position).getStatus().equalsIgnoreCase("Canceled")) {
                myViewHolder.binding.tvTktstatus.setBackgroundResource(R.drawable.circular_button);
            } else {
                myViewHolder.binding.tvTktstatus.setBackgroundColor(ctx.getResources().getColor(R.color.white));
            }

            myViewHolder.binding.tvTktstatus.setText(tripList.get(position).getStatus()+" ON ");

            if(tripList.get(position).getStatus().equalsIgnoreCase("UNPAID")) {
                myViewHolder.binding.tvpayBt.setText("PAY NOW");
                myViewHolder.binding.tvpayBt.setBackgroundResource(R.drawable.bg_mytrip_bt_pay);
                myViewHolder.binding.tvpayBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ((MyTripsActivity)ctx).showPaymentMethods(tripList.get(position).getBookingRefNo());

                    }
                });
            }
            else {
                myViewHolder.binding.tvpayBt.setText("PAID");
                myViewHolder.binding.tvpayBt.setBackgroundResource(R.drawable.bg_mytrip_bt_paid);
            }

            myViewHolder.binding.bookingRefNo.setText(tripList.get(position).getBookingRefNo());
            myViewHolder.binding.tvStatusWithDate.setVisibility(View.VISIBLE);
            myViewHolder.binding.tvStatusWithDate.setText(tripList.get(position).getStartDate());
            myViewHolder.binding.totalPriceTV.setText(tripList.get(position).getTotalPrice()+"/-");


        }
        //set Bus holder items
        else if (holder instanceof MyTripsListAdapter.MyViewBusHolder) {
            MyTripsListAdapter.MyViewBusHolder myViewHolder = (MyTripsListAdapter.MyViewBusHolder) holder;
            myViewHolder.binding.root.setTag(position);
            myViewHolder.binding.root.setOnClickListener(this);


            myViewHolder.binding.ivBg.setImageResource(ItemBgImage.getInstance(ctx).getBgResourceId(VIEW_TYPE_BUS));

            myViewHolder.binding.tvStations.setText(tripList.get(position).getOrigin() + " - " + tripList.get(position).getDestination());
            myViewHolder.binding.tvDate.setText(tripList.get(position).getServiceProvider());
            if (tripList.get(position).getStatus().equalsIgnoreCase("Canceled")) {
                myViewHolder.binding.tvTktstatus.setBackgroundResource(R.drawable.circular_button);
            } else {
                myViewHolder.binding.tvTktstatus.setBackgroundColor(ctx.getResources().getColor(R.color.white));
            }

            myViewHolder.binding.tvTktstatus.setText(tripList.get(position).getStatus()+" ON ");

            if(tripList.get(position).getStatus().equalsIgnoreCase("UNPAID")) {
                myViewHolder.binding.tvpayBt.setText("PAY NOW");
                myViewHolder.binding.tvpayBt.setBackgroundResource(R.drawable.bg_mytrip_bt_pay);
                myViewHolder.binding.tvpayBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ((MyTripsActivity)ctx).showPaymentMethods(tripList.get(position).getBookingRefNo());

                    }
                });
            }
            else {
                myViewHolder.binding.tvpayBt.setText("PAID");
                myViewHolder.binding.tvpayBt.setBackgroundResource(R.drawable.bg_mytrip_bt_paid);
            }


            myViewHolder.binding.bookingRefNo.setText(tripList.get(position).getBookingRefNo());
            myViewHolder.binding.tvStatusWithDate.setVisibility(View.VISIBLE);
            myViewHolder.binding.tvStatusWithDate.setText(tripList.get(position).getStartDate());
            myViewHolder.binding.totalPriceTV.setText(tripList.get(position).getTotalPrice()+"/-");
        }
        else if (holder instanceof MyTripsListAdapter.MyViewTrainHolder) {
            MyTripsListAdapter.MyViewTrainHolder myViewHolder = (MyTripsListAdapter.MyViewTrainHolder) holder;
            myViewHolder.binding.root.setOnClickListener(this);

            if (tripList.get(position).getStatus().equalsIgnoreCase("Canceled")) {
                myViewHolder.binding.tvTktstatus.setBackgroundResource(R.drawable.circular_button);
            } else {
                myViewHolder.binding.tvTktstatus.setBackgroundColor(ctx.getResources().getColor(R.color.white));
            }

            if(tripList.get(position).getStatus().equalsIgnoreCase("UNPAID")) {
                myViewHolder.binding.tvpayBt.setText("PAY NOW");
                myViewHolder.binding.tvpayBt.setBackgroundResource(R.drawable.bg_mytrip_bt_pay);
                myViewHolder.binding.tvpayBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ((MyTripsActivity)ctx).showPaymentMethods(tripList.get(position).getBookingRefNo());

                    }
                });
            }
            else {
                myViewHolder.binding.tvpayBt.setText("PAID");
                myViewHolder.binding.tvpayBt.setBackgroundResource(R.drawable.bg_mytrip_bt_paid);
            }

            myViewHolder.binding.tvStatusWithDate.setVisibility(View.VISIBLE);

            myViewHolder.binding.setVariable(BR.trip,tripList.get(position));
            myViewHolder.binding.setVariable(BR.tag,position);
            myViewHolder.binding.setVariable(BR.image,ItemBgImage.getInstance(ctx).getBgResourceId(VIEW_TYPE_TRAIN));
            myViewHolder.binding.executePendingBindings();

        }
        else if (holder instanceof MyTripsListAdapter.MyViewPackageHolder) {
            MyTripsListAdapter.MyViewPackageHolder myViewHolder = (MyTripsListAdapter.MyViewPackageHolder) holder;
            myViewHolder.binding.root.setTag(position);
            myViewHolder.binding.root.setOnClickListener(this);


            myViewHolder.binding.ivBg.setImageResource(ItemBgImage.getInstance(ctx).getBgResourceId(VIEW_TYPE_PACKAGE));

            myViewHolder.binding.tvStations.setText(tripList.get(position).getOrigin() + " - " + tripList.get(position).getDestination());
            myViewHolder.binding.tvDate.setText(tripList.get(position).getStartDate());
            if (tripList.get(position).getStatus().equalsIgnoreCase("Canceled")) {
                myViewHolder.binding.tvTktstatus.setBackgroundResource(R.drawable.circular_button);
            } else {
                myViewHolder.binding.tvTktstatus.setBackgroundColor(ctx.getResources().getColor(R.color.white));
            }

            myViewHolder.binding.tvTktstatus.setText(tripList.get(position).getStatus()+" ON ");

            if(tripList.get(position).getStatus().equalsIgnoreCase("UNPAID")) {
                myViewHolder.binding.tvpayBt.setText("PAY NOW");
                myViewHolder.binding.tvpayBt.setBackgroundResource(R.drawable.bg_mytrip_bt_pay);
                myViewHolder.binding.tvpayBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ((MyTripsActivity)ctx).showPaymentMethods(tripList.get(position).getBookingRefNo());

                    }
                });
            }
            else {
                myViewHolder.binding.tvpayBt.setText("PAID");
                myViewHolder.binding.tvpayBt.setBackgroundResource(R.drawable.bg_mytrip_bt_paid);
            }


            myViewHolder.binding.bookingRefNo.setText(tripList.get(position).getBookingRefNo());
            myViewHolder.binding.tvStatusWithDate.setVisibility(View.VISIBLE);
            myViewHolder.binding.tvStatusWithDate.setText(tripList.get(position).getStartDate());
            myViewHolder.binding.totalPriceTV.setText(tripList.get(position).getTotalPrice()+"/-");
        }
        else if (holder instanceof MyTripsListAdapter.LoadingViewHolder) {
            MyTripsListAdapter.LoadingViewHolder loadingViewHolder = (MyTripsListAdapter.LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


    }


    @Override
    public int getItemCount() {
        if ( tripList == null || tripList.isEmpty()) {
            if (isPrimaryApp)
                recyclerView.setVisibility(View.GONE);
            return 0;
        } else {
            if (isPrimaryApp)
                recyclerView.setVisibility(View.VISIBLE);
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
            NetworkTask networkTask = null;
            if(tripList.get(position).getType().equalsIgnoreCase("FLIGHT"))
            {
                networkTask = new NetworkTask(ctx, ID_VIEW_TRIP_FLIGHT);
            }else if(tripList.get(position).getType().equalsIgnoreCase("HOTEL"))
            {
                networkTask = new NetworkTask(ctx, ID_VIEW_TRIP_HOTEL);
            }else if(tripList.get(position).getType().equalsIgnoreCase("BUS"))
            {
                networkTask = new NetworkTask(ctx, ID_VIEW_TRIP_BUS);
            }
            else if(tripList.get(position).getType().equalsIgnoreCase("TRANSFER"))
            {
                networkTask = new NetworkTask(ctx, ID_VIEW_TRIP_TRANSFER);
            }
            else if(tripList.get(position).getType().equalsIgnoreCase("TRAIN"))
            {
                networkTask = new NetworkTask(ctx, ID_VIEW_TRIP_TRAIN);

            }
            else if(tripList.get(position).getType().equalsIgnoreCase("PACKAGE"))
            {
                Toast.makeText(ctx,"Comming Soon...", Toast.LENGTH_SHORT).show();
            }
            if(networkTask!=null) {
                Log.d("token", AppPreferences.getInstance(ctx).getToken());
                networkTask.setDialogMessage(ctx.getString(R.string.please_wait));
                networkTask.exposePostExecute(this);
                url = UIUtils.getBaseUrl(ctx) + WebServiceConstants.urlViewTrip;
                String paramsArray[] = new String[]{url, Requests.viewTripRequest(tripList.get(position).getBookingRefNo())};
                networkTask.execute(paramsArray);
            }
        }
    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {
        if (object == null || object.equals("")) {
            Utils.showAlertDialog(ctx, ctx.getString(R.string.app_name), ctx.getString(R.string.network_error), ctx.getString(R.string.ok), null, null, null);

        } else {
            Intent intent = null;
            switch (id)
            {

                case ID_VIEW_TRIP_FLIGHT:
                    intent = new Intent(ctx, TripDetailsActivity.class);
                    intent.putExtra("view_ticket_response", new Gson().fromJson(object, ViewTicketResponse.class));
                    break;
                case ID_VIEW_TRIP_HOTEL:
                    intent = new Intent(ctx, MyTripHotelDetailActivity.class);
                    intent.putExtra("view_ticket_response", new Gson().fromJson(object, HotelTicketResponse.class));
                    break;
                case ID_VIEW_TRIP_BUS:
                    intent = new Intent(ctx, BusVoucherActivity.class);
                    intent.putExtra("view_ticket_response", new Gson().fromJson(object, BusTicketResponse.class));
                    break;
                case ID_VIEW_TRIP_TRAIN:
                    intent = new Intent(ctx, TrainVoucherActivity.class);
                    intent.putExtra("view_ticket_response", new Gson().fromJson(object, TrainTicketResponse.class));
                    break;
                case ID_VIEW_TRIP_TRANSFER:
                    intent = new Intent(ctx, TransferVoucherActivity.class);
                    intent.putExtra("view_ticket_response", new Gson().fromJson(object, TransferTicketResponse.class));
                    break;

            }
            if(intent!=null)
                myTripsActivity.startActivityForResult(intent, Utils.TRIP_ACTION_CODE);

        }
    }



    public class MyViewFlightHolder extends RecyclerView.ViewHolder {

        private MytripItemFlightBinding binding;


        public MyViewFlightHolder(MytripItemFlightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }

    public class MyViewTransferHolder extends RecyclerView.ViewHolder {


        private MyTripItemTransferBinding binding;


        public MyViewTransferHolder(MyTripItemTransferBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

    }

    public class MyViewBusHolder extends RecyclerView.ViewHolder {

        private MytripItemBusBinding binding;


        public MyViewBusHolder(MytripItemBusBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

    }


    public class MyViewHotelHolder extends RecyclerView.ViewHolder {

        private MytripItemHotelBinding binding;


        public MyViewHotelHolder(MytripItemHotelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }
    public class MyViewTrainHolder extends RecyclerView.ViewHolder {

        private MytripItemTrainBinding binding;


        public MyViewTrainHolder(MytripItemTrainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }

    public class MyViewPackageHolder extends RecyclerView.ViewHolder {

        private MytripItemPackageBinding binding;


        public MyViewPackageHolder(MytripItemPackageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }



    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }






    static class ItemBgImage
    {
        private static ItemBgImage obj;
        private TypedArray imgFlight;
        private TypedArray imgHotel;
        private TypedArray imgBus;
        private TypedArray imgTransfer;
        private TypedArray imgTrain;
        private TypedArray imgPackage;
        private Context context;
        public static ItemBgImage getInstance(Context context)
        {
            if(obj==null)
            {
                obj =  new ItemBgImage(context);
            }
            return obj;

        }



        private ItemBgImage(Context context)
        {
            this.context =context;
            imgFlight = context.getResources().obtainTypedArray(R.array.array_flight_image);
            imgHotel = context.getResources().obtainTypedArray(R.array.array_hotel_image);
            imgBus = context.getResources().obtainTypedArray(R.array.array_bus_image);
            imgTransfer = context.getResources().obtainTypedArray(R.array.array_transfer_image);
            imgTrain = context.getResources().obtainTypedArray(R.array.array_train_image);
            imgPackage = context.getResources().obtainTypedArray(R.array.array_package_image);
        }

        public int getBgResourceId(int type)
        {


            int max = 0;
            int index = 0;
            switch (type)
            {
                case VIEW_TYPE_FLIGHT:
                    max = imgFlight.length();
                    index = randInt(0, max-1);
                    return imgFlight.getResourceId(index, R.drawable.flight_one);
                case VIEW_TYPE_HOTEL:
                    max = imgHotel.length();
                    index = randInt(0, max-1);
                    return imgHotel.getResourceId(index, R.drawable.flight_one);
                case VIEW_TYPE_BUS:
                    max = imgBus.length();
                    index = randInt(0, max-1);
                    return imgBus.getResourceId(index, R.drawable.flight_one);
                case VIEW_TYPE_TRANSFER:
                    max = imgTransfer.length();
                    index = randInt(0, max-1);
                    return imgTransfer.getResourceId(index, R.drawable.flight_one);
                case VIEW_TYPE_TRAIN:
                    max = imgTrain.length();
                    index = randInt(0, max-1);
                    return imgTrain.getResourceId(index, R.drawable.flight_one);
                case VIEW_TYPE_PACKAGE:
                    max = imgPackage.length();
                    index = randInt(0, max-1);
                    return imgPackage.getResourceId(index, R.drawable.flight_one);
            }

            return 0;
        }



        public  int randInt(int min, int max) {

            // NOTE: This will (intentionally) not run as written so that folks
            // copy-pasting have to think about how to initialize their
            // Random instance.  Initialization of the Random instance is outside
            // the main scope of the question, but some decent options are to have
            // a field that is initialized once and then re-used as needed or to
            // use ThreadLocalRandom (if using at least Java 1.7).
            Random rand = new Random();

            // nextInt is normally exclusive of the top value,
            // so add 1 to make it inclusive
            int randomNum = rand.nextInt((max - min) + 1) + min;

            return randomNum;
        }


    }




}


