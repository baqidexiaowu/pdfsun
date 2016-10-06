package com.jz.pdf.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jz.pdf.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by and2long on 16-8-24.
 */
public class HomeUrlActivity extends AppCompatActivity {


    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.wv_home_webview)
    WebView mWebview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_url);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebSettings settings = mWebview.getSettings();
        settings.setStandardFontFamily("sans-serif");
        settings.setDefaultFixedFontSize(10);
        settings.setDefaultFontSize(10);
        settings.setJavaScriptEnabled(true);
        mWebview.loadUrl("file:///android_asset/Info.html");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
