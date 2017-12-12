package org.levraievangile.Model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import org.levraievangile.View.Interfaces.DownloadView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class LoadDownloadVideo extends AsyncTask<Void, Void, ArrayList<DownloadFile>> {

    private  Context context;
    private DownloadView.ILoadDownload iLoadDownload;

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
        ContentResolver contentResolver = context.getContentResolver();
        ArrayList<DownloadFile> videoList = null;
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        //String selection = MediaStore.Video.Media.IS_VIDEO + "!= 0";
        String sortOrder = MediaStore.Video.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, null, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            videoList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                // Save to videoList
                videoList.add(new DownloadFile(data, title, album, artist, duration));
            }
        }
        cursor.close();
        return videoList;
    }

    public void initializeData(Context context, DownloadView.ILoadDownload iLoadDownload){
        this.context = context;
        this.iLoadDownload = iLoadDownload;
    }
}
