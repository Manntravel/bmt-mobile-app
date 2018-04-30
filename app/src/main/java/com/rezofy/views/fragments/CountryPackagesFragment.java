package com.rezofy.views.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.adapters.CountryPackageAdapter;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.models.response_models.CountryTours;
import com.rezofy.models.response_models.SelectedCountryTours;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.activities.HomeActivity;
import com.rezofy.views.activities.TripBoxHomeActivity;

import java.util.ArrayList;


public class CountryPackagesFragment extends PopularPackagesBaseFragment implements View.OnClickListener, NetworkTask.Result {

    private final int COUNTRY_PACKAGES = 1;
    private Context context;
    private CountryPackageAdapter countryPackageAdapter;
    private SelectedCountryTours selectedCountryTours;
    private String countryName;
    private Bundle bundle;

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

        iTVMenu.setText(getString(R.string.icon_text_k));
        bundle = getArguments();

    }

    @Override
    public void onStart() {
        super.onStart();
        if(bundle.getBoolean(Utils.TAG_WISH_LIST)){
            tvTitle.setText(Utils.TAG_WISHLIST);
            iTVFav.setVisibility(View.GONE);
            tvTitle.setAllCaps(true);
            SelectedCountryTours selectedCountryTours = getSelectedCountryToursFromDB();
            setAdapter(selectedCountryTours, true);
            if(selectedCountryTours.getCountryToursArrayList().size() < 1) {
                tvNoFav.setVisibility(View.VISIBLE);
                tvNoFav.setText(tvNoFav.getText().toString().concat(new String(Character.toChars(0x1F603))));
            }

        } else {
            getSelectedCountryToursFromDB();
            countryName = bundle.getString(Utils.TAG_COUNTRY_NAME);
            getTripRecord(countryName);
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.left_icon:
                getActivity().onBackPressed();
                break;
            case R.id.right_icon:
                if(!bundle.getBoolean(Utils.TAG_WISH_LIST)) {
                    CountryPackagesFragment countryPackagesFragment = new CountryPackagesFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Utils.TAG_WISH_LIST, true);
                    if(Utils.CURRENT_HOME_ACTIVITY.equals(Utils.TAG_HOME_ACTIVITY)) {
                        ((HomeActivity) context).changeFragment(countryPackagesFragment, bundle, Utils.TAG_COUNTRY_PACKAGES_FRAGMENT);
                    } else {
                        ((TripBoxHomeActivity) context).changeFragment(countryPackagesFragment, bundle, Utils.TAG_COUNTRY_PACKAGES_FRAGMENT);
                    }
                }

                break;

        }

    }

    private void getTripRecord(String countryName) {
        if (!Utils.isNetworkAvailable(getContext())) {
            Utils.showAlertDialog(getContext(), getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else {
            String url;
            NetworkTask networkTask = new NetworkTask(getContext(), COUNTRY_PACKAGES);
            networkTask.setDialogMessage(getString(R.string.please_wait));
            networkTask.exposePostExecute(this);
            String paramsArray[] = null;
            if(countryName != null)
                countryName = countryName.replace(" ", "%20");
            url = UIUtils.getBaseUrl2(getContext()) + WebServiceConstants.urlSelectedCountryTours + countryName;
            paramsArray = new String[]{url, null};
            networkTask.execute(paramsArray);
        }

    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {
        Log.d("Trip", "json of triplist is " + object);
        if (object == null || object.equals("")) {
            Utils.showAlertDialog(getActivity(), getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else if (id == COUNTRY_PACKAGES) {
            try {

                    Log.d("Trip", "json of triplist is " + object);
                    if (object.contains(getString(R.string.no_trips)))
                        selectedCountryTours = null;
                    else {
                        Gson gson = new Gson();
                        selectedCountryTours = gson.fromJson(object, SelectedCountryTours.class);
                        Log.d("Trip", "size of list is " + selectedCountryTours.getCountryToursArrayList().size());
                    }
                    setAdapter(selectedCountryTours, false);

            } catch (Exception e) {
                Log.d("Trip", "error in resultFromNetwork " + e);
            }
        }

    }

    private void setAdapter(SelectedCountryTours selectedCountryTours, boolean isWishList) {
        setFav(selectedCountryTours);
        countryPackageAdapter = new CountryPackageAdapter(getContext(), selectedCountryTours, isWishList);
        recyclerView.setAdapter(countryPackageAdapter);
    }

    private void setFav(SelectedCountryTours selectedCountryTours) {
        ArrayList<CountryTours> countryTourseslist = selectedCountryTours.getCountryToursArrayList();
        for (int i = 0; i < countryTourseslist.size(); i++) {
                if(tourIds.contains(countryTourseslist.get(i).getID())) {
                countryTourseslist.get(i).setIsFav(true);
            }
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
