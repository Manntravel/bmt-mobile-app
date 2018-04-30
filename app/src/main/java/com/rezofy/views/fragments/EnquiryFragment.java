package com.rezofy.views.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rezofy.R;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.controllers.DatabaseController;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.activities.DateSelectorActivity;
import com.rezofy.views.custom_views.IconTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class EnquiryFragment extends Fragment implements View.OnClickListener, NetworkTask.Result {

    private static final int ENQUIRY_INFO = 1;
    private static final String STATUS = "Status";
    private static final String OK = "OK";
    View fView, header, vDesign, viewULTravellers, vDestinationBottomLine, vMylocationBottomLine;
    private LinearLayout llMylocationInfo, llDestinationInfo, llDuration;
    private RelativeLayout rlTravellers;
    Spinner spMycountry, spDestnationCountry;
    IconTextView iTVMenu;
    TextInputLayout txtInputLayoutName, txtInputLayoutMobileNo, txtInputLayoutEmail;
    TextView tvTitle, tvSubmit, txtDate, tvDuration, tvMylocation, tvDestination, tvTravellersInfo, tvTravellersNos, tv2Nights3Days, tv3Nights4Days, tv4Nights5Days, tvOther;
    EditText etRemark, editTxtName, editTxtMobileNo, editTxtEmail, etMyCity, etDestinationCity;
    private int noOfAdults = 1, noOfChildren = 0, noOfInfants = 0, myCountryItemPosition, destinationCountryItemPosition;
    private IconTextView iTVTravellerIcon;
    private boolean isPackageDurationListVisible, isMyLocationVisible, isDestinationVisible;
    private String packageId;

    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (fView == null) {
            fView = inflater.inflate(R.layout.fragment_enquiry, container, false);
            initViews();
            setProperties();
            setListener();
        }

        return fView;
    }

    private void initViews() {
        header = fView.findViewById(R.id.header);
        if (header != null) {
            iTVMenu = (IconTextView) header.findViewById(R.id.left_icon);
            iTVMenu.setText(getString(R.string.icon_text_k));
            iTVMenu.setOnClickListener(this);
            iTVMenu.setTextSize(20);

            tvTitle = (TextView) header.findViewById(R.id.title);
            tvTitle.setText(getString(R.string.packages_text));

        }

        vDesign = fView.findViewById(R.id.v_design);

        packageId = getArguments().getString(Utils.TAG_PACKAGE_ID);

        llMylocationInfo = (LinearLayout) fView.findViewById(R.id.ll_mylocation_info);
        llDestinationInfo = (LinearLayout) fView.findViewById(R.id.ll_destination_info);
        llDuration = (LinearLayout) fView.findViewById(R.id.ll_duration);
        spMycountry = (Spinner) fView.findViewById(R.id.sp_mycountry);
        spDestnationCountry = (Spinner) fView.findViewById(R.id.sp_destnation_country);
        tvMylocation = (TextView) fView.findViewById(R.id.tv_mylocation);
        tvDestination = (TextView) fView.findViewById(R.id.tv_destination);
        editTxtName = (EditText) fView.findViewById(R.id.edit_txt_name);
        editTxtMobileNo = (EditText) fView.findViewById(R.id.edit_txt_mobile_no);
        editTxtEmail = (EditText) fView.findViewById(R.id.edit_txt_email);
        etMyCity = (EditText) fView.findViewById(R.id.et_my_city);
        etDestinationCity = (EditText) fView.findViewById(R.id.et_destination_city);
        txtDate = (TextView) fView.findViewById(R.id.txt_date);
        tvDuration = (TextView) fView.findViewById(R.id.tv_duration);
        tv2Nights3Days = (TextView) fView.findViewById(R.id.tv_2Nights3Days);
        tv3Nights4Days = (TextView) fView.findViewById(R.id.tv_3Nights4Days);
        tv4Nights5Days = (TextView) fView.findViewById(R.id.tv_4Nights5Days);
        tvOther = (TextView) fView.findViewById(R.id.tv_other);

        txtInputLayoutName = (TextInputLayout) fView.findViewById(R.id.txt_input_layout_name);
        txtInputLayoutMobileNo = (TextInputLayout) fView.findViewById(R.id.txt_input_layout_mobile_no);
        txtInputLayoutEmail = (TextInputLayout) fView.findViewById(R.id.txt_input_layout_email);

        vMylocationBottomLine = (View) fView.findViewById(R.id.v_mylocation_bottom_line);
        vDestinationBottomLine = (View) fView.findViewById(R.id.v_destination_bottom_line);

        tvSubmit = (TextView) fView.findViewById(R.id.tv_submit);

        etRemark = (EditText) fView.findViewById(R.id.et_remark);
        rlTravellers = (RelativeLayout) fView.findViewById(R.id.travellers);
        iTVTravellerIcon = (IconTextView) fView.findViewById(R.id.traveller_icon);
        tvTravellersInfo = (TextView) fView.findViewById(R.id.travellers_info);
        tvTravellersNos = (TextView) fView.findViewById(R.id.travellers_nos);
        viewULTravellers = fView.findViewById(R.id.ul_travellers);
    }

    private void setProperties() {
        int themeContrastColor = UIUtils.getThemeContrastColor(getContext());
        header.setBackgroundColor(UIUtils.getThemeColor(getContext()));
        vDesign.setBackgroundColor(UIUtils.getThemeColor(getContext()));
        UIUtils.setRoundedButtonProperties(tvSubmit);
        iTVMenu.setTextColor(themeContrastColor);
        tvTitle.setTextColor(themeContrastColor);
        UIUtils.setThemeLightSelector(rlTravellers);
        viewULTravellers.setBackgroundColor(themeContrastColor);
    }

    private void setListener() {
        iTVMenu.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        tvMylocation.setOnClickListener(this);
        tvDestination.setOnClickListener(this);

        spMycountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    myCountryItemPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spDestnationCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    destinationCountryItemPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements

        // Creating adapter for spinner

        List<String> countryList = DatabaseController.getInstance(getContext()).getCountryList();
        countryList.add(0, getString(R.string.select_country));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, countryList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner

            spMycountry.setAdapter(dataAdapter);

            ArrayList<String> countryList2 = new ArrayList<String>();

            countryList2.add(0, getString(R.string.select_country));
            countryList2.addAll(Utils.getPackageCountryList());

            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, countryList2);

            // Drop down layout style - list view with radio button
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spDestnationCountry.setAdapter(dataAdapter2);


        tvDuration.setOnClickListener(this);
        tv2Nights3Days.setOnClickListener(this);
        tv3Nights4Days.setOnClickListener(this);
        tv4Nights5Days.setOnClickListener(this);
        tvOther.setOnClickListener(this);


        rlTravellers.setOnClickListener(this);


        etRemark.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.et_remark) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.left_icon:
                Utils.hideSoftKey(getContext());
                getActivity().onBackPressed();
                break;

            case R.id.tv_mylocation:
                isMyLocationVisible = !isMyLocationVisible;
                if(isMyLocationVisible) {
                    llMylocationInfo.setVisibility(View.VISIBLE);
                    vMylocationBottomLine.setVisibility(View.GONE);
                } else {
                    llMylocationInfo.setVisibility(View.GONE);
                    vMylocationBottomLine.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.tv_destination:
                isDestinationVisible = !isDestinationVisible;
                if(isDestinationVisible) {
                    llDestinationInfo.setVisibility(View.VISIBLE);
                    vDestinationBottomLine.setVisibility(View.GONE);
                } else {
                    llDestinationInfo.setVisibility(View.GONE);
                    vDestinationBottomLine.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.txt_date:
                Intent dateIntent = new Intent(getActivity(), DateSelectorActivity.class);
                dateIntent.putExtra("isDeparture", true);
                dateIntent.putExtra("isDateDeparture", Utils.getTomorrowDate());
                dateIntent.putExtra("sourceTag", Utils.TAG_ENQUIRY_FRAGMENT);
                startActivityForResult(dateIntent, Utils.DATE_SELECTOR_CODE);

                break;
            case R.id.tv_duration:
                isPackageDurationListVisible = !isPackageDurationListVisible;
                if(isPackageDurationListVisible) {
                          llDuration.setVisibility(View.VISIBLE);
                } else {
                          llDuration.setVisibility(View.GONE);
                }
                break;

            case R.id.tv_2Nights3Days:
                tvDuration.setText(tv2Nights3Days.getText().toString());
                setDurationOptionVisibilityGone();
                break;
            case R.id.tv_3Nights4Days:
                tvDuration.setText(tv3Nights4Days.getText().toString());
                setDurationOptionVisibilityGone();
                break;
            case R.id.tv_4Nights5Days:
                tvDuration.setText(tv4Nights5Days.getText().toString());
                setDurationOptionVisibilityGone();
                break;
            case R.id.tv_other:
                tvDuration.setText(tvOther.getText().toString());
                setDurationOptionVisibilityGone();
                break;

            case R.id.travellers:
                createTravellersDialog();
                break;
            case R.id.tv_submit:

                if(validate()) {
                    sendData();
                }
                break;
        }

    }

    private void setDurationOptionVisibilityGone() {
        isPackageDurationListVisible = false;
        llDuration.setVisibility(View.GONE);
    }

    private boolean validate() {
        String errorMessage = null;


            if (editTxtName.getText().toString() == null || editTxtName.getText().toString().length() < 2) {
                errorMessage = getString(R.string.enter_mendatory_field);
                txtInputLayoutName.setError(getString(R.string.error_field_required));


            } if (editTxtMobileNo.getText().toString() == null || editTxtMobileNo.getText().toString().length() < 2) {
                errorMessage = getString(R.string.enter_mendatory_field);
                txtInputLayoutMobileNo.setError(getString(R.string.error_field_required));



            } else if (!Utils.isValidMobile(editTxtMobileNo.getText().toString(), getString(R.string.plus_91))) {
                  if(getString(R.string.plus_91).equals("+61") || getString(R.string.plus_91).equals("+64")){
                      errorMessage = getString(R.string.valid_phone_no_txt9);
                  } else {
                      errorMessage = getString(R.string.valid_phone_no_txt10);
                  }

        }  if (editTxtEmail.getText().toString() == null || editTxtEmail.getText().toString().length() < 2) {
                errorMessage = getString(R.string.enter_mendatory_field);
                txtInputLayoutEmail.setError(getString(R.string.error_field_required));

            } else if (!Utils.isValidEmail(editTxtEmail.getText().toString())) {
                errorMessage = getString(R.string.wrong_email_id);

         }
            if(myCountryItemPosition == 0) {
                errorMessage = getString(R.string.select_my_country);
            }
            if (etMyCity.getText().toString() == null || etMyCity.getText().toString().length() < 2) {
                errorMessage = getString(R.string.enter_my_city);

            }

        if(destinationCountryItemPosition == 0) {
            errorMessage = getString(R.string.select_destination_country);
        }
             if (etDestinationCity.getText().toString() == null || etDestinationCity.getText().toString().length() < 2) {
                errorMessage = getString(R.string.enter_destination_city);

            }
             if (txtDate.getText().toString() == null || txtDate.getText().toString().length() < 2) {
                errorMessage = getString(R.string.error_select_date);

            } if (tvDuration.getText().toString() == null || tvDuration.getText().toString().length() < 2) {
                errorMessage = getString(R.string.error_select_duration);

            }

        if (errorMessage != null) {
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            return false;

        } else
            return true;
    }

    private void sendData() {
        String name[] = editTxtName.getText().toString().split(" ");
        String firstName = name[0];
        String lastName = "";
        for (int i = 1; i < name.length; i++) {
            lastName = lastName.concat(name[i]).concat(" ");
        }

        if (!Utils.isNetworkAvailable(getContext())) {
            Utils.showAlertDialog(getContext(), getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else {
            try {
                String url;
                JSONObject obj = new JSONObject();
                obj.put("ID", packageId);
                obj.put("Tourdate", txtDate.getText().toString());
                obj.put("Duration", tvDuration.getText().toString());
                obj.put("Firstname", firstName);
                obj.put("Lastname", lastName);
                obj.put("Email", editTxtEmail.getText().toString());
                obj.put("Phonenumber", editTxtMobileNo.getText().toString());
                obj.put("Customeraddress", etMyCity.getText().toString());
                obj.put("Destinationcity", etDestinationCity.getText().toString());
                obj.put("Destinationcountry", Utils.getPackageCountryList().get((destinationCountryItemPosition - 1)));
                obj.put("Adults", noOfAdults);
                obj.put("Children", noOfChildren);
                obj.put("Remarks", etRemark.getText().toString());
                NetworkTask networkTask = new NetworkTask(getContext(), ENQUIRY_INFO);
                networkTask.setDialogMessage(getString(R.string.please_wait));
                networkTask.exposePostExecute(this);
                String paramsArray[] = null;
                url = UIUtils.getBaseUrl2(getContext()) + WebServiceConstants.urltourbooking;
                paramsArray = new String[]{url, obj.toString()};
                networkTask.execute(paramsArray);
            } catch (JSONException jsonexception) {
                jsonexception.printStackTrace();
            }
        }
    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {
        if (object == null || object.equals("")) {
            Utils.showAlertDialog(getActivity(), getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else if (id == ENQUIRY_INFO) {
            try {
                if(object.contains(STATUS)) {
                    JSONObject jsonObject = new JSONObject(object);
                    if(jsonObject.get(STATUS).equals(OK)) {
                        Toast.makeText(getContext(), getString(R.string.data_submit_sucessfully), Toast.LENGTH_SHORT).show();
                    }
                }


            } catch (Exception e) {
                Log.d("Trip", "error in resultFromNetwork " + e);
            }
        }
    }

    private void createTravellersDialog() {
        Dialog travellersDialog = new Dialog(getActivity());
        travellersDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        travellersDialog.setContentView(R.layout.dialog_traveller_info);
        travellersDialog.setCancelable(true);
        travellersDialog.getWindow().setLayout(Utils.getScreenSize(getActivity())[0] - 60, ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout llTravellerIcons = (LinearLayout) travellersDialog.findViewById(R.id.travellers_icons);
        View.OnClickListener clickListener = getDialogClicksListener(travellersDialog);
        TextView tvAdultDecrement = (TextView) travellersDialog.findViewById(R.id.adult_decrement);
        tvAdultDecrement.setOnClickListener(clickListener);
        TextView tvAdultIncrement = (TextView) travellersDialog.findViewById(R.id.adult_increment);
        tvAdultIncrement.setOnClickListener(clickListener);
        TextView tvChildrenDecrement = (TextView) travellersDialog.findViewById(R.id.children_decrement);
        tvChildrenDecrement.setOnClickListener(clickListener);
        TextView tvChildrenIncrement = (TextView) travellersDialog.findViewById(R.id.children_increment);
        tvChildrenIncrement.setOnClickListener(clickListener);
        TextView tvInfantsDecrement = (TextView) travellersDialog.findViewById(R.id.infant_decrement);
        tvInfantsDecrement.setOnClickListener(clickListener);
        tvInfantsDecrement.setVisibility(View.GONE);
        TextView tvInfantsIncrement = (TextView) travellersDialog.findViewById(R.id.infant_increment);
        tvInfantsIncrement.setOnClickListener(clickListener);
        tvInfantsIncrement.setVisibility(View.GONE);
        TextView tvOk = (TextView) travellersDialog.findViewById(R.id.ok);
        tvOk.setOnClickListener(clickListener);
        TextView tvAdultNos = (TextView) travellersDialog.findViewById(R.id.adult_no);
        tvAdultNos.setText(String.valueOf(noOfAdults));
        TextView tvChildrenNos = (TextView) travellersDialog.findViewById(R.id.children_no);
        tvChildrenNos.setText(String.valueOf(noOfChildren));
        TextView tvInfantNos = (TextView) travellersDialog.findViewById(R.id.infant_no);
        tvInfantNos.setText(String.valueOf(noOfInfants));
        tvInfantNos.setVisibility(View.GONE);
        TextView tvInfants = (TextView) travellersDialog.findViewById(R.id.tv_infants);
        tvInfants.setVisibility(View.GONE);
        TextView tvInfantsInfo = (TextView) travellersDialog.findViewById(R.id.tv_infants_info);
        tvInfantsInfo.setVisibility(View.GONE);
        manageLLTravellerIcons(llTravellerIcons, noOfAdults, noOfChildren);
        travellersDialog.show();

    }

    private View.OnClickListener getDialogClicksListener(final Dialog travellersDialog) {
        final LinearLayout llTravellerIcons = (LinearLayout) travellersDialog.findViewById(R.id.travellers_icons);
        final TextView tvAdultNos = (TextView) travellersDialog.findViewById(R.id.adult_no);
        tvAdultNos.setTextColor(UIUtils.getThemeColor(getContext()));
        final TextView tvChildrenNos = (TextView) travellersDialog.findViewById(R.id.children_no);
        tvChildrenNos.setTextColor(UIUtils.getThemeColor(getContext()));
        final TextView tvInfantNos = (TextView) travellersDialog.findViewById(R.id.infant_no);
        tvInfantNos.setTextColor(UIUtils.getThemeColor(getContext()));
        return new View.OnClickListener() {
            int dialogNoOfAdults = noOfAdults;
            int dialogNoOfChildren = noOfChildren;
            int dialogNoOfInfants = noOfInfants;

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.adult_decrement:
                        if (dialogNoOfAdults > 1 && dialogNoOfAdults > dialogNoOfInfants) {
                            tvAdultNos.setText(String.valueOf(--dialogNoOfAdults));
                            manageLLTravellerIcons(llTravellerIcons, dialogNoOfAdults, dialogNoOfChildren);

                        } else if (dialogNoOfAdults == dialogNoOfInfants) {
                            Toast.makeText(getContext(), getString(R.string.max_infants), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.adult_increment:
                        if ((dialogNoOfAdults + dialogNoOfChildren) < 9) {
                            tvAdultNos.setText(String.valueOf(++dialogNoOfAdults));
                            manageLLTravellerIcons(llTravellerIcons, dialogNoOfAdults, dialogNoOfChildren);
                            if ((dialogNoOfAdults + dialogNoOfChildren) == 9)
                                Toast.makeText(getContext(), getString(R.string.upto_max_passenger), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.max_passenger), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.children_decrement:
                        if (dialogNoOfChildren > 0) {
                            tvChildrenNos.setText(String.valueOf(--dialogNoOfChildren));
                            manageLLTravellerIcons(llTravellerIcons, dialogNoOfAdults, dialogNoOfChildren);
                        }
                        break;

                    case R.id.children_increment:
                        if ((dialogNoOfAdults + dialogNoOfChildren) < 9) {
                            tvChildrenNos.setText(String.valueOf(++dialogNoOfChildren));
                            manageLLTravellerIcons(llTravellerIcons, dialogNoOfAdults, dialogNoOfChildren);
                            if ((dialogNoOfAdults + dialogNoOfChildren) == 9)
                                Toast.makeText(getContext(), getString(R.string.upto_max_passenger), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.max_passenger), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.infant_decrement:
                        if (dialogNoOfInfants > 0) {
                            tvInfantNos.setText(String.valueOf(--dialogNoOfInfants));
                            manageLLTravellerIcons(llTravellerIcons, dialogNoOfAdults, dialogNoOfChildren);
                        }
                        break;

                    case R.id.infant_increment:
                        if (dialogNoOfInfants < dialogNoOfAdults) {
                            tvInfantNos.setText(String.valueOf(++dialogNoOfInfants));
                            manageLLTravellerIcons(llTravellerIcons, dialogNoOfAdults, dialogNoOfChildren);
                        } else {
                            Toast.makeText(getContext(), getString(R.string.max_infants), Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case R.id.ok:
                        noOfAdults = dialogNoOfAdults;
                        noOfChildren = dialogNoOfChildren;
                        noOfInfants = dialogNoOfInfants;
                        tvTravellersNos.setText(String.valueOf(noOfAdults + noOfChildren + noOfInfants));
                        if (noOfChildren == 0 && noOfInfants == 0 && noOfAdults > 1) {
                            iTVTravellerIcon.setText(getString(R.string.icon_text_b));
                            tvTravellersInfo.setText(getString(R.string.adults_text));

                        } else if (noOfChildren == 0 && noOfInfants == 0 && noOfAdults == 1) {
                            iTVTravellerIcon.setText(getString(R.string.icon_text_b));
                            tvTravellersInfo.setText(getString(R.string.adult_text));

                        } else if ((noOfChildren != 0 || noOfInfants != 0) && noOfAdults >= 1) {
                            if (noOfAdults > 1)
                                iTVTravellerIcon.setText(getString(R.string.icon_text_j));
                            tvTravellersInfo.setText(getString(R.string.travellers_text));
                        }
                        travellersDialog.dismiss();
                        break;

                }
            }
        };
    }

    private void manageLLTravellerIcons(final LinearLayout llTravellerIcons, int dialogNoOfAdults, int dialogNoOfChildren) {
        llTravellerIcons.removeAllViews();
        for (int i = 1; i <= dialogNoOfAdults; i++) {
            IconTextView iTVAdult = new IconTextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(-10, 0, -10, 0);
            iTVAdult.setLayoutParams(params);
            iTVAdult.setGravity(Gravity.CENTER);
            iTVAdult.setText("b");
            iTVAdult.setTextColor(getResources().getColor(R.color.grey_first));
            iTVAdult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
            llTravellerIcons.addView(iTVAdult);
        }
        for (int i = 0; i < dialogNoOfChildren; i++) {
            IconTextView iTVAdult = new IconTextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(-5, 0, -5, 0);
            iTVAdult.setLayoutParams(params);
            iTVAdult.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            iTVAdult.setText("b");
            iTVAdult.setTextColor(getResources().getColor(R.color.grey_first));
            iTVAdult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            llTravellerIcons.addView(iTVAdult);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Utils.DATE_SELECTOR_CODE) {
            String date = data.getStringExtra("returnDepartureDate");
            txtDate.setText(Utils.changeDateFormat(date, Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_yyyy_dash_MM_dash_dd));

        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Utils.PLACE_SEARCH_CODE) {
//    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
