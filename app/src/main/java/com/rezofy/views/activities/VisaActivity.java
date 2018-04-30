package com.rezofy.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.adapters.PaymentMethodAdapter;
import com.rezofy.asynctasks.NetworkTask;

import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;

import com.rezofy.views.custom_views.IconTextView;



/**
 * Created by linchpin on 9/2/17.
 */

public class VisaActivity  extends AppCompatActivity implements View.OnClickListener,  NetworkTask.Result {

    private IconTextView iTVMenu;

    private TextView tvTitle;

    private ImageView btnFloating;

    public String bookingRefNo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa);
        init();
        setProperties();

    }

    @Override
    public void onBackPressed() {

          super.onBackPressed();
    }

    private void setProperties() {
        findViewById(R.id.titleHeader).setBackgroundColor(UIUtils.getThemeColor(this));
        iTVMenu.setTextColor(UIUtils.getThemeContrastColor(this));
        tvTitle.setTextColor(UIUtils.getThemeContrastColor(this));
    }


    public void showMessage(View v)
    {
        Toast.makeText(this, "Comming Soon...", Toast.LENGTH_SHORT).show();
    }
    private void init()
    {



        iTVMenu = (IconTextView)findViewById(R.id.left_icon);
        iTVMenu.setText("k");
        iTVMenu.setTextSize(20);
        iTVMenu.setOnClickListener(this);
        tvTitle = (TextView)findViewById(R.id.title);
        tvTitle.setText(getString(R.string.visa_info));





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





    @Override
    public void onClick(View view) {

       /* if(view == llCancelStay)
        {
            Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.txt_booking_cancel), null, getString(R.string.text_ok), getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            String url;
                            NetworkTask networkTask = new NetworkTask(BusVoucherActivity.this, ID_CANCEL_TRIP);
                            networkTask.setDialogMessage(getString(R.string.please_wait));
                            networkTask.exposePostExecute(BusVoucherActivity.this);
                            url = UIUtils.getBaseUrl(BusVoucherActivity.this) + WebServiceConstants.urlCancelTrip;
                            String paramsArray[] = new String[]{url, Requests.cancelTripRequest(response.getTrip().getBookingRefNo())};
                            networkTask.execute(paramsArray);

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //((AppCompatActivity) activity).onBackPressed();
                            break;
                    }
                }
            });
        }
        else if(view == llPayStay)
        {
            AlertDialog.Builder builder;
            final AlertDialog alertDialog;


            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.custom_payment_alert,
                    (ViewGroup) findViewById(R.id.root));
            TextView tvBaseFare = (TextView)layout.findViewById(R.id.tvBaseFare);

            TextView tvTaxes = (TextView)layout.findViewById(R.id.tvTaxes);

            TextView tvTotal = (TextView)layout.findViewById(R.id.tvTotal);

            TextView tvClose = (TextView)layout.findViewById(R.id.tvClose);

            RelativeLayout rlPayNow = (RelativeLayout)layout.findViewById(R.id.rlPayNow);
            tvBaseFare.setText(response.getTrip().getBasePrice());
            tvTaxes.setText(response.getTrip().getTax());
            tvTotal.setText(response.getTrip().getGrandTotal());



            *//*TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("Hello, this is a custom dialog!");
            ImageView image = (ImageView) layout.findViewById(R.id.image);
            image.setImageResource(R.drawable.android);*//*

            builder = new AlertDialog.Builder(this);
            builder.setView(layout);
            alertDialog = builder.create();

            tvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(alertDialog!=null&&alertDialog.isShowing())
                        alertDialog.dismiss();
                }
            });

            rlPayNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(alertDialog!=null&&alertDialog.isShowing()) {
                        alertDialog.dismiss();
                        showPaymentMethods(response.getTrip().getBookingRefNo());
                    }
                }
            });
            // alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
            alertDialog.show();
        }
        else*/ if(view == iTVMenu)
        {

            finish();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == request_code_webView && resultCode == RESULT_OK) {
            String status = data.getStringExtra("STATUS");
            if (status.equals("true")) {
                //hit ticket request
                String tripJson = Utils.getTripJsonFromCreditCardSuccesJson();
                viewTicket(tripJson);
                // getTicket();
            } else {
                //show error dialog
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
                alert.setMessage(Utils.CreditCardFailureMsg);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        }*/
    }

   /* private void viewTicket(String object) {
        ViewTicketResponse viewTicketResponse = new Gson().fromJson(object, ViewTicketResponse.class);
        Intent intent = new Intent(this, ViewTicketActivity.class);
        Bundle bundle = getIntent().getExtras();
        bundle.putSerializable("view_ticket_response", viewTicketResponse);
        intent.putExtras(bundle);
        startActivity(intent);
    }*/

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {

       /* if(object != null && id == PAYMENT_METHODS)
        {
            PaymentMethodsResponse response = new Gson().fromJson(object,PaymentMethodsResponse.class);

            if(response!=null&& response.getGateways()!=null)
            {
                rvPaymentMethods.setAdapter(new PaymentMethodAdapter(this, response.getGateways(), rvPaymentMethods));
            }


        }
        else if(object != null && id == ID_CANCEL_TRIP)
        {


            try {
                ViewTicketResponse viewTicketResponse = new Gson().fromJson(object, ViewTicketResponse.class);
                if (viewTicketResponse.getTrip().getCancellationStatus().equals(Utils.TICKET_STATUS_CANCEL_REQUESTED)) {
                    Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.msg_cancellation_request_received), getString(R.string.ok), null, null, null);

                }

            } catch (Exception e) {
                Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.msg_cancellation_failure), getString(R.string.ok), null, null, null);
            }
        }*/

    }
}
