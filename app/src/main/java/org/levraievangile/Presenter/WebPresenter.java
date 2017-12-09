package org.levraievangile.Presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import org.levraievangile.View.Interfaces.LVEWebClient;
import org.levraievangile.View.Interfaces.WebView;
import org.levraievangile.View.Interfaces.WebView.ILoadWebPage;

import static org.levraievangile.Presenter.CommonPresenter.KEY_SHORT_CODE;

/**
 * Created by Maranatha on 07/12/2017.
 */

public class WebPresenter implements ILoadWebPage {
    private WebView.IWeb iWeb;
    private LVEWebClient lveWebClient;

    public WebPresenter(WebView.IWeb iWeb) {
        this.iWeb = iWeb;
    }

    public void loadWebData(Context context, Intent intent){
        iWeb.hideHeader();
        iWeb.initialize();
        iWeb.events();
        iWeb.progressBarVisibility(View.VISIBLE);
        iWeb.webViewVisibility(View.GONE);
        //--
        if(intent != null){
            this.lveWebClient = new LVEWebClient();
            this.lveWebClient.setiWeb(this.iWeb);
            iWeb.loadWebView(this.lveWebClient, intent.getStringExtra(KEY_SHORT_CODE));
        }
        else{
            iWeb.progressBarVisibility(View.GONE);
            iWeb.webViewVisibility(View.GONE);
            iWeb.closeActivity();
        }
    }

    @Override
    public void webViewLoadSuccess() {
        iWeb.progressBarVisibility(View.GONE);
        iWeb.webViewVisibility(View.VISIBLE);
    }

    @Override
    public void webViewLoadFailure() {
        iWeb.progressBarVisibility(View.GONE);
        iWeb.webViewVisibility(View.GONE);
    }
}
