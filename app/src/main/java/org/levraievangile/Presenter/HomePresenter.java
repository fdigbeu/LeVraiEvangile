package org.levraievangile.Presenter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.levraievangile.Model.Annee;
import org.levraievangile.Model.ApiClient;
import org.levraievangile.Model.Audio;
import org.levraievangile.Model.BonASavoir;
import org.levraievangile.Model.DAOFavoris;
import org.levraievangile.Model.Favoris;
import org.levraievangile.Model.Pdf;
import org.levraievangile.Model.Setting;
import org.levraievangile.Model.Video;
import org.levraievangile.R;
import org.levraievangile.View.Activities.VideoPlayerActivity;
import org.levraievangile.View.Interfaces.HomeView;
import org.levraievangile.View.Interfaces.NotificationView;
import org.levraievangile.View.Services.PlayerAudioService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.levraievangile.Presenter.CommonPresenter.KEY_ALL_GOOD_TO_KNOW_LIST;
import static org.levraievangile.Presenter.CommonPresenter.KEY_ALL_NEWS_YEARS_LIST;
import static org.levraievangile.Presenter.CommonPresenter.KEY_DOWNLOAD_FILES_LIST;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_AUDIOS_LIST;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_PLAYER_PLAY_NEXT;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_PLAYER_PLAY_PREVIOUS;
import static org.levraievangile.Presenter.CommonPresenter.KEY_NOTIF_PLAYER_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_RELOAD_NEW_DATA_GOOD_TO_KNOW;
import static org.levraievangile.Presenter.CommonPresenter.KEY_RELOAD_NEW_DATA_NEWS_YEAR;
import static org.levraievangile.Presenter.CommonPresenter.KEY_SETTING_CONFIRM_BEFORE_QUIT_APP;
import static org.levraievangile.Presenter.CommonPresenter.KEY_SHOW_NEW_AUDIO_NOTIF_PLAYER;
import static org.levraievangile.Presenter.CommonPresenter.KEY_SHOW_NEW_VIDEO_NOTIF_PLAYER;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_POSITION_VIDEO_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_PLAYER_SEND_DATA;
import static org.levraievangile.Presenter.CommonPresenter.VALUE_VIDEO_SELECTED_REQUEST_CODE;
import static org.levraievangile.Presenter.CommonPresenter.getAllDownloadFilesByKey;
import static org.levraievangile.Presenter.CommonPresenter.saveDataInSharePreferences;

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
    public HomePresenter() {}

    public HomePresenter(HomeView.IHome iHome) {
        this.iHome = iHome;
    }

    public HomePresenter(HomeView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
    }

    // Methods
    public void loadHomeData(Context context, Intent intent){
        try {
            iHome.initialize();
            iHome.events();
            iHome.askPermissionToSaveFile();
            //--
            if(intent != null){
                Audio audio = (Audio) intent.getSerializableExtra(KEY_SHOW_NEW_AUDIO_NOTIF_PLAYER);
                // Launch audio notification
                if(audio != null){
                    CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_AUDIOS_LIST, "["+audio.toString()+"]");
                    CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_SELECTED, "0");
                    CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_PLAY_NEXT, "0");
                    CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_PLAY_PREVIOUS, "0");
                    Intent mIntent = new Intent(context, PlayerAudioService.class);
                    mIntent.setAction(NotificationView.ACTION.STARTFOREGROUND_ACTION);
                    context.startService(mIntent);
                }
            }
        }
        catch (Exception ex){}
    }

    // Reload home page
    public void reLoadHomeData(){
        try {
            iHome.onReloadHomePage();
            int currentPage = iHome.retrieveCurrentViewPage();
            iHome.initializeCurrentViewPage(currentPage);
            iHome.slideViewPager(currentPage >= 3 ? 0 : currentPage, currentPage);
        }
        catch (Exception ex){}
    }

    // Cancel countDowntimer
    public void cancelCountDownTimer(CountDownTimer countDownTimer){
        try {
            CommonPresenter.cancelCountDownTimer(countDownTimer);
        }
        catch (Exception ex){}
    }

    // Launch activity
    public void launchActivity(String value, Class destination){
        try {
            iPlaceholder.launchActivity(value, destination);
        }
        catch (Exception ex){}
    }

    public void loadPlaceHolderData(final Context context, View view, int positionFrag){
        try {
            iPlaceholder.initialize(view);
            iPlaceholder.events();
            //--
            iPlaceholder.progressBarVisibility(View.VISIBLE);
            //--
            if(CommonPresenter.isMobileConnected(context)){
                // Verify if newsYears data must to be reloaded
                String reloadNewsYearsData = CommonPresenter.getDataFromSharePreferences(context, KEY_RELOAD_NEW_DATA_NEWS_YEAR);
                if(reloadNewsYearsData == null || reloadNewsYearsData.isEmpty() || reloadNewsYearsData.equalsIgnoreCase("YES")){
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
                            CommonPresenter.saveDataInSharePreferences(context, KEY_RELOAD_NEW_DATA_NEWS_YEAR, "NO");
                        }

                        @Override
                        public void onFailure(Call<List<Annee>> call, Throwable t) {
                            iPlaceholder.progressBarVisibility(View.GONE);
                        }
                    });
                }
                else{
                    iPlaceholder.progressBarVisibility(View.GONE);
                }

                // Verify if GoodToKnow data must to be reloaded
                String reloadGoodToKnowData = CommonPresenter.getDataFromSharePreferences(context, KEY_RELOAD_NEW_DATA_GOOD_TO_KNOW);
                if(reloadGoodToKnowData == null || reloadGoodToKnowData.isEmpty() || reloadGoodToKnowData.equalsIgnoreCase("YES")){
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
                            CommonPresenter.saveDataInSharePreferences(context, KEY_RELOAD_NEW_DATA_GOOD_TO_KNOW, "NO");
                        }

                        @Override
                        public void onFailure(Call<List<BonASavoir>> call, Throwable t) {
                            iPlaceholder.progressBarVisibility(View.GONE);
                        }
                    });
                }
                else{
                    iPlaceholder.progressBarVisibility(View.GONE);
                }
            }
            else{
                iPlaceholder.progressBarVisibility(View.GONE);
                View mView = CommonPresenter.getViewInTermsOfContext(context);
                CommonPresenter.showMessageSnackBar(mView, context.getResources().getString(R.string.no_connection));
            }
            //--
            loadFragmentData(context, positionFrag);
        }
        catch (Exception ex){}
    }

    // Refresh all data
    public void loadFragmentData(Context context, int positionFrag){
        try {
            int numberColumn = CommonPresenter.getNumberToDisplay(context);
            switch (positionFrag){
                // VIDEOS
                case 0:
                    ArrayList<Video> videos = CommonPresenter.listeSousMenuVideo();
                    if(videos != null){
                        iPlaceholder.loadSubMenuVideo(videos, numberColumn);
                    }
                    break;

                // AUDIOS
                case 1:
                    ArrayList<Audio> audios = CommonPresenter.listeSousMenuAudio();
                    if(audios != null){
                        iPlaceholder.loadSubMenuAudio(audios, numberColumn);
                    }
                    break;

                // PDF
                case 2:
                    ArrayList<Pdf> pdfs = CommonPresenter.listeSousMenuPdf();
                    if(pdfs != null){
                        iPlaceholder.loadSubMenuPdf(pdfs, numberColumn);
                    }
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
        catch (Exception ex){}
    }

    // Retrieve user action : When he clicks on top menu items
    public void retrieveUserAction(Context context, MenuItem item){
        try {
            CommonPresenter commonPresenter = null;
            switch (item.getItemId()){
                // Share LVE App
                case R.id.action_share:
                    ShareActionProvider shareProvider = new ShareActionProvider(context);
                    CommonPresenter.shareApplication(context, item, shareProvider);
                    break;

                // Search videos
                case R.id.action_search:
                    commonPresenter = new CommonPresenter();
                    commonPresenter.showFormSearch(context);
                    break;

                case R.id.action_config:
                    iHome.launchParameterActivity();
                    break;

                case R.id.action_contact:
                    commonPresenter = new CommonPresenter();
                    commonPresenter.showFormContact(context);
                    break;

                case R.id.action_favorite:
                    iHome.launchFavorisActivity();
                    break;

                case R.id.action_download:
                    iHome.launchDownloadActivity();
                    break;

                case R.id.action_update:
                    CommonPresenter.getLveMarketLink(context);
                    break;
            }
        }
        catch (Exception ex){}
    }

    /**
     * Manage action when files (audios, videos and pdf are correctly downloaded
     * @param context
     */
    public void fileIsDownloadSuccessFully(Context context) {
        //--
        try {
            ArrayList<Favoris> downloadList =  getAllDownloadFilesByKey(context, KEY_DOWNLOAD_FILES_LIST);
            for (int i=0; i<downloadList.size(); i++){
                DAOFavoris daoFavoris = new DAOFavoris(context);
                Favoris favoris = downloadList.get(i);
                if(!daoFavoris.isFavorisExists(favoris.getSrc(), favoris.getType())){
                    daoFavoris.insertData(favoris);
                }
            }
            // Clear download list
            saveDataInSharePreferences(context, KEY_DOWNLOAD_FILES_LIST, "");
        }
        catch (Exception ex){}
    }

    /**
     * Ask before leaving the app
     * @param context
     */
    public void retrieveBackPressedAction(Context context, HomeView.IHome iHome){
        try {
            Setting mSetting = CommonPresenter.getSettingObjectFromSharePreferences(context, KEY_SETTING_CONFIRM_BEFORE_QUIT_APP);
            if(mSetting.getChoice()){
                String title = context.getResources().getString(R.string.lb_title_confirm);
                String message = context.getResources().getString(R.string.lb_msg_confirm);
                CommonPresenter.showConfirmMessage(context, title, message, iHome);
            }
            else{
                iHome.closeActivity();
            }
        }
        catch (Exception ex){}
    }

    public void closeLVEApplication(){
        try {
            iHome.closeActivity();
        }
        catch (Exception ex){}
    }
}
