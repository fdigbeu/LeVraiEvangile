package org.levraievangile.View.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.levraievangile.Model.Favoris;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.FavorisView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Maranatha on 10/10/2017.
 */

public class FavorisRecyclerAdapter extends RecyclerView.Adapter<FavorisRecyclerAdapter.MyViewHolder> {

    private ArrayList<Favoris> favorisItems;
    private String typeResource;
    private Hashtable<Integer, MyViewHolder> mViewHolder;
    private FavorisView.IPlaceholder iPlaceholder;
    private int positionVideoSelected;
    private int positionAudioSelected;
    private int positionPdfSelected;

    public FavorisRecyclerAdapter(ArrayList<Favoris> favorisItems, FavorisView.IPlaceholder iPlaceholder) {
        this.favorisItems = favorisItems;
        this.iPlaceholder = iPlaceholder;
        mViewHolder = new Hashtable<>();
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

                }
            });
        }
    }
}
