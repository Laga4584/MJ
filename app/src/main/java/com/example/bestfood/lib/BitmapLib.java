package com.example.bestfood.lib;

import android.graphics.Bitmap;
import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 비트맵 관련 라이브러리
 */
public class BitmapLib {
    public static final String TAG = BitmapLib.class.getSimpleName();
    private volatile static BitmapLib instance;

    public static BitmapLib getInstance() {
        if (instance == null) {
            synchronized (BitmapLib.class) {
                if (instance == null) {
                    instance = new BitmapLib();
                }
            }
        }
        return instance;
    }

    /**
     * 비트맵을 별도 스레드에서 파일로 저장한다.
     * 결과를 알려줄 핸들러
     * @param file 파일 객체
     * @param bitmap 비트맵 객체
     */

    public void saveBitmapToFileThread(final Handler handler, final File file,
                                       final Bitmap bitmap) {
        new Thread() {
            @Override
            public void run() {
                MyLog.d("here bitmap saving");
                saveBitmapToFile(file, bitmap);
                handler.sendEmptyMessage(0);
                MyLog.d("here bitmap saving6");
            }
        }.start();
    }


    /**
     * 비트맵을 파일에 저장한다.
     * @param file 파일 객체
     * @param bitmap 비트맵 객체
     * @return 파일 저장 성공 여부
     */
    private boolean saveBitmapToFile(File file, Bitmap bitmap) {
        if (bitmap == null) return false;
        MyLog.d("here bitmap saving2");
        boolean save = false;

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            MyLog.d("here bitmap saving3");
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            MyLog.d("here bitmap saving4");
            save = true;
        } catch (Exception e) {
            MyLog.d("here bitmap saving5" + e);
            save = false;
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
        return save;
    }
}
