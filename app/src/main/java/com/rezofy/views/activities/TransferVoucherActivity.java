package com.rezofy.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.adapters.PaymentMethodAdapter;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.databinding.ActivityTrainVoucherBinding;
import com.rezofy.databinding.ActivityTransferVoucherBinding;
import com.rezofy.databinding.CustomPaymentAlertBinding;
import com.rezofy.models.response_models.PaymentMethodsResponse;
import com.rezofy.models.response_models.TrainTicketResponse;
import com.rezofy.models.response_models.TransferTicketResponse;
import com.rezofy.models.response_models.ViewTicketResponse;
import com.rezofy.models.ui.TrainVoucher;
import com.rezofy.models.ui.TransferVoucher;
import com.rezofy.requests.Requests;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;

import java.text.SimpleDateFormat;

/**
 * Created by linchpin on 16/2/17.
 */

public class TransferVoucherActivity   extends AppCompatActivity implements View.OnClickListener,  NetworkTask.Result {


    private TransferTicketResponse response;

    //View bottomSheet ;
    BottomSheetBehavior behavior;
    public String bookingRefNo;
    private int PAYMENT_METHODS = 2;
    // private RecyclerView rvPaymentMethods;
    private final int request_code_webView = 1;
    private final int ID_CANCEL_TRIP = 3;

    private ActivityTransferVoucherBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transfer_voucher);
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
        binding.titleHeader.getRoot().setBackgroundColor(UIUtils.getThemeColor(this));
        binding.titleHeader.leftIcon.setTextColor(UIUtils.getThemeContrastColor(this));
        binding.titleHeader.title.setTextColor(UIUtils.getThemeContrastColor(this));
        binding.titleHeader.rightIcon.setTextColor(themeContrastColor);
    }

    private void init()
    {

        binding.rlOverlay.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        binding.rvPaymentMethods.setLayoutManager(layoutManager);

        binding.rvPaymentMethods.setHasFixedSize(true);

        binding.rvPaymentMethods.setItemAnimator(new DefaultItemAnimator());


        behavior = BottomSheetBehavior.from( binding.designBottomSheet);
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
                        binding.rlOverlay.setVisibility(View.VISIBLE);
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        binding.rlOverlay.setVisibility(View.GONE);
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

        binding.titleHeader.leftIcon.setText("k");
        binding.titleHeader.leftIcon.setTextSize(20);
        binding.titleHeader.leftIcon.setOnClickListener(this);

        binding.titleHeader.title.setText(getString(R.string.my_trips_transfer_voucher));



        binding.titleHeader.rightIcon.setVisibility(View.VISIBLE);
        binding.titleHeader.rightIcon.setOnClickListener(this);
        binding.titleHeader.rightIcon.setText(getString(R.string.icon_text_I));
        binding.titleHeader.rightIcon.setTextSize(20);




        binding.btnFlaoting.setOnTouchListener(new View.OnTouchListener() {
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
        response = (TransferTicketResponse) getIntent().getSerializableExtra("view_ticket_response");
        String name = "";
        if(response.getTrip().getTravellers()!=null&&response.getTrip().getTravellers().size()>0)
            name = response.getTrip().getTravellers().get(0).getFirstName();
        String taxStr = "";

        try
        {
            float base = Float.parseFloat( response.getTrip().getSelectedResult().getFare().getBase().getPrice().getAmount());
            float total = Float.parseFloat( response.getTrip().getSelectedResult().getFare().getTotal().getPrice().getAmount());
            taxStr = ""+(total-base);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        TransferVoucher transferVoucher = new TransferVoucher(response.getTrip().getBookingRefNo(),
                response.getTrip().getOrigin(),
                response.getTrip().getSelectedResult().getOutboundDateTime(),
                response.getTrip().getDestination(),
                response.getTrip().getPnr(),
                 name,
                response.getTrip().getSelectedResult().getTransferProvider(),
                response.getTrip().getContactphone(),
                response.getTrip().getSelectedResult().getOutboundDateTime(),
                response.getTrip().getInvoiceNo(),
                response.getTrip().getSelectedResult().getNoOfTravellers(),
                response.getTrip().getSelectedResult().getFare().getBase().getPrice().getAmount(),
                taxStr,
                response.getTrip().getSelectedResult().getFare().getTotal().getPrice().getAmount());
        binding.setData(transferVoucher);

        if(response.getTrip().getStatus().equalsIgnoreCase("UNPAID"))
            binding.llPayStay.setOnClickListener(this);


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



            url = UIUtils.getBaseUrl(this) + WebServiceConstants.urlPaymentMethods;
            paramsArray = new String[]{url, null};
            networkTask.execute(paramsArray);
        }

    }
    public void showPaymentMethods(String bookingRefNo)
    {
        this.bookingRefNo = bookingRefNo;
        final int initState = BottomSheetBehavior.STATE_EXPANDED;
        binding.designBottomSheet.post(new Runnable() {
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
        binding.designBottomSheet.post(new Runnable() {
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

        if(view == binding.llCancelStay)
        {
            Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.txt_booking_cancel), null, getString(R.string.text_ok), getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            String url;
                            NetworkTask networkTask = new NetworkTask(TransferVoucherActivity.this, ID_CANCEL_TRIP);
                            networkTask.setDialogMessage(getString(R.string.please_wait));
                            networkTask.exposePostExecute(TransferVoucherActivity.this);
                            url = UIUtils.getBaseUrl(TransferVoucherActivity.this) + WebServiceConstants.urlCancelTrip;
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
        else if(view == binding.llPayStay)
        {
            AlertDialog.Builder builder;
            final AlertDialog alertDialog;


            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            CustomPaymentAlertBinding alertBinding = CustomPaymentAlertBinding.inflate(inflater);

            String baseFareString= response.getTrip().getSelectedResult().getFare().getBase().getPrice().getAmount();

            String totalString =  response.getTrip().getSelectedResult().getFare().getTotal().getPrice().getAmount();

            String taxStr = "";

            try
            {
                float base = Float.parseFloat( response.getTrip().getSelectedResult().getFare().getBase().getPrice().getAmount());
                float total = Float.parseFloat( response.getTrip().getSelectedResult().getFare().getTotal().getPrice().getAmount());
                taxStr = ""+(total-base);
            }catch (Exception e)
            {
                e.printStackTrace();
            }




            alertBinding.tvBaseFare.setText(baseFareString);
            alertBinding.tvTaxes.setText(taxStr);
            alertBinding.tvTotal.setText(totalString);





            builder = new AlertDialog.Builder(this);
            builder.setView(alertBinding.getRoot());
            alertDialog = builder.create();

            alertBinding.tvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(alertDialog!=null&&alertDialog.isShowing())
                        alertDialog.dismiss();
                }
            });

            alertBinding.rlPayNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(alertDialog!=null&&alertDialog.isShowing()) {
                        alertDialog.dismiss();
                        showPaymentMethods(response.getTrip().getBookingRefNo());
                    }
                }
            });

            alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
            alertDialog.show();
        }
        else if(view == binding.titleHeader.leftIcon)
        {

            finish();

        }
        else if( view == binding.titleHeader.rightIcon) {
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
                binding.rvPaymentMethods.setAdapter(new PaymentMethodAdapter(this, response.getGateways(), binding.rvPaymentMethods));
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
