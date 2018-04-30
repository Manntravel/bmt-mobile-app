package com.rezofy.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.utils.UIUtils;
import com.rezofy.views.custom_views.IconTextView;

/**
 * Created by anuj on 7/2/17.
 */
public class CurrencyConverterActivity extends AppCompatActivity implements View.OnClickListener{

    private View header;
    private IconTextView iTVMenu;
    private TextView tvTitle;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_currency_converter);

        initViews();
        setProperties();
        setData();
    }

    private void initViews(){
        context = getApplicationContext();

        header = findViewById(R.id.header);
        if(header != null) {
            iTVMenu = (IconTextView) header.findViewById(R.id.left_icon);
            iTVMenu.setText(getString(R.string.icon_text_h));
            iTVMenu.setOnClickListener(this);
            iTVMenu.setTextSize(20);

            tvTitle = (TextView) header.findViewById(R.id.title);
            tvTitle.setText(getString(R.string.currency_converter));

        }

    }

    private void setProperties() {
        int themeContrastColor = UIUtils.getThemeContrastColor(context);
        header.setBackgroundColor(UIUtils.getThemeColor(context));
        iTVMenu.setTextColor(themeContrastColor);
        tvTitle.setTextColor(themeContrastColor);
    }

    private void setData() {

    }

    @Override
    public void onClick(View v) {

    }
}
