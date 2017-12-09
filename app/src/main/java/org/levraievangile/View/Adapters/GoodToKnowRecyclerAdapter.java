package org.levraievangile.View.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.levraievangile.Model.BonASavoir;
import org.levraievangile.Model.Video;
import org.levraievangile.Presenter.HomePresenter;
import org.levraievangile.R;
import org.levraievangile.View.Activities.WebActivity;
import org.levraievangile.View.Interfaces.HomeView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Maranatha on 10/10/2017.
 */

public class GoodToKnowRecyclerAdapter extends RecyclerView.Adapter<GoodToKnowRecyclerAdapter.MyViewHolder> {

    private ArrayList<BonASavoir> goodToKnowItems;
    private Hashtable<Integer, MyViewHolder> mViewHolder;
    private HomeView.IPlaceholder iPlaceholder;
    private int positionSelected;

    public GoodToKnowRecyclerAdapter(ArrayList<BonASavoir> goodToKnowItems, HomeView.IPlaceholder iPlaceholder) {
        this.goodToKnowItems = goodToKnowItems;
        this.iPlaceholder = iPlaceholder;
        mViewHolder = new Hashtable<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bon_asavoir, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BonASavoir goodToKnow = goodToKnowItems.get(position);
        mViewHolder.put(position, holder);
        holder.positionItem = position;
        holder.horizontalLine.setVisibility(position+1 < goodToKnowItems.size() ? View.VISIBLE : View.GONE);
        holder.itemIcon.setImageResource(goodToKnow.getMipmap());
        holder.itemTitle.setText(goodToKnow.getTitre());
    }

    @Override
    public int getItemCount() {
        return goodToKnowItems.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        int positionItem;
        View container;
        ImageView itemIcon;
        TextView horizontalLine;
        TextView itemTitle;
        public MyViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            itemIcon = itemView.findViewById(R.id.item_icon);
            itemTitle = itemView.findViewById(R.id.item_title);
            horizontalLine = itemView.findViewById(R.id.horizontal_line);

            // Event
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positionSelected = positionItem;
                    HomePresenter homePresenter = new HomePresenter(iPlaceholder);
                    String pageUrl = view.getContext().getResources().getString(R.string.ws_url_good_toknow_detail);
                    homePresenter.launchActivity(pageUrl.replace("{ID}", ""+goodToKnowItems.get(positionSelected).getId()), WebActivity.class);
                }
            });
        }
    }
}
