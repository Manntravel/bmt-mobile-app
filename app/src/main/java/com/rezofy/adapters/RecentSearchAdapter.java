package com.rezofy.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.database.DbHelper;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.RecentSearchActivity;
import com.rezofy.views.custom_views.IconTextView;

/**
 * Created by linchpin on 12/2/16.
 */
public class RecentSearchAdapter extends RecyclerView.Adapter<SearchDataHolder> {
    private Context context;
    private CursorAdapter mCursorAdapter;
    private RecentSearchActivity recentSearchOb;

    public RecentSearchAdapter(Context context, Cursor cursor) {
        recentSearchOb = (RecentSearchActivity) context;
        this.context = context;
        mCursorAdapter = new CursorAdapter(context, cursor, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = LayoutInflater.from(context).inflate(R.layout.row_rcv_recentsearch, parent, false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, final Cursor cursor) {
                // Binding operations
                IconTextView iTVTripType = (IconTextView) view.findViewById(R.id.icon_departure_way);
                TextView cityNames = (TextView) view.findViewById(R.id.CityNames);
                TextView tvFlightSearchDetails = (TextView) view.findViewById(R.id.counts);
                if (cursor.getString(cursor.getColumnIndex(DbHelper.searchType)).equals(Utils.TYPE_ONE_WAY))
                    iTVTripType.setText(context.getString(R.string.icon_text_N));
                else
                    iTVTripType.setText(context.getString(R.string.icon_text_U));
                int noOfAdults = cursor.getInt(cursor.getColumnIndex(DbHelper.noOfAdults));
                int noOfChildren = cursor.getInt(cursor.getColumnIndex(DbHelper.noOfChildren));
                int noOfInfants = cursor.getInt(cursor.getColumnIndex(DbHelper.noOfInfants));
                StringBuilder sb = new StringBuilder();
                sb.append(Utils.changeDateFormat(cursor.getString(cursor.getColumnIndex(DbHelper.departDate)), Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_d_space_LLL));
                if (cursor.getString(cursor.getColumnIndex(DbHelper.searchType)).equals(Utils.TYPE_ROUND_TRIP)) {
                    sb.append(" - ");
                    sb.append(Utils.changeDateFormat(cursor.getString(cursor.getColumnIndex(DbHelper.retDate)), Utils.DATE_FORMAT_dd_dash_MM_dash_yyyy, Utils.DATE_FORMAT_d_space_LLL));
                }
                sb.append("  |  ");
                sb.append(String.valueOf(noOfAdults + noOfChildren + noOfInfants));
                sb.append(" ");
                sb.append(UIUtils.getLogicalTravellersText(context, noOfAdults, noOfChildren, noOfInfants));
                cityNames.setText(cursor.getString(cursor.getColumnIndex(DbHelper.originCityCode)) + "  -  " + cursor.getString(cursor.getColumnIndex(DbHelper.destCityCode)));
                tvFlightSearchDetails.setText(sb.toString());
                //Log.d("Trip","name is "+names+" :: "+tvFlightSearchDetails);

                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        // cursor.moveToPosition(position);
                        Log.d("Trip", "inside onclick origin city is " + cursor.getCount() + " ::: " + cursor.getString(cursor.getColumnIndex(DbHelper.originCityName)));
                        recentSearchOb.callFinish(cursor, position);


                        // Log.d("Trip", "positio is " + v.getTag());

                    }
                });

            }
        };

    }


    @Override
    public SearchDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mCursorAdapter.newView(context, mCursorAdapter.getCursor(), parent);
        return new SearchDataHolder(v);
    }


    @Override
    public void onBindViewHolder(final SearchDataHolder holder, final int position) {
        holder.itemView.setTag(position);
        mCursorAdapter.getCursor().moveToPosition(position); //EDITED: added this line as suggested in the comments below, thanks :)
        mCursorAdapter.bindView(holder.itemView, context, mCursorAdapter.getCursor());
    }


    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }
}


class SearchDataHolder extends RecyclerView.ViewHolder {
    public TextView cityNames, tvFlightSearchDetails;
    public IconTextView iTVTripType;

    public SearchDataHolder(View itemView) {
        super(itemView);
        cityNames = (TextView) itemView.findViewById(R.id.CityNames);
        tvFlightSearchDetails = (TextView) itemView.findViewById(R.id.counts);
        iTVTripType = (IconTextView) itemView.findViewById(R.id.icon_departure_way);
    }
}
