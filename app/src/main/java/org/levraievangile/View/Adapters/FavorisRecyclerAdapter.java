package org.levraievangile.View.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.levraievangile.Model.Audio;
import org.levraievangile.Model.Favoris;
import org.levraievangile.Model.Video;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.Presenter.FavorisPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.FavorisView;

import java.util.ArrayList;
import java.util.Hashtable;

import static org.levraievangile.Presenter.CommonPresenter.GOOGLE_DRIVE_READER;
import static org.levraievangile.Presenter.CommonPresenter.KEY_PDF_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.saveDataInSharePreferences;

/**
 * Created by Maranatha on 10/10/2017.
 */

public class FavorisRecyclerAdapter extends RecyclerView.Adapter<FavorisRecyclerAdapter.MyViewHolder> implements FavorisView.IFavorisRecycler {

    private ArrayList<Favoris> favorisItems;
    private String typeResource;
    private Hashtable<Integer, MyViewHolder> mViewHolder;
    private FavorisView.IPlaceholder iPlaceholder;
    // Audio favorites
    private FavorisPresenter favorisAudioPresenter;
    private FavorisView.IFravoris iFravoris;
    private int positionAudioSelected = -1;
    // Videos favorites
    private FavorisPresenter favorisVideoPresenter;
    private int positionVideoSelected = -1;
    private int previousVideoPosition = -1;
    private int nextVideoProsition = -1;
    private int previousAudioPosition = -1;
    private int nextAudioProsition = -1;
    // Pdf favorites
    private FavorisPresenter favorisPdfPresenter;
    private int positionPdfSelected = -1;

    public FavorisRecyclerAdapter(ArrayList<Favoris> favorisItems, FavorisView.IPlaceholder iPlaceholder) {
        this.favorisItems = favorisItems;
        this.iPlaceholder = iPlaceholder;
        mViewHolder = new Hashtable<>();
        // Instanciate Ref IFavorisRecycler in PlaceholderFragment
        favorisVideoPresenter = new FavorisPresenter(iPlaceholder);
        favorisVideoPresenter.retrieveAndSetIFavorisRecyclerReference(this);
    }

    public FavorisRecyclerAdapter(ArrayList<Favoris> favorisItems, FavorisView.IFravoris iFravoris) {
        this.favorisItems = favorisItems;
        this.iFravoris = iFravoris;
        mViewHolder = new Hashtable<>();
        // Instanciate Ref IFavorisRecycler in FavorisActivity
        favorisAudioPresenter = new FavorisPresenter(iFravoris);
        favorisAudioPresenter.retrieveAndSetIFavorisRecyclerReference(this);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favoris, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        typeResource = favorisItems.get(position).getType();
        holder.positionItem = position;
        //--
        int positionSelected = -1;
        switch (favorisItems.get(position).getType()) {
            case "video":
                positionSelected = positionVideoSelected;
                break;
            case "audio":
                positionSelected = positionAudioSelected;
                break;
            case "pdf":
                positionSelected = positionPdfSelected;
                break;
        }
        holder.container.setBackgroundResource(positionSelected == position ? R.color.colorAccentOpacity35 : R.drawable.submenu_item_hover);
        //--
        mViewHolder.put(position, holder);
        String dateFormat = CommonPresenter.changeFormatDate(favorisItems.get(position).getDate());
        String durationFormat = null;
        if(favorisItems.get(position).getDuree() != null && !favorisItems.get(position).getDuree().trim().contains("00:00:00")) {
            durationFormat = CommonPresenter.changeFormatDuration(favorisItems.get(position).getDuree());
        }
        //--
        holder.itemImage.setImageResource(CommonPresenter.getMipmapByTypeShortcode(favorisItems.get(position).getType_shortcode()));
        holder.itemTitle.setText(favorisItems.get(position).getTitre());
        String auteur = favorisItems.get(position).getAuteur().replace("null", "");
        holder.itemSubTitle.setText(dateFormat+(durationFormat != null ? " | "+durationFormat : "")+(auteur != null && !auteur.isEmpty() ? " | "+auteur : ""));
    }

