package org.levraievangile.View.Interfaces;

import org.levraievangile.Model.Pdf;
import org.levraievangile.Model.Video;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Maranatha on 06/12/2017.
 */

public class PdfView {
    public interface IPdf{
        public void initialize();
        public void events();
        public void loadPdfData(ArrayList<Pdf> pdfs, int numberColumns);
        public void launchActivity(String value);
        public void progressBarVisibility(int visibility);
        public void askPermissionToSaveFile();
        public void modifyHeaderInfos(String typeLibelle);
        public void closeActivity();
        public void modifyBarHeader(String title, String subTitle);
    }

    // Presenter interface
    public interface IPresenter{}


    public interface IApiRessource {
        @GET("webservice/ressources/pdf/{TYPE}/")
        Call<List<Pdf>> getAllPdfs(@Path(value = "TYPE", encoded = true) String keyWord);

        @GET("webservice/ressources/pdf/rechercher/motclef/{KEY_WORD}/")
        Call<List<Pdf>> getAllSearchPdfs(@Path(value = "KEY_WORD") String keyWord);
    }

}
