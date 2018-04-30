package com.rezofy.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rezofy.R;
import com.rezofy.models.response_models.FlightData;
import com.rezofy.models.response_models.SearchResponse;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.FlightsSearchDomesticTwowayActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by vkumar on 5/1/16.
 */
public class TwoWayDomesticFlightsSearchHeaderAdapter extends RecyclerView.Adapter<TwoWayDomesticFlightsSearchHeaderAdapter.ViewHolder> implements View.OnClickListener {

    private int categoryCount = 0;
    private SearchResponse searchResponse;
    private List<HeaderData> headerDataList;
    private int selectedCategoryPosition;
    private LinkedHashMap<String, ArrayList<FlightData>> originalMapSpecialFlights, toShowMapSpecialFlights;
    private Context context;
    private List<FlightData> originalRegularInboundFlights, toShowRegularInboundFlights, originalRegularOutboundFlights, toShowRegularOutboundFlights;
    private TwoWayDomesticFlightsSearchAdapter outboundAdapter, inboundAdapter;
    private RoundTripSpecialAdapter roundTripSpecialAdapter;
    private FlightsSearchDomesticTwowayActivity searchActivity;
    private String outboundText = "OUTBOUND", inboundText = "INBOUND";

    public TwoWayDomesticFlightsSearchHeaderAdapter(Context context, String data, TwoWayDomesticFlightsSearchAdapter outboundAdapter,
                                                    TwoWayDomesticFlightsSearchAdapter inboundAdapter, RoundTripSpecialAdapter roundTripSpecialAdapter) {
        this.context = context;
        searchActivity = (FlightsSearchDomesticTwowayActivity) context;
        this.outboundAdapter = outboundAdapter;
        this.inboundAdapter = inboundAdapter;
        this.roundTripSpecialAdapter = roundTripSpecialAdapter;
        parseDataToModelsAndUseInBackground(data);
    }

    private void parseDataToModelsAndUseInBackground(final String data) {
        final ProgressDialog pd = new ProgressDialog(context, R.style.TransparentProgressDialog);
        pd.setMessage(context.getString(R.string.please_wait));
        pd.setTitle(null);
        pd.setProgressStyle(R.style.TransparentProgressDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.custom_progress_dialog, null);
        ((TextView) v.findViewById(R.id.lodingText)).setText(context.getString(R.string.please_wait));
        pd.setView(v);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                searchResponse = new Gson().fromJson(data, SearchResponse.class);
                return null;
            }

