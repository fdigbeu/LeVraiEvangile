package org.levraievangile.Model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.DownloadView;

import java.io.FileDescriptor;
import java.util.ArrayList;

import static org.levraievangile.Presenter.CommonPresenter.VALUE_MEDIA_EXTERNAL_AUDIO_ALBUMART;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class LoadDownloadAudio extends AsyncTask<Void, Void, ArrayList<DownloadFile>> {

    private  Context context;
    private DownloadView.ILoadDownload iLoadDownload;
    private ArrayList<DownloadFile> audioList = null;

    @Override
    protected void onPreExecute() {
        iLoadDownload.downloadAudioStarted();
        super.onPreExecute();
    }

    @Override
    protected ArrayList<DownloadFile> doInBackground(Void... voids) {
        ArrayList<DownloadFile> downloadFiles = audiosFiles(context);
        return downloadFiles;
    }

    @Override
    protected void onPostExecute(ArrayList<DownloadFile> downloadFiles) {
        super.onPostExecute(downloadFiles);
        iLoadDownload.downloadAudioFinished(downloadFiles);
    }

    private ArrayList<DownloadFile> audiosFiles(Context context) {
        // Get Lve audio saved
        audioList = new ArrayList<>();
        DAOFavoris daoFavoris = new DAOFavoris(context);
        ArrayList<Favoris> mList = daoFavoris.getAllData("download_audio");
        if(mList != null && mList.size() > 0){
            for (int i=0; i < mList.size(); i++){
                Favoris favoris = mList.get(i);
                String data = CommonPresenter.getAudioPath()+"/"+favoris.getSrc();
                // Verify if file is always in the mobile
                if(CommonPresenter.isFileExists(data)) {
                    String title = favoris.getTitre();
                    String album = favoris.getType_libelle();
                    String artist = favoris.getAuteur();
                    String duration = favoris.getDuree();
                    String shortcode = favoris.getType_shortcode();
                    int mipmap = CommonPresenter.getMipmapByTypeShortcode(shortcode);
                    String date = favoris.getDate();
                    audioList.add(new DownloadFile(data, title, album, artist, duration, mipmap, date, shortcode, null));
                }
                else{
                    // Delete in the list
                    DAOFavoris mFavorisItem = new DAOFavoris(context);
                    mFavorisItem.deleteDataBy(favoris.getId());
                }
            }
        }
        //--
        getAllAudiosStorage(context);
        //--
        return audioList;
    }

    private void getAllAudiosStorage(Context context){
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.DATE_MODIFIED + " DESC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);
        //--
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String date = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED));
                long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                Bitmap bitmap = getAlbumart(context, albumId);
                if(bitmap == null){
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.sm_audio);
                }
                // Save to audioList;
                if(!data.contains("LVE/Audios/")) {
                    audioList.add(new DownloadFile(data, title, album, artist, CommonPresenter.getHourMinuteSecondBy(Integer.parseInt(duration)), R.mipmap.sm_audio, date, "download-audio", bitmap));
                }
            }
        }
        cursor.close();
    }

    public void initializeData(Context context, DownloadView.ILoadDownload iLoadDownload){
        this.context = context;
        this.iLoadDownload = iLoadDownload;
    }

    private Bitmap getAlbumart(Context context, Long album_id) {
        Bitmap albumArtBitMap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {

            final Uri sArtworkUri = Uri.parse(VALUE_MEDIA_EXTERNAL_AUDIO_ALBUMART);
            Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();
                albumArtBitMap = BitmapFactory.decodeFileDescriptor(fd, null,options);
                pfd = null;
                fd = null;
            }
        } catch (Error ee) {
        } catch (Exception e) {
        }

        if (null != albumArtBitMap) {
            return albumArtBitMap;
        }
        return null;
    }
}
