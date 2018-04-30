package com.rezofy.views.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.adapters.TicketAdapter;

public class TripDetailsActivity extends ViewTicketActivity {

    @Override
    protected void specialInit() {
        iTVMenu.setText(getString(R.string.icon_text_k));
        iTVMenu.setOnClickListener(this);
        tvTitle.setText(getString(R.string.my_trips));
        Bundle bundle = getIntent().getExtras();
        bundle.putSerializable("viewTicketResponse", viewTicketResponse);
        getIntent().putExtras(bundle);
        ticketAdapter = new TicketAdapter(this, viewTicketResponse, true);
        recyclerView.setAdapter(ticketAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_icon:
                setResult(ticketAdapter.getResultCode());
                finish();
                break;

            case R.id.right_icon:
                super.onClick(v);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(ticketAdapter.getResultCode());
        finish();
    }
}
