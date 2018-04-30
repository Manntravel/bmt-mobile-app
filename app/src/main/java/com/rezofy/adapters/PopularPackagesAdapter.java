package com.rezofy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.interfaces.OnLoadListener;
import com.rezofy.models.response_models.PopularPackages;
import com.rezofy.models.response_models.TourInformation;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.HomeActivity;
import com.rezofy.views.activities.TripBoxHomeActivity;
import com.rezofy.views.fragments.CountryPackagesFragment;

/**
 * Created by anuj on 11/1/17.
 */
public class PopularPackagesAdapter extends RecyclerView.Adapter {

    private final int VIEW_TYPE_FIRST = 0;
    private final int VIEW_TYPE_ODD = 1;
    private final int VIEW_TYPE_EVEN = 2;
    private Context context;
    private LayoutInflater layoutInflater;
    private PopularPackages popularPackages;
    private OnLoadListener mOnLoadMoreListener;
    private int lastVisibleItem, totalItemCount, visibleThreshold = 1;
    private boolean isLoading;

    public PopularPackagesAdapter(Context ctx, PopularPackages popularPackages, RecyclerView recyclerView) {

        context = ctx;
        layoutInflater = LayoutInflater.from(context);
        this.popularPackages = popularPackages;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //Log.d("Trip","total itemcount is "+totalItemCount+"::::"+lastVisibleItem);
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
             return VIEW_TYPE_FIRST;
        } else if(position % 2 == 1) {
            return VIEW_TYPE_ODD;
        } else {
            return VIEW_TYPE_EVEN;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_FIRST) {
           view = layoutInflater.inflate(R.layout.popular_packages_item1, parent, false);
            return new PopularPackagesHolder1(view);

        } else if (viewType == VIEW_TYPE_ODD) {
            view = layoutInflater.inflate(R.layout.popular_packages_item2, parent, false);
            return new PopularPackagesHolder2(view);

        } else {
            view = layoutInflater.inflate(R.layout.popular_packages_item3, parent, false);
            return new PopularPackagesHolder2(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final TourInformation tourInformation = popularPackages.getTourinfo().get(position);

        if(holder instanceof PopularPackagesHolder1) {
            final PopularPackagesHolder1 popularPackagesHolder1 = (PopularPackagesHolder1) holder;
            popularPackagesHolder1.tvPrice.setText(UIUtils.setAmountOnly(context, UIUtils.getFareToDisplay(context, tourInformation.getStarting_price())));
            popularPackagesHolder1.tvPlaceName.setText(tourInformation.getCountry_name());
            Utils.setImage(context, tourInformation.getThumbnail(), popularPackagesHolder1.ivHeader);
            popularPackagesHolder1.popular_packages_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCountryPackages(tourInformation.getCountry_name());
                }
            });

        } else {
            PopularPackagesHolder2 popularPackagesHolder2 = (PopularPackagesHolder2) holder;
            //UIUtils.setBackgroundGradientBottomToTop(popularPackagesHolder2.ivHeader);
            popularPackagesHolder2.tvPrice.setText(UIUtils.setAmountOnly(context, UIUtils.getFareToDisplay(context, tourInformation.getStarting_price())));
            popularPackagesHolder2.tvPlaceName.setText(tourInformation.getCountry_name());
            popularPackagesHolder2.tvPlaceNameBg.setText(tourInformation.getCountry_name());
            Utils.setImage(context, tourInformation.getThumbnail(), popularPackagesHolder2.ivHeader);
            popularPackagesHolder2.rlPlaceName.setBackgroundColor(UIUtils.getThemeColorWithOp65(context));
            popularPackagesHolder2.popular_packages_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCountryPackages(tourInformation.getCountry_name());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return popularPackages.getTourinfo().size();
    }

    public void openCountryPackages(String countryName) {

        CountryPackagesFragment countryPackagesFragment = new CountryPackagesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Utils.TAG_COUNTRY_NAME, countryName);
        bundle.putBoolean(Utils.TAG_WISH_LIST, false);
        if(Utils.CURRENT_HOME_ACTIVITY.equals(Utils.TAG_HOME_ACTIVITY)) {
            ((HomeActivity) context).changeFragment(countryPackagesFragment, bundle, Utils.TAG_COUNTRY_PACKAGES_FRAGMENT);
        }else {
            ((TripBoxHomeActivity) context).changeFragment(countryPackagesFragment, bundle, Utils.TAG_COUNTRY_PACKAGES_FRAGMENT);
        }

    }

    class PopularPackagesHolder1 extends RecyclerView.ViewHolder {

        private LinearLayout popular_packages_root;
        private RelativeLayout rlPrice;
        private ImageView ivHeader;
        private TextView tvPlaceName, tvPrice;

        public PopularPackagesHolder1(View itemView) {
            super(itemView);
            popular_packages_root = (LinearLayout) itemView.findViewById(R.id.popular_packages_root);
            ivHeader = (ImageView) itemView.findViewById(R.id.iv_header);
            tvPlaceName = (TextView) itemView.findViewById(R.id.tv_place_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            rlPrice = (RelativeLayout) itemView.findViewById(R.id.rl_price);
            rlPrice.setBackgroundColor(Color.parseColor(context.getString(R.string.color_card_view_first)));
        }
    }

    class PopularPackagesHolder2 extends RecyclerView.ViewHolder {
        private RelativeLayout popular_packages_root, rlPlaceName;
        private ImageView ivHeader;
        private TextView tvPlaceName, tvPrice, tvPlaceNameBg;

        public PopularPackagesHolder2(View itemView) {
            super(itemView);
            popular_packages_root = (RelativeLayout) itemView.findViewById(R.id.popular_packages_root);
            rlPlaceName = (RelativeLayout) itemView.findViewById(R.id.rl_place_name);
            ivHeader = (ImageView) itemView.findViewById(R.id.iv_header);
            tvPlaceName = (TextView) itemView.findViewById(R.id.tv_place_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvPlaceNameBg = (TextView) itemView.findViewById(R.id.tv_place_name_bg);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

}
