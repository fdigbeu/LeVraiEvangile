package org.levraievangile.View.Interfaces;

import android.view.View;

import org.levraievangile.Model.Annee;
import org.levraievangile.Model.Audio;
import org.levraievangile.Model.BonASavoir;
import org.levraievangile.Model.Pdf;
import org.levraievangile.Model.Video;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Maranatha on 05/12/2017.
 */

public class HomeView {
    public interface IHome{
        public void initialize();
        public void events();
        public void askPermissionToSaveFile();
        public void launchParameterActivity();
        public void launchDownloadActivity();
        public void launchFavorisActivity();
    }

    public interface IPlaceholder{
        public void initialize(View rootView);
        public void events();
        public void loadSubMenuVideo(ArrayList<Video> videos, int numberColumns);
        public void loadSubMenuAudio(ArrayList<Audio> audios, int numberColumns);
        public void loadSubMenuPdf(ArrayList<Pdf> pdfs, int numberColumns);
        public void loadSubMenuNewsYears(ArrayList<Annee> news, int numberColumns);
        public void loadSubMenuGoodToKnow(ArrayList<BonASavoir> goodToKnows, int numberColumns);
        public void progressBarVisibility(int visibility);
        public void launchActivity(String value, Class destination);
    }

    public interface IPresenter{}

    // Retrofit interface
    public interface IApiRessource {

        // webservice/actualites/liste/annees/ equal end of webservice url
        @GET("webservice/actualites/liste/annees/")
        Call<List<Annee>> getAllNewsYears();

        // webservice/bon-a-savoir/liste/ equal end of webservice url
        @GET("webservice/bon-a-savoir/liste/")
        Call<List<BonASavoir>> getAllGoodToKnows();
    }
}
