package com.jz.pdf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jz.pdf.R;
import com.jz.pdf.aws.Constants;
import com.jz.pdf.base.App;
import com.jz.pdf.bean.PDFInfos;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by L on 2016/8/31.
 */
public class PdfIntroActivity extends AppCompatActivity {

    @Bind(R.id.intro_iv_image)
    ImageView image;
    @Bind(R.id.intro_tv_desc)
    TextView desc;
    @Bind(R.id.toolBar)
    Toolbar toolBar;
    private Intent intent;
    private PDFInfos pdfInfos;
    private String pdfDescription;
    private String pdfImageName;
    private String pdfFileName;
    private String pdfAudioName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_intro);
        ButterKnife.bind(this);

        initBar();
        initData();
        initView();
    }

    private void initBar() {
        setSupportActionBar(toolBar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        desc.setText(pdfDescription);
        ImageLoader.getInstance().displayImage(Constants.S3_IMAGE_BUCKET + pdfImageName, image, App.imageOptions);
    }

    private void initData() {
        intent = getIntent();
        pdfInfos = (PDFInfos) intent.getSerializableExtra("pdfInfos");
        pdfDescription = pdfInfos.getPDFDescription();
        pdfFileName = pdfInfos.getPDFFileName();
        pdfAudioName = pdfInfos.getPDFAudioName();
        pdfImageName = pdfInfos.getPDFImageName();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick({R.id.intro_bt_read, R.id.ib_download})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.intro_bt_read:
                intent = new Intent(this, LoadPdfOnlineActivity.class);
                intent.putExtra("pdfFileName", pdfFileName);
                intent.putExtra("pdfAudioName", pdfAudioName);
                startActivity(intent);
                break;
            case R.id.ib_download:

                break;
        }
    }
}
