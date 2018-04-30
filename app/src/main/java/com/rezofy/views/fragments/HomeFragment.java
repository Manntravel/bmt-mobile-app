package com.rezofy.views.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.adapters.ItemHomeViewPagerAdapter;
import com.rezofy.adapters.OptionsAdapter;
import com.rezofy.preferences.AppPreferences;
import com.rezofy.utils.UIUtils;
import com.rezofy.views.activities.HomeActivity;
import com.rezofy.views.custom_views.IconTextView;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private View view;
    private RecyclerView rcOptions;
    private ViewPager pagerITD;
    private com.viewpagerindicator.CirclePageIndicator pageInITD;
    private PageIndicator indicator;
    private ItemHomeViewPagerAdapter itemHomeViewPagerAdapter;
    private int[] imageUrls;
    private TextView info_text_first, info_text_second, tvTitle;
    private IconTextView iTVMenu, iTVShareApp;
    private CardView card_view_first, card_view_second;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        pagerITD.setAdapter(itemHomeViewPagerAdapter);
        indicator.setViewPager(pagerITD);
        customizeIndicator();
        setProperties();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
       /* if (AppPreferences.getInstance(getContext()).getUserData().isEmpty())
            iTVMenu.setVisibility(View.GONE);
        else
            iTVMenu.setVisibility(View.VISIBLE);*/
    }

    private void init() {
        pagerITD = (ViewPager) view.findViewById(R.id.pagerITD);
        pageInITD = (CirclePageIndicator) view.findViewById(R.id.indicatorITD);
        indicator = pageInITD;
        imageUrls = new int[]{R.drawable.image1, R.drawable.image2, R.drawable.image3};
        itemHomeViewPagerAdapter = new ItemHomeViewPagerAdapter(getActivity(), imageUrls);
        rcOptions = (RecyclerView) view.findViewById(R.id.options);
        rcOptions.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        OptionsAdapter optionsAdapter = new OptionsAdapter(getActivity());
        rcOptions.setAdapter(optionsAdapter);
        rcOptions.scrollToPosition(optionsAdapter.getItemCount() - 1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rcOptions.smoothScrollToPosition(0);
            }
        }, 1000);
        iTVMenu = (IconTextView) view.findViewById(R.id.header).findViewById(R.id.left_icon);
        iTVMenu.setOnClickListener(this);
        iTVMenu.setText(getString(R.string.icon_text_h));
        iTVMenu.setTextSize(20);
        iTVShareApp = (IconTextView) view.findViewById(R.id.header).findViewById(R.id.right_icon);
        iTVShareApp.setVisibility(View.VISIBLE);
        iTVShareApp.setOnClickListener(this);
        iTVShareApp.setText(getString(R.string.icon_text_I));
        iTVShareApp.setTextSize(20);
        tvTitle = ((TextView) view.findViewById(R.id.header).findViewById(R.id.title));
        tvTitle.setText(getString(R.string.app_name));

        // card view property

        card_view_first = (CardView) view.findViewById(R.id.card_view_first);
        card_view_second = (CardView) view.findViewById(R.id.card_view_second);
        info_text_first = (TextView) view.findViewById(R.id.info_text_first);
        info_text_second = (TextView) view.findViewById(R.id.info_text_second);
        setListener();
    }

    private void setListener() {
        pagerITD.setOnPageChangeListener(this);
    }

    private void setProperties() {
        int themeContrastColor = UIUtils.getThemeContrastColor(getContext());

        // card property change
        card_view_first.setCardBackgroundColor(Color.parseColor(getString(R.string.color_card_view_first)));
        card_view_second.setCardBackgroundColor(Color.parseColor(getString(R.string.color_card_view_second)));
        info_text_first.setText(Html.fromHtml("&ldquo; " + "  " + getString(R.string.text_card_view_first) + "  " + "&ldquo;"));
        info_text_second.setText(Html.fromHtml("&ldquo; " + "  " + getString(R.string.text_card_view_second) + "  " + "&ldquo;"));

        //  header property
        view.findViewById(R.id.header).setBackgroundColor(UIUtils.getThemeColor(getContext()));
        rcOptions.setBackgroundColor(UIUtils.getThemeColor(getContext()));
        iTVMenu.setTextColor(themeContrastColor);
        iTVShareApp.setTextColor(themeContrastColor);
        tvTitle.setTextColor(themeContrastColor);
        info_text_first.setTextColor(themeContrastColor);
        info_text_second.setTextColor(themeContrastColor);
    }


    @Override
    public void onClick(View v) {

        int i = v.getId();
        switch (i) {
            case R.id.left_icon:
                ((HomeActivity) getActivity()).openDrawer();
                break;

            case R.id.right_icon:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_share_app));
                startActivity(Intent.createChooser(shareIntent, getString(R.string.text_share)));
                break;
        }
    }

    private void customizeIndicator() {
        final float density = getResources().getDisplayMetrics().density;
        pageInITD.setBackgroundColor(Color.TRANSPARENT);
        pageInITD.setPageColor(getResources().getColor(R.color.white_color));
        pageInITD.setFillColor(getResources().getColor(R.color.indicator_color));
        pageInITD.setStrokeColor(Color.TRANSPARENT);
        pageInITD.setStrokeWidth(density);
    }

    @Override
    public void onPageSelected(int position) {
        //    Toast.makeText(getActivity(), "onPageSelected"+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //   Toast.makeText(getActivity(), "onPageScrollStateChanged"+state, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Toast.makeText(getActivity(), "onPageScrolled"+position, Toast.LENGTH_SHORT).show();
    }
}
