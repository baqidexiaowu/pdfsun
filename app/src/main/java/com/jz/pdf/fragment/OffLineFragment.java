package com.jz.pdf.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jz.pdf.R;
import com.jz.pdf.base.App;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener;

import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by and2long on 16-8-29.
 */
public class OffLineFragment extends Fragment {

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.tv_download_status)
    TextView tvDownloadStatus;
    private OnFileDownloadStatusListener mOnFileDownloadStatusListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_offline, null);
        mOnFileDownloadStatusListener = new OnFileDownloadStatusListener() {

            @Override
            public void onFileDownloadStatusWaiting(DownloadFileInfo downloadFileInfo) {
                tvDownloadStatus.setText("等待下载");
            }

            @Override
            public void onFileDownloadStatusPreparing(DownloadFileInfo downloadFileInfo) {
                tvDownloadStatus.setText("准备中");
                Set<String> names = App.sp.getStringSet("names", null);
                if (names == null) {
                    names = new HashSet<>();
                }
                names.add(downloadFileInfo.getFileName());
                SharedPreferences.Editor editor = App.sp.edit();
                editor.putStringSet("names", names);
                editor.commit();
                progressBar.setMax((int) downloadFileInfo.getFileSizeLong());
                tvDownloadStatus.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFileDownloadStatusPrepared(DownloadFileInfo downloadFileInfo) {

            }

            @Override
            public void onFileDownloadStatusDownloading(DownloadFileInfo downloadFileInfo, float v, long l) {
                /*BigDecimal temp = new BigDecimal(downloadFileInfo.getDownloadedSizeLong());
                BigDecimal total = new BigDecimal(downloadFileInfo.getFileSizeLong());
                double percent = temp.divide(total, 2).doubleValue();
                System.out.println(percent);*/
                tvDownloadStatus.setText("下载中");
                progressBar.setProgress((int) downloadFileInfo.getDownloadedSizeLong());
            }

            @Override
            public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) {

            }

            @Override
            public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo) {
                tvDownloadStatus.setVisibility(View.GONE);
            }

            @Override
            public void onFileDownloadStatusFailed(String s, DownloadFileInfo downloadFileInfo, FileDownloadStatusFailReason fileDownloadStatusFailReason) {
                tvDownloadStatus.setText("下载失败");
            }
        };
        FileDownloader.registerDownloadStatusListener(mOnFileDownloadStatusListener);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FileDownloader.unregisterDownloadStatusListener(mOnFileDownloadStatusListener);
        ButterKnife.unbind(this);
    }


    @OnClick(R.id.iv_download_status)
    public void onClick() {
        /*Intent intent = new Intent(App.getAppContext(), LoadPdfOfflineActivity.class);
        startActivity(intent);*/
    }
}

