package com.rezofy.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.rezofy.R;
import com.rezofy.adapters.AddDocumentAdapter;
import com.rezofy.database.DbHelper;
import com.rezofy.models.DocumentTypes;
import com.rezofy.models.MyDocument;
import com.rezofy.utils.FloatingButtonMove;
import com.rezofy.utils.UIUtils;
import com.rezofy.views.custom_views.IconTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by linchpin on 7/2/17.
 */

public class AddDocumentActivity  extends AppCompatActivity implements View.OnClickListener {

    private IconTextView iTVMenu;

    private TextView tvTitle, tvEdit;
    private RecyclerView rvDocumentTypes;
    private ImageView btnFloating;
    private EditText etDocumentName;
    private TableRow trDone;
    private AddDocumentAdapter adapter;
    private String  selectedType = "";
    private int icon;
    private String path = "";



    final int REQUEST_ADD_DOCUMENT = 103;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);
        init();
        setProperties();
        setDataToViews();

    }

    public void setSelectedType(String selectedType, int icon )
    {
        this.selectedType = selectedType;
        this.icon = icon;
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

        rvDocumentTypes = (RecyclerView)findViewById(R.id.rvDocumentTypes);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        rvDocumentTypes.setLayoutManager(layoutManager);
        //rvPaymentMethods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvDocumentTypes.setHasFixedSize(true);

        rvDocumentTypes.setItemAnimator(new DefaultItemAnimator());


        iTVMenu = (IconTextView)findViewById(R.id.left_icon);
        iTVMenu.setText("k");
        iTVMenu.setTextSize(20);
        iTVMenu.setOnClickListener(this);
        tvTitle = (TextView)findViewById(R.id.title);
        tvTitle.setText(getString(R.string.my_document));

        tvEdit = (TextView)findViewById(R.id.tvEdit) ;
        tvEdit.setOnClickListener(this);
        etDocumentName = (EditText)findViewById(R.id.etDocumentName);
        trDone = (TableRow)findViewById(R.id.trDone);
        trDone.setOnClickListener(this);



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


    public void setDataToViews()
    {

        Bundle bundle= getIntent().getExtras();
        if(bundle!=null)
        {
            if(bundle.containsKey("path"))
            {

                path = bundle.getString("path");
            }
        }
        List<DocumentTypes> types = new ArrayList<>();

        String[] typeArray = getResources().getStringArray(R.array.array_document_type);
        TypedArray imgArray;
        imgArray = getResources().obtainTypedArray(R.array.array_document_type_image);



        for(int i=0;i<typeArray.length;i++)
        {
            //String type, int icon, boolean checked
            DocumentTypes t = new DocumentTypes(typeArray[i],
                    imgArray.getResourceId(i,R.drawable.passport),
                    false);
            types.add(t);
        }

        adapter = new AddDocumentAdapter(this,types,rvDocumentTypes);
        rvDocumentTypes.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {

         if(view == iTVMenu)
        {

            finish();

        }
        else if(view == trDone)
         {
             if(!path.equals("")&&!selectedType.equals("")&&etDocumentName.getText().length()>0) {
                 DbHelper helper = new DbHelper(this);
                 //String name, String date, String type, String path, int icon
                 SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm"); //"yyyy-MM-dd HH:mm:ss");"dd/MM/yyyy hh:mma"
                 Calendar calendar = Calendar.getInstance();
                 String date = df.format(calendar.getTime());
                 helper.insertDocument(new MyDocument(etDocumentName.getText().toString(), date, selectedType, path));
                 setResult(Activity.RESULT_OK,new Intent());
                 finish();
             }
             else
             {
                 Toast.makeText(this, "some fields missing", Toast.LENGTH_LONG).show();
             }
         }
        else if(view == tvEdit )
         {
             InputMethodManager inputMethodManager =
                     (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
             inputMethodManager.toggleSoftInputFromWindow(
                     tvEdit.getApplicationWindowToken(),
                     InputMethodManager.SHOW_FORCED, 0);
         }
    }





}
