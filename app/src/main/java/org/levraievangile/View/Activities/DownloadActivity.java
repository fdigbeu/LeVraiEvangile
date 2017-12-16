package org.levraievangile.View.Activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.levraievangile.Model.DownloadFile;
import org.levraievangile.Presenter.DownloadPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Adapters.DownloadPagerAdapter;
import org.levraievangile.View.Fragments.DownloadAudioFragment;
import org.levraievangile.View.Fragments.DownloadPdfFragment;
import org.levraievangile.View.Fragments.DownloadVideoFragment;
import org.levraievangile.View.Interfaces.DownloadView;
import org.levraievangile.View.ViewPagers.DownloadViewPager;

import java.util.ArrayList;
import java.util.Hashtable;

public class DownloadActivity extends AppCompatActivity implements DownloadView.IDownload {

    // Ref interfaces
    private Hashtable<Integer, ArrayList<DownloadFile>> downloadFilesList;
    private DownloadView.IDownloadAudioView iDownloadAudioView;
    private DownloadView.IDownloadVideoView iDownloadVideoView;
    private DownloadView.IDownloadPdfView iDownloadPdfView;

    // Ref widget
    private Toolbar toolbar;
    private TabLayout tabLayout;

    // Ref collections and adapter
    private DownloadPagerAdapter pagerAdapter;
    private ArrayList<Fragment> fragDownloads;
    private ArrayList<String> titleDownload;
    private DownloadViewPager downloadViewPager;

    // Presenter
    private DownloadPresenter downloadPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        // Load download data
        downloadFilesList = new Hashtable<>();
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
        switch (id){
            case android.R.id.home:
                downloadPresenter.retrieveUserAction(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initialize() {
        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabs);

        // Toolbar contents
        titleDownload = new ArrayList<>();
        titleDownload.add(getResources().getString(R.string.tab_text_audio));
        titleDownload.add(getResources().getString(R.string.tab_text_video));
        titleDownload.add(getResources().getString(R.string.tab_text_pdf));

        // ViewPager contents
        fragDownloads = new ArrayList<>();
        fragDownloads.add(Fragment.instantiate(this, DownloadAudioFragment.class.getName()));
        fragDownloads.add(Fragment.instantiate(this, DownloadVideoFragment.class.getName()));
        fragDownloads.add(Fragment.instantiate(this, DownloadPdfFragment.class.getName()));
        this.pagerAdapter  = new DownloadPagerAdapter(super.getSupportFragmentManager(), fragDownloads, titleDownload);

        // Set up the ViewPager with the sections adapter.
        downloadViewPager = findViewById(R.id.container);
        downloadViewPager.setAdapter(this.pagerAdapter);
        downloadViewPager.setPagingEnabled(true);

        // TabLayout
        tabLayout.setupWithViewPager(downloadViewPager);

        // Display Home Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void events() {
        // Download View Pager
        downloadViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public void closeActivity() {
        downloadPresenter.cancelAsyntask();
        this.finish();
    }

    @Override
    public void onBackPressed() {
        downloadPresenter.cancelAsyntask();
        super.onBackPressed();
    }

    // Ref interfaces instanciation
    public void instanciateIDownloadAudioView(DownloadView.IDownloadAudioView iDownloadAudioView) {
        this.iDownloadAudioView = iDownloadAudioView;
    }

    public void instanciateIDownloadVideoView(DownloadView.IDownloadVideoView iDownloadVideoView) {
        this.iDownloadVideoView = iDownloadVideoView;
    }

    public void instanciateIDownloadPdfView(DownloadView.IDownloadPdfView iDownloadPdfView) {
        this.iDownloadPdfView = iDownloadPdfView;
    }

    // Storage download files list
    @Override
    public void storageDownloadFilesList(int key,  ArrayList<DownloadFile> downloadFilesList) {
        this.downloadFilesList.put(key, downloadFilesList);
    }
    // Get Storage download audios files
    @Override
    public ArrayList<DownloadFile> getStorageDownloadFilesAudioData(){
        try {
            return downloadFilesList.get(0);
        }
        catch (Exception ex){}
        return null;
    }
    // Get Storage download videos files
    @Override
    public ArrayList<DownloadFile> getStorageDownloadFilesVideoData(){
        try {
            return downloadFilesList.get(1);
        }
        catch (Exception ex){}
        return null;
    }
    // Get Storage download pdf files
    @Override
    public ArrayList<DownloadFile> getStorageDownloadFilesPdfData(){
        try {
            return downloadFilesList.get(2);
        }
        catch (Exception ex){}
        return null;
    }
}
