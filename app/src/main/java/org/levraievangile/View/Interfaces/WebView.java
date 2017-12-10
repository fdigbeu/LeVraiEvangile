package org.levraievangile.View.Interfaces;

import android.content.Context;

/**
 * Created by Maranatha on 07/12/2017.
 */

public class WebView {
    public interface IWeb{
        public void hideHeader();
        public void initialize();
        public void events();
        public void loadWebView(LVEWebClient webClient, String url);
        public void progressBarVisibility(int visibility);
        public void webViewVisibility(int visibility);
        public void fabPdfLayoutVisibility(int visibility);
        public void fabCloseAppVisibility(int visibility);
        public void askPermissionToSaveFile();
        public void closeActivity();
    }

    public interface ILoadWebPage{
        public void webViewLoadSuccess(Context context);
        public void webViewLoadFailure();
    }

    public interface IPresenter{}
}
