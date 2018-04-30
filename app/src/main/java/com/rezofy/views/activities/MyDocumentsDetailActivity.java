package com.rezofy.views.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.azoft.carousellayoutmanager.DefaultChildSelectionListener;
import com.rezofy.R;
import com.rezofy.asynctasks.NetworkTask;

import com.rezofy.database.DbHelper;
//import com.rezofy.databinding.ItemViewBinding;
import com.rezofy.interfaces.ImageSelectedListener;
import com.rezofy.models.DocumentTypes;
import com.rezofy.models.MyDocument;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.views.custom_views.IconTextView;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Random;


/**
 * Created by linchpin on 9/2/17.
 */

public class MyDocumentsDetailActivity  extends AppCompatActivity implements View.OnClickListener,  NetworkTask.Result, ImageSelectedListener {

    private IconTextView iTVMenu;

    private TextView tvTitle, tvDocumentName;

    private ImageView btnFloating;

    private  ImageView ivDocumentImage;



    public String bookingRefNo;

    private ViewPager pager;






    public String type;
    List<MyDocument> list;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_document_detail);
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

    private void init()
    {

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            if(bundle.containsKey("type"))
            {
               Serializable object = bundle.getSerializable("type");
                if(object instanceof MyDocument) {
                    MyDocument document = (MyDocument)object;
                    type = document.getType();
                }
            }
        }

        iTVMenu = (IconTextView)findViewById(R.id.left_icon);
        iTVMenu.setText("k");
        iTVMenu.setTextSize(20);
        iTVMenu.setOnClickListener(this);
        tvTitle = (TextView)findViewById(R.id.title);
        tvTitle.setText(getString(R.string.my_document));
        ivDocumentImage = (ImageView)findViewById(R.id.ivDocumentImage);

        tvDocumentName = (TextView)findViewById(R.id.tvDocumentName);


        DbHelper helper = new DbHelper(this);
        list = helper.getAllDocument(type);


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


        final TestAdapter adapter = new TestAdapter(list);

        // create layout manager with needed params: vertical, cycle
        RecyclerView listHorizontal = (RecyclerView)findViewById(R.id.list_horizontal);
        initRecyclerView(listHorizontal, new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), adapter);
       // initRecyclerView(listHorizontal, new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, true), adapter);

        // fab button will add element to the end of the list
        /*binding.fabScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
*//*
                final int itemToRemove = adapter.mItemsCount;
                if (10 != itemToRemove) {
                    adapter.mItemsCount++;
                    adapter.notifyItemInserted(itemToRemove);
                }
*//*
                binding.listHorizontal.smoothScrollToPosition(adapter.getItemCount() - 2);
                binding.listVertical.smoothScrollToPosition(adapter.getItemCount() - 2);
            }
        });

        // fab button will remove element from the end of the list
        binding.fabChangeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
*//*
                final int itemToRemove = adapter.mItemsCount - 1;
                if (0 <= itemToRemove) {
                    adapter.mItemsCount--;
                    adapter.notifyItemRemoved(itemToRemove);
                }
*//*
                binding.listHorizontal.smoothScrollToPosition(1);
                binding.listVertical.smoothScrollToPosition(1);
            }
        });*/

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

    @Override
    public void onImageSelected(int position) {

        File imgFile = new File(list.get(position).getPath());

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());



            ivDocumentImage.setImageBitmap(myBitmap);
            tvDocumentName.setText(list.get(position).getName());

        }

    }



    private void initRecyclerView(final RecyclerView recyclerView, final CarouselLayoutManager layoutManager, final TestAdapter adapter) {
        // enable zoom effect. this line can be customized
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(2);

        recyclerView.setLayoutManager(layoutManager);
        // we expect only fixed sized item for now
        recyclerView.setHasFixedSize(true);
        // sample adapter with random data
        recyclerView.setAdapter(adapter);
        // enable center post scrolling
        recyclerView.addOnScrollListener(new CenterScrollListener());
        // enable center post touching on item and item click listener
        DefaultChildSelectionListener.initCenterItemListener(new DefaultChildSelectionListener.OnCenterItemClickListener() {
            @Override
            public void onCenterItemClicked(@NonNull final RecyclerView recyclerView, @NonNull final CarouselLayoutManager carouselLayoutManager, @NonNull final View v) {
                final int position = recyclerView.getChildLayoutPosition(v);
                final String msg = String.format(Locale.US, "Item %1$d was clicked", position);
                Toast.makeText(MyDocumentsDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        }, recyclerView, layoutManager);

        layoutManager.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {

            @Override
            public void onCenterItemChanged(final int adapterPosition) {
                if (CarouselLayoutManager.INVALID_POSITION != adapterPosition) {
                    onImageSelected(adapterPosition);
                  //  final int value = adapter.mPosition[adapterPosition];
/*
                    adapter.mPosition[adapterPosition] = (value % 10) + (value / 10 + 1) * 10;
                    adapter.notifyItemChanged(adapterPosition);
*/
                }
            }
        });
    }

    private static final class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {

        @SuppressWarnings("UnsecureRandomNumberGeneration")
       /* private final Random mRandom = new Random();
        private final int[] mColors;
        private final int[] mPosition;*/
        private int mItemsCount = 0;
        private List<MyDocument> list;

        TestAdapter(List<MyDocument> list) {
            this.list = list;
            this.mItemsCount = list.size();
            /*mColors = new int[mItemsCount];
            mPosition = new int[mItemsCount];
            for (int i = 0; mItemsCount > i; ++i) {
                //noinspection MagicNumber
                mColors[i] = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
                mPosition[i] = i;
            }*/
        }

        @Override
        public TestViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            //return new TestViewHolder(ItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
           return new TestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false));
        }

        @Override
        public void onBindViewHolder(final TestViewHolder holder, final int position) {
            File imgFile = new File(list.get(position).getPath());

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

               // holder.mItemViewBinding.ivDocumentImage.setImageBitmap(myBitmap);
                holder.ivDocumentImage.setImageBitmap(myBitmap);

            }


        }

        @Override
        public int getItemCount() {
            return mItemsCount;
        }
    }

    private static class TestViewHolder extends RecyclerView.ViewHolder {

        //private final ItemViewBinding mItemViewBinding;
        private final ImageView ivDocumentImage;

       /* TestViewHolder(final ItemViewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());

            mItemViewBinding = itemViewBinding;
        }*/
       public TestViewHolder(View itemView) {
           super(itemView);
           ivDocumentImage = (ImageView) itemView.findViewById(R.id.ivDocumentImage);
       }
    }


}
