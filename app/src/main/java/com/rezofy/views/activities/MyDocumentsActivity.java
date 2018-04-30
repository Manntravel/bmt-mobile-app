package com.rezofy.views.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.adapters.MyDocumentsListAdapter;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.database.DbHelper;
import com.rezofy.models.DocumentTypes;
import com.rezofy.models.MyDocument;
import com.rezofy.utils.UIUtils;
import com.rezofy.views.custom_views.IconTextView;
import com.rezofy.views.fragments.AddDocumentDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;



/**
 * Created by linchpin on 6/2/17.
 */

public class MyDocumentsActivity extends AppCompatActivity implements View.OnClickListener, NetworkTask.Result {


    private TextView tvTitle, tvAdd;
    private IconTextView iTVMenu;
    //private EditText etSearch;
    private RecyclerView rvMyDocuments;
    private RelativeLayout rlBelowToolBar;
    private String type = "";
    MyDocumentsListAdapter adapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_documents);
        init();
        setProperties();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setDataToViews();

    }

    private void setDataToViews()
    {

        // initialize list
        DbHelper helper = DbHelper.getInstance(this);
        List<MyDocument> list = helper.getAllDocument(type);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat destSdf = new SimpleDateFormat("dd/MM/yyyy hh:mma");
        for(MyDocument document: list)
        {
            try {
                document.setCreatedOn(destSdf.format(sdf.parse(document.getCreatedOn())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        adapter = new MyDocumentsListAdapter(this,list,rvMyDocuments);
        rvMyDocuments.setAdapter(adapter);

    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_trip, menu);
        // Associate searchable configuration with the SearchViewn
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        MenuItem searchMenu = menu.findItem(R.id.search);

        SearchView searchView =
                (SearchView) searchMenu.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        MenuItemCompat.setOnActionExpandListener(searchMenu,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        // Return true to allow the action view to expand


                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        // When the action view is collapsed, reset the query


                        // Return true to allow the action view to collapse
                        return true;
                    }
                });
        return true;
    }*/


    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }

    private void setProperties() {
        findViewById(R.id.header).setBackgroundColor(UIUtils.getThemeColor(this));
        rlBelowToolBar.setBackgroundColor(UIUtils.getThemeColor(this));

        iTVMenu.setTextColor(UIUtils.getThemeContrastColor(this));
        tvTitle.setTextColor(UIUtils.getThemeContrastColor(this));
    }

    private void init() {

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            if(bundle.containsKey("type"))
            {
                DocumentTypes t = (DocumentTypes)bundle.getSerializable("type");
                type = t.getType();
            }
        }
       /* etSearch = (EditText) findViewById(R.id.etSearch);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //performSearch();
                    return true;
                }
                return false;
            }
        });*/

        iTVMenu = (IconTextView) findViewById(R.id.header).findViewById(R.id.left_icon);
        iTVMenu.setText("k");
        iTVMenu.setTextSize(20);
        iTVMenu.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.header).findViewById(R.id.title);
        tvTitle.setText(getString(R.string.my_document));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(false); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        // shape.setCornerRadii(new float[] { 8, 8, 8, 8, 0, 0, 0, 0 });
        shape.setColor(UIUtils.getThemeColor(this));
        // shape.setStroke(3, borderColor);
        ab.setBackgroundDrawable(shape);

        rlBelowToolBar = (RelativeLayout)findViewById(R.id.rlBelowToolBar);
        tvAdd = (TextView)findViewById(R.id.tvAdd);
        tvAdd.setOnClickListener(this);
        rvMyDocuments = (RecyclerView)findViewById(R.id.rvMyDocuments);
        rvMyDocuments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvMyDocuments.setHasFixedSize(true);

        rvMyDocuments.setItemAnimator(new DefaultItemAnimator());


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_icon:
                finish();
                break;
            case R.id.tvAdd:
               showAddDocumentDialog();
                break;


        }
    }

    public void showAddDocumentDialog()
    {
       new AddDocumentDialogFragment().show(getSupportFragmentManager(), "ADD DIALOG");
    }


    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {


    }




    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

            if (query != null && query.length() > 0) {
               /* SearchListener listener = getSearchListener();
                if(listener!=null)
                    listener.searchFromList(query);*/

            }
        }
    }


}
