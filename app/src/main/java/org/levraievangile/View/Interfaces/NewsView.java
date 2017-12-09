package org.levraievangile.View.Interfaces;

import org.levraievangile.Model.Actualite;
import org.levraievangile.Model.Mois;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Maranatha on 06/12/2017.
 */

public class NewsView {
    public interface INews{
        public void initialize();
        public void events();
        public void loadNewsMonth(ArrayList<Mois> month, int year);
        public void monthRecyclerScrollTo(int position);
        public void loadNewsData(ArrayList<Actualite> news, int numberColumns);
        public void launchActivity(String value);
        public void progressBarVisibility(int visibility);
        public void closeActivity();
    }

    // Presenter interface
    public interface IPresenter{}


    public interface IApiRessource {
        @GET("webservice/actualites/annee/{YEAR}/liste/mois/")
        Call<List<Mois>> getAllNewsMonth(@Path(value = "YEAR", encoded = true) String keyWord);

        //{YEAR_AND_MONTH} <=> {YEAR}/mois/{MONTH}
        @GET("webservice/actualites/annee/{YEAR_AND_MONTH}/liste/")
        Call<List<Actualite>> getAllNews(@Path(value = "YEAR_AND_MONTH", encoded = true) String keyWord);

        //{YEAR_AND_MONTH_AND_ID} <=> {YEAR}/{MONTH}/id/{ID}
        @GET("webservice/actualites/webview/{YEAR_AND_MONTH_AND_ID}/")
        Call<Actualite> getNewsDetail(@Path(value = "YEAR_AND_MONTH_AND_ID", encoded = true) String keyWord);
    }
}
