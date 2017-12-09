package org.levraievangile.Presenter;

import android.content.Context;
import android.content.Intent;
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
        iPdf.progressBarVisibility(View.VISIBLE);
        //--
        if(CommonPresenter.isMobileConnected(context)) {
            if(intent != null) {
                //Get list pdfs by short-code
                final String shortCode = intent.getStringExtra(KEY_SHORT_CODE);
                Call<List<Pdf>> callPdfs = ApiClient.getApiClientLeVraiEvangile().create(PdfView.IApiRessource.class).getAllPdfs(shortCode);
                callPdfs.enqueue(new Callback<List<Pdf>>() {
                    @Override
                    public void onResponse(Call<List<Pdf>> call, Response<List<Pdf>> response) {
                        ArrayList<Pdf> pdfs = (ArrayList<Pdf>)response.body();
                        final String keyShortCode = KEY_ALL_PDFS_LIST+"-"+shortCode;
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
                iPdf.closeActivity();
            }
        }
    }
}
