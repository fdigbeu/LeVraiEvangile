package org.levraievangile.View.Activities;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import org.levraievangile.Model.DownloadFile;
import org.levraievangile.Model.Favoris;
import org.levraievangile.Presenter.DownloadPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Adapters.DownloadRecyclerAdapter;
import org.levraievangile.View.Interfaces.DownloadView;

import java.util.ArrayList;

public class DownloadActivity extends AppCompatActivity implements DownloadView.IDownload {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    // Presenter
    private DownloadPresenter downloadPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        // Load download data
        downloadPresenter = new DownloadPresenter(this);
        downloadPresenter.loadDownloadData(DownloadActivity.this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
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
    }

    @Override
    public void events() {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements DownloadView.IPlaceholder{

        private RecyclerView downloadRecyclerView;
        private ProgressBar downloadProgressBar;
        // Presenter
        private DownloadPresenter downloadPresenter;
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
            View rootView = inflater.inflate(R.layout.fragment_download, container, false);

            // Show data
            fragNumber = getArguments().getInt(ARG_SECTION_NUMBER)-1;
            downloadPresenter = new DownloadPresenter(this);
            downloadPresenter.loadPlaceHolderData(getActivity(), rootView, fragNumber);

            return rootView;
        }

        @Override
        public void initialize(View rootView) {
            downloadRecyclerView = rootView.findViewById(R.id.downloadRecyclerView);
            downloadProgressBar = rootView.findViewById(R.id.downloadProgressBar);
        }

        @Override
        public void events() {

        }

        @Override
        public void loadDownloadAudioData(ArrayList<DownloadFile> downloads, int numberColumns) {
            /*GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
            downloadRecyclerView.setLayoutManager(gridLayout);
            downloadRecyclerView.setHasFixedSize(true);
            DownloadRecyclerAdapter adapter = new DownloadRecyclerAdapter(downloads, this);
            downloadRecyclerView.setAdapter(adapter);*/
            //--
            if(downloads != null && downloads.size() > 0){
                for (int i=0; i<downloads.size(); i++){
                    Log.i("TAG_AUDIO_DOWNLOAD", "loadDownloadAudioData("+i+") : "+downloads.get(i).toString());
                }
            }
        }

        @Override
        public void loadDownloadVideoData(ArrayList<DownloadFile> downloads, int numberColumns) {
            if(downloads != null && downloads.size() > 0){
                for (int i=0; i<downloads.size(); i++){
                    Log.i("TAG_VIDEO_DOWNLOAD", "loadDownloadVideoData("+i+") : "+downloads.get(i).toString());
                }
            }
        }

        @Override
        public void loadDownloadPdfData(ArrayList<DownloadFile> downloads, int numberColumns) {

        }

        @Override
        public void progressBarVisibility(int visibility) {
            downloadProgressBar.setVisibility(visibility);
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
            return 3;
        }
    }
}
