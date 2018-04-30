package com.rezofy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.database.DbHelper;
import com.rezofy.models.response_models.CountryTours;
import com.rezofy.models.response_models.SelectedCountryTours;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.HomeActivity;
import com.rezofy.views.activities.TripBoxHomeActivity;
import com.rezofy.views.fragments.PackageDetailFragment;

/**
 * Created by anuj on 11/1/17.
 */
public class CountryPackageAdapter extends RecyclerView.Adapter {

    private final int VIEW_TYPE_FIRST = 0;
    private final int VIEW_TYPE_SECOND = 1;
    private Context context;
    private LayoutInflater layoutInflater;
    private SelectedCountryTours selectedCountryTours;
    private String firstThumbnail;
    private boolean isWhisList;

    public CountryPackageAdapter(Context ctx, SelectedCountryTours selectedCountryTours, boolean isWishList) {

        context = ctx;
        layoutInflater = LayoutInflater.from(context);
        this.selectedCountryTours = selectedCountryTours;
        this.isWhisList = isWishList;
        if(selectedCountryTours != null && !isWishList) {
            if(selectedCountryTours.getCountryToursArrayList() != null && selectedCountryTours.getCountryToursArrayList().size() > 0) {
                firstThumbnail = selectedCountryTours.getCountryToursArrayList().get(selectedCountryTours.getCountryToursArrayList().size() - 1).getThumbnail();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 && !isWhisList) {
            return VIEW_TYPE_FIRST;
        } else {
            return VIEW_TYPE_SECOND;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_FIRST) {
            view = layoutInflater.inflate(R.layout.country_packages_item1, parent, false);
            return new PopularPackagesHolder1(view);

        } else {
            view = layoutInflater.inflate(R.layout.country_packages_item2, parent, false);
            return new PopularPackagesHolder2(view);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position > 0 && !isWhisList)
            position--;
        final CountryTours countryTours = selectedCountryTours.getCountryToursArrayList().get(position);

        if(holder instanceof PopularPackagesHolder1) {
            PopularPackagesHolder1 popularPackagesHolder1 = (PopularPackagesHolder1) holder;
            popularPackagesHolder1.tvPlaceNameBg.setText(countryTours.getCountry_name());
            popularPackagesHolder1.tvPlaceName.setText(countryTours.getCountry_name());
            Utils.setImage(context, firstThumbnail, popularPackagesHolder1.ivHeader);
            popularPackagesHolder1.llPlaceName.setBackgroundColor(UIUtils.getThemeColorWithOp65(context));

        } else {
            final PopularPackagesHolder2 popularPackagesHolder2 = (PopularPackagesHolder2) holder;
            popularPackagesHolder2.tvBestPlace.setText(countryTours.getName());
            popularPackagesHolder2.tvFav.setText("fav");
            popularPackagesHolder2.tvDuration.setText(countryTours.getDuration());
            popularPackagesHolder2.tvPrice.setText(UIUtils.setAmountOnly(context, UIUtils.getFareToDisplay(context, countryTours.getStarting_price())));
            Utils.setImage(context, countryTours.getThumbnail(), popularPackagesHolder2.ivHeader);
            if(!isWhisList) {
                popularPackagesHolder2.rlDuration.setBackgroundColor(UIUtils.getThemeColor(context));
                popularPackagesHolder2.rlPrice.setBackgroundColor(UIUtils.getThemeColor(context));
                popularPackagesHolder2.tvDuration.setTextColor(Color.WHITE);
            }
            popularPackagesHolder2.rlCountryPackageRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPackageDetail(countryTours.getID(), countryTours.getName(), countryTours.getCity_name(), countryTours.getStarting_price(), countryTours.getThumbnail(),
                            countryTours.getPhonenumber(), countryTours.getIsFav(), countryTours.getDuration());
                popularPackagesHolder2.rlPrice.setBackgroundColor(Color.parseColor(context.getString(R.string.color_card_view_first)));
                }
            });
            if (countryTours.getIsFav()) {
                popularPackagesHolder2.tvFav.setText(context.getString(R.string.icon_text_fav));
                popularPackagesHolder2.tvFav.setTextColor(context.getResources().getColor(R.color.red_first));
            } else {
                popularPackagesHolder2.tvFav.setText(context.getString(R.string.icon_text_unFav));
                popularPackagesHolder2.tvFav.setTextColor(context.getResources().getColor(R.color.white));
            }
            popularPackagesHolder2.tvFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (countryTours.getIsFav()) {
                        countryTours.setIsFav(false);
                        popularPackagesHolder2.tvFav.setText(context.getString(R.string.icon_text_unFav));
                        popularPackagesHolder2.tvFav.setTextColor(context.getResources().getColor(R.color.white));
                        DbHelper.getInstance(context).deleteRow(countryTours.getID());
                    } else {
                        countryTours.setIsFav(true);
                        popularPackagesHolder2.tvFav.setText(context.getString(R.string.icon_text_fav));
                        popularPackagesHolder2.tvFav.setTextColor(context.getResources().getColor(R.color.red_first));
                        DbHelper.getInstance(context).insertDatatoFavoriteTable(countryTours.getID(), countryTours.getName(),
                                countryTours.getDuration(), countryTours.getStarting_price(), countryTours.getCity_name(),
                                countryTours.getThumbnail(), countryTours.getPhonenumber() );
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(!isWhisList)
          return selectedCountryTours.getCountryToursArrayList().size() + 1;
        else
            return selectedCountryTours.getCountryToursArrayList().size();

    }

