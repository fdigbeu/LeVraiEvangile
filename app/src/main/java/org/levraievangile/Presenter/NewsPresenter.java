package org.levraievangile.Presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.levraievangile.Model.Actualite;
import org.levraievangile.Model.ApiClient;
import org.levraievangile.Model.Mois;
import org.levraievangile.View.Interfaces.NewsView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.levraievangile.Presenter.CommonPresenter.KEY_ALL_NEWS_MONTH_LIST;
import static org.levraievangile.Presenter.CommonPresenter.KEY_SHORT_CODE;

/**
 * Created by Maranatha on 06/12/2017.
 */

public class NewsPresenter {
    // Ref interface
    private NewsView.INews iNews;

    // Constructor
    public NewsPresenter(NewsView.INews iNews) {
        this.iNews = iNews;
    }

    // Load news data
    public void loadNewsData(Context context, Intent intent){
        iNews.initialize();
        iNews.events();
        //--
        loadNewsMonthData(context, intent);
    }

    // Launch activity
    public void launchActivity(String value){
        iNews.launchActivity(value);
    }

    /**
     * Load list of the month
     * @param context
     * @param intent
     */
    public void loadNewsMonthData(final Context context, Intent intent){

        iNews.progressBarVisibility(View.VISIBLE);
        //--
        if(intent != null) {
            try {
                //Get list month news by short-code
                final String yearValue = intent.getStringExtra(KEY_SHORT_CODE);
                iNews.modifyHeaderInfos(yearValue);
                if(CommonPresenter.isMobileConnected(context)) {
                    Call<List<Mois>> callMonth = ApiClient.getApiClientLeVraiEvangile().create(NewsView.IApiRessource.class).getAllNewsMonth(yearValue);
                    callMonth.enqueue(new Callback<List<Mois>>() {
                        @Override
                        public void onResponse(Call<List<Mois>> call, Response<List<Mois>> response) {
                            ArrayList<Mois> months = (ArrayList<Mois>) response.body();
                            final String keyShortCode = KEY_ALL_NEWS_MONTH_LIST + "-" + yearValue;
                            CommonPresenter.saveDataInSharePreferences(context, keyShortCode, months.toString());
                            iNews.loadNewsMonth(months, Integer.parseInt(yearValue));
                        }

                        @Override
                        public void onFailure(Call<List<Mois>> call, Throwable t) {
                            ArrayList<Mois> months = CommonPresenter.getAllNewsMonthSavedBy(context, yearValue);
                            iNews.loadNewsMonth(months, Integer.parseInt(yearValue));
                        }
                    });
                }
                else{
                    ArrayList<Mois> months = CommonPresenter.getAllNewsMonthSavedBy(context, yearValue);
                    iNews.loadNewsMonth(months, Integer.parseInt(yearValue));
                    //--
                    if(months.size()==0){
                        CommonPresenter.showNoConnectionMessage(context, true);
                    }
                }
            }
            catch (Exception ex){}
        }
        else{
            iNews.closeActivity();
        }
    }



    // Manage menu Item
    public void retrieveUserAction(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                iNews.closeActivity();
                break;
        }
    }

    /**
     * Position to scroll
     * @param position
     */
    public void monthRecyclerScrollTo(int position){
        iNews.monthRecyclerScrollTo(position+1);
    }

    /**
     * Load News of selected month
     * @param selectedMonth
     * @param selectedYear
     */
    public void loadNewsSelectedMonthData(int selectedMonth, int selectedYear){
        iNews.progressBarVisibility(View.VISIBLE);
        String monthAndYear = selectedYear+"/mois/"+(selectedMonth > 9 ? selectedMonth : "0"+selectedMonth);
        Log.i("TAG_MONTH_SELECTED", monthAndYear);
        Call<List<Actualite>> callNews = ApiClient.getApiClientLeVraiEvangile().create(NewsView.IApiRessource.class).getAllNews(monthAndYear);
        callNews.enqueue(new Callback<List<Actualite>>() {
            @Override
            public void onResponse(Call<List<Actualite>> call, Response<List<Actualite>> response) {
                ArrayList<Actualite> newsSelected = (ArrayList<Actualite>)response.body();
                iNews.loadNewsData(newsSelected, 1);
                iNews.progressBarVisibility(View.GONE);
                Log.i("TAG_RESPONSE_DATA", response.toString());
            }

            @Override
            public void onFailure(Call<List<Actualite>> call, Throwable t) {
                iNews.progressBarVisibility(View.GONE);
                Log.i("TAG_RESPONSE_ERROR", t.toString());
            }
        });
    }
}
