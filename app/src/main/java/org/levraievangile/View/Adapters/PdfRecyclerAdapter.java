package org.levraievangile.View.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.levraievangile.Model.Pdf;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.Presenter.HomePresenter;
import org.levraievangile.Presenter.PdfPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Activities.PdfActivity;
import org.levraievangile.View.Activities.WebActivity;
import org.levraievangile.View.Interfaces.HomeView;
import org.levraievangile.View.Interfaces.PdfView;

import java.util.ArrayList;
import java.util.Hashtable;

import static org.levraievangile.Presenter.CommonPresenter.GOOGLE_DRIVE_READER;
import static org.levraievangile.Presenter.CommonPresenter.KEY_PDF_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.saveDataInSharePreferences;

/**
 * Created by Maranatha on 10/10/2017.
 */

public class PdfRecyclerAdapter extends RecyclerView.Adapter<PdfRecyclerAdapter.MyViewHolder> {

    private ArrayList<Pdf> pdfItems;
    private Hashtable<Integer, PdfRecyclerAdapter.MyViewHolder> mViewHolder;
    private HomeView.IPlaceholder iPlaceholder;
    private PdfView.IPdf iPdf;
    private int resLayout;
    private int positionSelected;

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
            String auteur = pdf.getAuteur();
            //--
            holder.itemIcon.setImageResource(CommonPresenter.getMipmapByTypeShortcode(pdf.getType_shortcode()));
            holder.itemTitle.setText(pdf.getTitre());
            holder.itemSubTitle.setText(dateFormat+(auteur != null ? " | "+auteur : ""));
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
                    positionSelected = positionItem;
                    if(iPlaceholder != null){
                        HomePresenter homePresenter = new HomePresenter(iPlaceholder);
                        homePresenter.launchActivity(pdfItems.get(positionSelected).getType_shortcode(), PdfActivity.class);
                    }
                    else if(iPdf != null){
                        PdfPresenter pdfPresenter = new PdfPresenter(iPdf);
                        pdfPresenter.launchActivity(GOOGLE_DRIVE_READER+pdfItems.get(positionSelected).getUrlacces()+pdfItems.get(positionSelected).getSrc());
                        saveDataInSharePreferences(view.getContext(), KEY_PDF_SELECTED, pdfItems.get(positionSelected).toString());
                    }
                    else{}
                }
            });
        }
    }
}
