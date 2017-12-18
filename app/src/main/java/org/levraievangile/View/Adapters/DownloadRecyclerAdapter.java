package org.levraievangile.View.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.levraievangile.Model.DownloadFile;
import org.levraievangile.Model.Video;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.Presenter.DownloadPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.DownloadView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Maranatha on 10/10/2017.
 */

public class DownloadRecyclerAdapter extends RecyclerView.Adapter<DownloadRecyclerAdapter.MyViewHolder> implements DownloadView.IDownloadVideoRecycler, DownloadView.IDownloadAudioRecycler, DownloadView.IDownloadPdfRecycler {

    private ArrayList<DownloadFile> downloadItems;
    private String typeResource;
    private Hashtable<Integer, MyViewHolder> mViewHolder;
    private DownloadView.IDownloadAudioView iDownloadAudioView;
    private DownloadView.IDownloadVideoView iDownloadVideoView;
    private DownloadView.IDownloadPdfView iDownloadPdfView;
    private int positionSelected = -1;
    private int previousVideoPosition = -1;
    private int nextVideoProsition = -1;

    /**
     * IDownloadAudioView constructor
     * @param downloadItems
     * @param iDownloadAudioView
     */
    public DownloadRecyclerAdapter(ArrayList<DownloadFile> downloadItems, DownloadView.IDownloadAudioView iDownloadAudioView) {
        this.downloadItems = downloadItems;
        this.iDownloadAudioView = iDownloadAudioView;
        mViewHolder = new Hashtable<>();
        //--
        DownloadPresenter downloadPresenter = new DownloadPresenter(iDownloadAudioView);
        downloadPresenter.retrieveAndSetIDownloadAudioRecyclerReference(this);
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
        String auteur = downloadItems.get(position).getArtist().replace("null", "");
        holder.itemSubTitle.setText((dateFormat.equalsIgnoreCase("01/18/1970") ? "" : dateFormat+" | ")+(durationFormat != null ? durationFormat : "")+(auteur != null && !auteur.isEmpty() ? " | "+auteur : ""));
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

    }

    @Override
    public void playPreviousAudio() {

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        int positionItem;
        View container;
        ImageView itemImage;
        TextView itemTitle;
        TextView itemSubTitle;
        public MyViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container_layout);
            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemSubTitle = itemView.findViewById(R.id.item_subtitle);

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
                        // TODO - Player audio and Notification
                    }
                    else{}
                    //--
                    addFocusToItemSelection(view);
                }
            });
        }
    }

    //--
    private void addFocusToItemSelection(View view){
        for (int i=downloadItems.size()-1; i>=0; i--){
            if(mViewHolder.containsKey(i)){
                mViewHolder.get(i).container.setBackgroundResource(R.drawable.submenu_item_hover);
            }
        }
        view.setBackgroundResource(R.color.colorAccentOpacity35);
    }
}
