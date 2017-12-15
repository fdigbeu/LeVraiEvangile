package org.levraievangile.Model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        if(mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                Favoris favoris = mList.get(i);
                String data = CommonPresenter.getVideoPath() + "/" + favoris.getSrc();
                // Verify if file is always in the mobile
                if(CommonPresenter.isFileExists(data)) {
                    String title = favoris.getTitre();
                    String album = favoris.getType_libelle();
                    String artist = favoris.getAuteur();
                    String duration = favoris.getDuree();
                    String shortcode = favoris.getType_shortcode();
                    int mipmap = CommonPresenter.getMipmapByTypeShortcode(shortcode);
                    String date = favoris.getDate();
                    videoList.add(new DownloadFile(data, title, album, artist, duration, mipmap, date, shortcode));
                }
                else{
                    // Delete in the list
                    DAOFavoris mFavorisItem = new DAOFavoris(context);
                    mFavorisItem.deleteDataBy(favoris.getId());
                }
            }
        }
        //--
        getAllVideosStorage(context);
        //--
        return videoList;
    }

    private void getAllVideosStorage(Context context){
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Video.Media.DATE_MODIFIED + " DESC";
        Cursor cursor = contentResolver.query(uri, null, null, null, sortOrder);
        //--
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String date = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED));
                long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                Bitmap bitmap = getAlbumart(context, albumId);
                if(bitmap == null){
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.sm_video);
                }
                // Save to videoList
                if(!data.contains("LVE/Videos/")) {
                    videoList.add(new DownloadFile(data, title, album, artist, CommonPresenter.getHourMinuteSecondBy(Integer.parseInt(duration)), R.mipmap.sm_video, date, "download-video", bitmap));
                }
            }
        }
        cursor.close();
    }

    public void initializeData(Context context, DownloadView.ILoadDownload iLoadDownload){
        this.context = context;
        this.iLoadDownload = iLoadDownload;
    }

    private Bitmap getAlbumart(Context context, long resourceId) {
        try {

            Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(), resourceId, MediaStore.Video.Thumbnails.MICRO_KIND, null);
            return bitmap;
        }
        catch (Exception ex){}
        return null;
    }
}
