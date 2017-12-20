package org.levraievangile.Presenter;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.levraievangile.Model.DAOFavoris;
import org.levraievangile.Model.Favoris;
import org.levraievangile.Model.Video;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.FavorisView;

import java.util.ArrayList;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class FavorisPresenter {
    // Ref interface
    private FavorisView.IFravoris iFravoris;

    private FavorisView.IPlaceholder iPlaceholder;

    // Constructor
    public FavorisPresenter(FavorisView.IFravoris iFravoris) {
        this.iFravoris = iFravoris;
    }

    public FavorisPresenter(FavorisView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
    }

    // Load favoris data
    public void loadFavorisData(Context context){
        iFravoris.initialize();
        iFravoris.events();
    }



    public void loadPlaceHolderData(Context context, View rootView, int positionFrag){
        iPlaceholder.initialize(rootView);
        iPlaceholder.events();
        iPlaceholder.progressBarVisibility(View.VISIBLE);
        //--
        loadFragmentData(context, positionFrag);
    }

    // Refresh all data
    public void loadFragmentData(Context context, int positionFrag){

        // Load Audio files
        switch (positionFrag){
            // AUDIOS
            case 0:
                DAOFavoris daoAudioFavoris = new DAOFavoris(context);
                ArrayList<Favoris> audioList = daoAudioFavoris.getAllData("audio");
                iPlaceholder.loadFavorisAudioData(audioList, 1);
                iPlaceholder.progressBarVisibility(View.GONE);
                break;
            // VIDEOS
            case 1:
                DAOFavoris daoVideoFavoris = new DAOFavoris(context);
                ArrayList<Favoris> videoList = daoVideoFavoris.getAllData("video");
                iPlaceholder.loadFavorisAudioData(videoList, 1);
                iPlaceholder.progressBarVisibility(View.GONE);
                break;
            // PDF
            case 2:
                DAOFavoris daoPdfFavoris = new DAOFavoris(context);
                ArrayList<Favoris> pdfList = daoPdfFavoris.getAllData("pdf");
                iPlaceholder.loadFavorisAudioData(pdfList, 1);
                iPlaceholder.progressBarVisibility(View.GONE);
                break;

        }
    }

    // Manage menu Item
    public void retrieveUserAction(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                iFravoris.closeActivity();
                break;
        }
    }

    // Launch activity
    public void launchActivity(String value){
        iPlaceholder.launchActivity(value);
    }

    /**
     * Play video player
     * @param context
     * @param video
     * @param position
     */
    public void playLVEVideoPlayer(Context context, Video video, int position){
        if(CommonPresenter.isMobileConnected(context)){
            iPlaceholder.launchVideoToPlay(video, position);
        }
        else{
            String title = context.getResources().getString(R.string.no_connection);
            String message = context.getResources().getString(R.string.detail_no_connection);
            CommonPresenter.showMessage(context, title.toUpperCase(), message, false);
        }
    }

    /**
     * Scroll video data items to positon
     * @param position
     */
    public void srcollVideoDataItemsToPosition(int position){
        iPlaceholder.scrollVideoDataToPosition(position);
    }

    /**
     * Play next video from FavorisRecyclerAdapter
     * @param iFavorisRecycler
     */
    public void playNextVideoInPlayer(FavorisView.IFavorisRecycler iFavorisRecycler){
        if(iFavorisRecycler != null){
            iFavorisRecycler.playNextVideo();
            Log.i("TAG_NEXT_VIDEO", "TAG_NEXT_VIDEO : iFavorisRecycler != null");
        }
    }

    /**
     * Play previous video from FavorisRecyclerAdapter
     * @param iFavorisRecycler
     */
    public void playPreviousVideoInPlayer(FavorisView.IFavorisRecycler iFavorisRecycler){
        if(iFavorisRecycler != null){
            iFavorisRecycler.playPreviousVideo();
            Log.i("TAG_PREVIOUS_VIDEO", "TAG_PREVIOUS_VIDEO : iFavorisRecycler != null");
        }
    }

    // Set FavorisActivity FavorisRecyclerAdapter Attribute
    public void retrieveAndSetIFavorisRecyclerReference(FavorisView.IFavorisRecycler iFavorisRecycler){
        if(iPlaceholder != null){
            iPlaceholder.instanciateIFavorisRecycler(iFavorisRecycler);
        }
    }
}
