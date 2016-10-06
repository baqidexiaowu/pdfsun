package com.jz.pdf.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;

import com.jz.pdf.R;
import com.jz.pdf.aws.CognitoClientManager;
import com.jz.pdf.aws.DynamoDBManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.wlf.filedownloader.FileDownloadConfiguration;
import org.wlf.filedownloader.FileDownloader;

import java.io.File;


public class App extends Application {
    public static Context appContext;
    private static Handler mHandler;
    public static DisplayImageOptions imageOptions;
    public static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        CognitoClientManager.init(this);
        DynamoDBManager.init();
        initImageLoader();
        mHandler = new Handler();
        imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_gray_rectangle)
                .showImageOnFail(R.mipmap.icon_cloud_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        initFileDownloader();
        initSharedPreferences();
        //LeakCanary.install(this);
    }

    private void initSharedPreferences() {
        sp = getSharedPreferences("offLineList", Context.MODE_PRIVATE);
    }

    private void initFileDownloader() {
        // 1、创建Builder
        FileDownloadConfiguration.Builder builder = new FileDownloadConfiguration.Builder(this);
        // 2.配置Builder
        // 配置下载文件保存的文件夹
        builder.configFileDownloadDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                "FileDownloader");
        // 配置同时下载任务数量，如果不配置默认为2
        builder.configDownloadTaskSize(3);
        // 配置失败时尝试重试的次数，如果不配置默认为0不尝试
        builder.configRetryDownloadTimes(5);
        // 开启调试模式，方便查看日志等调试相关，如果不配置默认不开启
        builder.configDebugMode(true);
        // 配置连接网络超时时间，如果不配置默认为15秒
        builder.configConnectTimeout(25000);// 25秒

        // 3、使用配置文件初始化FileDownloader
        FileDownloadConfiguration configuration = builder.build();
        FileDownloader.init(configuration);
    }


    public static Handler getMainHandler() {
        return mHandler;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .denyCacheImageMultipleSizesInMemory().memoryCache(new WeakMemoryCache())
                .threadPoolSize(4).threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize((int) (Runtime.getRuntime().freeMemory() / 3))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(50)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

}