    @Override
    public int getItemCount() {
        return favorisItems.size();
    }

    @Override
    public void playNextVideo() {
        // Scroll recyclerView
        FavorisPresenter favorisPresenter = new FavorisPresenter(iPlaceholder);
        favorisPresenter.srcollVideoDataItemsToPosition(CommonPresenter.getScrollToNextValue(nextVideoProsition, favorisItems.size()));
        //--
        mViewHolder.get(nextVideoProsition).container.performClick();
    }

    @Override
    public void playPreviousVideo() {
        // Scroll recyclerView
        FavorisPresenter favorisPresenter = new FavorisPresenter(iPlaceholder);
        favorisPresenter.srcollVideoDataItemsToPosition(CommonPresenter.getScrollToPreviousValue(previousVideoPosition, favorisItems.size()));
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
                    switch (favorisItems.get(positionItem).getType()){
                        // VIDEOS
                        case "video":
                            positionVideoSelected = positionItem;
                            Favoris favorisVideoSelected = favorisItems.get(positionVideoSelected);
                            previousVideoPosition = CommonPresenter.getPreviousRessourceValue(positionVideoSelected);;
                            nextVideoProsition = CommonPresenter.getNextRessourceValue(positionVideoSelected, favorisItems.size());
                            //--
                            Video video = new Video(favorisVideoSelected.getRessource_id(), favorisVideoSelected.getUrlacces(), favorisVideoSelected.getSrc(), favorisVideoSelected.getTitre(), favorisVideoSelected.getAuteur(), favorisVideoSelected.getDuree(), favorisVideoSelected.getDate(), favorisVideoSelected.getType_libelle(), favorisVideoSelected.getType_shortcode(), CommonPresenter.getMipmapByTypeShortcode(favorisVideoSelected.getType_shortcode()));
                            favorisVideoPresenter.playLVEVideoPlayer(view.getContext(), video, previousVideoPosition);
                            break;
                        // AUDIOS
                        case "audio":
                            positionAudioSelected = positionItem;
                            Favoris favorisAudioSelected = favorisItems.get(positionAudioSelected);
                            previousAudioPosition = CommonPresenter.getPreviousRessourceValue(positionItem);;
                            nextAudioProsition = CommonPresenter.getNextRessourceValue(positionItem, favorisItems.size());
                            Audio audioSelected = new Audio(favorisAudioSelected.getRessource_id(), favorisAudioSelected.getUrlacces(), favorisAudioSelected.getSrc(), favorisAudioSelected.getTitre(), favorisAudioSelected.getAuteur(), favorisAudioSelected.getDuree(), favorisAudioSelected.getDate(), favorisAudioSelected.getType_libelle(), favorisAudioSelected.getType_shortcode(), CommonPresenter.getMipmapByTypeShortcode(favorisAudioSelected.getType_shortcode()));
                            FavorisPresenter favorisPresenter = new FavorisPresenter(iFravoris);
                            favorisPresenter.playLVEAudioPlayer(view.getContext(), audioSelected, positionAudioSelected);
                            break;
                        // PDFS
                        case "pdf":
                            positionPdfSelected = positionItem;
                            Favoris favorisPdfSelected = favorisItems.get(positionPdfSelected);
                            favorisPdfPresenter = new FavorisPresenter(iPlaceholder);
                            favorisPdfPresenter.launchActivity(GOOGLE_DRIVE_READER+favorisPdfSelected.getUrlacces()+favorisPdfSelected.getSrc());
                            saveDataInSharePreferences(view.getContext(), KEY_PDF_SELECTED, favorisPdfSelected.toString());
                            break;
                    }
                    //--
                    addFocusToItemSelection(view);
                }
            });
        }
    }

    //--
    private void addFocusToItemSelection(View view){
        for (int i=favorisItems.size()-1; i>=0; i--){
            if(mViewHolder.containsKey(i)){
                mViewHolder.get(i).container.setBackgroundResource(R.drawable.submenu_item_hover);
            }
        }
        view.setBackgroundResource(R.color.colorAccentOpacity35);
    }
}
