package org.levraievangile.View.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import org.levraievangile.Model.Setting;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.SettingsView;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by Maranatha on 10/10/2017.
 */

public class SettingRecyclerAdapter extends RecyclerView.Adapter<SettingRecyclerAdapter.MyViewHolder> {

    private List<Setting> settingItems;
    private Hashtable<Integer, MyViewHolder> mViewHolder;
    private SettingsView.ISettings iSettings;
    private int positionSelected;
    private String keySelected;

    public SettingRecyclerAdapter(List<Setting> settingItems, SettingsView.ISettings iSettings) {
        this.iSettings = iSettings;
        this.settingItems = settingItems;
        mViewHolder = new Hashtable<>();
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
        //--
        holder.itemSubtitle.setText(mSetting.getLibelle());
        holder.checkBoxSetting.setChecked(mSetting.getChoice());
    }

    @Override
    public int getItemCount() {
        return settingItems.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        int positionItem;
        View linearLayout;
        TextView itemTitle;
        TextView itemSubtitle;
        CheckBox checkBoxSetting;

        public MyViewHolder(View itemView) {
            super(itemView);
            // View when it's a menus or resources items
            linearLayout = itemView.findViewById(R.id.container_setting);
            itemTitle = itemView.findViewById(R.id.item_setting_title);
            itemSubtitle = itemView.findViewById(R.id.item_setting_subtitle);
            checkBoxSetting = itemView.findViewById(R.id.item_setting_checkBox);
            // Events
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positionSelected = positionItem;
                    Setting mSetting = settingItems.get(positionItem);
                    //--
                    keySelected = CommonPresenter.KEY_SETTINGS[positionItem];
                    CommonPresenter.saveSettingObjectInSharePreferences(view.getContext(), keySelected, !mSetting.getChoice());
                    mViewHolder.get(positionItem).checkBoxSetting.setChecked(!mSetting.getChoice());
                    settingItems.get(positionItem).setChoice(!mSetting.getChoice());
                }
            });
        }
    }
}
