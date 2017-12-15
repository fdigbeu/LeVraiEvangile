package org.levraievangile.Presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.levraievangile.Model.DAOFavoris;
import org.levraievangile.Model.Favoris;
import org.levraievangile.Model.Pdf;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.LVEWebClient;
import org.levraievangile.View.Interfaces.WebView;
import org.levraievangile.View.Interfaces.WebView.ILoadWebPage;

import static org.levraievangile.Presenter.CommonPresenter.GOOGLE_DRIVE_READER;
import static org.levraievangile.Presenter.CommonPresenter.KEY_SHORT_CODE;
import static org.levraievangile.Presenter.CommonPresenter.KEY_URL_NEWS_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.saveDataInSharePreferences;

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
            String url = intent.getStringExtra(KEY_SHORT_CODE);
            if(url.startsWith(GOOGLE_DRIVE_READER)){
                saveDataInSharePreferences(context, KEY_URL_NEWS_SELECTED, url);
            }
            else {
                saveDataInSharePreferences(context, KEY_URL_NEWS_SELECTED, "NULL");
            }
            this.lveWebClient = new LVEWebClient();
            this.lveWebClient.setiWeb(this.iWeb);
            iWeb.loadWebView(this.lveWebClient, url);
        }
        else{
            iWeb.progressBarVisibility(View.GONE);
            iWeb.webViewVisibility(View.GONE);
            iWeb.closeActivity();
            iWeb.fabPdfLayoutVisibility(View.GONE);
        }
    }

    public void retrieveUserAction(View view){
        Pdf pdfSelected = CommonPresenter.getPdfSelected(view.getContext());
        switch (view.getId()){
            // Download
            case R.id.fab_pdf_dowload:
                downloadThisPdf(view.getContext(), pdfSelected);
                break;

            // Share
            case R.id.fab_pdf_share:
                CommonPresenter.sharePdf(view.getContext(), pdfSelected);
                break;

            // Save to favorite
            case R.id.fab_pdf_favorite:
                DAOFavoris daoFavoris = new DAOFavoris(view.getContext());
                if(!daoFavoris.isFavorisExists(pdfSelected.getSrc())){

                    Favoris favoris = new Favoris(pdfSelected.getId(), "pdf", pdfSelected.getMipmap(), pdfSelected.getUrlacces(), pdfSelected.getSrc(), pdfSelected.getTitre(), pdfSelected.getAuteur(), "00:00:00", pdfSelected.getDate(), pdfSelected.getType_libelle(), pdfSelected.getType_shortcode(), pdfSelected.getId());
                    daoFavoris.insertData(favoris);
                    Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.pdf_add_to_favorite), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.pdf_already_add_to_favorite), Toast.LENGTH_SHORT).show();
                }
                break;

            // Close activity
            case R.id.fab_close_app:
                iWeb.closeActivity();
                break;
        }
    }

    // Download pdf
    private void downloadThisPdf(Context context, Pdf pdf){
        if(pdf != null){
            if(CommonPresenter.isStorageDownloadFileAccepted(context)){
                String url = pdf.getUrlacces()+pdf.getSrc();
                String filename = pdf.getSrc();
                String auteur = pdf.getAuteur();
                String description = "LVE-APP-DOWNLOADER ("+pdf.getType_libelle()+(auteur != null ? " | "+auteur : "")+")";
                CommonPresenter.getFileByDownloadManager(context, url, filename, description, "pdf");
                Toast.makeText(context, context.getResources().getString(R.string.lb_downloading), Toast.LENGTH_SHORT).show();
                Log.i("TAG_DOWNLOAD_FILE", "URL = "+url);
            }
            else{
                iWeb.askPermissionToSaveFile();
            }
        }
    }

    @Override
    public void webViewLoadSuccess(Context context) {
        iWeb.progressBarVisibility(View.GONE);
        iWeb.webViewVisibility(View.VISIBLE);
        iWeb.fabCloseAppVisibility(View.VISIBLE);
        String url = CommonPresenter.getDataFromSharePreferences(context, KEY_URL_NEWS_SELECTED);
        Log.i("TAG_WEBVIEW_URL", "webViewLoadSuccess() = "+url);
        iWeb.fabPdfLayoutVisibility((url != null && url.startsWith(GOOGLE_DRIVE_READER)) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void webViewLoadFailure() {
        iWeb.progressBarVisibility(View.GONE);
        iWeb.webViewVisibility(View.GONE);
        iWeb.fabCloseAppVisibility(View.VISIBLE);
    }
}
