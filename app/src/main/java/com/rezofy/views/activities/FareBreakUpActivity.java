package com.rezofy.views.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.models.response_models.FlightData;
import com.rezofy.models.response_models.PriceAndConversionModel;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.custom_views.IconTextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Linchpin66 on 23-01-2016.
 */
public class FareBreakUpActivity extends AppCompatActivity implements View.OnClickListener {
    protected TextView tvTotalBaseFare, tvNetPayable, tvTitle, tvTotalTax;
    protected LinearLayout llTaxes;
    private FlightData flightDataFirstWay, flightDataSecondWay;
    protected IconTextView tvFinish;
    protected List<PriceAndConversionModel> listTaxesFirstWay;
    protected List<String> listTaxKeysFirstWay, listTaxKeysSecondWay;
    protected ImageView btnFloating;
    private float convenienceFee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_break_up);
        init();
        setProperties();
        setFareValues();

    }

    private void setProperties() {
        findViewById(R.id.header).setBackgroundColor(UIUtils.getThemeColor(this));
        tvFinish.setTextColor(UIUtils.getThemeContrastColor(this));
        tvTitle.setTextColor(UIUtils.getThemeContrastColor(this));

    }

    protected void setFareValues() {
        if (listTaxKeysSecondWay == null) {
            tvTotalBaseFare.setText(UIUtils.getFareToDisplay(this, flightDataFirstWay.getFare().getBase().getPrice().getAmount()));
            for (int i = 0; i < listTaxKeysFirstWay.size(); i++) {
                if (!listTaxKeysFirstWay.get(i).equals(Utils.KEY_PUBLISHED_PRICE_DIFF)) {
                    if (!listTaxKeysFirstWay.get(i).equalsIgnoreCase("totalTax")) {
                        View taxView = LayoutInflater.from(this).inflate(R.layout.layout_tax, null, false);
                        ((TextView) taxView.findViewById(R.id.tax_name)).setText(listTaxKeysFirstWay.get(i));
                        ((TextView) taxView.findViewById(R.id.tax_value)).setText(UIUtils.getFareToDisplay(this, listTaxesFirstWay.get(i).getPrice().getAmount()));
                        llTaxes.addView(taxView);
                    } else {
                        tvTotalTax.setText(UIUtils.getFareToDisplay(this, listTaxesFirstWay.get(i).getPrice().getAmount()));
                    }
                }
            }
            float netPayable = Float.parseFloat(flightDataFirstWay.getFare().getTotal().getPrice().getAmount()) + convenienceFee;
            tvNetPayable.setText(UIUtils.getFareToDisplay(this, netPayable));
        } else {
            tvTotalBaseFare.setText(UIUtils.getFareToDisplay(this, Float.parseFloat(flightDataFirstWay.getFare().getBase().getPrice().getAmount()) + Float.parseFloat(flightDataSecondWay.getFare().getBase().getPrice().getAmount())));
            for (int i = 0; i < listTaxKeysFirstWay.size(); i++) {
                if (listTaxKeysSecondWay.contains(listTaxKeysFirstWay.get(i)) && !listTaxKeysFirstWay.get(i).equals(Utils.KEY_PUBLISHED_PRICE_DIFF)) {
                    if (!listTaxKeysFirstWay.get(i).equalsIgnoreCase("totalTax")) {
                        View taxView = LayoutInflater.from(this).inflate(R.layout.layout_tax, null, false);
                        ((TextView) taxView.findViewById(R.id.tax_name)).setText(listTaxKeysFirstWay.get(i));
                        ((TextView) taxView.findViewById(R.id.tax_value)).setText(UIUtils.getFareToDisplay(this, Float.parseFloat(flightDataFirstWay.getFare().getTaxes().get(listTaxKeysFirstWay.get(i)).getPrice().getAmount()) + Float.parseFloat(flightDataSecondWay.getFare().getTaxes().get(listTaxKeysFirstWay.get(i)).getPrice().getAmount())));
                        llTaxes.addView(taxView);
                    } else {
                        tvTotalTax.setText(UIUtils.getFareToDisplay(this, Float.parseFloat(flightDataFirstWay.getFare().getTaxes().get(listTaxKeysFirstWay.get(i)).getPrice().getAmount()) + Float.parseFloat(flightDataSecondWay.getFare().getTaxes().get(listTaxKeysFirstWay.get(i)).getPrice().getAmount())));
                    }
                }
            }
            tvNetPayable.setText(UIUtils.getFareToDisplay(this, Float.parseFloat(flightDataFirstWay.getFare().getTotal().getPrice().getAmount()) + Float.parseFloat(flightDataSecondWay.getFare().getTotal().getPrice().getAmount()) + convenienceFee));
        }
    }

    protected void init() {
        flightDataFirstWay = (FlightData) getIntent().getSerializableExtra("flightDataFirstWay");
        if (getIntent().getSerializableExtra("flightDataSecondWay") != null)
            flightDataSecondWay = (FlightData) getIntent().getSerializableExtra("flightDataSecondWay");
        createTaxesData();
        convenienceFee = getIntent().getFloatExtra("convenienceFees", 0.0f);
        tvTitle = (TextView) findViewById(R.id.title);
        tvTotalBaseFare = (TextView) findViewById(R.id.tvBaseFare);
        llTaxes = (LinearLayout) findViewById(R.id.layout_taxes);
        tvTotalTax = (TextView) findViewById(R.id.tvTotalTaxes);
        tvNetPayable = (TextView) findViewById(R.id.tvNetpayble);
        tvFinish = (IconTextView) findViewById(R.id.tvFinish);
        tvFinish.setOnClickListener(this);
        btnFloating = (ImageView) findViewById(R.id.btn_flaoting);
        btnFloating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatingButtonMove floatingButtonMove = new FloatingButtonMove(getApplicationContext());
                floatingButtonMove.dragView(v, event);
                return true;
            }
        });
        if(Boolean.parseBoolean(getString(R.string.hideChatIcon))) {
            btnFloating.setVisibility(View.GONE);
        }
    }

    protected void createTaxesData() {
        listTaxesFirstWay = new ArrayList<>(flightDataFirstWay.getFare().getTaxes().values());
        Iterator<String> iterator = flightDataFirstWay.getFare().getTaxes().keySet().iterator();
        listTaxKeysFirstWay = new ArrayList<>();
        while (iterator.hasNext())
            listTaxKeysFirstWay.add(iterator.next());

        if (getIntent().getStringExtra("selected_tab") != null && !getIntent().getStringExtra("selected_tab").equals(Utils.TAB_GDS)) {
            if (null != getIntent().getSerializableExtra("flightDataSecondWay")) {
                iterator = flightDataSecondWay.getFare().getTaxes().keySet().iterator();
                listTaxKeysSecondWay = new ArrayList<>();
                while (iterator.hasNext())
                    listTaxKeysSecondWay.add(iterator.next());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvFinish) {
            finish();
        }
    }
}
