package org.levraievangile.View.Activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import org.levraievangile.Presenter.WebPresenter;
import org.levraievangile.View.Interfaces.LVEWebClient;
import org.levraievangile.View.Interfaces.WebView.IWeb;

import org.levraievangile.R;

public class WebActivity extends AppCompatActivity implements IWeb {
    // Ref widgets
    private WebView webView;
    private ProgressBar progressBar;
    // Ref presenter
    private WebPresenter webPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        // Load presenter data
        webPresenter = new WebPresenter(this);
        webPresenter.loadWebData(WebActivity.this, this.getIntent());
    }

    @Override
    public void hideHeader() {
        getSupportActionBar().hide();
    }

    @Override
    public void initialize() {
        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progress);
    }

    @Override
    public void events() {

    }

    @Override
    public void loadWebView(LVEWebClient webClient, String url) {
        Log.i("TAG_URL", "LOADING URL : "+url);
        // Configure related browser settings
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        // Enable responsive layout
        webView.getSettings().setUseWideViewPort(true);

        // Display image from url : Android 5 and plus...
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // Zoom out if the content width is greater than the width of the viewport
        webView.getSettings().setLoadWithOverviewMode(true);

        //allow manual zoom and get rid of pinch to zoom
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.setBackgroundColor(0x00000000);
        webView.loadUrl(url);
        webView.setWebViewClient(webClient);
    }

    @Override
    public void progressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void webViewVisibility(int visibility) {
        webView.setVisibility(visibility);
    }

    @Override
    public void closeActivity() {
        this.finish();
    }
}
