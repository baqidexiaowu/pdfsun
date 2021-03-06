package com.jz.pdf.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.jz.pdf.base.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by zhangdroid on 2016/3/3.
 */
public class CommonUtils {

    public static boolean isListEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static void gotoActivity(Activity from, Class<?> cls, boolean needFinish) {
        Intent intent = new Intent(from, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (needFinish)
            from.finish();
        from.startActivity(intent);
    }

    public static void gotoActivity(Activity from, Class<?> cls, boolean needFinish, String key, Serializable value) {
        Intent intent = new Intent(from, cls);
        if (value != null) {
            intent.putExtra(key, value);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (needFinish)
            from.finish();
        from.startActivity(intent);
    }

    public static void gotoActivity(Activity from, Class<?> cls, boolean needFinish, String key, Parcelable value) {
        Intent intent = new Intent(from, cls);
        if (value != null) {
            intent.putExtra(key, value);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (needFinish)
            from.finish();
        from.startActivity(intent);
    }

    public static void gotoActivity(Activity from, Class<?> cls, boolean needFinish, Map<String, String> map) {
        Intent intent = new Intent(from, cls);
        if (map != null && !map.isEmpty()) {
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = map.get(key);
                if (!TextUtils.isEmpty(value))
                    intent.putExtra(key, value);
            }
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (needFinish)
            from.finish();
        from.startActivity(intent);
    }



    //读取.Txt 文件
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }




    public static void blur(int[] in, int[] out, int width, int height,
                            float radius) {
        int widthMinus1 = width - 1;
        int r = (int) radius;
        int tableSize = 2 * r + 1;
        int divide[] = new int[256 * tableSize];

        for (int i = 0; i < 256 * tableSize; i++)
            divide[i] = i / tableSize;

        int inIndex = 0;

        for (int y = 0; y < height; y++) {
            int outIndex = y;
            int ta = 0, tr = 0, tg = 0, tb = 0;

            for (int i = -r; i <= r; i++) {
                int rgb = in[inIndex + clamp(i, 0, width - 1)];
                ta += (rgb >> 24) & 0xff;
                tr += (rgb >> 16) & 0xff;
                tg += (rgb >> 8) & 0xff;
                tb += rgb & 0xff;
            }

            for (int x = 0; x < width; x++) {
                out[outIndex] = (divide[ta] << 24) | (divide[tr] << 16)
                        | (divide[tg] << 8) | divide[tb];

                int i1 = x + r + 1;
                if (i1 > widthMinus1)
                    i1 = widthMinus1;
                int i2 = x - r;
                if (i2 < 0)
                    i2 = 0;
                int rgb1 = in[inIndex + i1];
                int rgb2 = in[inIndex + i2];

                ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
                tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
                tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
                tb += (rgb1 & 0xff) - (rgb2 & 0xff);
                outIndex += height;
            }
            inIndex += width;
        }
    }
    /** 水平方向模糊度 */
    private static float hRadius = 10;
    /** 竖直方向模糊度 */
    private static float vRadius = 10;
    /** 模糊迭代度 */
    private static int iterations = 7;
    //高斯模糊
    public static Drawable BoxBlurFilter(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] inPixels = new int[width * height];
        int[] outPixels = new int[width * height];
        Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
        bmp.getPixels(inPixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < iterations; i++) {
            blur(inPixels, outPixels, width, height, hRadius);
            blur(outPixels, inPixels, height, width, vRadius);
        }
        blurFractional(inPixels, outPixels, width, height, hRadius);
        blurFractional(outPixels, inPixels, height, width, vRadius);
        bitmap.setPixels(inPixels, 0, width, 0, 0, width, height);
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }


    public static void blurFractional(int[] in, int[] out, int width,
                                      int height, float radius) {
        radius -= (int) radius;
        float f = 1.0f / (1 + 2 * radius);
        int inIndex = 0;

        for (int y = 0; y < height; y++) {
            int outIndex = y;

            out[outIndex] = in[0];
            outIndex += height;
            for (int x = 1; x < width - 1; x++) {
                int i = inIndex + x;
                int rgb1 = in[i - 1];
                int rgb2 = in[i];
                int rgb3 = in[i + 1];

                int a1 = (rgb1 >> 24) & 0xff;
                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >> 8) & 0xff;
                int b1 = rgb1 & 0xff;
                int a2 = (rgb2 >> 24) & 0xff;
                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >> 8) & 0xff;
                int b2 = rgb2 & 0xff;
                int a3 = (rgb3 >> 24) & 0xff;
                int r3 = (rgb3 >> 16) & 0xff;
                int g3 = (rgb3 >> 8) & 0xff;
                int b3 = rgb3 & 0xff;
                a1 = a2 + (int) ((a1 + a3) * radius);
                r1 = r2 + (int) ((r1 + r3) * radius);
                g1 = g2 + (int) ((g1 + g3) * radius);
                b1 = b2 + (int) ((b1 + b3) * radius);
                a1 *= f;
                r1 *= f;
                g1 *= f;
                b1 *= f;
                out[outIndex] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
                outIndex += height;
            }
            out[outIndex] = in[width - 1];
            inIndex += width;
        }
    }

    public static int clamp(int x, int a, int b) {
        return (x < a) ? a : (x > b) ? b : x;
    }

    public static void runOnUIThread(Runnable runnable) {
        App.getMainHandler().post(runnable);
    }

    //随机数
    public static int RandomNum(int range){
        Random random=new Random();
        int i = random.nextInt(range);
        return i;
    }

    //在字符串中插入字符串
    public static String Stringinsert(String a,String b,int t){
        return a.substring(0,t)+b+a.substring(t+1,a.length());
    }
    //限制字段长度
    public static String Stringdelete(String a,int t){
        return a.substring(0,t);
    }

    //把相同规格的字符串用“，”分割开返回字符串数组
    public static String[] convertStrToArray(String str){
        String[] strArray = null;
        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }

    public static float getDimens(int resId) {
        return App.appContext.getResources()
                .getDimension(resId);
    }

    public static String getString(int resId) {
        return App.appContext.getResources()
                .getString(resId);
    }

    public static String[] getStringArray(int resId) {
        return App.appContext.getResources()
                .getStringArray(resId);
    }

    /***
     * 移除掉当前的子视图
     */
    public static void removeSelfFromParent(View child) {
        //判断当前的子视图是否是空值
        if (child != null) {
            //得到子视图的父亲
            ViewParent parent = child.getParent();
            //判断当前的父视图,是否是ViewGroup的类型
            if (parent != null && parent instanceof ViewGroup) {
                //移除当前的子视图
                ViewGroup viewGroup = (ViewGroup) parent;
                //移除掉当前的子视图
                viewGroup.removeView(child);
            }
        }
    }

}
