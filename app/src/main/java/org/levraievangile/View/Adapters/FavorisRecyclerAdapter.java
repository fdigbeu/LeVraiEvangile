package org.levraievangile.View.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private int positionAudioSelected;
    // Videos favorites
    private FavorisPresenter favorisVideoPresenter;
    private int positionVideoSelected;
    private int previousVideoPosition = -1;
    private int nextVideoProsition = -1;
    // Pdf favorites
    private FavorisPresenter favorisPdfPresenter;
    private int positionPdfSelected;

    public FavorisRecyclerAdapter(ArrayList<Favoris> favorisItems, FavorisView.IPlaceholder iPlaceholder) {
        this.favorisItems = favorisItems;
        this.iPlaceholder = iPlaceholder;
        mViewHolder = new Hashtable<>();
        // Instanciate Ref IFavorisRecycler in PlaceholderFragment
        favorisVideoPresenter = new FavorisPresenter(iPlaceholder);
        favorisVideoPresenter.retrieveAndSetIFavorisRecyclerReference(this);
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
                    Favoris favorisSelected = favorisItems.get(positionPdfSelected);
                    switch (favorisSelected.getType()){
                        // VIDEOS
                        case "video":
                            positionVideoSelected = positionItem;
                            previousVideoPosition = CommonPresenter.getPreviousRessourceValue(positionVideoSelected);;
                            nextVideoProsition = CommonPresenter.getNextRessourceValue(positionVideoSelected, favorisItems.size());
                            //--
                            Video video = new Video(favorisSelected.getRessource_id(), favorisSelected.getUrlacces(), favorisSelected.getSrc(), favorisSelected.getTitre(), favorisSelected.getAuteur(), favorisSelected.getDuree(), favorisSelected.getDate(), favorisSelected.getType_libelle(), favorisSelected.getType_shortcode(), CommonPresenter.getMipmapByTypeShortcode(favorisSelected.getType_shortcode()));
                            favorisVideoPresenter.playLVEVideoPlayer(view.getContext(), video, previousVideoPosition);
                            break;
                        // AUDIOS
                        case "audio":
                            positionAudioSelected = positionItem;
                            // TODO - Player audio and Notification
                            break;
                        // PDFS
                        case "pdf":
                            positionPdfSelected = positionItem;
                            favorisPdfPresenter = new FavorisPresenter(iPlaceholder);
                            favorisPdfPresenter.launchActivity(GOOGLE_DRIVE_READER+favorisSelected.getUrlacces()+favorisSelected.getSrc());
                            saveDataInSharePreferences(view.getContext(), KEY_PDF_SELECTED, favorisSelected.toString());
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
