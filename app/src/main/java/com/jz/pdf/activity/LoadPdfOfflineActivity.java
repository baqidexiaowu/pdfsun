package com.jz.pdf.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.barteksc.pdfviewer.PDFView;
import com.jz.pdf.R;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by L on 2016/8/30.
 */
public class LoadPdfOfflineActivity extends AppCompatActivity {

    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.pdfView)
    PDFView pdfView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_pdf_offline);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        setTitle("离线阅读");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        File file = new File(Environment.getExternalStorageDirectory(), "pdfsun/pdf/Slide01.pdf");
        if (file.exists()) {
            pdfView.fromFile(file)
                    //.enableSwipe(true)
                    //.defaultPage(0)
                    .load();
        } else {
            Snackbar.make(pdfView, "文件不存在", Snackbar.LENGTH_SHORT).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
