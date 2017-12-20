package org.levraievangile.View.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.TextView;

import org.levraievangile.Model.Favoris;
import org.levraievangile.Model.Video;
import org.levraievangile.Presenter.FavorisPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Adapters.FavorisRecyclerAdapter;
import org.levraievangile.View.Interfaces.FavorisView;

import java.util.ArrayList;

import static org.levraievangile.Presenter.CommonPresenter.KEY_SHORT_CODE;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_POSITION_VIDEO_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_VIDEO_PLAY_NEXT;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_VIDEO_PLAY_PREVIOUS;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_PLAYER_RETURN_DATA;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_PLAYER_SEND_DATA;
import static org.levraievangile.Presenter.CommonPresenter.VALUE_VIDEO_SELECTED_REQUEST_CODE;

public class FavorisActivity extends AppCompatActivity implements FavorisView.IFravoris {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    // Presenter
    private FavorisPresenter favorisPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);
        // Load data favoris
        favorisPresenter = new FavorisPresenter(this);
        favorisPresenter.loadFavorisData(FavorisActivity.this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favoris, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                favorisPresenter.retrieveUserAction(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initialize() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        // Display Home Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void events() {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements FavorisView.IPlaceholder {
        // Ref widgets
        private RecyclerView favorisRecyclerView;
        private ProgressBar favorisProgressBar;
        // Ref IFavorisRecycler
        private FavorisView.IFavorisRecycler iFavorisRecycler;
        // Presenter
        private FavorisPresenter favorisPresenter;
        //--
        private int fragNumber;
        
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_favoris, container, false);

            // Show data
            fragNumber = getArguments().getInt(ARG_SECTION_NUMBER)-1;
            favorisPresenter = new FavorisPresenter(this);
            favorisPresenter.loadPlaceHolderData(getActivity(), rootView, fragNumber);
            return rootView;
        }

        @Override
        public void initialize(View rootView) {
            favorisRecyclerView = rootView.findViewById(R.id.favorisRecyclerView);
            favorisProgressBar = rootView.findViewById(R.id.favorisProgressBar);
        }

        @Override
        public void events() {

        }

        @Override
        public void loadFavorisAudioData(ArrayList<Favoris> favorites, int numberColumns) {
            GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
            favorisRecyclerView.setLayoutManager(gridLayout);
            favorisRecyclerView.setHasFixedSize(true);
            FavorisRecyclerAdapter adapter = new FavorisRecyclerAdapter(favorites, this);
            favorisRecyclerView.setAdapter(adapter);
        }

        @Override
        public void loadFavorisVideoData(ArrayList<Favoris> favorites, int numberColumns) {
            GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
            favorisRecyclerView.setLayoutManager(gridLayout);
            favorisRecyclerView.setHasFixedSize(true);
            FavorisRecyclerAdapter adapter = new FavorisRecyclerAdapter(favorites, this);
            favorisRecyclerView.setAdapter(adapter);
        }

        @Override
        public void loadFavorisPdfData(ArrayList<Favoris> favorites, int numberColumns) {
            GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
            favorisRecyclerView.setLayoutManager(gridLayout);
            favorisRecyclerView.setHasFixedSize(true);
            FavorisRecyclerAdapter adapter = new FavorisRecyclerAdapter(favorites, this);
            favorisRecyclerView.setAdapter(adapter);
        }

        @Override
        public void scrollVideoDataToPosition(int positionScroll) {
            favorisRecyclerView.scrollToPosition(positionScroll);
        }

        @Override
        public void progressBarVisibility(int visibility) {
            favorisProgressBar.setVisibility(visibility);
        }


        @Override
        public void launchActivity(String value) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra(KEY_SHORT_CODE, value);
            startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public void launchVideoToPlay(Video video, int position) {
            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtra(KEY_VIDEO_PLAYER_SEND_DATA, video);
            intent.putExtra(KEY_VALUE_POSITION_VIDEO_SELECTED, position);
            startActivityForResult(intent, VALUE_VIDEO_SELECTED_REQUEST_CODE);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == VALUE_VIDEO_SELECTED_REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    String result = data.getStringExtra(KEY_VIDEO_PLAYER_RETURN_DATA);
                    switch (result){
                        case KEY_VALUE_VIDEO_PLAY_NEXT:
                            favorisPresenter.playNextVideoInPlayer(iFavorisRecycler);
                            break;

                        case KEY_VALUE_VIDEO_PLAY_PREVIOUS:
                            favorisPresenter.playPreviousVideoInPlayer(iFavorisRecycler);
                            break;
                    }
                }
                else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.i("TAG_VIDEO_FRAGMENT", "Activity.RESULT_CANCELED = "+requestCode);
                    Log.i("TAG_VIDEO_FRAGMENT", "Activity.RESULT_CANCELED = "+resultCode);
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public void instanciateIFavorisRecycler(FavorisView.IFavorisRecycler iFavorisRecycler) {
            this.iFavorisRecycler = iFavorisRecycler;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    @Override
    public void closeActivity() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
