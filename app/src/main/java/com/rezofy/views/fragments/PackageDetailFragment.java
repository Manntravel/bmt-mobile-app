package com.rezofy.views.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.database.DbHelper;
import com.rezofy.models.response_models.TourDetail;
import com.rezofy.models.response_models.TourDetailList;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.activities.HomeActivity;
import com.rezofy.views.activities.TripBoxHomeActivity;
import com.rezofy.views.custom_views.IconTextView;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class PackageDetailFragment extends Fragment implements View.OnClickListener, NetworkTask.Result {

    private final int PACKAGES_DETAIL = 1;
    private OnFragmentInteractionListener mListener;
    private Context context;
    private View fView, header;
    private LinearLayout llCall, llEnquiry;
    private IconTextView iTVMenu, iTVFav, iTVSearch, ivPackFav;
    private TextView tvTitle, tvTourDetail, tvTourItinerary, tvOverview, tvBestPlace, tvPrice, tvPlaceNameBg;
    private String packageId, tourDetail, tourItinerary, packageName, cityName, phoneNo, startingPrice, thumbnail, duration;
    private TourDetailList tourDetailList;
    private ImageView ivHeader;
    private Bundle bundle;
    private boolean isFav;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (fView == null) {
            fView = inflater.inflate(R.layout.fragment_package_detail, container, false);
            initViews();
            setProperties();
            setListener();
        }
        return fView;
    }

    private void initViews() {
        Nammu.init(getActivity());
        context = getContext();
        header = fView.findViewById(R.id.header);
        if (header != null) {
            iTVMenu = (IconTextView) header.findViewById(R.id.left_icon);
            iTVMenu.setText(getString(R.string.icon_text_k));
            iTVMenu.setOnClickListener(this);
            iTVMenu.setTextSize(20);

            tvTitle = (TextView) header.findViewById(R.id.title);
            tvTitle.setText(getString(R.string.packages_text));

            iTVSearch = (IconTextView) header.findViewById(R.id.right_icon3);
            iTVSearch.setVisibility(View.VISIBLE);
            iTVSearch.setOnClickListener(this);
            iTVSearch.setText(getString(R.string.icon_text_search));
            iTVSearch.setTextSize(20);

            iTVFav = (IconTextView) header.findViewById(R.id.right_icon);
            iTVFav.setVisibility(View.VISIBLE);
            iTVFav.setOnClickListener(this);
            iTVFav.setText(getString(R.string.icon_text_fav));
            iTVFav.setTextSize(20);

        }

        bundle = getArguments();
        tvTourDetail = (TextView) fView.findViewById(R.id.tv_tour_detail);
        tvTourItinerary = (TextView) fView.findViewById(R.id.tv_tour_itinerary);
        tvOverview = (TextView) fView.findViewById(R.id.tv_overview);

        packageName = bundle.getString(Utils.TAG_PACKAGE_NAME);
        cityName = bundle.getString(Utils.TAG_PACKAGE_NAME);
        isFav = bundle.getBoolean(Utils.TAG_FAVORITE);

        tvPlaceNameBg = (TextView) fView.findViewById(R.id.tv_place_name_bg);
        tvPlaceNameBg.setText(cityName);

        tvBestPlace = (TextView) fView.findViewById(R.id.tv_best_place);
        tvBestPlace.setText(packageName);

        tvPrice = (TextView) fView.findViewById(R.id.tv_price);
        startingPrice = bundle.getString(Utils.TAG_STARTING_PRICE);
        tvPrice.setText(UIUtils.setAmountOnly(getContext(), UIUtils.getFareToDisplay(getContext(), startingPrice)));

        thumbnail = bundle.getString(Utils.TAG_THUMBNAIL);
        ivHeader = (ImageView) fView.findViewById(R.id.iv_header);
        Utils.setImage(getContext(), thumbnail, ivHeader);

        duration = bundle.getString(Utils.TAG_DURATION);
        llCall = (LinearLayout) fView.findViewById(R.id.ll_call);
        llEnquiry = (LinearLayout) fView.findViewById(R.id.ll_enquiry);

        phoneNo = bundle.getString(Utils.TAG_PHONENUMBER);

        ivPackFav = (IconTextView) fView.findViewById(R.id.iv_pack_fav);

        packageId = bundle.getString(Utils.TAG_PACKAGE_ID);
        getTripRecord(packageId);
    }

    private void setProperties() {
        int themeContrastColor = UIUtils.getThemeContrastColor(getContext());
        header.setBackgroundColor(UIUtils.getThemeColor(getContext()));
        iTVMenu.setTextColor(themeContrastColor);
        iTVFav.setTextColor(themeContrastColor);
        tvTitle.setTextColor(themeContrastColor);
        tvPrice.setBackgroundColor(Color.parseColor(getString(R.string.color_card_view_first)));
        tvBestPlace.setBackgroundColor(UIUtils.getThemeColorWithOp65(context));
    }

    private void setListener() {
        iTVMenu.setOnClickListener(this);
        iTVFav.setOnClickListener(this);
        tvTourDetail.setOnClickListener(this);
        tvTourItinerary.setOnClickListener(this);
        llCall.setOnClickListener(this);
        llEnquiry.setOnClickListener(this);
        ivPackFav.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        DbHelper dbHelper = DbHelper.getInstance(context);
        isFav = dbHelper.isPackageFav(packageId);
        setFavorite(isFav);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.left_icon:
                getActivity().onBackPressed();
                break;
            case R.id.right_icon:
                CountryPackagesFragment countryPackagesFragment = new CountryPackagesFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean(Utils.TAG_WISH_LIST, true);
                if(Utils.CURRENT_HOME_ACTIVITY.equals(Utils.TAG_HOME_ACTIVITY)) {
                    ((HomeActivity) getContext()).changeFragment(countryPackagesFragment, bundle, Utils.TAG_COUNTRY_PACKAGES_FRAGMENT);
                } else {
                    ((TripBoxHomeActivity) getContext()).changeFragment(countryPackagesFragment, bundle, Utils.TAG_COUNTRY_PACKAGES_FRAGMENT);
                }
                break;
            case R.id.iv_pack_fav:
                if(isFav) {
                    setFavorite(false);
                    isFav = false;
                } else {
                    setFavorite(true);
                    isFav = true;
                }
                break;
            case R.id.right_icon3:
                if (Utils.packagesCountryList != null && Utils.packagesCountryList.getCountries().size() > 0) {
                    PlaceSearchFragment placeSearchFragment = new PlaceSearchFragment();
                    bundle = new Bundle();
                    bundle.putSerializable(Utils.TAG_PACKAGES_COUNTRY_LIST, Utils.packagesCountryList);
                    if(Utils.CURRENT_HOME_ACTIVITY.equals(Utils.TAG_HOME_ACTIVITY)) {
                        ((HomeActivity) context).changeFragment(placeSearchFragment, bundle, Utils.TAG_PLACE_SEARCH_FRAGMENT);
                    } else {
                        ((TripBoxHomeActivity) context).changeFragment(placeSearchFragment, bundle, Utils.TAG_PLACE_SEARCH_FRAGMENT);
                    }
                }
                break;
            case R.id.ll_call:
                final Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(getContext().getString(R.string.call) + phoneNo));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    Nammu.askForPermission(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, new PermissionCallback() {
                        @Override
                        public void permissionGranted() {
                            applyCall(callIntent);

                        }

                        @Override
                        public void permissionRefused() {
                            Toast.makeText(getActivity(), R.string.text_permisson_refused, Toast.LENGTH_LONG).show();
                        }
                    });

                else {
                       applyCall(callIntent);
                }
                break;
            case R.id.ll_enquiry:

                EnquiryFragment popularPackagesFragment = new EnquiryFragment();
                bundle = new Bundle();
                bundle.putString(Utils.TAG_PACKAGE_ID, packageId);
                if(Utils.CURRENT_HOME_ACTIVITY.equals(Utils.TAG_HOME_ACTIVITY)) {
                    ((HomeActivity) getContext()).changeFragment(popularPackagesFragment, bundle, Utils.TAG_ENQUIRY_FRAGMENT);
                } else {
                    ((TripBoxHomeActivity) getContext()).changeFragment(popularPackagesFragment, bundle, Utils.TAG_ENQUIRY_FRAGMENT);
                }

                break;
            case R.id.tv_tour_detail:
                setDetailTxt();
                break;
            case R.id.tv_tour_itinerary:
                tvOverview.setText(Html.fromHtml(tourItinerary));
                tvTourItinerary.setTextColor(Color.WHITE);
                tvTourItinerary.setBackgroundColor(UIUtils.getThemeColor(getContext()));
                tvTourDetail.setTextColor(Color.BLACK);
                tvTourDetail.setBackgroundColor(Color.WHITE);
                break;
        }

    }

    private void setFavorite(boolean value) {
        if(value) {
            ivPackFav.setText(context.getString(R.string.icon_text_fav));
            ivPackFav.setTextColor(context.getResources().getColor(R.color.red_first));
            DbHelper.getInstance(context).insertDatatoFavoriteTable(packageId, packageName,
                    duration, startingPrice, cityName,
                    thumbnail, phoneNo);
        } else {
            ivPackFav.setText(context.getString(R.string.icon_text_unFav));
            ivPackFav.setTextColor(context.getResources().getColor(R.color.white));
            DbHelper.getInstance(context).deleteRow(packageId);
        }

    }

    private void applyCall(Intent callIntent) {
        getContext().startActivity(callIntent);
    }

    private void setDetailTxt(){
        tvOverview.setText(Html.fromHtml(tourDetail));
        tvTourDetail.setTextColor(Color.WHITE);
        tvTourDetail.setBackgroundColor(UIUtils.getThemeColor(getContext()));
        tvTourItinerary.setTextColor(Color.BLACK);
        tvTourItinerary.setBackgroundColor(Color.WHITE);
    }

    private void getTripRecord(String packageId) {
        if (!Utils.isNetworkAvailable(getContext())) {
            Utils.showAlertDialog(getContext(), getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else {
            String url;
            NetworkTask networkTask = new NetworkTask(getContext(), PACKAGES_DETAIL);
            networkTask.setDialogMessage(getString(R.string.please_wait));
            networkTask.exposePostExecute(this);
            String paramsArray[] = null;
            url = UIUtils.getBaseUrl2(getContext()) + WebServiceConstants.urlSelectedTourDetails + packageId;
            paramsArray = new String[]{url, null};
            networkTask.execute(paramsArray);
        }

    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {

        if (object == null || object.equals("")) {
            Utils.showAlertDialog(getActivity(), getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else if (id == PACKAGES_DETAIL) {
            try {
                        Gson gson = new Gson();
                        tourDetailList = gson.fromJson(object, TourDetailList.class);
                        TourDetail mTourDetail = tourDetailList.getTourdetails().get(0);
                        tourDetail = mTourDetail.getDetails();
                        tourItinerary = mTourDetail.getItinerary();
                        setDetailTxt();

            } catch (Exception e) {
                Log.d("Trip", "error in resultFromNetwork " + e);
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
