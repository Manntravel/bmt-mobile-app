package com.rezofy.views.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.rezofy.R;
import com.rezofy.utils.UIUtils;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.HomeActivity;
import com.rezofy.views.custom_views.IconTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment implements View.OnClickListener {

    private View view;
    public IconTextView iTVMenu, iTVShareApp;
    public TextView tvTitle;
    private WebView webView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_web_view, container, false);
        init();
        setProperties();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
       /* if (AppPreferences.getInstance(getContext()).getUserData().isEmpty())
            iTVMenu.setVisibility(View.GONE);
        else
            iTVMenu.setVisibility(View.VISIBLE);*/
    }

    private void init() {
        iTVMenu = (IconTextView) view.findViewById(R.id.header).findViewById(R.id.left_icon);
        iTVMenu.setOnClickListener(this);
        iTVMenu.setText(getString(R.string.icon_text_h));
        iTVMenu.setTextSize(20);
        iTVShareApp = (IconTextView) view.findViewById(R.id.header).findViewById(R.id.right_icon);
        iTVShareApp.setVisibility(View.VISIBLE);
        iTVShareApp.setOnClickListener(this);
        iTVShareApp.setText(getString(R.string.icon_text_I));
        iTVShareApp.setTextSize(20);
        tvTitle = ((TextView) view.findViewById(R.id.header).findViewById(R.id.title));
        tvTitle.setText(getArguments().getString("title"));
        webView = (WebView) view.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            private ProgressDialog pd;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (pd == null)
                    pd = Utils.getProgressDialog(getContext(), R.string.please_wait, false, null);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (pd != null)
                    pd.dismiss();
            }
        });
        webView.loadUrl(getArguments().getString("url"));
    }

    private void setProperties() {
        int themeContrastColor = UIUtils.getThemeContrastColor(getContext());
        view.findViewById(R.id.header).setBackgroundColor(UIUtils.getThemeColor(getContext()));
        iTVMenu.setTextColor(themeContrastColor);
        iTVShareApp.setTextColor(themeContrastColor);
        tvTitle.setTextColor(themeContrastColor);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.left_icon:
                ((HomeActivity) getActivity()).openDrawer();
                break;

            case R.id.right_icon:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_share_app));
                startActivity(Intent.createChooser(shareIntent, getString(R.string.text_share)));
                break;
        }

    }
}
