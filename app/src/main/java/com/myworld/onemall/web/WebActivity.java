package com.myworld.onemall.web;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myworld.onemall.R;
import com.myworld.onemall.base.BaseActivity;

public class WebActivity extends BaseActivity {
    private WebView webView;
    private ProgressBar progressBar;
    private String url, title;
    private WebChromeClient.FileChooserParams fileChooserParams;
    private ValueCallback<Uri[]> valueCallback;
    private TextView titleText;
    private ImageView back;
    private static final int REQUEST_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");

        webView = (WebView) findViewById(R.id.web);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        titleText = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                WebActivity.this.valueCallback = filePathCallback;
                WebActivity.this.fileChooserParams = fileChooserParams;
                Intent intent = fileChooserParams.createIntent();
                startActivityForResult(intent, REQUEST_FILE);
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (TextUtils.isEmpty(title))
                    titleText.setText(view.getTitle());
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                boolean handled = UrlHandler.handled(view.getContext(), url);
                return handled;
            }
        });
        webView.loadUrl(url);
        titleText.setText(title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_FILE) {
            valueCallback.onReceiveValue(new Uri[]{data.getData()});
        }
    }

    public static void startWebActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }
}
