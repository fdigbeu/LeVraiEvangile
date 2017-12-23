package org.levraievangile.View.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.levraievangile.Model.Pdf;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.Presenter.PdfPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Adapters.PdfRecyclerAdapter;
import org.levraievangile.View.Interfaces.PdfView;

import java.util.ArrayList;

import static org.levraievangile.Presenter.CommonPresenter.KEY_SHORT_CODE;
import static org.levraievangile.Presenter.CommonPresenter.VALUE_PERMISSION_TO_SAVE_FILE;

public class PdfActivity extends AppCompatActivity implements PdfView.IPdf{

    // Ref widgets
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe_refresh_pdf;
    // Presenter
    private PdfPresenter pdfPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        // Load presenter data
        pdfPresenter = new PdfPresenter(this);
        pdfPresenter.loadPdfData(PdfActivity.this, this.getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pdf, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                pdfPresenter.retrieveUserAction(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initialize() {
        recyclerView = findViewById(R.id.pdfRecyclerView);
        progressBar = findViewById(R.id.pdfProgressBar);
        swipe_refresh_pdf = findViewById(R.id.swipe_refresh_pdf);

        // Display Home Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void events() {
        swipe_refresh_pdf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pdfPresenter.reloadPdfData(PdfActivity.this, getIntent());
            }
        });
    }

    @Override
    public void stopRefreshing(boolean refreshing){
        swipe_refresh_pdf.setRefreshing(!refreshing);
    }

    @Override
    public void loadPdfData(ArrayList<Pdf> pdfs, int numberColumns) {
        GridLayoutManager gridLayout = new GridLayoutManager(PdfActivity.this, numberColumns);
        recyclerView.setLayoutManager(gridLayout);
        recyclerView.setHasFixedSize(true);
        int resLayout = R.layout.item_ressource;
        PdfRecyclerAdapter adapter = new PdfRecyclerAdapter(pdfs, resLayout, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void launchActivity(String value) {
        Intent intent = new Intent(PdfActivity.this, WebActivity.class);
        intent.putExtra(KEY_SHORT_CODE, value);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void askPermissionToSaveFile() {
        int permissionCheck = ContextCompat.checkSelfPermission(PdfActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PdfActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, VALUE_PERMISSION_TO_SAVE_FILE);
        }
    }

    @Override
    public void modifyHeaderInfos(String typeLibelle) {
        getSupportActionBar().setTitle(getResources().getString(R.string.tab_text_pdf)+" ("+typeLibelle+")");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case VALUE_PERMISSION_TO_SAVE_FILE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Ceate folders
                    CommonPresenter.createFolder();
                }
                else {
                    Toast.makeText(PdfActivity.this, getResources().getString(R.string.lb_storage_file_require), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void progressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void recyclerViewVisibility(int visibility) {
        recyclerView.setVisibility(visibility);
    }

    @Override
    public void closeActivity() {
        this.finish();
    }

    @Override
    public void modifyBarHeader(String title, String subTitle) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subTitle);
    }
}
