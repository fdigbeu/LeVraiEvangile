package org.levraievangile.View.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.Presenter.WebPresenter;
import org.levraievangile.View.Interfaces.LVEWebClient;
import org.levraievangile.View.Interfaces.WebView.IWeb;

import org.levraievangile.R;

import static org.levraievangile.Presenter.CommonPresenter.VALUE_PERMISSION_TO_SAVE_FILE;

public class WebActivity extends AppCompatActivity implements IWeb {
    // Ref widgets
    private WebView webView;
    private ProgressBar progressBar;
    private View fabPdfLayout;
    private FloatingActionButton fabPdfDowload, fabPdfShare, fabPdfFavorite, fabCloseApp;
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
        fabPdfLayout = findViewById(R.id.fab_pdf_layout);
        fabPdfDowload = findViewById(R.id.fab_pdf_dowload);
        fabPdfShare = findViewById(R.id.fab_pdf_share);
        fabPdfFavorite = findViewById(R.id.fab_pdf_favorite);
        fabCloseApp = findViewById(R.id.fab_close_app);
    }

    @Override
    public void events() {
        // When user clicks on download button
        fabPdfDowload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webPresenter.retrieveUserAction(view);
            }
        });
        // When user clicks on share button
        fabPdfShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webPresenter.retrieveUserAction(view);
            }
        });
        // When user clicks on favorite button
        fabPdfFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webPresenter.retrieveUserAction(view);
            }
        });
        fabCloseApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webPresenter.retrieveUserAction(view);
            }
        });
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
    public void askPermissionToSaveFile() {
        int permissionCheck = ContextCompat.checkSelfPermission(WebActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(WebActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, VALUE_PERMISSION_TO_SAVE_FILE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case VALUE_PERMISSION_TO_SAVE_FILE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Ceate folders
                    CommonPresenter.createFolder();
                }
                else {
                    Toast.makeText(WebActivity.this, getResources().getString(R.string.lb_storage_file_require), Toast.LENGTH_LONG).show();
                }
                break;
        }
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
    public void fabPdfLayoutVisibility(int visibility) {
        fabPdfLayout.setVisibility(visibility);
    }

    @Override
    public void fabCloseAppVisibility(int visibility) {
        fabCloseApp.setVisibility(visibility);
    }

    @Override
    public void closeActivity() {
        this.finish();
    }
}
