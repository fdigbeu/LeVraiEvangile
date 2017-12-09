package org.levraievangile.View.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.levraievangile.Model.Actualite;
import org.levraievangile.Model.Annee;
import org.levraievangile.Model.Mois;
import org.levraievangile.Presenter.NewsPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.NewsView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Maranatha on 10/10/2017.
 */

public class NewsMonthRecyclerAdapter extends RecyclerView.Adapter<NewsMonthRecyclerAdapter.MyViewHolder> {

    private ArrayList<Mois> monthItems;
    private Hashtable<Integer, MyViewHolder> mViewHolder;
    private int monthSelected = -1;
    private int positionSelected = -1;
    private int yearSelected;
    private NewsView.INews iNews;

    public NewsMonthRecyclerAdapter(ArrayList<Mois> monthItems, int yearSelected, NewsView.INews iNews) {
        this.monthItems = monthItems;
        this.yearSelected = yearSelected;
        this.iNews = iNews;
        mViewHolder = new Hashtable<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_month, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Mois month = monthItems.get(position);
        mViewHolder.put(position, holder);
        holder.positionItem = position;
        holder.itemButton.setText(month.getLettre());
        // If never selected
        if(monthSelected < 0){
            positionSelected = 0;
            monthSelected = monthItems.get(positionSelected).getChiffre();
            // Load first item
            NewsPresenter newsPresenter = new NewsPresenter(iNews);
            newsPresenter.loadNewsSelectedMonthData(monthSelected, yearSelected);
        }
        //--
        mViewHolder.get(position).itemButton.setBackgroundResource(positionSelected == position ? R.drawable.ic_btn_news_radius_selected : R.drawable.btn_month_news);
    }

    @Override
    public int getItemCount() {
        return monthItems.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        int positionItem;
        Button itemButton;
        public MyViewHolder(View itemView) {
            super(itemView);

            itemButton = itemView.findViewById(R.id.btn_item_month);

            // Event
            itemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    unSelectedAllMonth();
                    view.setBackgroundResource(R.drawable.ic_btn_news_radius_selected);
                    positionSelected = positionItem;
                    monthSelected = monthItems.get(positionSelected).getChiffre();
                    // Load news selected list
                    NewsPresenter newsPresenter = new NewsPresenter(iNews);
                    newsPresenter.loadNewsSelectedMonthData(monthSelected, yearSelected);
                    newsPresenter.monthRecyclerScrollTo(positionSelected);
                    Log.i("TAG_POSITION_SELECTED", "positionSelected = "+positionSelected);
                }
            });
        }
    }

    private void unSelectedAllMonth(){
        for (int i=0; i<monthItems.size(); i++){
            if(mViewHolder.containsKey(i)){
                mViewHolder.get(i).itemButton.setBackgroundResource(R.drawable.btn_month_news);
            }
        }
    }
}
