package com.rezofy.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rezofy.R;

import com.rezofy.adapters.MyTripsListAdapter;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.interfaces.FragmentLifeCycle;
import com.rezofy.interfaces.OnLoadListener;
import com.rezofy.interfaces.SearchListener;
import com.rezofy.models.response_models.MyTrip;
import com.rezofy.models.response_models.MyTripResponse;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linchpin on 3/2/17.
 */

public class MyTripFragment  extends Fragment implements NetworkTask.Result, SearchListener, FragmentLifeCycle {

    private Activity activity;
    private int TRIP_RECORD = 1;

    private int pageNo = 1;

    private View view;
    private RecyclerView myTripRV;
    private MyTripsListAdapter adapter;
    private List<MyTrip> tripList;
    private List<MyTrip> tempTripList;
    private boolean loadMoreData = true;
    private boolean isAllDataLoaded = false;
    private TextView tvNoItem, tvNoListItem;

    private static final String TAG = MyTripFragment.class.getSimpleName();

    private String type;



    public MyTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
        Bundle bundle = getArguments();
        if(bundle!=null)
            if(bundle.containsKey("type"))
            {
                type = bundle.getString("type");
            }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mytrip, container, false);
        initView();
        return view;
    }

    private void initView() {
       /* loadMoreData = true;
        isAllDataLoaded = false;
        pageNo = 1;*/
        myTripRV = (RecyclerView) view.findViewById(R.id.myTripRV);
        tvNoItem = (TextView) view.findViewById(R.id.tvNoItem);
        tvNoListItem = (TextView)view.findViewById(R.id.tvNoListItem);
        myTripRV.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        myTripRV.setHasFixedSize(true);

        myTripRV.setItemAnimator(new DefaultItemAnimator());
        if (tripList == null) {
            myTripRV.setVisibility(View.GONE);
            tvNoListItem.setVisibility(View.VISIBLE);
            getTripRecord(pageNo);
        }
    }

    private void getTripRecord(int pageNov) {
        if (!Utils.isNetworkAvailable(activity)) {
            Utils.showAlertDialog(activity, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else {
            String url;
            Log.d("token", AppPreferences.getInstance(activity).getToken());
            NetworkTask networkTask = new NetworkTask(activity, TRIP_RECORD);
            networkTask.setDialogMessage(getString(R.string.please_wait));
            networkTask.exposePostExecute(this);
            String paramsArray[] = null;
            //url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlTripRecord + pageNo;

            String requestParamsData = "pageNo=" + pageNo + "&tripType="+type;
            url = UIUtils.getBaseUrl(activity) + WebServiceConstants.urlTripRecord;
            paramsArray = new String[]{url, requestParamsData};
            networkTask.execute(paramsArray);
        }

    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {


        try {
            if (object != null && id == TRIP_RECORD) {

                if (tripList == null) {


                    MyTripResponse response = new Gson().fromJson(object, MyTripResponse.class);
                    if (response.getMessages() != null) {

                        myTripRV.setVisibility(View.GONE);
                        tvNoListItem.setVisibility(View.VISIBLE);

                    } else if (response.getTripList() != null) {
                        myTripRV.setVisibility(View.VISIBLE);
                        tvNoListItem.setVisibility(View.GONE);
                        tripList = removeUnwantedType(response.getTripList());
                        setAdapter(tripList);
                    }



                } else {
                    tvNoItem.setVisibility(View.GONE);

                    MyTripResponse response = new Gson().fromJson(object, MyTripResponse.class);
                    if (response.getMessages() != null) {
                        Log.d("Trip", "No more data");
                        isAllDataLoaded = true;
                        loadMoreData = false;
                    } else if (response.getTripList() != null) {

                        loadMoreData = true;
                        tripList.addAll(removeUnwantedType(response.getTripList()));
                        adapter.notifyDataSetChanged();
                    }
                    adapter.setLoaded();

                }

            }

        } catch (Exception e) {
            Log.d("Trip", "error in resultFromNetwork " + e);
        }
    }

    public List<MyTrip> removeUnwantedType(List<MyTrip> tripList)
    {
        List<MyTrip> list = new ArrayList<>();
        for(MyTrip trip: tripList)
        {
            if(trip.getType().equals("VISA")||trip.getType().equals("INSURANCE"))
            {

            }
            else
            {
                list.add(trip);
            }
        }
        return list;
    }

    private void setAdapter(List<MyTrip> tripList) {
        adapter = new MyTripsListAdapter(activity, tripList, myTripRV);
        myTripRV.setAdapter(adapter);
        setLoadMoreListener();

    }

    private void setLoadMoreListener() {
        try {
            adapter.setOnLoadMoreListener(new OnLoadListener() {
                @Override
                public void onLoadMore() {
                    Log.d("Trip", "Load More");
                    tripList.add(null);
                    myTripRV.post(new Runnable() {
                        public void run() {
                            adapter.notifyItemInserted(tripList.size() - 1);
                        }
                    });
                    //adapter.notifyItemInserted(tripList.size() - 1);

                    //Load more data for reyclerview
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Trip", "Load More 2");

                            //Remove loading item
                            tripList.remove(tripList.size() - 1);
                            adapter.notifyItemRemoved(tripList.size());
                            if (loadMoreData && !isAllDataLoaded) {
                                pageNo++;
                                getTripRecord(pageNo);
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.d("Trip", "eror in setLoadMoreListener " + e);
        }
    }

    @Override
    public void searchFromList(String query) {

        Log.d("Query " + getClass().getName(), query);
        loadMoreData = false;

        tempTripList = new ArrayList<MyTrip>();

        for (MyTrip trip : tripList) {

            query = query.toUpperCase();
            if ((trip.getPnr() != null ? (trip.getPnr().toUpperCase().contains(query)) : false)
                    || (trip.getBookingRefNo() != null ? (trip.getBookingRefNo().toUpperCase().contains(query)) : false)
                    || (trip.getLastName() != null ? (trip.getLastName().toUpperCase().contains(query)) : false)) {
                tempTripList.add(trip);
            }
        }
        setAdapter(tempTripList);
        if (tempTripList.size() > 0) {
            myTripRV.setVisibility(View.VISIBLE);
            tvNoItem.setVisibility(View.GONE);
        } else {
            myTripRV.setVisibility(View.GONE);
            tvNoItem.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void searchOpened() {

    }

    @Override
    public void searchClosed() {
        loadMoreData = true;
        setAdapter(tripList);
        myTripRV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPauseFragment() {
        // Toast.makeText(getActivity(), "onPauseFragment():" + TAG, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment() {
        // Toast.makeText(getActivity(), "onResumeFragment():" + TAG, Toast.LENGTH_SHORT).show();
        loadMoreData = true;
        if (myTripRV != null)
            myTripRV.setVisibility(View.VISIBLE);
        if (tvNoItem != null) {
            tvNoItem.setVisibility(View.GONE);
        }
        if (tripList != null)
            setAdapter(tripList);



    }
}