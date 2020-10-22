package com.tyhj.wallpaper.ui.service;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.display.loglibrary.LogUtil;
import com.tyhj.wallpaper.Application;

import util.SharedPreferencesUtil;
import util.image.ImageUtil;

public class VideoWallpaper extends WallpaperService {

    public static final String CLOSE_VOLUME = "close_volume";

    @Override
    public void onCreate() {
        super.onCreate();
        ImageUtil.getWidth(this);
        LogUtil.i("VideoWallpaper onCreate");
    }

    @Override
    public Engine onCreateEngine() {
        LogUtil.i("VideoWallpaper onCreateEngine");
        return new VideoEngine();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i("VideoWallpaper onDestroy");
    }

    class VideoEngine extends Engine {

        private MediaPlayer mp;
        private int progress = 0;
        private int resid;
        private String uriString;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            LogUtil.i("VideoEngine onCreate");
            setTouchEventsEnabled(true);
            resid = Application.getImageId();
            uriString = Application.getImageDir();
            if (uriString != null) {
                mp = MediaPlayer.create(getApplicationContext(), Uri.parse(uriString));
            } else {
                mp = MediaPlayer.create(getApplicationContext(), resid);
            }
            mp.setLooping(true);
            if (SharedPreferencesUtil.getBoolean(CLOSE_VOLUME, false)) {
                mp.setVolume(0.0f, 0.0f);
            } else {
                mp.setVolume(0.7f, 0.7f);
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            mp.setSurface(holder.getSurface());
            LogUtil.i("VideoEngine onSurfaceCreated");
            //mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            LogUtil.i("VideoEngine onVisibilityChanged");
            if (mp != null) {
                if (visible) {
                    if (!mp.isPlaying()) {
                        if (uriString == null || !uriString.equals(Application.getImageDir())) {
                            uriString = Application.getImageDir();
                            mp.reset();
                            mp.release();
                            mp = MediaPlayer.create(getApplicationContext(), Uri.parse(uriString));
                            mp.setSurface(getSurfaceHolder().getSurface());
                            mp.setLooping(true);
                            if (SharedPreferencesUtil.getBoolean(CLOSE_VOLUME, false)) {
                                mp.setVolume(0.0f, 0.0f);
                            } else {
                                mp.setVolume(0.7f, 0.7f);
                            }
                            mp.start();
                            return;
                        } else if (resid != -1 && Application.getImageId() != -1) {
                            if (resid != Application.getImageId()) {
                                resid = Application.getImageId();
                                mp.reset();
                                mp.release();
                                mp = MediaPlayer.create(getApplicationContext(), resid);
                                mp.setSurface(getSurfaceHolder().getSurface());
                                mp.setLooping(true);
                                if (SharedPreferencesUtil.getBoolean(CLOSE_VOLUME, false)) {
                                    mp.setVolume(0.0f, 0.0f);
                                } else {
                                    mp.setVolume(0.7f, 0.7f);
                                }
                                mp.start();
                                return;
                            }
                        }
                        if (SharedPreferencesUtil.getBoolean(CLOSE_VOLUME, false)) {
                            mp.setVolume(0.0f, 0.0f);
                        } else {
                            mp.setVolume(0.7f, 0.7f);
                        }
                        mp.seekTo(progress);
                        mp.start();
                    }
                } else {
                    progress = mp.getCurrentPosition();
                    mp.pause();
                }
            }
        }


        @Override
        public void onDestroy() {
            LogUtil.i("VideoEngine onDestroy");
            destroy();
            super.onDestroy();
        }

        public void destroy() {
            if (mp != null) {
                mp.stop();
                mp.reset();
                mp.release();
            }
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
