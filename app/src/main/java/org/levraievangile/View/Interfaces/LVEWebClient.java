package org.levraievangile.View.Interfaces;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.levraievangile.Presenter.WebPresenter;
import org.levraievangile.View.Interfaces.WebView.IWeb;


/**
 * Created by Maranatha on 17/11/2017.
 */

public class LVEWebClient extends WebViewClient {

    private IWeb iWeb;

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (Uri.parse(url).getHost().endsWith("levraievangile.org")
                || Uri.parse(url).getHost().endsWith("levraievangile.com")) {
            return false;
        }
        //--
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if (Uri.parse(url).getHost().endsWith("levraievangile.org")
                || Uri.parse(url).getHost().endsWith("levraievangile.com")) {
            return false;
        }
        //--
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        WebPresenter webPresenter = new WebPresenter(iWeb);
        webPresenter.webViewLoadSuccess();
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        WebPresenter webPresenter = new WebPresenter(iWeb);
        webPresenter.webViewLoadFailure();
    }

    // Getters & Setteurs
    public void setiWeb(IWeb iWeb) {
        this.iWeb = iWeb;
    }
}
