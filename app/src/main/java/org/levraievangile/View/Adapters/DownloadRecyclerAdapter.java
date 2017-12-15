package org.levraievangile.View.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.levraievangile.Model.DownloadFile;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.DownloadView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Maranatha on 10/10/2017.
 */

public class DownloadRecyclerAdapter extends RecyclerView.Adapter<DownloadRecyclerAdapter.MyViewHolder> {

    private ArrayList<DownloadFile> downloadItems;
    private String typeResource;
    private Hashtable<Integer, MyViewHolder> mViewHolder;
    private DownloadView.IPlaceholder iPlaceholder;
    private int positionVideoSelected;
    private int positionAudioSelected;
    private int positionPdfSelected;

    public DownloadRecyclerAdapter(ArrayList<DownloadFile> downloadItems, DownloadView.IPlaceholder iPlaceholder) {
        this.downloadItems = downloadItems;
        this.iPlaceholder = iPlaceholder;
        mViewHolder = new Hashtable<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.positionItem = position;
        mViewHolder.put(position, holder);
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
