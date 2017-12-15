package org.levraievangile.Presenter;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;

import org.levraievangile.Model.DAOFavoris;
import org.levraievangile.Model.Favoris;
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
}
