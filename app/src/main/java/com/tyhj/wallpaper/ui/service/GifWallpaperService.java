package com.tyhj.wallpaper.ui.service;

import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.display.loglibrary.LogUtil;
import com.tyhj.wallpaper.Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import util.image.ImageUtil;

/**
 * Created by Tyhj on 2017/5/23.
 */

public class GifWallpaperService extends WallpaperService {

    private final Handler mHandler = new Handler();
    private Movie movie;
    private float scaleWidth, scaleHeight;




    public void onServiceConnected(){

    }


    @Override
    public void onCreate() {
        super.onCreate();
        ImageUtil.getWidth(this);
        LogUtil.i("onCreate");
    }

    private void initGif() {
        InputStream stream = null;
        try {
            if (Application.getGifPath() == null) {
                stream = getAssets().open("flower.gif");
            } else {
                File file = new File(Application.getGifPath());
                if (file.exists()) {
                    stream = new FileInputStream(file);
                } else {
                    stream = getAssets().open("flower.gif");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(e.toString());
        }
        movie = Movie.decodeStream(stream);
        //获取gif的宽高
        int width = movie.width();
        int height = movie.height();
        // 设置想要的大小
        int newWidth = ImageUtil.SCREEN_WIDTH;
        int newHeight = ImageUtil.SCREEN_HEIGHT;

        LogUtil.i("宽度：", newWidth + "");
        LogUtil.i("长度：", newHeight + "");

        // 计算缩放比例
        scaleWidth = ((float) newWidth) / width;
        scaleHeight = ((float) newHeight) / height;

        scaleWidth = (scaleWidth > scaleHeight) ? scaleWidth : scaleHeight;
    }

    @Override
    public Engine onCreateEngine() {
        return new Mngine();
    }


    //Engine是WallpaperService中的一个内部类，实现了壁纸窗口的创建以及Surface的维护工作
    class Mngine extends Engine {

        //线程
        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                drawFrame();
            }
        };

        private void drawFrame() {
            if (movie == null) {
                initGif();
            }
            Canvas canvas = null;
            canvas = getSurfaceHolder().lockCanvas();
            canvas.scale(scaleWidth, scaleWidth);
            canvas.save();
            //绘制此gif的某一帧，并刷新本身
            movie.draw(canvas, 0, 0);
            movie.setTime((int) (System.currentTimeMillis() % movie.duration()));
            canvas.restore();
            //结束锁定画图，并提交改变,画画完成(解锁)
            getSurfaceHolder().unlockCanvasAndPost(canvas);
            mHandler.postDelayed(runnable, 50);   //50ms表示每50ms绘制一帧
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
            initGif();
            LogUtil.i("Engine onCreate");
        }

        public Mngine() {

        }


        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            drawFrame();
            LogUtil.i("onSurfaceCreated");
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(runnable);
            movie = null;
            LogUtil.i("onDestroy");
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            LogUtil.i("onVisibilityChanged："+visible);
            /*下面这个判断好玩，就是说，如果屏幕壁纸状态转为显式时重新绘制壁纸，否则黑屏幕，隐藏就可以*/
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(runnable);
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            LogUtil.i("onSurfaceChanged");
            drawFrame();
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            LogUtil.i("onSurfaceDestroyed");
            super.onSurfaceDestroyed(holder);
            mHandler.removeCallbacks(runnable);
        }


    }


}
