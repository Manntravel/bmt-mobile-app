package com.rezofy.views.activities;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.models.response_models.Fare;
import com.rezofy.models.response_models.FlightData;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.custom_views.IconTextView;

import java.util.ArrayList;
import java.util.Iterator;

public class UpdatedFareBreakUpActivity extends FareBreakUpActivity {

    private Fare fare, inboundFare;
    private Float convenienceFee;

    @Override
    protected void init() {
        fare = (Fare) getIntent().getSerializableExtra("fare");
        inboundFare = (Fare) getIntent().getSerializableExtra("inboundFare");
        convenienceFee = getIntent().getFloatExtra("convenienceFees", 0.0f);
        createTaxesData();
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
    }

    @Override
    protected void createTaxesData() {
        listTaxesFirstWay = new ArrayList<>(fare.getTaxes().values());
        Iterator<String> iterator = fare.getTaxes().keySet().iterator();
        listTaxKeysFirstWay = new ArrayList<>();
        while (iterator.hasNext())
            listTaxKeysFirstWay.add(iterator.next());

        if (inboundFare != null) {
            iterator = inboundFare.getTaxes().keySet().iterator();
            listTaxKeysSecondWay = new ArrayList<>();
            while (iterator.hasNext())
                listTaxKeysSecondWay.add(iterator.next());
        }
    }

    @Override
    protected void setFareValues() {
        if (listTaxKeysSecondWay == null) {
            tvTotalBaseFare.setText(UIUtils.getFareToDisplay(this, fare.getBase().getPrice().getAmount()));
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
            float netPayable = Float.parseFloat(fare.getTotal().getPrice().getAmount()) + convenienceFee;
            tvNetPayable.setText(UIUtils.getFareToDisplay(this, netPayable));
        } else {
            tvTotalBaseFare.setText(UIUtils.getFareToDisplay(this, Float.parseFloat(inboundFare.getBase().getPrice().getAmount()) + Float.parseFloat(fare.getBase().getPrice().getAmount())));
            for (int i = 0; i < listTaxKeysFirstWay.size(); i++) {
                if (listTaxKeysSecondWay.contains(listTaxKeysFirstWay.get(i)) && !listTaxKeysFirstWay.get(i).equals(Utils.KEY_PUBLISHED_PRICE_DIFF)) {
                    if (!listTaxKeysFirstWay.get(i).equalsIgnoreCase("totalTax")) {
                        View taxView = LayoutInflater.from(this).inflate(R.layout.layout_tax, null, false);
                        ((TextView) taxView.findViewById(R.id.tax_name)).setText(listTaxKeysFirstWay.get(i));
                        ((TextView) taxView.findViewById(R.id.tax_value)).setText(UIUtils.getFareToDisplay(this, Float.parseFloat(fare.getTaxes().get(listTaxKeysFirstWay.get(i)).getPrice().getAmount()) + Float.parseFloat(inboundFare.getTaxes().get(listTaxKeysFirstWay.get(i)).getPrice().getAmount())));
                        llTaxes.addView(taxView);
                    } else {
                        tvTotalTax.setText(UIUtils.getFareToDisplay(this, Float.parseFloat(fare.getTaxes().get(listTaxKeysFirstWay.get(i)).getPrice().getAmount()) + Float.parseFloat(fare.getTaxes().get(listTaxKeysSecondWay.get(i)).getPrice().getAmount())));
                    }
                }
            }
            tvNetPayable.setText(UIUtils.getFareToDisplay(this, Float.parseFloat(fare.getTotal().getPrice().getAmount()) + Float.parseFloat(inboundFare.getTotal().getPrice().getAmount()) + convenienceFee));
        }
    }
}
