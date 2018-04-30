package com.rezofy.views.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.rezofy.R;
import com.rezofy.adapters.MyTripsAdapter;
import com.rezofy.adapters.PaymentMethodAdapter;
import com.rezofy.adapters.TicketAdapter;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.models.response_models.HotelTicketResponse;
import com.rezofy.models.response_models.HotelTrip;
import com.rezofy.models.response_models.PaymentMethodsResponse;
import com.rezofy.models.response_models.TripList;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by linchpin on 23/1/17.
 */


public class MyTripHotelDetailActivity extends AppCompatActivity implements View.OnClickListener, NetworkTask.Result {

    private IconTextView iTVMenu,iTVShareApp;

    private TextView tvTitle, tvPnr, tvHotel, tvAddr, tvCheckin, tvCheckout, tvDuration, tvPerson, tvRoom, tvName, tvMailId, tvTypeOfRoom, tvBaseFare,tvTaxes, tvTotal, tvPayText;
    private ImageView ivHotel;
    private LinearLayout llCancelStay, llPayStay;
    private  HotelTicketResponse response;
    private ImageView btnFloating;
    private RelativeLayout rlOverlay;
    View bottomSheet ;
    BottomSheetBehavior behavior;
    public String bookingRefNo;
    private int PAYMENT_METHODS = 2;
    private RecyclerView rvPaymentMethods;
    private final int request_code_webView = 1;
    private final int ID_CANCEL_TRIP = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_voucher);
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
        findViewById(R.id.header).setBackgroundColor(UIUtils.getThemeColor(this));
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

        initImageLoader(MyTripHotelDetailActivity.this);
        iTVMenu = (IconTextView) findViewById(R.id.header).findViewById(R.id.left_icon);
        iTVMenu.setText("k");
        iTVMenu.setTextSize(20);
        iTVMenu.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.header).findViewById(R.id.title);
        tvTitle.setText(getString(R.string.my_trips_hotel_voucher));

        iTVShareApp = (IconTextView) findViewById(R.id.header).findViewById(R.id.right_icon);
        iTVShareApp.setVisibility(View.VISIBLE);
        iTVShareApp.setOnClickListener(this);
        iTVShareApp.setText(getString(R.string.icon_text_I));
        iTVShareApp.setTextSize(20);

        tvPayText = (TextView)findViewById(R.id.tvPayTxt);
        tvHotel = (TextView)findViewById(R.id.tvHotel);
        tvPnr = (TextView)findViewById(R.id.tvPnr);
        tvAddr = (TextView)findViewById(R.id.tvAddr);
        tvCheckin = (TextView)findViewById(R.id.tvCheckin);
        tvCheckout = (TextView)findViewById(R.id.tvCheckout);
        tvDuration= (TextView)findViewById(R.id.tvDuration);
        tvPerson= (TextView)findViewById(R.id.tvPerson);
        tvRoom = (TextView)findViewById(R.id.tvRoom);
        tvName = (TextView)findViewById(R.id.tvName);
        tvMailId= (TextView)findViewById(R.id.tvMailId);
        tvTypeOfRoom= (TextView)findViewById(R.id.tvTypeOfRoom);
        tvBaseFare= (TextView)findViewById(R.id.tvBaseFare);
        tvTaxes= (TextView)findViewById(R.id.tvTaxes);
        tvTotal= (TextView)findViewById(R.id.tvTotal);

        ivHotel = (ImageView)findViewById(R.id.ivHotel);


        llCancelStay = (LinearLayout)findViewById(R.id.llCancelStay);
        llPayStay = (LinearLayout)findViewById(R.id.llPayStay);



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

    private void setDataToViews()
    {
        SimpleDateFormat sourceSdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat destSdf = new SimpleDateFormat("dd MMM yy");

        response = (HotelTicketResponse) getIntent().getSerializableExtra("view_ticket_response");

        if(response!=null)
        {
            llCancelStay.setOnClickListener(this);
            if(response.getTrip().getStatus().equalsIgnoreCase("UNPAID")) {
                llPayStay.setOnClickListener(this);
                tvPayText.setText(getString(R.string.pay_for_stay));
            }
            else
            {

                tvPayText.setText(getString(R.string.paid));
            }

            tvHotel.setText(response.getTrip().getSelectedResult().getName());
            tvAddr.setText(response.getTrip().getSelectedResult().getAddress());
            tvPnr.setText("PNR "+response.getTrip().getPnr());

            try {

                tvCheckin.setText(destSdf.format(sourceSdf.parse(response.getTrip().getStartDate())));
                tvCheckout.setText(destSdf.format(sourceSdf.parse(response.getTrip().getEndDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int personCount = 0;
            int roomCount = response.getTrip().getSelectedResult().getSelectedRooms().size();
            String category = "";
            if(response.getTrip().getSelectedResult().getSelectedRooms().size()>0)
            {
                category = response.getTrip().getSelectedResult().getSelectedRooms().get(0).getSelectedRoomInfo().getCategory();
                for(HotelTrip.SelectedRooms selectedRooms : response.getTrip().getSelectedResult().getSelectedRooms())
                {
                    personCount = personCount + selectedRooms.getSelectedRoomInfo().getNoOfAdults()+ selectedRooms.getSelectedRoomInfo().getNoOfChildren()+ selectedRooms.getSelectedRoomInfo().getNoOfInfant()+selectedRooms.getSelectedRoomInfo().getNoOfSeniors();
                }
            }


            long daydiff = 0;

            try {
                Date date1 = sourceSdf.parse(response.getTrip().getStartDate());
                Date date2 = sourceSdf.parse(response.getTrip().getEndDate());
                long diff = date2.getTime() - date1.getTime();
                Log.d("Difference" , ""+diff);
                daydiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                // System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvDuration.setText(daydiff+" DAYS, "+daydiff+" NIGHTS");
            if(personCount>1)
                tvPerson.setText(personCount+" PERSONS");
            else
                tvPerson.setText(personCount+" PERSON");

            if(response.getTrip().getSelectedResult().getSelectedRooms().size()>1)
                tvRoom.setText(response.getTrip().getSelectedResult().getSelectedRooms().size()+" ROOMS");
            else
                tvRoom.setText(response.getTrip().getSelectedResult().getSelectedRooms().size()+" ROOM");

            String name = "";
            for(HotelTrip.SelectedRooms room: response.getTrip().getSelectedResult().getSelectedRooms())
            {
                name = name+room.getSelectedRoomInfo().getGuests().get(0).getFirstName()+" "+room.getSelectedRoomInfo().getGuests().get(0).getLastName()+", ";
            }

            // tvName.setText(response.getTrip().getBookedByName());
            tvName.setText(name);
            tvMailId.setText(response.getTrip().getEmail_id());
            tvTypeOfRoom.setText(category);
            tvBaseFare.setText(response.getTrip().getBasePrice());
            tvTaxes.setText(response.getTrip().getTax());
            tvTotal.setText(response.getTrip().getGrandTotal());

            ImageLoader.getInstance().displayImage(response.getTrip().getSelectedResult().getImage(),ivHotel);
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
                            NetworkTask networkTask = new NetworkTask(MyTripHotelDetailActivity.this, ID_CANCEL_TRIP);
                            networkTask.setDialogMessage(getString(R.string.please_wait));
                            networkTask.exposePostExecute(MyTripHotelDetailActivity.this);
                            url = UIUtils.getBaseUrl(MyTripHotelDetailActivity.this) + WebServiceConstants.urlCancelTrip;
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

    public  void initImageLoader(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.hotel_bg)
                .showImageForEmptyUri(R.drawable.hotel_bg)
                .showImageOnFail(R.drawable.hotel_bg)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(options)
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
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
            Log.d("cancel trip", object);
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
