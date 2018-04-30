package com.rezofy.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.models.request_models.DateInfo;
import com.rezofy.models.request_models.Passport;
import com.rezofy.models.request_models.Traveller;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.PassengerActivity;
import com.rezofy.views.custom_views.IconTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by linchpin11192 on 21-Jan-2016.
 */
public class PassengerIconsAdapter extends RecyclerView.Adapter<PassengerIconsAdapter.ViewHolder> implements View.OnClickListener {
    private int noOfAdults;
    private int noOfChildren;
    private int noOfInfants;
    private int totalPassengers;
    private int selectedPassengerPosition = 0;
    private List<Traveller> listPassengersInfo;
    private Context context;
    private PassengerActivity passengerActivity;
    private boolean doLegContainAirAsia;
    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    public PassengerIconsAdapter(Context context, int noOfAdults, int noOfChildren, int noOfInfants, List<Traveller> listPassengersInfo, boolean doLegContainAirAsia) {
        this.context = context;
        passengerActivity = (PassengerActivity) context;
        this.noOfAdults = noOfAdults;
        this.noOfChildren = noOfChildren;
        this.noOfInfants = noOfInfants;
        totalPassengers = noOfAdults + noOfChildren + noOfInfants;
        this.listPassengersInfo = listPassengersInfo;
        this.doLegContainAirAsia = doLegContainAirAsia;
        showSelectedPassengerInfo();
    }

    @Override
    public PassengerIconsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.passenger_icon_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.tvPassengerNo.setText((position + 1) + "");
        if (position == selectedPassengerPosition) {
            holder.iTVPassenger.setSelected(true);
            holder.tvPassengerNo.setSelected(true);
        } else {
            holder.iTVPassenger.setSelected(false);
            holder.tvPassengerNo.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return totalPassengers;
    }

    @Override
    public void onClick(View v) {
        int clickedPosition = (int) v.getTag();
        if (clickedPosition != selectedPassengerPosition)
            handlePositionChange(clickedPosition);
    }

    public void handlePositionChange(int desiredPosition) {
        saveSelectedPassengerInfo();
        setPassengerInfoToDefault();
        selectedPassengerPosition = desiredPosition;
        handleViews();
        showSelectedPassengerInfo();
        passengerActivity.getRvPassengers().smoothScrollToPosition(desiredPosition);
        passengerActivity.getEtFirstName().requestFocus();
        notifyDataSetChanged();
    }

    public void handleViews() {
        passengerActivity.getPassengerDate().setVisibility(View.GONE);
        passengerActivity.getLlResidenceSpinner().setVisibility(View.GONE);
        passengerActivity.getLlPPIssuedBySpinner().setVisibility(View.GONE);
        passengerActivity.getEtPassportNo().setVisibility(View.GONE);
        passengerActivity.getTvPPExpDate().setVisibility(View.GONE);
        if (passengerActivity.getOriginCountryCode().equals(context.getString(R.string.originCountryCode)) && passengerActivity.getDestCountryCode().equals(context.getString(R.string.originCountryCode))) {
            if (!listPassengersInfo.get(selectedPassengerPosition).getType().equals(Utils.PASSENGER_TYPE_ADULT) || doLegContainAirAsia)
                passengerActivity.getPassengerDate().setVisibility(View.VISIBLE);
        } else {
            passengerActivity.getPassengerDate().setVisibility(View.VISIBLE);
            passengerActivity.getLlResidenceSpinner().setVisibility(View.VISIBLE);
            passengerActivity.getLlPPIssuedBySpinner().setVisibility(View.VISIBLE);
            passengerActivity.getEtPassportNo().setVisibility(View.VISIBLE);
            passengerActivity.getTvPPExpDate().setVisibility(View.VISIBLE);

        }
    }

    public void setPassengerInfoToDefault() {
        passengerActivity.getEtFirstName().setText("");
        passengerActivity.getEtLastName().setText("");
        passengerActivity.getPassengerDate().setText(passengerActivity.getTEXT_DOB());
        passengerActivity.getGenderSpinner().setSelection(0);
        passengerActivity.getMealSpinner().setSelection(0);
        passengerActivity.getEtPassportNo().setText("");
        passengerActivity.getSpinnerResidence().setSelection(((CountrySpinnerAdapter) passengerActivity.getSpinnerResidence().getAdapter()).getCountryList().indexOf("India"));
        passengerActivity.getSpinnerIssuedBy().setSelection(((CountrySpinnerAdapter) passengerActivity.getSpinnerIssuedBy().getAdapter()).getCountryList().indexOf("India"));
        passengerActivity.getTvPPExpDate().setText(passengerActivity.getTEXT_PED());
    }