    public void openPackageDetail(String packageId, String packageName, String cityName, String startingPrice, String thumbnail, String phonenumber, boolean isFav, String duration) {

        PackageDetailFragment packageDetailFragment = new PackageDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Utils.TAG_PACKAGE_ID, packageId);
        bundle.putString(Utils.TAG_PACKAGE_NAME, packageName);
        bundle.putString(Utils.TAG_CITY_NAME, cityName);
        bundle.putString(Utils.TAG_STARTING_PRICE, startingPrice);
        bundle.putString(Utils.TAG_THUMBNAIL, thumbnail);
        bundle.putString(Utils.TAG_PHONENUMBER, phonenumber);
        bundle.putBoolean(Utils.TAG_FAVORITE, isFav);
        bundle.putString(Utils.TAG_DURATION, duration);
        if(Utils.CURRENT_HOME_ACTIVITY.equals(Utils.TAG_HOME_ACTIVITY)) {
            ((HomeActivity) context).changeFragment(packageDetailFragment, bundle, Utils.TAG_PACKAGE_DETAIL_FRAGMENT);
        } else {
            ((TripBoxHomeActivity) context).changeFragment(packageDetailFragment, bundle, Utils.TAG_PACKAGE_DETAIL_FRAGMENT);
        }

    }

    class PopularPackagesHolder1 extends RecyclerView.ViewHolder {

        private LinearLayout llPlaceName;
        private RelativeLayout rlCountryPackageRoot;
        private ImageView ivHeader;
        private TextView tvPlaceName, tvPlaceNameBg;

        public PopularPackagesHolder1(View itemView) {
            super(itemView);
            llPlaceName = (LinearLayout) itemView.findViewById(R.id.ll_placeName);
            rlCountryPackageRoot = (RelativeLayout) itemView.findViewById(R.id.country_packages_root1);
            ivHeader = (ImageView) itemView.findViewById(R.id.iv_header);
            tvPlaceNameBg = (TextView) itemView.findViewById(R.id.tv_place_name_bg);
            tvPlaceName = (TextView) itemView.findViewById(R.id.tv_place_name);
        }
    }

    class PopularPackagesHolder2 extends RecyclerView.ViewHolder {
        private RelativeLayout rlCountryPackageRoot, rlDuration, rlPrice, rlBestPlace;
        private LinearLayout llDuration;
        private ImageView ivHeader;
        private TextView tvDuration, tvPrice, tvBestPlace, tvFav;

        public PopularPackagesHolder2(View itemView) {
            super(itemView);
            rlCountryPackageRoot = (RelativeLayout) itemView.findViewById(R.id.country_packages_root2);
            ivHeader = (ImageView) itemView.findViewById(R.id.iv_header);

            rlBestPlace = (RelativeLayout) itemView.findViewById(R.id.rl_best_place);
            tvBestPlace = (TextView) itemView.findViewById(R.id.tv_best_place);
            tvFav = (TextView) itemView.findViewById(R.id.tv_fav);

            llDuration = (LinearLayout) itemView.findViewById(R.id.ll_duration);
            rlDuration = (RelativeLayout) itemView.findViewById(R.id.rl_duration);
            rlPrice = (RelativeLayout) itemView.findViewById(R.id.rl_price);
            tvDuration = (TextView) itemView.findViewById(R.id.tv_duration);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);

        }
    }

}
