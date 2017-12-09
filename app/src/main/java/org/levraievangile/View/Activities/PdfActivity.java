package org.levraievangile.View.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import org.levraievangile.Model.Pdf;
import org.levraievangile.Presenter.PdfPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Adapters.PdfRecyclerAdapter;
import org.levraievangile.View.Interfaces.PdfView;

import java.util.ArrayList;

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
    public void progressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void closeActivity() {
        this.finish();
    }
}
