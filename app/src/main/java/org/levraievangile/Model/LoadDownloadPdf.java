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

public class LoadDownloadPdf extends AsyncTask<Void, Void, ArrayList<DownloadFile>> {

    private  Context context;
    private DownloadView.ILoadDownload iLoadDownload;
    ArrayList<DownloadFile> pdfList = null;

    @Override
    protected void onPreExecute() {
        iLoadDownload.downloadVideoStarted();
        super.onPreExecute();
    }

    @Override
    protected ArrayList<DownloadFile> doInBackground(Void... voids) {
        ArrayList<DownloadFile> downloadFiles = pdfsFiles(context);
        return downloadFiles;
    }

    @Override
    protected void onPostExecute(ArrayList<DownloadFile> downloadFiles) {
        super.onPostExecute(downloadFiles);
        iLoadDownload.downloadPdfFinished(downloadFiles);
    }

    private ArrayList<DownloadFile> pdfsFiles(Context context) {
        // Get Lve pdf saved
        pdfList = new ArrayList<>();
        DAOFavoris daoFavoris = new DAOFavoris(context);
        ArrayList<Favoris> mList = daoFavoris.getAllData("download_pdf");
        if(mList != null && mList.size() > 0){
            for (int i=mList.size()-1; i > 0; i--){
                Favoris favoris = mList.get(i);
                String data = CommonPresenter.getVideoPath()+"/"+favoris.getSrc();
                String title = favoris.getTitre();
                String album = favoris.getType_libelle();
                String artist = favoris.getAuteur();
                String duration = favoris.getDuree();
                int mipmap = CommonPresenter.getMipmapByTypeShortcode(favoris.getType_shortcode());
                String shortcode = "download-pdf";
                String date = favoris.getDate();
                pdfList.add(new DownloadFile(data, title, album, artist, duration, mipmap, date, shortcode));
            }
        }
        //--
        return pdfList;
    }

    public void initializeData(Context context, DownloadView.ILoadDownload iLoadDownload){
        this.context = context;
        this.iLoadDownload = iLoadDownload;
    }
}