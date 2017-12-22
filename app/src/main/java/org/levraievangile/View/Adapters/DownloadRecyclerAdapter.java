package org.levraievangile.View.Adapters;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.levraievangile.Model.Audio;
import org.levraievangile.Model.DownloadFile;
import org.levraievangile.Model.Video;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.Presenter.DownloadPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.DownloadView;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Maranatha on 10/10/2017.
 */

public class DownloadRecyclerAdapter extends RecyclerView.Adapter<DownloadRecyclerAdapter.MyViewHolder> implements DownloadView.IDownloadVideoRecycler, DownloadView.IDownloadAudioRecycler, DownloadView.IDownloadPdfRecycler {

    private ArrayList<DownloadFile> downloadItems;
    private Hashtable<Integer, MyViewHolder> mViewHolder;
    private DownloadView.IDownloadAudioView iDownloadAudioView;
    private DownloadView.IDownloadVideoView iDownloadVideoView;
    private DownloadView.IDownloadPdfView iDownloadPdfView;
    private DownloadView.IDownload iDownload;
    private int positionSelected = -1;
    private int previousVideoPosition = -1;
    private int nextVideoProsition = -1;
    private int previousAudioPosition = -1;
    private int nextAudioProsition = -1;

    /**
     * IDownloadAudioView constructor
     * @param downloadItems
     * @param iDownloadAudioView
     */
    public DownloadRecyclerAdapter(ArrayList<DownloadFile> downloadItems, DownloadView.IDownloadAudioView iDownloadAudioView, DownloadView.IDownload iDownload) {
        this.downloadItems = downloadItems;
        this.iDownloadAudioView = iDownloadAudioView;
        this.iDownload = iDownload;
        mViewHolder = new Hashtable<>();
        //-- Set from iDownloadAudioView
        DownloadPresenter downloadPresenter = new DownloadPresenter(iDownloadAudioView);
        downloadPresenter.retrieveAndSetIDownloadAudioRecyclerReference(this);
        //-- Set from iDownload
        DownloadPresenter mDownloadPresenter = new DownloadPresenter(iDownload);
        mDownloadPresenter.retrieveAndSetIDownloadAudioRecyclerReference(this);
    }

    /**
     * IDownloadVideoView constructor
     * @param downloadItems
     * @param iDownloadVideoView
     */
    public DownloadRecyclerAdapter(ArrayList<DownloadFile> downloadItems, DownloadView.IDownloadVideoView iDownloadVideoView) {
        this.downloadItems = downloadItems;
        this.iDownloadVideoView = iDownloadVideoView;
        mViewHolder = new Hashtable<>();
        //--
        DownloadPresenter downloadPresenter = new DownloadPresenter(iDownloadVideoView);
        downloadPresenter.retrieveAndSetIDownloadVideoRecyclerReference(this);
    }

