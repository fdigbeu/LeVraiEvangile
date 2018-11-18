package org.levraievangile.View.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.levraievangile.Model.Video;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.Presenter.HomePresenter;
import org.levraievangile.Presenter.VideoPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Activities.VideoActivity;
import org.levraievangile.View.Interfaces.HomeView;
import org.levraievangile.View.Interfaces.VideoView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Maranatha on 10/10/2017.
 */

public class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoRecyclerAdapter.MyViewHolder> implements VideoView.IVideoRecycler {

    private ArrayList<Video> videoItems;
    private Hashtable<Integer, MyViewHolder> mViewHolder;
    private HomeView.IPlaceholder iPlaceholder;
    private VideoView.IVideo iVideo;
    private Video videoSelected;
    private int resLayout;
    private int positionSelected = -1;
    private int previousVideoPosition = -1;
    private int nextVideoProsition = -1;

    public VideoRecyclerAdapter(ArrayList<Video> videoItems, int resLayout, HomeView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
        this.resLayout = resLayout;
        this.videoItems = videoItems;
        mViewHolder = new Hashtable<>();
    }

    public VideoRecyclerAdapter(ArrayList<Video> videoItems, int resLayout, VideoView.IVideo iVideo) {
        this.iVideo = iVideo;
        this.resLayout = resLayout;
        this.videoItems = videoItems;
        mViewHolder = new Hashtable<>();
        //--
        VideoPresenter videoPresenter = new VideoPresenter(iVideo);
        videoPresenter.retrieveAndSetIVideoRecyclerReference(this);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resLayout, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Video video = videoItems.get(position);
        mViewHolder.put(position, holder);
        holder.container.setBackgroundResource(positionSelected == position ? R.color.colorAccentOpacity35 : R.drawable.submenu_item_hover);
        holder.positionItem = position;
        if(iPlaceholder != null) {
            holder.itemIcon.setImageResource(video.getMipmap());
            holder.itemTitle.setText(video.getType_libelle());
        }
        else if(iVideo != null){
            String dateFormat = CommonPresenter.changeFormatDate(video.getDate());
            String durationFormat = CommonPresenter.changeFormatDuration(video.getDuree());
            //-- 
            holder.itemIcon.setImageResource(CommonPresenter.getMipmapByTypeShortcode(video.getType_shortcode()));
            holder.itemTitle.setText(video.getTitre());
            holder.itemSubTitle.setText(dateFormat+" | "+durationFormat+" | "+video.getAuteur());
        }
        else{}
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    @Override
    public void playNextVideo() {
        // Scroll recyclerView
        VideoPresenter videoPresenter = new VideoPresenter(iVideo);
        videoPresenter.srcollVideoDataItemsToPosition(CommonPresenter.getScrollToNextValue(nextVideoProsition, videoItems.size()));
        //--
        mViewHolder.get(nextVideoProsition).container.performClick();
    }

    @Override
    public void playPreviousVideo() {
        // Scroll recyclerView
        VideoPresenter videoPresenter = new VideoPresenter(iVideo);
        videoPresenter.srcollVideoDataItemsToPosition(CommonPresenter.getScrollToPreviousValue(previousVideoPosition, videoItems.size()));
        //--
        mViewHolder.get(previousVideoPosition).container.performClick();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        int positionItem;
        View container;
        ImageView itemIcon;
        TextView itemTitle;
        TextView itemSubTitle;

        public MyViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            itemIcon = itemView.findViewById(R.id.item_icon);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemSubTitle = itemView.findViewById(R.id.item_subtitle);

            // Event
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoSelected = videoItems.get(positionItem);
                    Log.e("TAG_URL_VIDEO", videoSelected.getUrlacces()+videoSelected.getSrc());
                    if(iPlaceholder != null){
                        HomePresenter homePresenter = new HomePresenter(iPlaceholder);
                        homePresenter.launchActivity(videoSelected.getType_shortcode(), VideoActivity.class);
                    }
                    else if(iVideo != null){
                        positionSelected = positionItem;
                        addFocusToItemSelection(view);

                        previousVideoPosition = CommonPresenter.getPreviousRessourceValue(positionItem);;
                        nextVideoProsition = CommonPresenter.getNextRessourceValue(positionItem, videoItems.size());

                        VideoPresenter videoPresenter = new VideoPresenter(iVideo);
                        videoPresenter.playLVEVideoPlayer(view.getContext(), videoSelected, positionSelected);
                    }
                    else{}
                }
            });
        }
    }

    //--
    private void addFocusToItemSelection(View view){
        for (int i=videoItems.size()-1; i>=0; i--){
            if(mViewHolder.containsKey(i)){
                mViewHolder.get(i).container.setBackgroundResource(R.drawable.submenu_item_hover);
            }
        }
        view.setBackgroundResource(R.color.colorAccentOpacity35);
    }
}
