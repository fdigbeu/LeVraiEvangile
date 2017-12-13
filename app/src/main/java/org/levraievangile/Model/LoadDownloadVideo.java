package org.levraievangile.Model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.DownloadView;

import java.util.ArrayList;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class LoadDownloadVideo extends AsyncTask<Void, Void, ArrayList<DownloadFile>> {

    private  Context context;
    private DownloadView.ILoadDownload iLoadDownload;
    ArrayList<DownloadFile> videoList = null;

    @Override
    protected void onPreExecute() {
        iLoadDownload.downloadVideoStarted();
        super.onPreExecute();
    }

    @Override
    protected ArrayList<DownloadFile> doInBackground(Void... voids) {
        ArrayList<DownloadFile> downloadFiles = videosFiles(context);
        return downloadFiles;
    }

    @Override
    protected void onPostExecute(ArrayList<DownloadFile> downloadFiles) {
        super.onPostExecute(downloadFiles);
        iLoadDownload.downloadVideoFinished(downloadFiles);
    }

    private ArrayList<DownloadFile> videosFiles(Context context) {
        // Get Lve video saved
        videoList = new ArrayList<>();
        DAOFavoris daoFavoris = new DAOFavoris(context);
        ArrayList<Favoris> mList = daoFavoris.getAllData("download_video");
        if(mList != null && mList.size() > 0){
            for (int i=mList.size()-1; i > 0; i--){
                Favoris favoris = mList.get(i);
                String data = CommonPresenter.getVideoPath()+"/"+favoris.getSrc();
                String title = favoris.getTitre();
                String album = favoris.getType_libelle();
                String artist = favoris.getAuteur();
                String duration = favoris.getDuree();
                String shortcode = "download-video";
                int mipmap = CommonPresenter.getMipmapByTypeShortcode(favoris.getType_shortcode());
                String date = favoris.getDate();
                videoList.add(new DownloadFile(data, title, album, artist, duration, mipmap, date, shortcode));
            }
        }
        //--
        //getAllVideosStorage();
        //--
        return videoList;
    }

    private void getAllVideosStorage(){
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC";
        Cursor cursor = contentResolver.query(uri, null, null, null, sortOrder);
        //--
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String date = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
                // Save to videoList
                if(!data.contains("LVE/Videos/")) {
                    videoList.add(new DownloadFile(data, title, album, artist, CommonPresenter.getHourMinuteSecondBy(Integer.parseInt(duration)), R.mipmap.sm_video, date, "download-video"));
                }
            }
        }
        cursor.close();
    }

    public void initializeData(Context context, DownloadView.ILoadDownload iLoadDownload){
        this.context = context;
        this.iLoadDownload = iLoadDownload;
    }
}
