package org.levraievangile.Presenter;

import android.content.Context;
import android.view.View;

import org.levraievangile.Model.Annee;
import org.levraievangile.Model.ApiClient;
import org.levraievangile.Model.Audio;
import org.levraievangile.Model.BonASavoir;
import org.levraievangile.Model.Pdf;
import org.levraievangile.Model.Video;
import org.levraievangile.View.Interfaces.HomeView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.levraievangile.Presenter.CommonPresenter.KEY_ALL_GOOD_TO_KNOW_LIST;
import static org.levraievangile.Presenter.CommonPresenter.KEY_ALL_NEWS_YEARS_LIST;

/**
 * Created by Maranatha on 05/12/2017.
 */

public class HomePresenter {
    // Ref interface
    private HomeView.IHome iHome;
    private HomeView.IPlaceholder iPlaceholder;

    // Ref interface retrofit
    private HomeView.IApiRessource iApiRessource;

    // Constructors
    public HomePresenter(HomeView.IHome iHome) {
        this.iHome = iHome;
    }

    public HomePresenter(HomeView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
    }

    // Methods
    public void loadHomeData(Context context){
        iHome.initialize();
        iHome.events();
    }

    // Launch activity
    public void launchActivity(String value, Class destination){
        iPlaceholder.launchActivity(value, destination);
    }

    public void loadPlaceHolderData(final Context context, View view, int positionFrag){
        iPlaceholder.initialize(view);
        iPlaceholder.events();
        //--
        iPlaceholder.progressBarVisibility(View.VISIBLE);
        //--
        if(CommonPresenter.isMobileConnected(context)){
            //Get list news years
            iApiRessource = ApiClient.getApiClientLeVraiEvangile().create(HomeView.IApiRessource.class);
            Call<List<Annee>> callAnnees = iApiRessource.getAllNewsYears();
            callAnnees.enqueue(new Callback<List<Annee>>() {
                @Override
                public void onResponse(Call<List<Annee>> call, Response<List<Annee>> response) {
                    ArrayList<Annee> newsYears = (ArrayList<Annee>)response.body();
                    iPlaceholder.progressBarVisibility(View.GONE);
                    // Save news years list
                    CommonPresenter.saveDataInSharePreferences(context, KEY_ALL_NEWS_YEARS_LIST, newsYears.toString());
                }

                @Override
                public void onFailure(Call<List<Annee>> call, Throwable t) {
                    iPlaceholder.progressBarVisibility(View.GONE);
                }
            });

            //Get list good to know
            iApiRessource = ApiClient.getApiClientLeVraiEvangile().create(HomeView.IApiRessource.class);
            Call<List<BonASavoir>> callGoodToKnows = iApiRessource.getAllGoodToKnows();
            callGoodToKnows.enqueue(new Callback<List<BonASavoir>>() {
                @Override
                public void onResponse(Call<List<BonASavoir>> call, Response<List<BonASavoir>> response) {
                    ArrayList<BonASavoir> goodToKnows = (ArrayList<BonASavoir>)response.body();
                    iPlaceholder.progressBarVisibility(View.GONE);
                    // Save good to know list
                    CommonPresenter.saveDataInSharePreferences(context, KEY_ALL_GOOD_TO_KNOW_LIST, goodToKnows.toString());
                }

                @Override
                public void onFailure(Call<List<BonASavoir>> call, Throwable t) {
                    iPlaceholder.progressBarVisibility(View.GONE);
                }
            });
        }
        //--
        loadFragmentData(context, positionFrag);
    }

    // Refresh all data
    public void loadFragmentData(Context context, int positionFrag){

        int numberColumn = CommonPresenter.getNumberToDisplay(context);
        switch (positionFrag){
            // VIDEOS
            case 0:
                ArrayList<Video> videos = CommonPresenter.listeSousMenuVideo();
                iPlaceholder.loadSubMenuVideo(videos, numberColumn);
                break;

            // AUDIOS
            case 1:
                ArrayList<Audio> audios = CommonPresenter.listeSousMenuAudio();
                iPlaceholder.loadSubMenuAudio(audios, numberColumn);
                break;

            // PDF
            case 2:
                ArrayList<Pdf> pdfs = CommonPresenter.listeSousMenuPdf();
                iPlaceholder.loadSubMenuPdf(pdfs, numberColumn);
                break;

            // NEWS YEARS
            case 3:
                ArrayList<Annee> newsYears = CommonPresenter.getAllSaveYears(context);
                if(newsYears != null){
                    iPlaceholder.loadSubMenuNewsYears(newsYears, numberColumn);
                    iPlaceholder.progressBarVisibility(View.GONE);
                }
                break;

            // GOOD TO KNOW
            case 4:
                ArrayList<BonASavoir> goodToKnows = CommonPresenter.getAllSaveGoodToKnow(context);
                if(goodToKnows != null){
                    iPlaceholder.loadSubMenuGoodToKnow(goodToKnows, 1);
                    iPlaceholder.progressBarVisibility(View.GONE);
                }
                break;
        }
    }
}
