package org.levraievangile.Presenter;

import android.content.Context;

import org.levraievangile.View.Interfaces.FavorisView;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class FavorisPresenter {
    // Ref interface
    private FavorisView.IFravoris iFravoris;

    // Constructor
    public FavorisPresenter(FavorisView.IFravoris iFravoris) {
        this.iFravoris = iFravoris;
    }

    // Load favoris data
    public void loadFavorisData(Context context){
        iFravoris.initialize();
        iFravoris.events();
    }
}
