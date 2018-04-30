package com.rezofy.views.activities;

import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rezofy.R;
import com.rezofy.adapters.MyDocumentTypeAdapter;
import com.rezofy.asynctasks.NetworkTask;
import com.rezofy.database.DbHelper;
import com.rezofy.models.DocumentTypes;
import com.rezofy.models.MyDocument;
import com.rezofy.models.response_models.UserDocumentList;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.utils.WebServiceConstants;
import com.rezofy.views.custom_views.IconTextView;
import com.rezofy.views.fragments.AddDocumentDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linchpin on 8/2/17.
 */

public class MyDocumentTypeActivity extends AppCompatActivity implements View.OnClickListener, NetworkTask.Result {


    private static final int DOCUMENT_LIST = 0;
    private TextView tvTitle, tvAdd;
    private IconTextView iTVMenu;
    private EditText etSearch;
    private RecyclerView rvMyDocuments;
    private RelativeLayout rlBelowToolBar;
    private MyDocumentTypeAdapter adapter;

    private String[] typeArray;
    TypedArray imgArray;
    DownloadManager downloadManager;
    private MyDocument myDocument;
    private List<MyDocument> downloadlist;
    private int downloadDocNo;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_documents);
        init();
        setProperties();
        setDataToViews();
        getDocument();
       // new CurrencyConverterDialog().show(getSupportFragmentManager(),"CurrencyDialog");
       // new FlightTrackerDialog().show(getSupportFragmentManager(),"FlightTracker");

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setDataToViews()
    {

        List<DocumentTypes> types = getDocumentTypeList();

        adapter = new MyDocumentTypeAdapter(this,types,rvMyDocuments);
        rvMyDocuments.setAdapter(adapter);
        // initialize list
       /* DbHelper helper = new DbHelper(this);
        List<MyDocument> list = helper.getAllDocument();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat destSdf = new SimpleDateFormat("dd/MM/yyyy hh:mma");
        for(MyDocument document: list)
        {
            try {
                document.setDate(destSdf.format(sdf.parse(document.getDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        MyDocumentsListAdapter adapter = new MyDocumentsListAdapter(this,list,rvMyDocuments);
        rvMyDocuments.setAdapter(adapter);*/

    }

    private List<DocumentTypes> getDocumentTypeList() {

        List<DocumentTypes> types = new ArrayList<>();

        for(int i=0;i<typeArray.length;i++)
        {
            //String type, int icon, boolean checked
            DbHelper helper = DbHelper.getInstance(this);
            int count = helper.getDocumentCount(typeArray[i]);
            DocumentTypes t = new DocumentTypes(typeArray[i],
                    imgArray.getResourceId(i,R.drawable.passport),
                    false);
            t.setCount(count);
            types.add(t);
        }

        return types;
    }

    private void getDocument() {
        getDocumentFormServer();
    }

    public void getDocumentFormServer() {
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else {
                String url;
                NetworkTask networkTask = new NetworkTask(this, DOCUMENT_LIST);
                networkTask.setDialogMessage(getString(R.string.please_wait));
                networkTask.exposePostExecute(this);
                String paramsArray[] = null;
                url = UIUtils.getBaseUrl(this) + WebServiceConstants.urldocumentlist + "?documenttype=&pageNo=1";
                paramsArray = new String[]{url, null};
                networkTask.execute(paramsArray);
        }
    }

    @Override
    public void resultFromNetwork(String object, int id, int arg1, String arg2) {
        Log.d("Trip", "json of triplist is " + object);
        if (object == null || object.equals("")) {
            Utils.showAlertDialog(this, getString(R.string.app_name), getString(R.string.network_error), getString(R.string.ok), null, null, null);

        } else if (id == DOCUMENT_LIST) {
            try {

                        if (object.contains(getString(R.string.user_doc_list))) {
                            Gson gson = new Gson();
                            UserDocumentList userDocumentList = gson.fromJson(object, UserDocumentList.class);
                            Log.d("Trip", "size of list is " + userDocumentList.getUserDocumentList().size());

                            compareData(userDocumentList);
                        }

            } catch (Exception e) {
                Log.d("Trip", "error in resultFromNetwork " + e);
            }
        }

    }

    private void compareData(UserDocumentList userDocumentList) {
        DbHelper dbHelper = DbHelper.getInstance(this);
        List<MyDocument> docList = dbHelper.getAllDocument();
        downloadlist = new ArrayList<MyDocument>();

        List<MyDocument> serverList = userDocumentList.getUserDocumentList();
        for(int i = 0; i < serverList.size(); i++) {
            MyDocument serverDoc = serverList.get(i);
            if(docList.size() > 0) {
                for (int j = 0; j < docList.size(); j++) {
                    MyDocument saveDoc = docList.get(j);
                    if (serverDoc.getId().equals(saveDoc.getId())) {
                        if (!serverDoc.getCreatedOn().equals(saveDoc.getCreatedOn())) {
                            downloadlist.add(serverDoc);
                        }
                        docList.remove(saveDoc);
                        break;

                    } else {
                        if (j == docList.size() - 1) {
                            downloadlist.add(serverDoc);
                        }
                    }
                }
            } else { if(serverList.size() > 0) {
                downloadlist.add(serverDoc);
            }

            }
        }

        if(downloadlist.size() > 0) {
            // download file
            downloadFile();
        }


    }

    public void downloadFile() {

            try {
                myDocument = downloadlist.get(downloadDocNo);
                String url = UIUtils.getBaseUrl(this) + WebServiceConstants.urldownloadDocument + "?id=" + myDocument.getId() + "&userId=" + myDocument.getUserId();//"url you want to download";
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDescription("Some descrition");
                request.setTitle("document");
                // in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                File directory =  new File(Environment.getExternalStorageDirectory(),
                        "tripbox");
                if(!directory.exists())
                    directory.mkdir();
                //Environment.DIRECTORY_DOWNLOADS
                request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().getAbsolutePath(), "tripbox/" +
                        myDocument.getDocumentFileName()); //"name-of-the-file.ext");

               // get download service and enqueue file
                downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                long l = downloadManager.enqueue(request);


            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                Cursor cursor = downloadManager.query(query);
                if (cursor.moveToFirst()) {

                    int columnIndex = cursor
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);

                    int status = cursor.getInt(columnIndex);
                    int columnReason = cursor
                            .getColumnIndex(DownloadManager.COLUMN_REASON);
                    int reason = cursor.getInt(columnReason);

                    switch (status) {
                        case DownloadManager.STATUS_FAILED:
                            String failedReason = "";
                            switch (reason) {
                                case DownloadManager.ERROR_CANNOT_RESUME:
                                    failedReason = "ERROR_CANNOT_RESUME";
                                    break;
                                case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                                    failedReason = "ERROR_DEVICE_NOT_FOUND";
                                    break;
                                case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                                    failedReason = "ERROR_FILE_ALREADY_EXISTS";
                                    break;
                                case DownloadManager.ERROR_FILE_ERROR:
                                    failedReason = "ERROR_FILE_ERROR";
                                    break;
                                case DownloadManager.ERROR_HTTP_DATA_ERROR:
                                    failedReason = "ERROR_HTTP_DATA_ERROR";
                                    break;
                                case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                                    failedReason = "ERROR_INSUFFICIENT_SPACE";
                                    break;
                                case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                                    failedReason = "ERROR_TOO_MANY_REDIRECTS";
                                    break;
                                case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                                    failedReason = "ERROR_UNHANDLED_HTTP_CODE";
                                    break;
                                case DownloadManager.ERROR_UNKNOWN:
                                    failedReason = "ERROR_UNKNOWN";
                                    break;
                            }

//                            Toast.makeText(this, "FAILED: " + failedReason,
//                                    Toast.LENGTH_LONG).show();
                            break;
                        case DownloadManager.STATUS_PAUSED:
                            String pausedReason = "";

                            switch (reason) {
                                case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                                    pausedReason = "PAUSED_QUEUED_FOR_WIFI";
                                    break;
                                case DownloadManager.PAUSED_UNKNOWN:
                                    pausedReason = "PAUSED_UNKNOWN";
                                    break;
                                case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                                    pausedReason = "PAUSED_WAITING_FOR_NETWORK";
                                    break;
                                case DownloadManager.PAUSED_WAITING_TO_RETRY:
                                    pausedReason = "PAUSED_WAITING_TO_RETRY";
                                    break;
                            }

//                            Toast.makeText(this, "PAUSED: " + pausedReason,
//                                    Toast.LENGTH_LONG).show();
                            break;
                        case DownloadManager.STATUS_PENDING:
                            //Toast.makeText(this, "PENDING", Toast.LENGTH_LONG).show();
                            break;
                        case DownloadManager.STATUS_RUNNING:
                            //Toast.makeText(this, "RUNNING", Toast.LENGTH_LONG).show();
                            break;
                        case DownloadManager.STATUS_SUCCESSFUL:
                            String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));

                            DbHelper dbHelper = DbHelper.getInstance(MyDocumentTypeActivity.this);
                            myDocument.setPath(uriString);
                            dbHelper.insertDocument(myDocument);
                            updateUi();

                            // Toast.makeText(this, "SUCCESSFUL", Toast.LENGTH_LONG).show();
                            // GetFile();
                            break;
                    }

                    if(downloadDocNo < (downloadlist.size() - 1) ) {
                        downloadDocNo++;
                        downloadFile();
                    }
                }
            }
        }
    };

    public void updateUi() {
        List<DocumentTypes> types = getDocumentTypeList();
        adapter.setDocumentTypes(types);
        adapter.notifyDataSetChanged();
    }

   /* @Override
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

        typeArray = getResources().getStringArray(R.array.array_document_type);
        imgArray = getResources().obtainTypedArray(R.array.array_document_type_image);

        final IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, filter);

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
