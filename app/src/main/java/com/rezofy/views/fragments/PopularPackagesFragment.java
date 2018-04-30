package com.rezofy.views.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.adapters.PopularPackagesAdapter;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.interfaces.OnLoadListener;
import com.rezofy.models.response_models.PackagesCountry;
import com.rezofy.models.response_models.PackagesCountryList;
import com.rezofy.models.response_models.PopularPackages;
import com.rezofy.models.response_models.TourInformation;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.activities.BaseHomeActivity;
import com.rezofy.views.activities.HomeActivity;
import com.rezofy.views.activities.TripBoxHomeActivity;

import org.json.JSONObject;

import java.util.ArrayList;


public class PopularPackagesFragment extends PopularPackagesBaseFragment implements View.OnClickListener, NetworkTask.Result {

    private final int POPULAR_PACKAGES = 1;
    private final int COUNTRY_LIST = 2;
    private Context context;
    private PopularPackagesAdapter popularPackagesAdapter;
    int pageNo = 1;
    private PopularPackages popularPackages;
    private boolean loadMoreData = true;

    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fView = super.onCreateView(inflater, container, savedInstanceState);
        initViews();

        return fView;
    }

    private void initViews() {
        context = getContext();
        getTripRecord(pageNo);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.left_icon:
                ((BaseHomeActivity)getActivity()).openDrawer();

                break;
            case R.id.right_icon:
                CountryPackagesFragment countryPackagesFragment = new CountryPackagesFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean(Utils.TAG_WISH_LIST, true);
                if(Utils.CURRENT_HOME_ACTIVITY.equals(Utils.TAG_HOME_ACTIVITY)) {
                ((HomeActivity) context).changeFragment(countryPackagesFragment, bundle, Utils.TAG_COUNTRY_PACKAGES_FRAGMENT);
                } else {
                    ((TripBoxHomeActivity) context).changeFragment(countryPackagesFragment, bundle, Utils.TAG_COUNTRY_PACKAGES_FRAGMENT);
                }
                break;

        }

    }

    private void getTripRecord(int pageNo) {
        if (!Utils.isNetworkAvailable(getContext())) {
            Utils.showAlertDialog(getContext(), getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else {
            String url;
            NetworkTask networkTask = new NetworkTask(getContext(), POPULAR_PACKAGES);
            networkTask.setDialogMessage(getString(R.string.please_wait));
            networkTask.exposePostExecute(this);
            String paramsArray[] = null;
            url = UIUtils.getBaseUrl2(getContext()) + WebServiceConstants.urlPopularPackages + pageNo;
            paramsArray = new String[]{url, null};
            networkTask.execute(paramsArray);
        }

    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {
        Log.d("Trip", "json of triplist is " + object + "::::" + pageNo);
        if (object == null || object.equals("")) {
            Utils.showAlertDialog(getActivity(), getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else if (id == POPULAR_PACKAGES) {
            try {
                if (pageNo == 1) {

                    if (object != null && id == POPULAR_PACKAGES) {
                    Log.d("Trip", "json of triplist is " + object);
                    if (object.contains(getString(R.string.no_trips)))
                        popularPackages = null;
                    else {
                        Gson gson = new Gson();
                        popularPackages = gson.fromJson(object, PopularPackages.class);
                        Log.d("Trip", "size of list is " + popularPackages.getTourinfo().size());
                    }
                    setAdapter(popularPackages);

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
                        PopularPackages temppopularPackages = gson.fromJson(object, PopularPackages.class);
                        ArrayList<TourInformation> lst = temppopularPackages.getTourinfo();
                        popularPackages.getTourinfo().addAll(lst);

                        popularPackagesAdapter.notifyDataSetChanged();

                    }
                    popularPackagesAdapter.setLoaded();
                }

            } catch (Exception e) {
                Log.d("Trip", "error in resultFromNetwork " + e);
            }
        } else if (id == COUNTRY_LIST) {
            if (object.contains(getString(R.string.countries))) {
                Gson gson = new Gson();
                Utils.packagesCountryList = gson.fromJson(object, PackagesCountryList.class);
                ArrayList<PackagesCountry> packagesCountryArrayList = Utils.packagesCountryList.getCountries();
                ArrayList<String> countryNameList = new ArrayList<String>();
                for (int i = 0; i < packagesCountryArrayList.size(); i++) {
                    countryNameList.add(packagesCountryArrayList.get(i).getName());
                }
                Utils.setPackageCountryList(countryNameList);

            }
        }


    }

    private void setAdapter(PopularPackages popularPackages) {
        popularPackagesAdapter = new PopularPackagesAdapter(getContext(), popularPackages, recyclerView);
        recyclerView.setAdapter(popularPackagesAdapter);
        setLoadMoreListener();

        getCountryList();
    }

    private void setLoadMoreListener() {
        try {
            popularPackagesAdapter.setOnLoadMoreListener(new OnLoadListener() {
                @Override
                public void onLoadMore() {
                    Log.d("Trip", "Load More");
                    popularPackages.getTourinfo().add(null);
                    popularPackagesAdapter.notifyItemInserted(popularPackages.getTourinfo().size() - 1);

                    //Load more data for reyclerview
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Trip", "Load More 2");

                            //Remove loading item
                            popularPackages.getTourinfo().remove(popularPackages.getTourinfo().size() - 1);
                            popularPackagesAdapter.notifyItemRemoved(popularPackages.getTourinfo().size());
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

    private void getCountryList() {
        if (!Utils.isNetworkAvailable(getContext())) {
            Utils.showAlertDialog(getContext(), getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else {
            String url;
            NetworkTask networkTask = new NetworkTask(getContext(), COUNTRY_LIST);
            networkTask.setDialogMessage(getString(R.string.please_wait));
            networkTask.exposePostExecute(this);
            String paramsArray[] = null;
            url = UIUtils.getBaseUrl2(getContext()) + WebServiceConstants.urlallcountry;
            paramsArray = new String[]{url, null};
            networkTask.execute(paramsArray);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
