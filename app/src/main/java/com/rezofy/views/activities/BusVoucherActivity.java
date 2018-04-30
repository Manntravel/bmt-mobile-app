package com.rezofy.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.adapters.PaymentMethodAdapter;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.models.response_models.BusTicketResponse;
import com.rezofy.models.response_models.BusTrip;
import com.rezofy.models.response_models.HotelTicketResponse;
import com.rezofy.models.response_models.PaymentMethodsResponse;
import com.rezofy.models.response_models.ViewTicketResponse;
import com.rezofy.requests.Requests;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.custom_views.IconTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by linchpin on 25/1/17.
 */

public class BusVoucherActivity extends AppCompatActivity implements View.OnClickListener,  NetworkTask.Result {

    private IconTextView iTVMenu, iTVShareApp;

    private TextView tvTitle;
    private TextView tvOneSource, tvOneDestination, tvOneDate, tvOneBoardingPoint, tvOneBoard, tvOneSeats, tvOneTicket, tvOneOperator, tvOneOperatorAddr;

    private TextView tvTwoSource, tvTwoDestination, tvTwoDate, tvTwoBoardingPoint, tvTwoBoard, tvTwoSeats, tvTwoTicket, tvTwoOperator, tvTwoOperatorAddr;

    private TextView tvPassengers, tvPnr, tvPayAmt, tvPayText;

    private CardView cardOne, cardTwo, cardPassenger;
    private LinearLayout llCancelStay, llPayStay;
    private RelativeLayout rlOverlay;
    private ImageView btnFloating;
    BusTicketResponse response;

