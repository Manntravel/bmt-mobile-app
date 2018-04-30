package com.rezofy.views.fragments;

import android.content.Intent;
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
import com.rezofy.adapters.MyTripsAdapter;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.interfaces.OnLoadListener;
import com.rezofy.models.response_models.TripList;
import com.rezofy.models.response_models.TripRecord;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.activities.HomeActivity;
import com.rezofy.views.custom_views.IconTextView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Linchpin66 on 26-01-2016.
 */
public class MyTripsFragment extends Fragment implements View.OnClickListener, NetworkTask.Result {

    private View fView;
    private IconTextView iTVMenu;
    private RecyclerView myTripRV;
    private int TRIP_RECORD = 1;
    private MyTripsAdapter adapter;
    TripList tripList;
    private int pageNo = 1;
    private boolean loadMoreData = true;
    private TextView tvTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (fView == null) {
            fView = inflater.inflate(R.layout.activity_trip, container, false);
           // init();
            //setProperties();
        }
        return fView;
    }

    /*private void setProperties() {
        fView.findViewById(R.id.header).setBackgroundColor(UIUtils.getThemeColor(getContext()));
        iTVMenu.setTextColor(UIUtils.getThemeContrastColor(getContext()));
        tvTitle.setTextColor(UIUtils.getThemeContrastColor(getContext()));
    }

    private void init() {
        iTVMenu = (IconTextView) fView.findViewById(R.id.header).findViewById(R.id.left_icon);
        iTVMenu.setText(getString(R.string.icon_text_h));
        iTVMenu.setTextSize(20);
        iTVMenu.setOnClickListener(this);
        tvTitle = (TextView) fView.findViewById(R.id.header).findViewById(R.id.title);
        tvTitle.setText(getString(R.string.my_trips));

        myTripRV = (RecyclerView) fView.findViewById(R.id.myTripRV);
        myTripRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        myTripRV.setHasFixedSize(true);

        myTripRV.setItemAnimator(new DefaultItemAnimator());
        getTripRecord(pageNo);

    }*/

    private void setLoadMoreListener() {
        try {
            adapter.setOnLoadMoreListener(new OnLoadListener() {
                @Override
                public void onLoadMore() {
                    Log.d("Trip", "Load More");
                    tripList.getTripList().add(null);
                    adapter.notifyItemInserted(tripList.getTripList().size() - 1);

                    //Load more data for reyclerview
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Trip", "Load More 2");

                            //Remove loading item
                            tripList.getTripList().remove(tripList.getTripList().size() - 1);
                            adapter.notifyItemRemoved(tripList.getTripList().size());
                            if (loadMoreData) {
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

    private void getTripRecord(int pageNo) {
        if (!Utils.isNetworkAvailable(getContext())) {
            Utils.showAlertDialog(getContext(), getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else {
            String url;
            NetworkTask networkTask = new NetworkTask(getContext(), TRIP_RECORD);
            networkTask.setDialogMessage(getString(R.string.please_wait));
            networkTask.exposePostExecute(this);
            String paramsArray[] = null;
            url = UIUtils.getBaseUrl(getContext()) + WebServiceConstants.urlTripRecord + pageNo;
            paramsArray = new String[]{url, null};
            networkTask.execute(paramsArray);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == requestCode) {
            pageNo = 1;
            getTripRecord(pageNo);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_icon:
                ((HomeActivity) getActivity()).openDrawer();
                break;
        }
    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {
        Log.d("Trip", "json of triplist is " + object + "::::" + pageNo);
        if (object == null || object.equals("")) {
            Utils.showAlertDialog(getActivity(), getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else if (id == TRIP_RECORD) {
            try {
                if (pageNo == 1) {

                    if (object != null && id == TRIP_RECORD) {
                        Log.d("Trip", "json of triplist is " + object);
                        if (object.contains(getString(R.string.no_trips)))
                            tripList = null;
                        else {
                            Gson gson = new Gson();
                            tripList = gson.fromJson(object, TripList.class);
                            Log.d("Trip", "size of list is " + tripList.getTripList().size());
                        }
                        setAdapter(tripList);

                    }

                } else {
                    //do code
                    JSONObject jsonOb = new JSONObject(object);
                    if (jsonOb.has("messages")) {
                        Log.d("Trip", "No more data");
                        loadMoreData = false;
                    } else {
                        loadMoreData = true;
                        Gson gson = new Gson();
                        TripList tempTripList = gson.fromJson(object, TripList.class);
                        ArrayList<TripRecord> lst = tempTripList.getTripList();
                        tripList.getTripList().addAll(lst);

                        adapter.notifyDataSetChanged();

                    }
                    adapter.setLoaded();
                }

            } catch (Exception e) {
                Log.d("Trip", "error in resultFromNetwork " + e);
            }
        }


    }

    private void setAdapter(TripList tripRecordsList) {
        //adapter = new MyTripsAdapter(this, getContext(), tripRecordsList, myTripRV);
        myTripRV.setAdapter(adapter);
        setLoadMoreListener();
    }

    public RecyclerView getMyTripRV() {
        return myTripRV;
    }

    public void setMyTripRV(RecyclerView myTripRV) {
        this.myTripRV = myTripRV;
    }
}
