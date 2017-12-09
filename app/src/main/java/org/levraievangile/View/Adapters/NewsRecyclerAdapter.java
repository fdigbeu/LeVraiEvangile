package org.levraievangile.View.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.levraievangile.Model.Actualite;
import org.levraievangile.Model.Annee;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.Presenter.HomePresenter;
import org.levraievangile.Presenter.NewsPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Activities.NewsActivity;
import org.levraievangile.View.Interfaces.HomeView;
import org.levraievangile.View.Interfaces.NewsView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Maranatha on 10/10/2017.
 */

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.MyViewHolder> {

    private ArrayList<Annee> newsItemsYears;
    private Hashtable<Integer, MyViewHolder> mViewHolder;
    private ArrayList<Actualite> newsItems;
    private HomeView.IPlaceholder iPlaceholder;
    private NewsView.INews iNews;
    private Actualite newsSelected;
    private Annee yearSelected;
    private Context context;
    private int resLayout;
    private final String urlAccess = "http://www.levraievangile.org/uploads/";

    // For Home data constructor
    public NewsRecyclerAdapter(ArrayList<Annee> newsItemsYears, int resLayout, HomeView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
        this.newsItemsYears = newsItemsYears;
        this.resLayout = resLayout;
        mViewHolder = new Hashtable<>();
    }

    // For News data constructor
    public NewsRecyclerAdapter(Context context, ArrayList<Actualite> newsItems, int resLayout, NewsView.INews iNews) {
        this.context = context;
        this.iNews = iNews;
        this.resLayout = resLayout;
        this.newsItems = newsItems;
        mViewHolder = new Hashtable<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resLayout, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        mViewHolder.put(position, holder);
        holder.positionItem = position;
        Annee newsYear = null; Actualite news = null;
        // If it's to load home data
        if(iPlaceholder != null){
            newsYear = newsItemsYears.get(position);
            holder.itemIcon.setImageResource(R.mipmap.sm_calendrier);
            holder.itemTitle.setText(newsYear.getAnnee());
        }
        // If it's to load News data
        else if(iNews != null){
            news = newsItems.get(position);
            holder.itemIcon.setScaleType(ImageView.ScaleType.FIT_XY);
            Hashtable<String, Integer> screenSize = CommonPresenter.getScreenSize(context);
            int width = screenSize.get("largeur");
            int height = screenSize.get("hauteur");
            int imgWidth = width <= height ? width : height;
            Picasso.with(context).load(urlAccess+news.getUrlImage()).memoryPolicy(MemoryPolicy.NO_CACHE).resize(Math.round(imgWidth/5), Math.round(imgWidth/5)).into(holder.itemIcon);
            holder.itemTitle.setText(news.getTitre());
        }
        else{}
    }

    @Override
    public int getItemCount() {
        return newsItemsYears != null ? newsItemsYears.size() : newsItems.size();
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

                    if(iPlaceholder != null){
                        yearSelected = newsItemsYears.get(positionItem);
                        HomePresenter homePresenter = new HomePresenter(iPlaceholder);
                        homePresenter.launchActivity(yearSelected.getAnnee(), NewsActivity.class);
                    }
                    else if(iNews != null){
                        newsSelected = newsItems.get(positionItem);
                        NewsPresenter newsPresenter = new NewsPresenter(iNews);
                        String urlPage = view.getContext().getResources().getString(R.string.ws_url_news_detail);
                        String tabDate[] = newsSelected.getDate().split("-");
                        newsPresenter.launchActivity(urlPage.replace("{ANNEE}", tabDate[0]).replace("{MOIS}", tabDate[1]).replace("{ID}", ""+newsSelected.getId()));
                    }
                    else{}
                }
            });
        }
    }
}
