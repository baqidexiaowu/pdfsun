package com.jz.pdf.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;

import com.jz.pdf.utils.ToastUtil;

/**
 * 播放音频服务
 */
public class AudioService extends Service {

    private MediaPlayer mediaPlayer;
    public static String pdfAudioPath;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        try {
            mediaPlayer = new MediaPlayer();
            String url = "http://192.168.23.37:8080/BigBang-BadBoy.mp3";
            mediaPlayer.setDataSource(pdfAudioPath);
            mediaPlayer.setLooping(false);
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    ToastUtil.showToast(getApplicationContext(), "播放完毕");
                    //mediaPlayer.seekTo(0);
                    //mediaPlayer.start();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    ToastUtil.showToast(getApplicationContext(), "音频文件加载失败");
                    return false;
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    System.out.println("准备好，开始播放");
                }
            });
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getIntExtra("action", -1);
        System.out.println("传递过来的数字：" + action);
        switch (action) {
            case 0://暂停
                pause();
                break;
            case 1://播放
                play();
                break;
            case 2://停止
                stop();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    public void play() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            System.out.println("播放");
        }
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            System.out.println("暂停");
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            //停止后重新准备好。
            try {
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
