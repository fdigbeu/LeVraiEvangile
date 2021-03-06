package org.levraievangile.Presenter;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import org.levraievangile.Model.ApiClient;
import org.levraievangile.Model.Pdf;
import org.levraievangile.View.Interfaces.PdfView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.levraievangile.Presenter.CommonPresenter.KEY_ALL_PDFS_LIST;
import static org.levraievangile.Presenter.CommonPresenter.KEY_FORM_SEARCH_WORD;
import static org.levraievangile.Presenter.CommonPresenter.KEY_IS_USER_LEVEL_ADMIN;
import static org.levraievangile.Presenter.CommonPresenter.KEY_SHORT_CODE;

/**
 * Created by Maranatha on 06/12/2017.
 */

public class PdfPresenter {
    // Ref interface
    private PdfView.IPdf iPdf;

    // Ref interface retrofit
    private PdfView.IApiRessource iApiRessource;

    public PdfPresenter(PdfView.IPdf iPdf) {
        this.iPdf = iPdf;
    }

    public void loadPdfData(final Context context, Intent intent){
        try {
            iPdf.initialize();
            iPdf.events();
            iPdf.askPermissionToSaveFile();
            iPdf.progressBarVisibility(View.VISIBLE);
            iPdf.recyclerViewVisibility(View.GONE);
            //--
            if(intent != null && intent.getStringExtra(KEY_FORM_SEARCH_WORD) == null) {
                try {
                    String userLevel = CommonPresenter.getDataFromSharePreferences(context, KEY_IS_USER_LEVEL_ADMIN);
                    String shortCodeLevel = userLevel.equalsIgnoreCase("YES") ? "/acces-admin" : "";
                    //Get list pdfs by short-code
                    final String shortCode = intent.getStringExtra(KEY_SHORT_CODE);
                    iPdf.modifyHeaderInfos(CommonPresenter.getLibelleByTypeShortcode(shortCode));
                    if(CommonPresenter.isMobileConnected(context)) {
                        Call<List<Pdf>> callPdfs = ApiClient.getApiClientLeVraiEvangile().create(PdfView.IApiRessource.class).getAllPdfs(shortCode+shortCodeLevel);
                        callPdfs.enqueue(new Callback<List<Pdf>>() {
                            @Override
                            public void onResponse(Call<List<Pdf>> call, Response<List<Pdf>> response) {
                                ArrayList<Pdf> pdfs = (ArrayList<Pdf>) response.body();
                                final String keyShortCode = KEY_ALL_PDFS_LIST + "-" + shortCode;
                                if(pdfs != null) {
                                    CommonPresenter.saveDataInSharePreferences(context, keyShortCode, pdfs.toString());
                                }
                                iPdf.loadPdfData(pdfs, 1);
                                iPdf.progressBarVisibility(View.GONE);
                                iPdf.recyclerViewVisibility(View.VISIBLE);
                                iPdf.stopRefreshing(true);
                            }

                            @Override
                            public void onFailure(Call<List<Pdf>> call, Throwable t) {
                                ArrayList<Pdf> pdfs = CommonPresenter.getAllPdfSavedBy(context, shortCode);
                                iPdf.loadPdfData(pdfs, 1);
                                iPdf.progressBarVisibility(View.GONE);
                                iPdf.recyclerViewVisibility(View.VISIBLE);
                                iPdf.stopRefreshing(true);
                            }
                        });
                    }
                    else{
                        ArrayList<Pdf> pdfs = CommonPresenter.getAllPdfSavedBy(context, shortCode);
                        iPdf.loadPdfData(pdfs, 1);
                        iPdf.progressBarVisibility(View.GONE);
                        iPdf.recyclerViewVisibility(View.VISIBLE);
                        iPdf.stopRefreshing(true);
                        //--
                        if(pdfs.size()==0){
                            // Display no connection message
                            CommonPresenter.showNoConnectionMessage(context, true);
                        }
                    }
                }
                catch (Exception ex){}
            }
            else{
                // If it's to search
                final String keyWord = intent.getStringExtra(KEY_FORM_SEARCH_WORD);
                Call<List<Pdf>> callPdfs = ApiClient.getApiClientLeVraiEvangile().create(PdfView.IApiRessource.class).getAllSearchPdfs(keyWord);
                callPdfs.enqueue(new Callback<List<Pdf>>() {
                    @Override
                    public void onResponse(Call<List<Pdf>> call, Response<List<Pdf>> response) {
                        ArrayList<Pdf> pdfs = (ArrayList<Pdf>) response.body();
                        iPdf.loadPdfData(pdfs, 1);
                        iPdf.progressBarVisibility(View.GONE);
                        iPdf.recyclerViewVisibility(View.VISIBLE);
                        iPdf.stopRefreshing(true);
                        iPdf.modifyBarHeader("Recherche de pdf", "TOTAL : "+pdfs.size()+" | MOT CLÉ : "+keyWord);
                    }

                    @Override
                    public void onFailure(Call<List<Pdf>> call, Throwable t) {
                        iPdf.progressBarVisibility(View.GONE);
                        iPdf.recyclerViewVisibility(View.VISIBLE);
                        iPdf.stopRefreshing(true);
                        iPdf.modifyBarHeader("Recherche de pdf", "Aucun pdf trouvé | MOT CLÉ : "+keyWord);
                    }
                });
            }
        }
        catch (Exception ex){}
    }

    // Reload pdf data
    public void reloadPdfData(Context context, Intent intent){
        try {
            loadPdfData(context, intent);
            iPdf.progressBarVisibility(View.GONE);
        }
        catch (Exception ex){}
    }


    // Manage menu Item
    public void retrieveUserAction(MenuItem item){
        try {
            switch (item.getItemId()){
                case android.R.id.home:
                    iPdf.closeActivity();
                    break;
            }
        }
        catch (Exception ex){}
    }

    // Launch activity
    public void launchActivity(String value){
        try {
            iPdf.launchActivity(value);
        }
        catch (Exception ex){}
    }
}
