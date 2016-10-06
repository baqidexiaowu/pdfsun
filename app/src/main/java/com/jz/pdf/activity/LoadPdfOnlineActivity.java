package com.jz.pdf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.jz.pdf.R;
import com.jz.pdf.aws.Constants;
import com.jz.pdf.service.AudioService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by and2long on 16-8-28.
 */
public class LoadPdfOnlineActivity extends AppCompatActivity {


    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.wv_loadPdf)
    WebView mWebView;
    @Bind(R.id.ib_play_audio)
    ImageButton ibPlay;
    @Bind(R.id.sb_audio_progress)
    SeekBar mSeekBar;
    private Intent intent;
    private String pdfFileName;
    private String pdfAudioName;

    private static boolean isPlaying = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_pdf_online);
        ButterKnife.bind(this);
        initBar();
        initData();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        intent = getIntent();
        pdfFileName = intent.getStringExtra("pdfFileName");
        pdfAudioName = intent.getStringExtra("pdfAudioName");
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        String pdfUrl = Constants.S3_PDF_BUCKET + pdfFileName;
        //String data = "<iframe src='http://docs.google.com/gview?embedded=true&url="+pdfUrl+"'"+" width='100%' height='100%' style='border: none;'></iframe>";
        //mWebView.loadData(data, "text/html", "UTF-8");
        mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfUrl);
        AudioService.pdfAudioPath = Constants.S3_AUDIO_BUCKET + pdfAudioName;
    }

    private void initBar() {
        setSupportActionBar(toolBar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @OnClick({R.id.ib_play_audio, R.id.sb_audio_progress})
    public void onClick(View view) {
        intent = new Intent(this, AudioService.class);
        switch (view.getId()) {
            case R.id.ib_play_audio:
                if (isPlaying) {
                    isPlaying = false;
                    intent.putExtra("action", 0);   //暂停
                    ibPlay.setBackgroundResource(R.mipmap.ic_play);
                } else {
                    isPlaying = true;
                    intent.putExtra("action", 1);   //播放
                    ibPlay.setBackgroundResource(R.mipmap.ic_pause);
                }
                startService(intent);
                break;
            case R.id.sb_audio_progress:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止播放。
        intent = new Intent(this, AudioService.class);
        intent.putExtra("action", 2);
        stopService(intent);
    }
}
