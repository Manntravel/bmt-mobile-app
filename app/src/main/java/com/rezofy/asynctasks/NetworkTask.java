package com.rezofy.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.utils.Utils;

import org.apache.http.NameValuePair;

import java.util.List;


/**
 * Created by LinchPin on 6/16/2015.
 */
public class NetworkTask extends AsyncTask<String, String, String> {
    private boolean isDoinBackground, isPostExecute, isPreExecute, isProgressDialog = true;
    private String dialogMessage = "Loading";
    private DoInBackground doInBackground;
    private Result result;
    private PreNetwork preNetwork;
    private ProgressDialog pd;
    private Context ctx;
    private int id;
    private int arg1;
    private String arg2;
    private List<NameValuePair> nameValuePairs;
    private boolean isDialogAndTaskCancellable = false;


    public NetworkTask(Context ctx) {
        this.ctx = ctx;
    }

    public NetworkTask(Context ctx, int id) {
        this.ctx = ctx;
        this.id = id;
    }

    public NetworkTask(Context ctx, String arg2) {
        this.ctx = ctx;
        this.arg2 = arg2;
    }

    public NetworkTask(Context ctx, int id, List<NameValuePair> nameValuePairs) {
        this.ctx = ctx;
        this.id = id;
        this.nameValuePairs = nameValuePairs;
    }

    public NetworkTask(Context ctx, int id, int arg1, String arg2) {
        this.ctx = ctx;
        this.id = id;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public void setDialogMessage(String dialogMessage) {
        this.dialogMessage = dialogMessage;
    }

    public void exposeDoInBackground(DoInBackground doInBackground) {
        this.isDoinBackground = true;
        this.doInBackground = doInBackground;
    }

    public void exposePostExecute(Result result) {
        this.isPostExecute = true;
        this.result = result;
    }

    public void exposePreExecute(PreNetwork preNetwork) {
        this.isPreExecute = true;
        this.preNetwork = preNetwork;
    }

    public void setProgressDialog(boolean isProgress) {
        isProgressDialog = isProgress;
    }


    public interface DoInBackground {

        String doInBackground(Integer id, String... params);
    }

    public interface Result {
        void resultFromNetwork(String object, int id, int arg1, String arg2);
    }

    public interface PreNetwork {

        void preNetwork(int id);
    }

    @Override
    protected void onPreExecute() {

        if (ctx != null) {
            if (isProgressDialog) {
                pd = new ProgressDialog(ctx, R.style.TransparentProgressDialog);
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        NetworkTask.this.cancel(true);
                    }
                });
                pd.setMessage(dialogMessage);
                pd.setTitle(null);
                pd.show();

                pd.setProgressStyle(R.style.TransparentProgressDialog);
                LayoutInflater inflater = LayoutInflater.from(ctx);
                View v = inflater.inflate(R.layout.custom_progress_dialog, null);
                ((TextView) v.findViewById(R.id.lodingText)).setText(dialogMessage);
                pd.setContentView(v);
                pd.setCancelable(isDialogAndTaskCancellable);
                pd.setIndeterminate(true);
            }
            if (isPreExecute && preNetwork != null)
                preNetwork.preNetwork(id);
        }
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d("Hem", "inside doInbackground" + isDoinBackground);
        String responseString = null;
        if (isDoinBackground && doInBackground != null) {
            return doInBackground.doInBackground(id, params);

        } else {
            String url = params[0];
            String requestJSONString = params[1];
            Log.d("Trip","request is "+requestJSONString);
            if (url.contains("booking-initialize.do")) {
                responseString = Utils.httpPostRaw(url, nameValuePairs);
            } else
                responseString = Utils.httpPostRaw(url, requestJSONString, ctx);
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String myresult) {
        try {
            if (ctx != null) {
                if (pd != null && isProgressDialog && pd.isShowing()) pd.dismiss();

                if (isPostExecute && result != null)
                    result.resultFromNetwork(myresult, id, arg1, arg2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPostExecute(myresult);
    }

    public void setDialogAndTaskCancellable(boolean isDialogAndTaskCancellable) {
        this.isDialogAndTaskCancellable = isDialogAndTaskCancellable;
    }
}