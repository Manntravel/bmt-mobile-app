package com.rezofy.views.custom_views;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by techbirds on 04-09-2015.
 */
public class IconTextView extends TextView {


    public IconTextView(Context context) {
        super(context, null);
        setText();
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setText();
    }

    public IconTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    void setText() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/rezofy.ttf");
        setTypeface(typeface);
    }
}