            @Override
            protected void onPreExecute() {
                searchActivity.getRlRoot().setVisibility(View.GONE);
                pd.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                originalRegularOutboundFlights = searchResponse.getData().getREGULAR().getOUTBOUND();
                originalRegularInboundFlights = searchResponse.getData().getREGULAR().getINBOUND();
                toShowRegularOutboundFlights = new ArrayList<>();
                toShowRegularInboundFlights = new ArrayList<>();
                toShowRegularOutboundFlights.addAll(originalRegularOutboundFlights);
                toShowRegularInboundFlights.addAll(originalRegularInboundFlights);
                createOriginalMapSpecialFlights(data);
                toShowMapSpecialFlights = new LinkedHashMap<>();
                toShowMapSpecialFlights.putAll(originalMapSpecialFlights);
                headerDataList = new ArrayList<>();
                resetData();
                if (!headerDataList.isEmpty())
                    searchActivity.manageTvFares();
                notifyDataSetChanged();
                pd.dismiss();
                searchActivity.getRlRoot().setVisibility(View.VISIBLE);
            }
        }.execute();

    }

    public void resetData() {
        setCategoriesData();
        if (!headerDataList.isEmpty()) {
            searchActivity.getRcViewHeader().setVisibility(View.VISIBLE);
            searchActivity.findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.fares).setVisibility(View.VISIBLE);
            searchActivity.findViewById(R.id.filter_layout).setVisibility(View.VISIBLE);
            searchActivity.getTvBook().setVisibility(View.VISIBLE);
            setSelectedCategoryPosition(0);
            manageUIAndFlightAdapters();
        } else {
            searchActivity.getRcViewHeader().setVisibility(View.GONE);
            searchActivity.findViewById(R.id.content_main).findViewById(R.id.header).findViewById(R.id.fares).setVisibility(View.GONE);
            searchActivity.findViewById(R.id.filter_layout).setVisibility(View.GONE);
            searchActivity.getTvBook().setVisibility(View.GONE);
        }
    }

    private void setLowestPrice(HeaderData headerData) {
        if (headerData.getFareResultsType().equals(Utils.FARE_RESULTS_TYPE_REGULAR)
                || (headerData.getFareResultsType().equals(Utils.FARE_RESULTS_TYPE_SPECIAL) && !headerData.getTab().equals(Utils.TAB_GDS))) {
            List<FlightData> outboundList, inboundList;
            outboundList = new ArrayList<>();
            inboundList = new ArrayList<>();
            if (headerData.getFareResultsType().equals(Utils.FARE_RESULTS_TYPE_REGULAR)) {
                outboundList.addAll(toShowRegularOutboundFlights);
                inboundList.addAll(toShowRegularInboundFlights);
            } else {
                outboundList.addAll(toShowMapSpecialFlights.get(headerData.getTab() + outboundText));
                inboundList.addAll(toShowMapSpecialFlights.get(headerData.getTab() + inboundText));
            }
            Utils.setSellingPriceInFlightDataList(outboundList);
            Utils.setSellingPriceInFlightDataList(inboundList);
            Collections.sort(outboundList, UIUtils.priceIncComparator);
            Collections.sort(inboundList, UIUtils.priceIncComparator);
            headerData.setLowestCost(UIUtils.getFareToDisplay(context, (float) Math.ceil(outboundList.get(0).getFare().getSellingPrice())
                    + (float) Math.ceil(inboundList.get(0).getFare().getSellingPrice())));

        } else {
            List<FlightData> specialList = new ArrayList<>();
            specialList.addAll(toShowMapSpecialFlights.get(Utils.TAB_GDS));
            Utils.setSellingPriceInFlightDataList(specialList);
            Collections.sort(specialList, UIUtils.priceIncComparator);
            headerData.setLowestCost(UIUtils.getFareToDisplay(context, specialList.get(0).getFare().getSellingPrice()));
        }
    }

    private void manageUIAndFlightAdapters() {
        if (headerDataList.get(selectedCategoryPosition).getFareResultsType().equals(Utils.FARE_RESULTS_TYPE_REGULAR)) {
            searchActivity.getLlNonGDS().setVisibility(View.VISIBLE);
            setAdapters(toShowRegularOutboundFlights, toShowRegularInboundFlights);

        } else {
            String tab = headerDataList.get(selectedCategoryPosition).getTab();
            if (tab.equals(Utils.TAB_GDS)) {
                searchActivity.getLlNonGDS().setVisibility(View.GONE);
                roundTripSpecialAdapter.setFlightDataList(toShowMapSpecialFlights.get(Utils.TAB_GDS));
                roundTripSpecialAdapter.setSelectedFlightPosition(0);
                roundTripSpecialAdapter.notifyDataSetChanged();

            } else {
                searchActivity.getLlNonGDS().setVisibility(View.VISIBLE);
                setAdapters(toShowMapSpecialFlights.get(tab + outboundText), toShowMapSpecialFlights.get(tab + inboundText));
            }
        }
    }

    private void setAdapters(List<FlightData> outboundFlightDataList, List<FlightData> inboundFlightDataList) {
        outboundAdapter.setFlightDataList(outboundFlightDataList);
        outboundAdapter.setSelectedFlightPositionToDefault();
        outboundAdapter.notifyDataSetChanged();
        searchActivity.getRcViewOutbound().smoothScrollToPosition(outboundAdapter.getSelectedFlightPosition());
        inboundAdapter.setFlightDataList(inboundFlightDataList);
        inboundAdapter.setSelectedFlightPositionToDefault();
        inboundAdapter.notifyDataSetChanged();
        searchActivity.getRcViewInbound().smoothScrollToPosition(inboundAdapter.getSelectedFlightPosition());
    }


    private void createOriginalMapSpecialFlights(String data) {
        try {
            JSONObject responseJSONObject = new JSONObject(data);
            originalMapSpecialFlights = new Gson().fromJson(responseJSONObject.getJSONObject("data").getJSONObject("SPECIAL").toString(),
                    new TypeToken<LinkedHashMap<String, ArrayList<FlightData>>>() {
                    }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setCategoriesData() {
        headerDataList.clear();
        categoryCount = 0;
        if ((toShowRegularOutboundFlights != null && !toShowRegularOutboundFlights.isEmpty())
                || (toShowRegularInboundFlights != null && !toShowRegularInboundFlights.isEmpty())) {
            ++categoryCount;
            HeaderData headerData = new HeaderData();
            headerData.setAirlineName(context.getString(R.string.all_airlines_text));
            headerData.setFareResultsType(Utils.FARE_RESULTS_TYPE_REGULAR);
            headerData.setTab(Utils.TAB_REGULAR);
            setLowestPrice(headerData);
            headerDataList.add(headerData);
        }

        if (toShowMapSpecialFlights.containsKey(Utils.TAB_GDS) && !toShowMapSpecialFlights.get(Utils.TAB_GDS).isEmpty()) {
            ++categoryCount;
            HeaderData headerData = new HeaderData();
            headerData.setAirlineName(context.getString(R.string.gds_airlines_text));
            headerData.setFareResultsType(Utils.FARE_RESULTS_TYPE_SPECIAL);
            headerData.setTab(Utils.TAB_GDS);
            setLowestPrice(headerData);
            headerDataList.add(headerData);
        }

        List<String> keysList = new ArrayList<>(toShowMapSpecialFlights.keySet());
        List<String> carriersList = new ArrayList<>();
        for (int i = 0; i < keysList.size(); i++) {
            String carrierCode = keysList.get(i).substring(0, 2);
            if (!keysList.get(i).equals(Utils.TAB_GDS)
                    && !toShowMapSpecialFlights.get(keysList.get(i)).isEmpty()
                    && !carriersList.contains(carrierCode)) {
                carriersList.add(carrierCode);
                ++categoryCount;
                HeaderData headerData = new HeaderData();
                headerData.setAirlineName(toShowMapSpecialFlights.get(keysList.get(i)).get(0).getSegments().get(0).getLegs().get(0).getAirline());
                headerData.setFareResultsType(Utils.FARE_RESULTS_TYPE_SPECIAL);
                headerData.setTab(toShowMapSpecialFlights.get(keysList.get(i)).get(0).getCarrier());
                setLowestPrice(headerData);
                headerDataList.add(headerData);
            }
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder != null) {
            holder.rlRoot.setTag(position);
            holder.rlRoot.setOnClickListener(this);
            if (position == selectedCategoryPosition) {
                holder.tvAirlineName.setTextColor(UIUtils.getThemeContrastColor(context));
                holder.tvLowestCost.setTextColor(UIUtils.getThemeContrastColor(context));
            } else {
                holder.tvAirlineName.setTextColor(UIUtils.getThemeColor(context));
                holder.tvLowestCost.setTextColor(UIUtils.getThemeColor(context));
            }
            holder.tvAirlineName.setText(headerDataList.get(position).getAirlineName());
            if (headerDataList.get(position).getFareResultsType().equals(Utils.FARE_RESULTS_TYPE_REGULAR))
                holder.tvLowestCost.setVisibility(View.GONE);
            else {
                holder.tvLowestCost.setVisibility(View.VISIBLE);
                holder.tvLowestCost.setText(headerDataList.get(position).getLowestCost());
            }
        }
    }

    @Override
    public int getItemCount() {
        return categoryCount;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rc_viewhorizoitem, parent, false));
    }

    @Override
    public void onClick(View v) {
        selectedCategoryPosition = (int) v.getTag();
        manageUIAndFlightAdapters();
        searchActivity.manageTvFares();
        notifyDataSetChanged();
    }

    public TwoWayDomesticFlightsSearchAdapter getInboundAdapter() {
        return inboundAdapter;
    }

    public void setInboundAdapter(TwoWayDomesticFlightsSearchAdapter inboundAdapter) {
        this.inboundAdapter = inboundAdapter;
    }

    public TwoWayDomesticFlightsSearchAdapter getOutboundAdapter() {
        return outboundAdapter;
    }

    public void setOutboundAdapter(TwoWayDomesticFlightsSearchAdapter outboundAdapter) {
        this.outboundAdapter = outboundAdapter;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLowestCost;
        private TextView tvAirlineName;
        private RelativeLayout rlRoot;

        public ViewHolder(View itemView) {
            super(itemView);
            rlRoot = (RelativeLayout) itemView.findViewById(R.id.root);
            tvLowestCost = (TextView) itemView.findViewById(R.id.cost);
            tvAirlineName = (TextView) itemView.findViewById(R.id.name);
        }
    }

    public class HeaderData {
        private String airlineName;
        private String lowestCost;
        private String fareResultsType;
        private String tab;

        public String getAirlineName() {
            return airlineName;
        }

        public void setAirlineName(String airlineName) {
            this.airlineName = airlineName;
        }

        public String getLowestCost() {
            return lowestCost;
        }

        public void setLowestCost(String lowestCost) {
            this.lowestCost = lowestCost;
        }

        public String getFareResultsType() {
            return fareResultsType;
        }

        public void setFareResultsType(String fareResultsType) {
            this.fareResultsType = fareResultsType;
        }

        public String getTab() {
            return tab;
        }

        public void setTab(String tab) {
            this.tab = tab;
        }
    }

    public int getSelectedCategoryPosition() {
        return selectedCategoryPosition;
    }

    public void setSelectedCategoryPosition(int selectedCategoryPosition) {
        this.selectedCategoryPosition = selectedCategoryPosition;
    }

    public List<HeaderData> getHeaderDataList() {
        return headerDataList;
    }

    public void setHeaderDataList(List<HeaderData> headerDataList) {
        this.headerDataList = headerDataList;
    }

    public LinkedHashMap<String, ArrayList<FlightData>> getOriginalMapSpecialFlights() {
        return originalMapSpecialFlights;
    }

    public void setOriginalMapSpecialFlights(LinkedHashMap<String, ArrayList<FlightData>> originalMapSpecialFlights) {
        this.originalMapSpecialFlights = originalMapSpecialFlights;
    }

    public LinkedHashMap<String, ArrayList<FlightData>> getToShowMapSpecialFlights() {
        return toShowMapSpecialFlights;
    }

    public void setToShowMapSpecialFlights(LinkedHashMap<String, ArrayList<FlightData>> toShowMapSpecialFlights) {
        this.toShowMapSpecialFlights = toShowMapSpecialFlights;
    }

    public List<FlightData> getOriginalRegularInboundFlights() {
        return originalRegularInboundFlights;
    }

    public void setOriginalRegularInboundFlights(List<FlightData> originalRegularInboundFlights) {
        this.originalRegularInboundFlights = originalRegularInboundFlights;
    }

    public List<FlightData> getToShowRegularInboundFlights() {
        return toShowRegularInboundFlights;
    }

    public void setToShowRegularInboundFlights(List<FlightData> toShowRegularInboundFlights) {
        this.toShowRegularInboundFlights = toShowRegularInboundFlights;
    }

    public List<FlightData> getOriginalRegularOutboundFlights() {
        return originalRegularOutboundFlights;
    }

    public void setOriginalRegularOutboundFlights(List<FlightData> originalRegularOutboundFlights) {
        this.originalRegularOutboundFlights = originalRegularOutboundFlights;
    }

    public List<FlightData> getToShowRegularOutboundFlights() {
        return toShowRegularOutboundFlights;
    }

    public void setToShowRegularOutboundFlights(List<FlightData> toShowRegularOutboundFlights) {
        this.toShowRegularOutboundFlights = toShowRegularOutboundFlights;
    }

    public SearchResponse getSearchResponse() {
        return searchResponse;
    }
}
