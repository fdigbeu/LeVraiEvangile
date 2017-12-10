package org.questionsreponses.View.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import org.questionsreponses.Model.Setting;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.Presenter.SettingsPresenter;
import org.questionsreponses.R;
import org.questionsreponses.View.Interfaces.SettingsView.*;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by Maranatha on 10/10/2017.
 */

public class SettingRecyclerAdapter extends RecyclerView.Adapter<SettingRecyclerAdapter.MyViewHolder> implements ISettingRecycler {

    private List<Setting> settingItems;
    private Hashtable<Integer, MyViewHolder> mViewHolder;
    private ISettings iSettings;
    private int positionSelected;
    private String keySelected;

    public SettingRecyclerAdapter(List<Setting> settingItems, ISettings iSettings) {
        this.iSettings = iSettings;
        this.settingItems = settingItems;
        mViewHolder = new Hashtable<>();
        // bind Ref interface of this adapter
        SettingsPresenter settingsPresenter = new SettingsPresenter(iSettings);
        settingsPresenter.setSettingsISettingRecycler(this);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.positionItem = position;
        mViewHolder.put(position, holder);
        Setting mSetting = settingItems.get(position);
        holder.itemTitle.setText(mSetting.getTitle());
        // If it's not to change number of quizz
        if(mSetting.getTotal() == 0){
            holder.itemSubtitle.setText(mSetting.getLibelle());
            holder.checkBoxSetting.setVisibility(View.VISIBLE);
            holder.itemLineBar.setVisibility(View.VISIBLE);
            holder.checkBoxSetting.setChecked(mSetting.getChoice());
        }
        // It's to change number of quizz
        else if(mSetting.getTotal() > 0){
            holder.itemSubtitle.setText(mSetting.getLibelle()+" "+mSetting.getTotal());
            holder.checkBoxSetting.setVisibility(View.GONE);
            holder.itemLineBar.setVisibility(View.GONE);
        }
        else{}
    }

    @Override
    public int getItemCount() {
        return settingItems.size();
    }

    @Override
    public void changeNumberOfQuizz(Context context, int totalQuizz) {
        Setting mSetting = settingItems.get(positionSelected);
        mViewHolder.get(positionSelected).itemSubtitle.setText(mSetting.getLibelle()+" "+totalQuizz);
        CommonPresenter.saveSettingObjectInSharePreferences(context, keySelected, mSetting.getChoice(), totalQuizz);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        int positionItem;
        View linearLayout;
        TextView itemTitle;
        TextView itemSubtitle;
        TextView itemLineBar;
        CheckBox checkBoxSetting;

        public MyViewHolder(View itemView) {
            super(itemView);
            // View when it's a menus or resources items
            linearLayout = itemView.findViewById(R.id.container_setting);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemSubtitle = itemView.findViewById(R.id.item_subtitle);
            itemLineBar = itemView.findViewById(R.id.item_line_bar);
            checkBoxSetting = itemView.findViewById(R.id.item_checkBox);
            // Events
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positionSelected = positionItem;
                    Setting mSetting = settingItems.get(positionItem);
                    // If it's not to change number of quizz
                    keySelected = CommonPresenter.KEY_SETTINGS[positionItem];
                    if(mSetting.getTotal() == 0){
                        CommonPresenter.saveSettingObjectInSharePreferences(view.getContext(), keySelected, !mSetting.getChoice(), 0);
                        mViewHolder.get(positionItem).checkBoxSetting.setChecked(!mSetting.getChoice());
                        settingItems.get(positionItem).setChoice(!mSetting.getChoice());
                    }
                    // It's to change number of quizz
                    else if(mSetting.getTotal() > 0){
                        SettingsPresenter settingsPresenter = new SettingsPresenter(iSettings);
                        settingsPresenter.displayNumberOfTheQuizzList(view);
                    }
                    else{}
                }
            });
        }
    }
}