    /**
     * IDownloadPdfView constructor
     * @param downloadItems
     * @param iDownloadPdfView
     */
    public DownloadRecyclerAdapter(ArrayList<DownloadFile> downloadItems, DownloadView.IDownloadPdfView iDownloadPdfView) {
        this.downloadItems = downloadItems;
        this.iDownloadPdfView = iDownloadPdfView;
        mViewHolder = new Hashtable<>();
        //--
        DownloadPresenter downloadPresenter = new DownloadPresenter(iDownloadPdfView);
        downloadPresenter.retrieveAndSetIDownloadPdfRecyclerReference(this);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, parent, false);
        return new  MyViewHolder(view);
    }

    /**
     * Bind All view holder : Video, Audio and pdf
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            holder.positionItem = position;
            mViewHolder.put(position, holder);
            holder.container.setBackgroundResource(positionSelected == position ? R.color.colorAccentOpacity35 : R.drawable.submenu_item_hover);
            if(downloadItems.get(position).getBitmap() == null){
                holder.itemImage.setImageResource(CommonPresenter.getMipmapByTypeShortcode(downloadItems.get(position).getShortcode()));
            }
            else if(downloadItems.get(position).getBitmap() != null){
                holder.itemImage.setImageBitmap(downloadItems.get(position).getBitmap());
            }
            else{}
            holder.itemTitle.setText(downloadItems.get(position).getTitle());
            String dateFormat = CommonPresenter.changeFormatDate(downloadItems.get(position).getDate());
            String durationFormat = CommonPresenter.changeFormatDuration(downloadItems.get(position).getDuration());
            String auteur = downloadItems.get(position).getArtist().replace("null", "<unknown>");
            String content = (dateFormat.equalsIgnoreCase("01/18/1970") ? "" : dateFormat+" | ")+((durationFormat != null && !downloadItems.get(position).getData().endsWith(".pdf")) ? durationFormat : "")+(auteur != null && !auteur.isEmpty() ? " | "+auteur : "");
            holder.itemSubTitle.setText(content.replace("|  |", "|"));
        }
        catch (Exception ex){
            holder.itemTitle.setText(downloadItems.get(position).getTitle());
            String dateFormat = CommonPresenter.changeFormatDate(downloadItems.get(position).getDate());
            String durationFormat = CommonPresenter.changeFormatDuration(downloadItems.get(position).getDuration());
            String auteur = "<unknown>";
            holder.itemSubTitle.setText((dateFormat.equalsIgnoreCase("01/18/1970") ? "" : dateFormat+" | ")+(durationFormat != null ? durationFormat : "")+(auteur != null && !auteur.isEmpty() ? " | "+auteur : ""));
        }
    }

    @Override
    public int getItemCount() {
        return downloadItems.size();
    }

    /**
     * Pay next video
     */
    @Override
    public void playNextVideo() {
        // Scroll recyclerView
        DownloadPresenter downloadPresenter = new DownloadPresenter(iDownloadVideoView);
        downloadPresenter.srcollVideoDataItemsToPosition(CommonPresenter.getScrollToNextValue(nextVideoProsition, downloadItems.size()));
        //--
        mViewHolder.get(nextVideoProsition).container.performClick();
    }

    /**
     * Play previous video
     */
    @Override
    public void playPreviousVideo() {
        // Scroll recyclerView
        DownloadPresenter downloadPresenter = new DownloadPresenter(iDownloadVideoView);
        downloadPresenter.srcollVideoDataItemsToPosition(CommonPresenter.getScrollToPreviousValue(previousVideoPosition, downloadItems.size()));
        //--
        mViewHolder.get(previousVideoPosition).container.performClick();
    }

    @Override
    public void playNextAudio() {
        // Scroll recyclerView
        DownloadPresenter downloadPresenter = new DownloadPresenter(iDownloadAudioView);
        downloadPresenter.srcollAudioDataItemsToPosition(CommonPresenter.getScrollToNextValue(nextAudioProsition, downloadItems.size()));
        //--
        mViewHolder.get(nextAudioProsition).container.performClick();
    }

    @Override
    public void playPreviousAudio() {
        // Scroll recyclerView
        DownloadPresenter downloadPresenter = new DownloadPresenter(iDownloadAudioView);
        downloadPresenter.srcollAudioDataItemsToPosition(CommonPresenter.getScrollToPreviousValue(previousAudioPosition, downloadItems.size()));
        //--
        mViewHolder.get(previousAudioPosition).container.performClick();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        int positionItem;
        View container;
        ImageView itemImage;
        TextView itemTitle;
        TextView itemSubTitle;
        ImageView itemToDelete;
        public MyViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container_layout);
            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemSubTitle = itemView.findViewById(R.id.item_subtitle);
            itemToDelete = itemView.findViewById(R.id.item_delete);

            // Event
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    positionSelected = positionItem;

                    // When download video is clicked
                    if(iDownloadVideoView != null){
                        DownloadFile downloadFile = downloadItems.get(positionSelected);
                        Video videoSelected = new Video(0, "", downloadFile.getData(), downloadFile.getTitle(), downloadFile.getArtist(), downloadFile.getDuration(), downloadFile.getDate(), "", downloadFile.getShortcode(), downloadFile.getMipmap());
                        previousVideoPosition = CommonPresenter.getPreviousRessourceValue(positionItem);;
                        nextVideoProsition = CommonPresenter.getNextRessourceValue(positionItem, downloadItems.size());
                        DownloadPresenter downloadPresenter = new DownloadPresenter(iDownloadVideoView);
                        downloadPresenter.playLVEVideoPlayer(videoSelected, positionSelected);
                    }
                    else if(iDownloadPdfView != null){
                        DownloadFile downloadFile = downloadItems.get(positionSelected);
                        DownloadPresenter downloadPresenter = new DownloadPresenter(iDownloadPdfView);
                        downloadPresenter.readPdfFile(downloadFile.getData());
                    }
                    else if(iDownloadAudioView != null){
                        DownloadFile downloadFile = downloadItems.get(positionSelected);
                        Audio audioSelected = new Audio(0, "", downloadFile.getData(), downloadFile.getTitle(), downloadFile.getArtist(), downloadFile.getDuration(), downloadFile.getDate(), "", downloadFile.getShortcode(), downloadFile.getMipmap());
                        previousAudioPosition = CommonPresenter.getPreviousRessourceValue(positionItem);
                        nextAudioProsition = CommonPresenter.getNextRessourceValue(positionItem, downloadItems.size());
                        DownloadPresenter downloadPresenter = new DownloadPresenter(iDownload);
                        downloadPresenter.retrieveAudioSelected(view.getContext(), audioSelected, positionSelected);
                    }
                    else{}
                    //--
                    addFocusToItemSelection(view);
                }
            });

            itemToDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positionSelected = positionItem;
                    DownloadFile downloadFile = downloadItems.get(positionSelected);
                    String type = null;
                    if(iDownloadVideoView != null){type = "video";}
                    else if(iDownloadPdfView != null){type = "pdf";}
                    else if(iDownloadAudioView != null){type = "audio";}
                    else{}
                    //--
                    if(type != null) {
                        deleteFile(view.getContext(), new File(downloadFile.getData()), positionSelected, type);
                    }
                }
                // Clear selected after 500 ms
                CountDownTimer countDownTimer = new CountDownTimer(500, 500) {
                    public void onTick(long millisUntilFinished) {}
                    public void onFinish() {
                        unSelectedAllItem();
                    }
                }.start();
            });
        }
    }


    /**
     * Delete selected file
     * @param context
     * @param file
     * @param position
     * @param type
     */
    private void deleteFile(final Context context, final File file, final int position, final String type){
        // Remove file from Android
        if(file != null && file.exists()){
            if(file.delete()){
                downloadItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, downloadItems.size());
            }
        }
        // Remove file from MediaStore Database
        if(type.equalsIgnoreCase("video")) {
            String[] projection = {MediaStore.Video.Media._ID};
            String selection = MediaStore.Video.Media.DATA + " = ?";
            String[] selectionArgs = new String[]{
                    file.getAbsolutePath()
            };
            Uri queryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = context.getContentResolver();
            Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
            if (c.moveToFirst()) {
                long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                Uri deleteUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                contentResolver.delete(deleteUri, null, null);
            } else {
            }
            c.close();
        }
        else if(type.equalsIgnoreCase("audio")) {
            String[] projection = {MediaStore.Audio.Media._ID};
            String selection = MediaStore.Audio.Media.DATA + " = ?";
            String[] selectionArgs = new String[]{
                    file.getAbsolutePath()
            };
            Uri queryUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = context.getContentResolver();
            Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
            if (c.moveToFirst()) {
                long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                Uri deleteUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                contentResolver.delete(deleteUri, null, null);
            } else {
            }
            c.close();
        }
        else{}
    }


    /**
     * Add focus to Selected Item
     * @param view
     */
    private void addFocusToItemSelection(View view){
        // UnSelected All items
        unSelectedAllItem();
        // Change Item selected color
        view.setBackgroundResource(R.color.colorAccentOpacity35);
    }

    // UnSelected All items
    private void unSelectedAllItem(){
        for (int i=downloadItems.size()-1; i>=0; i--){
            if(mViewHolder.containsKey(i)){
                mViewHolder.get(i).container.setBackgroundResource(R.drawable.submenu_item_hover);
            }
        }
    }
}
