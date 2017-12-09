package org.levraievangile.View.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.levraievangile.Model.Pdf;
import org.levraievangile.Presenter.CommonPresenter;
import org.levraievangile.Presenter.PdfPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Adapters.PdfRecyclerAdapter;
import org.levraievangile.View.Interfaces.PdfView;

import java.util.ArrayList;

import static org.levraievangile.Presenter.CommonPresenter.VALUE_PERMISSION_TO_SAVE_FILE;

public class PdfActivity extends AppCompatActivity implements PdfView.IPdf{

    // Ref widgets
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
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
    public void initialize() {
        recyclerView = findViewById(R.id.pdfRecyclerView);
        progressBar = findViewById(R.id.pdfProgressBar);
    }

    @Override
    public void events() {

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
    public void askPermissionToSaveFile() {
        int permissionCheck = ContextCompat.checkSelfPermission(PdfActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PdfActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, VALUE_PERMISSION_TO_SAVE_FILE);
        }
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
    public void closeActivity() {
        this.finish();
    }
}
