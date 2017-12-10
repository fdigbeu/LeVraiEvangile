package org.levraievangile.View.Interfaces;

import android.content.Context;
import android.view.View;

import org.levraievangile.Model.Setting;

import java.util.List;

/**
 * Created by Maranatha on 10/12/2017.
 */

public class SettingsView {
    // SettingsActivity interface
    public interface ISettings{
        public void initialize();
        public void events();
        public void loadSettingData(List<Setting> settingItems, int numberColumns, int positionScroll);
        public void closeActivity();
    }

    // Presenter interface
    public interface Ipresenter{}
}