    public void saveSelectedPassengerInfo() {
        if (!passengerActivity.getEtFirstName().getText().toString().trim().isEmpty())
            listPassengersInfo.get(selectedPassengerPosition).setFirstName(passengerActivity.getEtFirstName().getText().toString());
        else
            listPassengersInfo.get(selectedPassengerPosition).setFirstName(null);


        if (!passengerActivity.getEtLastName().getText().toString().trim().isEmpty())
            listPassengersInfo.get(selectedPassengerPosition).setLastName(passengerActivity.getEtLastName().getText().toString());
        else
            listPassengersInfo.get(selectedPassengerPosition).setLastName(null);

        if (!passengerActivity.getPassengerDate().getText().toString().equals(passengerActivity.getTEXT_DOB()))
            listPassengersInfo.get(selectedPassengerPosition).setDateOfBirth(createAndGetDateInfo(passengerActivity.getPassengerDate()));

        if (passengerActivity.getLlResidenceSpinner().getVisibility() == View.VISIBLE)
            listPassengersInfo.get(selectedPassengerPosition).setPassport(managePassport());
        listPassengersInfo.get(selectedPassengerPosition).setTitle(createAndGetTitle(passengerActivity.getGenderSpinner().getSelectedItemPosition()));
        listPassengersInfo.get(selectedPassengerPosition).setMealPref(createAndGetMealPref(passengerActivity.getMealSpinner().getSelectedItemPosition()));
    }

    private Passport managePassport() {
        Passport passport = listPassengersInfo.get(selectedPassengerPosition).getPassport();
        if (passport == null)
            passport = new Passport();
        if (!passengerActivity.getTvPPExpDate().getText().toString().equals(passengerActivity.getTEXT_PED()))
            passport.setPassExpDate(createAndGetDateInfo(passengerActivity.getTvPPExpDate()));
        if (!passengerActivity.getEtPassportNo().getText().toString().trim().isEmpty())
            passport.setPassPortNo(passengerActivity.getEtPassportNo().getText().toString());
        else
            passport.setPassPortNo(null);
        passport.setNationality(((CountrySpinnerAdapter) passengerActivity.getSpinnerResidence().getAdapter()).getCountryList().get(passengerActivity.getSpinnerResidence().getSelectedItemPosition()));
        passport.setIssuingCountry(((CountrySpinnerAdapter) passengerActivity.getSpinnerIssuedBy().getAdapter()).getCountryList().get(passengerActivity.getSpinnerIssuedBy().getSelectedItemPosition()));
        return passport;
    }

