package com.tyhj.wallpaper.ui.service;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.tyhj.wallpaper.Application;
import com.tyhj.wallpaper.R;

import model.entity.WallPaperNow;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.tyhj.wallpaper.Application.log;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        WallPaperNow wallPaperNow= Application.getLiteOrm().query(WallPaperNow.class).get(0);
        log("wallPaperNow",wallPaperNow.toString());
        switch (wallPaperNow.getId()) {
            case -1:
                changePaper(MallpaperService.class.getCanonicalName(),this);
                wallPaperNow=new WallPaperNow(-1,-1,0,null);
                Application.getLiteOrm().save(wallPaperNow);
                break;
            case -2:
                changePaper(CameraLiveWallpaper.class.getCanonicalName(),this);
                wallPaperNow=new WallPaperNow(-2,-1,0,null);
                Application.getLiteOrm().save(wallPaperNow);
                break;
            case -3:
                Application.setImageId(R.raw.bird);
                Application.setImageDir(null);
                changePaper(VideoWallpaper.class.getCanonicalName(),this);
                wallPaperNow=new WallPaperNow(-3,-1,0,null);
                Application.getLiteOrm().save(wallPaperNow);
                break;
            case -4:
                Application.setImageId(R.raw.girl);
                Application.setImageDir(null);
                changePaper(VideoWallpaper.class.getCanonicalName(),this);
                wallPaperNow=new WallPaperNow(-4,-1,0,null);
                Application.getLiteOrm().save(wallPaperNow);
                break;
            default:
                if(wallPaperNow.getPath()!=null&&wallPaperNow.getPath().endsWith(".mp4")){
                    Application.setImageId(-1);
                    Application.setImageDir(wallPaperNow.getPath());
                    changePaper(VideoWallpaper.class.getCanonicalName(),this);
                }else if(wallPaperNow.getPath()!=null&&wallPaperNow.getPath().endsWith(".gif")){
                    Application.setGifPath(wallPaperNow.getPath());
                    changePaper(GifWallpaperService.class.getCanonicalName(),this);
                }
                break;
        }


        return super.onStartCommand(intent, flags, startId);
    }

    //设置壁纸
    private void changePaper(String name,Context context) {
        Intent chooseIntent;
        if (Build.VERSION.SDK_INT >= 16) {
            chooseIntent = new Intent();
            chooseIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            chooseIntent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            chooseIntent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(context.getPackageName(), name));
            context.startActivity(chooseIntent);
        } else {
            chooseIntent = new Intent(Intent.ACTION_SET_WALLPAPER);
            context.startActivity(Intent.createChooser(chooseIntent, "选择壁纸"));
        }
    }

}
