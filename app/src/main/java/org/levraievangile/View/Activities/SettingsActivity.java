package org.levraievangile.View.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import org.levraievangile.Model.Setting;
import org.levraievangile.Presenter.SettingPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Adapters.SettingRecyclerAdapter;
import org.levraievangile.View.Interfaces.SettingsView;

import java.util.List;

public class SettingsActivity extends AppCompatActivity implements SettingsView.ISettings {

    // Ref widgets
    private RecyclerView setting_recycler;
    private SettingRecyclerAdapter recyclerAdapter;
    // Ref presenter
    private SettingPresenter settingsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Load presenter data
        settingsPresenter = new SettingPresenter(this);
        settingsPresenter.loadSettingsData(SettingsActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                settingsPresenter.retrieveUserAction(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initialize() {
        setting_recycler = findViewById(R.id.setting_recycler);

        // Display Home Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void events() {}

    @Override
    public void loadSettingData(List<Setting> settingItems, int numberColumns, int positionScroll) {
        GridLayoutManager gridLayout = new GridLayoutManager(SettingsActivity.this, numberColumns);
        setting_recycler.setLayoutManager(gridLayout);
        setting_recycler.setHasFixedSize(true);
        recyclerAdapter = new SettingRecyclerAdapter(settingItems, this);
        setting_recycler.setAdapter(recyclerAdapter);
        setting_recycler.scrollToPosition(positionScroll);
    }

    @Override
    public void closeActivity() {
        this.finish();
    }
}
