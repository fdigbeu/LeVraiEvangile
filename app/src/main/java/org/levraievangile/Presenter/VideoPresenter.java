package org.levraievangile.Presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import org.levraievangile.Model.ApiClient;
import org.levraievangile.Model.Video;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.VideoView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.levraievangile.Presenter.CommonPresenter.KEY_ALL_VIDEOS_LIST;
import static org.levraievangile.Presenter.CommonPresenter.KEY_FORM_SEARCH_WORD;
import static org.levraievangile.Presenter.CommonPresenter.KEY_IS_USER_LEVEL_ADMIN;
import static org.levraievangile.Presenter.CommonPresenter.KEY_SHORT_CODE;

/**
 * Created by Maranatha on 06/12/2017.
 */

public class VideoPresenter {
    // Ref interface
    private VideoView.IVideo iVideo;

    // Ref interface retrofit
    private VideoView.IApiRessource iApiRessource;

    public VideoPresenter(VideoView.IVideo iVideo) {
        this.iVideo = iVideo;
    }

    public void loadVideoData(final Context context, Intent intent){
        iVideo.initialize();
        iVideo.events();
        iVideo.askPermissionToSaveFile();
        iVideo.progressBarVisibility(View.VISIBLE);
        //--
        if(intent != null && intent.getStringExtra(KEY_FORM_SEARCH_WORD) == null) {
            try {
                String userLevel = CommonPresenter.getDataFromSharePreferences(context, KEY_IS_USER_LEVEL_ADMIN);
                String shortCodeLevel = userLevel.equalsIgnoreCase("YES") ? "/acces-admin" : "";
                //Get list video by short-code
                final String shortCode = intent.getStringExtra(KEY_SHORT_CODE);
                iVideo.modifyHeaderInfos(CommonPresenter.getLibelleByTypeShortcode(shortCode));
                if(CommonPresenter.isMobileConnected(context)) {
                    Call<List<Video>> callVideos = ApiClient.getApiClientLeVraiEvangile().create(VideoView.IApiRessource.class).getAllVideos(shortCode+shortCodeLevel);
                    callVideos.enqueue(new Callback<List<Video>>() {
                        @Override
                        public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                            ArrayList<Video> videos = (ArrayList<Video>) response.body();
                            final String keyShortCode = KEY_ALL_VIDEOS_LIST + "-" + shortCode;
                            if(videos != null) {
                                CommonPresenter.saveDataInSharePreferences(context, keyShortCode, videos.toString());
                            }
                            iVideo.loadVideoData(videos, 1);
                            iVideo.progressBarVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<List<Video>> call, Throwable t) {
                            String key = KEY_ALL_VIDEOS_LIST+"-"+shortCode;
                            ArrayList<Video> videos = CommonPresenter.getAllVideosByKey(context, key);
                            iVideo.loadVideoData(videos, 1);
                            iVideo.progressBarVisibility(View.GONE);
                        }
                    });
                }
                else{
                    String key = KEY_ALL_VIDEOS_LIST+"-"+shortCode;
                    ArrayList<Video> videos = CommonPresenter.getAllVideosByKey(context, key);
                    iVideo.loadVideoData(videos, 1);
                    iVideo.progressBarVisibility(View.GONE);
                    //--
                    if(videos.size()==0){
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
            Call<List<Video>> callVideos = ApiClient.getApiClientLeVraiEvangile().create(VideoView.IApiRessource.class).getAllSearchVideos(keyWord);
            callVideos.enqueue(new Callback<List<Video>>() {
                @Override
                public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                    ArrayList<Video> videos = (ArrayList<Video>) response.body();
                    iVideo.loadVideoData(videos, 1);
                    iVideo.progressBarVisibility(View.GONE);
                    //--
                    iVideo.modifyBarHeader("Recherche de vidéos", "TOTAL : "+videos.size()+" | MOT CLÉ : "+keyWord);
                }

                @Override
                public void onFailure(Call<List<Video>> call, Throwable t) {
                    iVideo.progressBarVisibility(View.GONE);
                    iVideo.modifyBarHeader("Recherche de vidéos", "Aucune vidéo trouvée | MOT CLÉ : "+keyWord);
                }
            });
        }
    }

    // Manage menu Item
    public void retrieveUserAction(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                iVideo.closeActivity();
                break;
        }
    }

    /**
     * Scroll video data items to positon
     * @param position
     */
    public void srcollVideoDataItemsToPosition(int position){
        iVideo.scrollVideoDataToPosition(position);
    }

    /**
     * Play next video from VideoRecyclerAdapter
     * @param iVideoRecycler
     */
    public void playNextVideoInPlayer(VideoView.IVideoRecycler iVideoRecycler){
        if(iVideoRecycler != null){
            iVideoRecycler.playNextVideo();
        }
    }

    /**
     * Play previous video from VideoRecyclerAdapter
     * @param iVideoRecycler
     */
    public void playPreviousVideoInPlayer(VideoView.IVideoRecycler iVideoRecycler){
        if(iVideoRecycler != null){
            iVideoRecycler.playPreviousVideo();
        }
    }


    // Set VideoActivity VideoRecyclerAdapteur Attribute
    public void retrieveAndSetIVideoRecyclerReference(VideoView.IVideoRecycler iVideoRecycler){
        if(iVideo != null){
            iVideo.instanciateIVideoRecycler(iVideoRecycler);
        }
    }

    /**
     * Play video player
     * @param context
     * @param video
     * @param position
     */
    public void playLVEVideoPlayer(Context context, Video video, int position){
        if(CommonPresenter.isMobileConnected(context)){
            iVideo.launchVideoToPlay(video, position);
        }
        else{
            // Display no connection message
            CommonPresenter.showNoConnectionMessage(context, true);
        }
    }
}
