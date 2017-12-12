package org.levraievangile.View.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import org.levraievangile.Model.Annee;
import org.levraievangile.Model.Audio;
import org.levraievangile.Model.BonASavoir;
import org.levraievangile.Model.Pdf;
import org.levraievangile.Model.Video;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.Presenter.HomePresenter;
import org.levraievangile.R;
import org.levraievangile.View.Adapters.AudioRecyclerAdapter;
import org.levraievangile.View.Adapters.GoodToKnowRecyclerAdapter;
import org.levraievangile.View.Adapters.NewsRecyclerAdapter;
import org.levraievangile.View.Adapters.PdfRecyclerAdapter;
import org.levraievangile.View.Adapters.VideoRecyclerAdapter;
import org.levraievangile.View.Interfaces.HomeView;

import java.util.ArrayList;

import static org.levraievangile.Presenter.CommonPresenter.KEY_SHORT_CODE;
import static org.levraievangile.Presenter.CommonPresenter.VALUE_PERMISSION_TO_SAVE_FILE;

public class HomeActivity extends AppCompatActivity implements HomeView.IHome {

    // Ref widgets
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    // Ref presenter
    private HomePresenter homePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Load home Data
        homePresenter = new HomePresenter(this);
        homePresenter.loadHomeData(HomeActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_search:
                homePresenter.retrieveUserAction(HomeActivity.this, item);
                return true;

            case R.id.action_share:
                homePresenter.retrieveUserAction(HomeActivity.this, item);
                return true;

            case R.id.action_config:
                homePresenter.retrieveUserAction(HomeActivity.this, item);
                return true;

            case R.id.action_contact:
                homePresenter.retrieveUserAction(HomeActivity.this, item);
                return true;

            case R.id.action_favorite:
                homePresenter.retrieveUserAction(HomeActivity.this, item);
                return true;

            case R.id.action_download:
                homePresenter.retrieveUserAction(HomeActivity.this, item);
                return true;

            case R.id.action_update:
                homePresenter.retrieveUserAction(HomeActivity.this, item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initialize() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = findViewById(R.id.tabs);
    }

    @Override
    public void events() {
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    @Override
    public void askPermissionToSaveFile() {
        int permissionCheck = ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, VALUE_PERMISSION_TO_SAVE_FILE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case VALUE_PERMISSION_TO_SAVE_FILE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Ceate folders
                    CommonPresenter.createFolder();
                }
                else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.lb_storage_file_require), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements HomeView.IPlaceholder {
        // Ref widgets
        private ProgressBar progressBar;
        private RecyclerView recyclerView;
        // Presenter
        private HomePresenter homePresenter;
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
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);

            // Show data
            fragNumber = getArguments().getInt(ARG_SECTION_NUMBER)-1;
            homePresenter = new HomePresenter(this);
            homePresenter.loadPlaceHolderData(getActivity(), rootView, fragNumber);

            return rootView;
        }

        @Override
        public void initialize(View rootView) {
            progressBar = rootView.findViewById(R.id.subMenuProgressBar);
            recyclerView = rootView.findViewById(R.id.subMenuRecyclerView);
        }

        @Override
        public void events() {

        }

        @Override
        public void loadSubMenuVideo(ArrayList<Video> videos, int numberColumns) {
            GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
            recyclerView.setLayoutManager(gridLayout);
            recyclerView.setHasFixedSize(true);
            int resLayout = R.layout.item_submenu;
            VideoRecyclerAdapter adapter = new VideoRecyclerAdapter(videos, resLayout, this);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void loadSubMenuAudio(ArrayList<Audio> audios, int numberColumns) {
            GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
            recyclerView.setLayoutManager(gridLayout);
            recyclerView.setHasFixedSize(true);
            int resLayout = R.layout.item_submenu;
            AudioRecyclerAdapter adapter = new AudioRecyclerAdapter(audios, resLayout, this);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void loadSubMenuPdf(ArrayList<Pdf> pdfs, int numberColumns) {
            GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
            recyclerView.setLayoutManager(gridLayout);
            recyclerView.setHasFixedSize(true);
            int resLayout = R.layout.item_submenu;
            PdfRecyclerAdapter adapter = new PdfRecyclerAdapter(pdfs, resLayout, this);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void loadSubMenuNewsYears(ArrayList<Annee> years, int numberColumns) {
            GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
            recyclerView.setLayoutManager(gridLayout);
            recyclerView.setHasFixedSize(true);
            NewsRecyclerAdapter adapter = new NewsRecyclerAdapter(years, R.layout.item_submenu, this);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void loadSubMenuGoodToKnow(ArrayList<BonASavoir> goodToKnows, int numberColumns) {
            GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
            recyclerView.setLayoutManager(gridLayout);
            recyclerView.setHasFixedSize(true);
            GoodToKnowRecyclerAdapter adapter = new GoodToKnowRecyclerAdapter(goodToKnows, this);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void progressBarVisibility(int visibility) {
            progressBar.setVisibility(visibility);
        }

        @Override
        public void launchActivity(String value, Class destination) {
            Intent intent = new Intent(getActivity(), destination);
            intent.putExtra(KEY_SHORT_CODE, value);
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }
    }

    @Override
    public void launchParameterActivity(){
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void launchDownloadActivity(){
        Intent intent = new Intent(HomeActivity.this, DownloadActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void launchFavorisActivity(){
        Intent intent = new Intent(HomeActivity.this, FavorisActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