    private DateInfo createAndGetDateInfo(TextView tvDate) {
        try {
            DateInfo dateInfo = new DateInfo();
            SimpleDateFormat sdfMonth = new SimpleDateFormat("LLL");
            java.util.Date date = format.parse((String) tvDate.getText());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            dateInfo.setDate((String) tvDate.getText());
            dateInfo.setDay(date.getDate() + "");
            dateInfo.setYear(calendar.get(Calendar.YEAR) + "");
            dateInfo.setMonth(sdfMonth.format(date));
            return dateInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String createAndGetTitle(int selectedPosition) {
        if (selectedPosition == 0)
            return null;
        else if (selectedPosition == 1)
            return Utils.TITLE_MR;
        else
            return Utils.TITLE_MS;
    }

    private String createAndGetMealPref(int selectedPosition) {
        if (selectedPosition == 0)
            return Utils.OPTION_NP;
        else if (selectedPosition == 1)
            return Utils.MEAL_VEG;
        else
            return Utils.MEAL_NON_VEG;
    }


    public void showSelectedPassengerInfo() {
        setPassengerNoOnBasisOfType();
        if (listPassengersInfo.get(selectedPassengerPosition).getFirstName() != null)
            passengerActivity.getEtFirstName().setText(listPassengersInfo.get(selectedPassengerPosition).getFirstName());
        if (listPassengersInfo.get(selectedPassengerPosition).getLastName() != null)
            passengerActivity.getEtLastName().setText(listPassengersInfo.get(selectedPassengerPosition).getLastName());
        passengerActivity.getTvPassengerType().setText(listPassengersInfo.get(selectedPassengerPosition).getType());
        if (listPassengersInfo.get(selectedPassengerPosition).getDateOfBirth() != null)
            passengerActivity.getPassengerDate().setText(listPassengersInfo.get(selectedPassengerPosition).getDateOfBirth().getDate());
        passengerActivity.getGenderSpinner().setSelection(getDecodedTitle());
        if (listPassengersInfo.get(selectedPassengerPosition).getPassport() != null) {
            passengerActivity.getSpinnerResidence().setSelection(((CountrySpinnerAdapter) passengerActivity.getSpinnerResidence().getAdapter()).getCountryList().indexOf(listPassengersInfo.get(selectedPassengerPosition).getPassport().getNationality()));
            passengerActivity.getSpinnerIssuedBy().setSelection(((CountrySpinnerAdapter) passengerActivity.getSpinnerIssuedBy().getAdapter()).getCountryList().indexOf(listPassengersInfo.get(selectedPassengerPosition).getPassport().getIssuingCountry()));
            if (listPassengersInfo.get(selectedPassengerPosition).getPassport().getPassPortNo() != null)
                passengerActivity.getEtPassportNo().setText(listPassengersInfo.get(selectedPassengerPosition).getPassport().getPassPortNo());
            if (listPassengersInfo.get(selectedPassengerPosition).getPassport().getPassExpDate() != null)
                passengerActivity.getTvPPExpDate().setText(listPassengersInfo.get(selectedPassengerPosition).getPassport().getPassExpDate().getDate());
        }
        passengerActivity.getMealSpinner().setSelection(getDecodedMealPref());
    }

    private void setPassengerNoOnBasisOfType() {
        passengerActivity.getTvPassengerNo().setText(context.getString(R.string.passenger_no_text) + (selectedPassengerPosition + 1));
        String passengerTypeText;
        if (listPassengersInfo.get(selectedPassengerPosition).getType().equals(Utils.PASSENGER_TYPE_ADULT))
            passengerTypeText = context.getString(R.string.adult_small_text);
        else if (listPassengersInfo.get(selectedPassengerPosition).getType().equals(Utils.PASSENGER_TYPE_CHILD))
            passengerTypeText = context.getString(R.string.child_small_text);
        else
            passengerTypeText = context.getString(R.string.infant_small_text);
        passengerActivity.getTvPassengerNo().setText(passengerActivity.getTvPassengerNo().getText().toString().concat("\n" + passengerTypeText));
    }

    private int getDecodedTitle() {
        if (listPassengersInfo.get(selectedPassengerPosition).getTitle() == null)
            return 0;
        else if (listPassengersInfo.get(selectedPassengerPosition).getTitle().equals(Utils.TITLE_MR))
            return 1;
        else
            return 2;
    }

    private int getDecodedMealPref() {
        if (listPassengersInfo.get(selectedPassengerPosition).getMealPref() == Utils.OPTION_NP)
            return 0;
        else if (listPassengersInfo.get(selectedPassengerPosition).getMealPref().equals(Utils.MEAL_VEG))
            return 1;
        else
            return 2;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        IconTextView iTVPassenger;
        TextView tvPassengerNo;
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_selected}, // selected
                new int[]{-android.R.attr.state_selected}, // unselected
        };

        int[] colors = new int[]{
                UIUtils.getThemeColor(context), Color.BLACK
        };

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemView.setOnClickListener(PassengerIconsAdapter.this);
            iTVPassenger = (IconTextView) itemView.findViewById(R.id.passenger_icon);
            ColorStateList colorStateList = new ColorStateList(states, colors);
            iTVPassenger.setTextColor(colorStateList);
            tvPassengerNo = (TextView) itemView.findViewById(R.id.passenger_no);
            tvPassengerNo.setTextColor(colorStateList);
        }
    }

    public int getSelectedPassengerPosition() {
        return selectedPassengerPosition;
    }

    public void setSelectedPassengerPosition(int selectedPassengerPosition) {
        this.selectedPassengerPosition = selectedPassengerPosition;
    }
}
