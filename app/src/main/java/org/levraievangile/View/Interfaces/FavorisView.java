package org.levraievangile.View.Interfaces;

import android.view.View;

import org.levraievangile.Model.Favoris;

import java.util.ArrayList;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class FavorisView {
    public interface IFravoris{
        public void initialize();
        public void events();
        public void closeActivity();
    }

    public interface IPlaceholder{
        public void initialize(View rootView);
        public void events();
        public void loadFavorisData(ArrayList<Favoris> favoris, int numberColumns);
        public void progressBarVisibility(int visibility);
    }

    public interface IPresenter{}
}