    View bottomSheet ;
    BottomSheetBehavior behavior;
    public String bookingRefNo;
    private int PAYMENT_METHODS = 2;
    private RecyclerView rvPaymentMethods;
    private final int request_code_webView = 1;
    private final int ID_CANCEL_TRIP = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_voucher);
        init();
        setProperties();
        setDataToViews();
        getPaymentMethods();
    }

    @Override
    public void onBackPressed() {

        if(isPaymentMethodsShow())
            hidePaymentMethods();
        else
            super.onBackPressed();
    }

    private void setProperties() {
        int themeContrastColor = UIUtils.getThemeContrastColor(this);
        findViewById(R.id.titleHeader).setBackgroundColor(UIUtils.getThemeColor(this));
        iTVMenu.setTextColor(UIUtils.getThemeContrastColor(this));
        tvTitle.setTextColor(UIUtils.getThemeContrastColor(this));
        iTVShareApp.setTextColor(themeContrastColor);
    }

    private void init()
    {


        rlOverlay = (RelativeLayout) findViewById(R.id.rlOverlay);
        rlOverlay.setOnClickListener(this);
        rvPaymentMethods = (RecyclerView)findViewById(R.id.rvPaymentMethods);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        rvPaymentMethods.setLayoutManager(layoutManager);
        //rvPaymentMethods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvPaymentMethods.setHasFixedSize(true);

        rvPaymentMethods.setItemAnimator(new DefaultItemAnimator());

        bottomSheet = findViewById(R.id.design_bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        rlOverlay.setVisibility(View.VISIBLE);
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        rlOverlay.setVisibility(View.GONE);
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_HIDDEN");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
            }
        });
        iTVMenu = (IconTextView)findViewById(R.id.left_icon);
        iTVMenu.setText("k");
        iTVMenu.setTextSize(20);
        iTVMenu.setOnClickListener(this);
        tvTitle = (TextView)findViewById(R.id.title);
        tvTitle.setText(getString(R.string.my_trips_bus_voucher));


        iTVShareApp = (IconTextView) findViewById(R.id.titleHeader).findViewById(R.id.right_icon);
        iTVShareApp.setVisibility(View.VISIBLE);
        iTVShareApp.setOnClickListener(this);
        iTVShareApp.setText(getString(R.string.icon_text_I));
        iTVShareApp.setTextSize(20);

        tvOneSource = (TextView) findViewById(R.id.tvOneSource);
        tvOneDestination = (TextView) findViewById(R.id.tvOneDestination);
        tvOneDate  = (TextView) findViewById(R.id.tvOneDate);
        tvOneBoardingPoint  = (TextView) findViewById(R.id.tvOneBoardingPoint);
        tvOneBoard = (TextView) findViewById(R.id.tvOneBoard);
        tvOneSeats = (TextView) findViewById(R.id.tvOneSeats);
        tvOneTicket = (TextView) findViewById(R.id.tvOneTicketNo);
        tvOneOperator = (TextView) findViewById(R.id.tvOneOperator);
        tvOneOperatorAddr = (TextView) findViewById(R.id.tvOneOperatorAddr);

        tvTwoSource = (TextView) findViewById(R.id.tvTwoSource);
        tvTwoDestination = (TextView) findViewById(R.id.tvTwoDestination);
        tvTwoDate = (TextView) findViewById(R.id.tvTwoDate);
        tvTwoBoardingPoint = (TextView) findViewById(R.id.tvTwoBoardingPoint);
        tvTwoBoard = (TextView) findViewById(R.id.tvTwoBoard);
        tvTwoSeats = (TextView) findViewById(R.id.tvTwoSeats);
        tvTwoTicket = (TextView) findViewById(R.id.tvTwoTicketNo);
        tvTwoOperator = (TextView) findViewById(R.id.tvTwoOperator);
        tvTwoOperatorAddr = (TextView) findViewById(R.id.tvTwoOperatorAddr);

        tvPnr = (TextView)findViewById(R.id.tvPnr);
        tvPayAmt = (TextView)findViewById(R.id.tvPayAmt);
        tvPayText = (TextView)findViewById(R.id.tvPayTxt);

        tvPassengers = (TextView)findViewById(R.id.tvPassengers);

        cardOne = (CardView)findViewById(R.id.card_one);
        cardTwo = (CardView)findViewById(R.id.card_two);
        cardPassenger = (CardView)findViewById(R.id.card_passenger);

        llCancelStay = (LinearLayout)findViewById(R.id.llCancelStay);
        llPayStay = (LinearLayout)findViewById(R.id.llPayStay);


        btnFloating = (ImageView) findViewById(R.id.btn_flaoting);
        if(Boolean.parseBoolean(getString(R.string.hideChatIcon))) {
            btnFloating.setVisibility(View.GONE);
        }
        btnFloating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatingButtonMove floatingButtonMove = new FloatingButtonMove(getApplicationContext());
                floatingButtonMove.dragView(v, event);
                return true;
            }
        });



    }

    private void setDataToViews()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        SimpleDateFormat destSdf = new SimpleDateFormat("hh:mm a");
        response = (BusTicketResponse) getIntent().getSerializableExtra("view_ticket_response");
        if(response!=null)
        {
            llCancelStay.setOnClickListener(this);
            if(response.getTrip().getStatus().equalsIgnoreCase("UNPAID")) {
                llPayStay.setOnClickListener(this);
                tvPayText.setText(getString(R.string.payable_amt));
            }
            else
            {

                tvPayText.setText(getString(R.string.paid));
            }
        }
        if(response!=null && response.getTrip().getSelectedResult().getSegments().size()>1)
        {


            tvOneSource.setText(response.getTrip().getOrigin());
            tvOneDestination.setText(response.getTrip().getDestination());
            tvOneDate.setText(response.getTrip().getSelectedResult().getSegments().get(0).getDepartDate().getDateWithoutTime());
            tvOneBoardingPoint.setText(response.getTrip().getSelectedResult().getSegments().get(0).getDepartPoint().getAddress());
            String boardTimeOne = "";
            try {
                Date boardDate = sdf.parse(response.getTrip().getSelectedResult().getSegments().get(0).getDepartDate().getTime());
                boardTimeOne = destSdf.format(boardDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvOneBoard.setText(boardTimeOne);


            String seats= "";
            for(BusTrip.SelectedSeats selectedSeats: response.getTrip().getSelectedResult().getSegments().get(0).getSelectedSeats())
            {
                seats = seats+selectedSeats.getNumber();
            }

            tvOneSeats.setText(seats);
            tvOneTicket.setText(response.getTrip().getTicketNumbers());

            tvOneOperator.setText(response.getTrip().getSelectedResult().getSegments().get(0).getOperator().getName());
            tvOneOperatorAddr.setText(response.getTrip().getSelectedResult().getSegments().get(0).getOperator().getAddressLine1());

            String passenger = "";
            for(BusTrip.Traveller traveller: response.getTrip().getTravellers())
            {
                passenger = passenger+traveller.getFirstName()+" "+traveller.getLastName()+"\n";
            }
            tvPassengers.setText(passenger);

            tvTwoSource.setText(response.getTrip().getOrigin());
            tvTwoDestination.setText(response.getTrip().getDestination());
            tvTwoDate.setText(response.getTrip().getSelectedResult().getSegments().get(1).getDepartDate().getDateWithoutTime());
            tvTwoBoardingPoint.setText(response.getTrip().getSelectedResult().getSegments().get(1).getDepartPoint().getAddress());
            String boardTimeTwo = "";
            try {
                Date boardDate = sdf.parse(response.getTrip().getSelectedResult().getSegments().get(1).getDepartDate().getTime());
                boardTimeTwo = destSdf.format(boardDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvTwoBoard.setText(boardTimeTwo);
            String Twoseats= "";
            for(BusTrip.SelectedSeats selectedSeats: response.getTrip().getSelectedResult().getSegments().get(1).getSelectedSeats())
            {
                Twoseats = Twoseats+selectedSeats.getNumber();
            }

            tvTwoSeats.setText(Twoseats);
            tvTwoTicket.setText(response.getTrip().getTicketNumbers());

            tvTwoOperator.setText(response.getTrip().getSelectedResult().getSegments().get(1).getOperator().getName());
            tvTwoOperatorAddr.setText(response.getTrip().getSelectedResult().getSegments().get(1).getOperator().getAddressLine1());
            tvPnr.setText(response.getTrip().getPnr());
            tvPayAmt.setText(response.getTrip().getGrandTotal());


        }
        else if(response!=null && response.getTrip().getSelectedResult().getSegments().size()==1)
        {
            cardTwo.setVisibility(View.GONE);
            tvOneSource.setText(response.getTrip().getOrigin());
            tvOneDestination.setText(response.getTrip().getDestination());
            tvOneDate.setText(response.getTrip().getSelectedResult().getSegments().get(0).getDepartDate().getDateWithoutTime());
            tvOneBoardingPoint.setText(response.getTrip().getSelectedResult().getSegments().get(0).getDepartPoint().getAddress());
            String boardTimeOne = "";
            try {
                Date boardDate = sdf.parse(response.getTrip().getSelectedResult().getSegments().get(0).getDepartDate().getTime());
                boardTimeOne = destSdf.format(boardDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvOneBoard.setText(boardTimeOne);


           // tvOneBoard.setText(response.getTrip().getSelectedResult().getSegments().get(0).getDepartDate().getTime());
            String seats= "";
            for(BusTrip.SelectedSeats selectedSeats: response.getTrip().getSelectedResult().getSegments().get(0).getSelectedSeats())
            {
                seats = seats+selectedSeats.getNumber();
            }

            tvOneSeats.setText(seats);
            tvOneTicket.setText(response.getTrip().getTicketNumbers());

            tvOneOperator.setText(response.getTrip().getSelectedResult().getSegments().get(0).getOperator().getName());
            tvOneOperatorAddr.setText(response.getTrip().getSelectedResult().getSegments().get(0).getOperator().getAddressLine1());

            String passenger = "";
            for(BusTrip.Traveller traveller: response.getTrip().getTravellers())
            {
                passenger = passenger+traveller.getFirstName()+" "+traveller.getLastName()+"\n";
            }
            tvPassengers.setText(passenger);
            tvPnr.setText(response.getTrip().getPnr());
            tvPayAmt.setText(response.getTrip().getGrandTotal());

        }
    }

    private void getPaymentMethods() {
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else {
            String url;

            NetworkTask networkTask = new NetworkTask(this, PAYMENT_METHODS);
            networkTask.setDialogMessage(getString(R.string.please_wait));
            networkTask.exposePostExecute(this);
            String paramsArray[] = null;
            //url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlTripRecord + pageNo;


            url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlPaymentMethods;
            paramsArray = new String[]{url, null};
            networkTask.execute(paramsArray);
        }

    }
    public void showPaymentMethods(String bookingRefNo)
    {
        this.bookingRefNo = bookingRefNo;
        final int initState = BottomSheetBehavior.STATE_EXPANDED;
        bottomSheet.post(new Runnable() {
            @Override
            public void run() {
                behavior.setState(initState);
            }
        });

    }
    public void hidePaymentMethods()
    {
        this.bookingRefNo = "";
        final int initState = BottomSheetBehavior.STATE_COLLAPSED;
        bottomSheet.post(new Runnable() {
            @Override
            public void run() {
                behavior.setState(initState);
            }
        });
    }
    public boolean isPaymentMethodsShow()
    {
        if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
        {
            return  true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onClick(View view) {

        if(view == llCancelStay)
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



            /*TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("Hello, this is a custom dialog!");
            ImageView image = (ImageView) layout.findViewById(R.id.image);
            image.setImageResource(R.drawable.android);*/

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
        else if(view == iTVMenu)
        {

            finish();

        }
        else if( view == iTVShareApp) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_share_app));
            startActivity(Intent.createChooser(shareIntent, getString(R.string.text_share)));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_code_webView && resultCode == RESULT_OK) {
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
        }
    }

    private void viewTicket(String object) {
        ViewTicketResponse viewTicketResponse = new Gson().fromJson(object, ViewTicketResponse.class);
        Intent intent = new Intent(this, ViewTicketActivity.class);
        Bundle bundle = getIntent().getExtras();
        bundle.putSerializable("view_ticket_response", viewTicketResponse);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {

        if(object != null && id == PAYMENT_METHODS)
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
        }

    }
}
