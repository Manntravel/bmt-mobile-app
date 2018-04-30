package com.rezofy.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rezofy.R;

/**
 * Created by linchpinub4 on 2/7/15.
 */
public class ItemHomeViewPagerAdapter extends PagerAdapter {
    private int[] imageurls;
    private Context context;
    private ImageView ivLeftArrow, ivRightArrow;
    LayoutInflater inflater;
    private TextView tvImageTitle;
    int i = 0;


    public ItemHomeViewPagerAdapter(Context context, int[] imageurls) {
        this.ivLeftArrow = ivLeftArrow;
        this.ivRightArrow = ivRightArrow;
        this.context = context;
        this.imageurls = imageurls;
    }

    @Override
    public int getCount() {
        return imageurls.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        //  return view == ((ImageView) object);
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        i++;
        /*ImageView imageView = new ImageView(context);
        imageView.setPadding(0, 0, 0, 0);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(imageurls[position]);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;*/
        ImageView imgflag;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container, false);
        imgflag = (ImageView) itemView.findViewById(R.id.pager_imageview);
        imgflag.setImageResource(imageurls[position]);
        tvImageTitle = (TextView) itemView.findViewById(R.id.tv_Pager);
        tvImageTitle.setVisibility(View.GONE);
        if (i % 3 == 0)
            tvImageTitle.setText(context.getResources().getString(R.string.view_pager_first_image_text));
        else if (i % 3 == 1)
            tvImageTitle.setText(context.getResources().getString(R.string.view_pager_second_image_text));
        else if (i % 3 == 2)
            tvImageTitle.setText(context.getResources().getString(R.string.view_pager_third_image_text));

        ((ViewPager) container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // ((ViewPager) container).removeView((ImageView) object);
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}