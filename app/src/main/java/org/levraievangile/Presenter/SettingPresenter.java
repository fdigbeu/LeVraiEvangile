package org.levraievangile.Presenter;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;

import org.levraievangile.Model.Setting;
import org.levraievangile.View.Interfaces.SettingsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maranatha on 28/11/2017.
 */

public class SettingPresenter {

    // Ref interface
    private SettingsView.ISettings iSettings;

    // Constructor
    public SettingPresenter(SettingsView.ISettings iSettings) {
        this.iSettings = iSettings;
    }

    // Load Setting data
    public void loadSettingsData(Context context){
        iSettings.initialize();
        iSettings.events();
        // Load All setting data
        Setting mSetting = null;
        List<Setting> settingList = new ArrayList<>();
        mSetting = CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_CONFIRM_BEFORE_QUIT_APP);
        settingList.add(mSetting);
        mSetting = CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_AUDIO_NOTIFICATION);
        settingList.add(mSetting);
        mSetting = CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_VIDEO_NOTIFICATION);
        settingList.add(mSetting);
        mSetting = CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_CONCATENATE_AUDIO_READING);
        settingList.add(mSetting);
        mSetting = CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_CONCATENATE_VIDEO_READING);
        settingList.add(mSetting);
        mSetting = CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_WIFI_EXCLUSIF);
        settingList.add(mSetting);
        iSettings.loadSettingData(settingList, 1, 0);
    }

    // Retrieve event when user clicks
    public void retrieveUserAction(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                iSettings.closeActivity();
                break;
        }
    }
}
