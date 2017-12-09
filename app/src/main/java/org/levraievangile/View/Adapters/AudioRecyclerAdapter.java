package org.levraievangile.View.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.levraievangile.Model.Audio;
import org.levraievangile.Model.Audio;
import org.levraievangile.Presenter.AudioPresenter;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.Presenter.HomePresenter;
import org.levraievangile.R;
import org.levraievangile.View.Activities.AudioActivity;
import org.levraievangile.View.Interfaces.AudioView;
import org.levraievangile.View.Interfaces.HomeView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Maranatha on 10/10/2017.
 */

public class AudioRecyclerAdapter extends RecyclerView.Adapter<AudioRecyclerAdapter.MyViewHolder> implements AudioView.IAudioRecycler {

    private ArrayList<Audio> audioItems;
    private Hashtable<Integer, MyViewHolder> mViewHolder;
    private HomeView.IPlaceholder iPlaceholder;
    private AudioView.IAudio iAudio;
    private Audio audioSelected;
    private int resLayout;
    private int positionSelected = -1;
    private int previousAudioPosition = -1;
    private int nextAudioProsition = -1;

    public AudioRecyclerAdapter(ArrayList<Audio> audioItems, int resLayout, HomeView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
        this.resLayout = resLayout;
        this.audioItems = audioItems;
        mViewHolder = new Hashtable<>();
    }

    public AudioRecyclerAdapter(ArrayList<Audio> audioItems, int resLayout, AudioView.IAudio iAudio) {
        this.iAudio = iAudio;
        this.resLayout = resLayout;
        this.audioItems = audioItems;
        mViewHolder = new Hashtable<>();
        //--
        AudioPresenter audioPresenter = new AudioPresenter(iAudio);
        audioPresenter.retrieveAndSetIAudioRecyclerReference(this);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resLayout, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Audio audio = audioItems.get(position);
        mViewHolder.put(position, holder);
        holder.container.setBackgroundResource(positionSelected == position ? R.color.colorAccentOpacity35 : R.drawable.submenu_item_hover);
        holder.positionItem = position;
        if(iPlaceholder != null) {
            holder.itemIcon.setImageResource(audio.getMipmap());
            holder.itemTitle.setText(audio.getType_libelle());
        }
        else if(iAudio != null){
            String dateFormat = CommonPresenter.changeFormatDate(audio.getDate());
            String durationFormat = CommonPresenter.changeFormatDuration(audio.getDuree());
            //--
            holder.itemIcon.setImageResource(CommonPresenter.getMipmapByTypeShortcode(audio.getType_shortcode()));
            holder.itemTitle.setText(audio.getTitre());
            holder.itemSubTitle.setText(dateFormat+" | "+durationFormat+" | "+audio.getAuteur());
        }
        else{}
    }

    @Override
    public int getItemCount() {
        return audioItems.size();
    }

    @Override
    public void playNextAudio() {
        // Scroll recyclerView
        AudioPresenter audioPresenter = new AudioPresenter(iAudio);
        audioPresenter.srcollAudioDataItemsToPosition(CommonPresenter.getScrollToNextValue(nextAudioProsition, audioItems.size()));
        //--
        mViewHolder.get(nextAudioProsition).container.performClick();
    }

    @Override
    public void playPreviousAudio() {
        // Scroll recyclerView
        AudioPresenter audioPresenter = new AudioPresenter(iAudio);
        audioPresenter.srcollAudioDataItemsToPosition(CommonPresenter.getScrollToPreviousValue(previousAudioPosition, audioItems.size()));
        //--
        mViewHolder.get(previousAudioPosition).container.performClick();
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

                    audioSelected = audioItems.get(positionItem);
                    if(iPlaceholder != null){
                        HomePresenter homePresenter = new HomePresenter(iPlaceholder);
                        homePresenter.launchActivity(audioSelected.getType_shortcode(), AudioActivity.class);
                    }
                    else if(iAudio != null){

                        positionSelected = positionItem;
                        addFocusToItemSelection(view);

                        previousAudioPosition = CommonPresenter.getPreviousRessourceValue(positionItem);;
                        nextAudioProsition = CommonPresenter.getNextRessourceValue(positionItem, audioItems.size());

                        AudioPresenter audioPresenter = new AudioPresenter(iAudio);
                        audioPresenter.playLVEAudioPlayer(view.getContext(), audioSelected, positionSelected);
                    }
                    else{}
                }
            });
        }
    }

    //--
    private void addFocusToItemSelection(View view){
        for (int i=audioItems.size()-1; i>=0; i--){
            if(mViewHolder.containsKey(i)){
                mViewHolder.get(i).container.setBackgroundResource(R.drawable.submenu_item_hover);
            }
        }
        view.setBackgroundResource(R.color.colorAccentOpacity35);
    }
}
