package org.levraievangile.View.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.levraievangile.Model.Pdf;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.Presenter.HomePresenter;
import org.levraievangile.R;
import org.levraievangile.View.Activities.PdfActivity;
import org.levraievangile.View.Interfaces.HomeView;
import org.levraievangile.View.Interfaces.PdfView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Maranatha on 10/10/2017.
 */

public class PdfRecyclerAdapter extends RecyclerView.Adapter<PdfRecyclerAdapter.MyViewHolder> {

    private ArrayList<Pdf> pdfItems;
    private Hashtable<Integer, PdfRecyclerAdapter.MyViewHolder> mViewHolder;
    private HomeView.IPlaceholder iPlaceholder;
    private PdfView.IPdf iPdf;
    private Pdf pdfSelected;
    private int resLayout;

    public PdfRecyclerAdapter(ArrayList<Pdf> pdfItems, int resLayout, HomeView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
        this.resLayout = resLayout;
        this.pdfItems = pdfItems;
        mViewHolder = new Hashtable<>();
    }

    public PdfRecyclerAdapter(ArrayList<Pdf> pdfItems, int resLayout, PdfView.IPdf iPdf) {
        this.iPdf = iPdf;
        this.resLayout = resLayout;
        this.pdfItems = pdfItems;
        mViewHolder = new Hashtable<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resLayout, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Pdf pdf = pdfItems.get(position);
        mViewHolder.put(position, holder);
        holder.positionItem = position;
        if(iPlaceholder != null) {
            holder.itemIcon.setImageResource(pdf.getMipmap());
            holder.itemTitle.setText(pdf.getType_libelle());
        }
        else if(iPdf!= null){
            String dateFormat = CommonPresenter.changeFormatDate(pdf.getDate());
            //--
            holder.itemIcon.setImageResource(CommonPresenter.getMipmapByTypeShortcode(pdf.getType_shortcode()));
            holder.itemTitle.setText(pdf.getTitre());
            holder.itemSubTitle.setText(dateFormat+(pdf.getAuteur() != null ? " | "+pdf.getAuteur() : ""));
        }
        else{}
    }

    @Override
    public int getItemCount() {
        return pdfItems.size();
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

                    pdfSelected = pdfItems.get(positionItem);
                    if(iPlaceholder != null){
                        HomePresenter homePresenter = new HomePresenter(iPlaceholder);
                        homePresenter.launchActivity(pdfSelected.getType_shortcode(), PdfActivity.class);
                    }
                    else if(iPdf != null){

                    }
                    else{}
                }
            });
        }
    }
}
