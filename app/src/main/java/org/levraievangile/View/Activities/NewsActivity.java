package org.levraievangile.View.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import org.levraievangile.Model.Actualite;
import org.levraievangile.Model.Mois;
import org.levraievangile.Presenter.NewsPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Adapters.NewsMonthRecyclerAdapter;
import org.levraievangile.View.Adapters.NewsRecyclerAdapter;
import org.levraievangile.View.Interfaces.NewsView;

import java.util.ArrayList;

import static org.levraievangile.Presenter.CommonPresenter.KEY_SHORT_CODE;

public class NewsActivity extends AppCompatActivity implements NewsView.INews {

    // Ref widgets
    private ProgressBar progressBar;
    private RecyclerView monthRecyclerView;
    private LinearLayoutManager monthLayoutManager;
    private RecyclerView newsRecyclerView;
    // Presenter
    private NewsPresenter newsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        // Load presenter data
        newsPresenter = new NewsPresenter(this);
        newsPresenter.loadNewsData(NewsActivity.this, this.getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initialize() {
        monthRecyclerView = findViewById(R.id.monthRecyclerView);
        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        progressBar = findViewById(R.id.newsProgressBar);

        // Display Home Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void events() {

    }

    @Override
    public void loadNewsMonth(ArrayList<Mois> month, int year) {
        NewsMonthRecyclerAdapter recyclerAdapter=new NewsMonthRecyclerAdapter(month, year, this);
        monthRecyclerView.setHasFixedSize(true);
        monthLayoutManager = new LinearLayoutManager(NewsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        monthRecyclerView.setLayoutManager(monthLayoutManager);
        monthRecyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void monthRecyclerScrollTo(int position){
        monthLayoutManager.scrollToPosition(position);
    }

    @Override
    public void loadNewsData(ArrayList<Actualite> news, int numberColumns) {
        GridLayoutManager gridLayout = new GridLayoutManager(NewsActivity.this, numberColumns);
        newsRecyclerView.setLayoutManager(gridLayout);
        newsRecyclerView.setHasFixedSize(true);
        int resLayout = R.layout.item_bon_asavoir;
        NewsRecyclerAdapter adapter = new NewsRecyclerAdapter(NewsActivity.this, news, resLayout, this);
        newsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void launchActivity(String value) {
        Intent intent = new Intent(NewsActivity.this, WebActivity.class);
        intent.putExtra(KEY_SHORT_CODE, value);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void progressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void modifyHeaderInfos(String typeLibelle) {
        getSupportActionBar().setTitle(getResources().getString(R.string.tab_text_news)+" ("+typeLibelle+")");
    }

    @Override
    public void closeActivity() {
        this.finish();
    }
}
