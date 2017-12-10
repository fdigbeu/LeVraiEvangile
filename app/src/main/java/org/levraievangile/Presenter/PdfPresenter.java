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
        iPdf.initialize();
        iPdf.events();
        iPdf.askPermissionToSaveFile();
        iPdf.progressBarVisibility(View.VISIBLE);
        //--
        if(intent != null) {
            try {
                //Get list pdfs by short-code
                final String shortCode = intent.getStringExtra(KEY_SHORT_CODE);
                iPdf.modifyHeaderInfos(CommonPresenter.getLibelleByTypeShortcode(shortCode));
                if(CommonPresenter.isMobileConnected(context)) {
                    Call<List<Pdf>> callPdfs = ApiClient.getApiClientLeVraiEvangile().create(PdfView.IApiRessource.class).getAllPdfs(shortCode);
                    callPdfs.enqueue(new Callback<List<Pdf>>() {
                        @Override
                        public void onResponse(Call<List<Pdf>> call, Response<List<Pdf>> response) {
                            ArrayList<Pdf> pdfs = (ArrayList<Pdf>) response.body();
                            final String keyShortCode = KEY_ALL_PDFS_LIST + "-" + shortCode;
                            CommonPresenter.saveDataInSharePreferences(context, keyShortCode, pdfs.toString());
                            iPdf.loadPdfData(pdfs, 1);
                            iPdf.progressBarVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<List<Pdf>> call, Throwable t) {
                            ArrayList<Pdf> pdfs = CommonPresenter.getAllPdfSavedBy(context, shortCode);
                            iPdf.loadPdfData(pdfs, 1);
                            iPdf.progressBarVisibility(View.GONE);
                        }
                    });
                }
                else{
                    ArrayList<Pdf> pdfs = CommonPresenter.getAllPdfSavedBy(context, shortCode);
                    iPdf.loadPdfData(pdfs, 1);
                    iPdf.progressBarVisibility(View.GONE);
                }
            }
            catch (Exception ex){}
        }
        else{
            iPdf.closeActivity();
        }
    }



    // Manage menu Item
    public void retrieveUserAction(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                iPdf.closeActivity();
                break;
        }
    }

    // Launch activity
    public void launchActivity(String value){
        iPdf.launchActivity(value);
    }
}
