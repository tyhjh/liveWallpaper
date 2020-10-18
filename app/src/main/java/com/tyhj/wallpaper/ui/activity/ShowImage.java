package com.tyhj.wallpaper.ui.activity;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dhht.annotation.Background;
import com.dhht.annotation.Click;
import com.dhht.annotation.UiThread;
import com.dhht.annotation.ViewById;
import com.dhht.annotationlibrary.ViewInjector;
import com.dhht.annotationlibrary.threadPool.AppExecutors;
import com.tyhj.wallpaper.Application;
import com.tyhj.wallpaper.R;
import com.tyhj.wallpaper.ui.common.BaseActivity;
import com.tyhj.wallpaper.ui.service.CameraLiveWallpaper;
import com.tyhj.wallpaper.ui.service.MallpaperService;
import com.tyhj.wallpaper.ui.service.VideoWallpaper;
import com.tyhj.wallpaper.ui.views.MyVideoView;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import di.component.DaggerPaperComponent;
import di.modules.ShowImageModule;
import model.entity.WallPaper;
import model.entity.WallPaperNow;
import permison.PermissonUtil;
import permison.listener.PermissionListener;
import presenter.ShowDownloadFile;
import presenter.impl.DownloadPresenter;
import util.ConvertUtil;
import util.file.FileUtil;
import util.https.InternetUtil;
import util.image.BlurUtil;

public class ShowImage extends BaseActivity implements ShowDownloadFile {

    int mProgress = 0;

    WallPaperNow wallPaperNow;

    boolean canDownload=true;

    boolean firstToast=true;

    @Inject
    DownloadPresenter presenter;

    WallPaper wallPaper;

    private Uri uri;

    @ViewById
    MyVideoView videoView;

    @ViewById
    CardView cdv;

    @ViewById
    RelativeLayout relative;

    @ViewById
    ImageView iv_back, iv_delete,ivPreview;

    @ViewById
    ProgressBar progress;

    @ViewById
    TextView tv_action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_image);
        ViewInjector.injectView(this);
        wallPaper = (WallPaper) getIntent().getSerializableExtra("wallPager");
        if (wallPaper.getId() == -2) {
            PermissonUtil.checkPermission(this, new PermissionListener() {
                @Override
                public void havePermission() {
                    setSelf();
                }

                @Override
                public void requestPermissionFail() {
                    finish();
                }
            }, Manifest.permission.CAMERA);
        } else {
            setSelf();
        }
            if(wallPaper.getType() == 1&&!FileUtil.isFile(wallPaper.getDataPath())){
                wallPaper.setType(2);
            }

            if(wallPaper.getType()==2&&FileUtil.isFile(Environment.getExternalStorageDirectory() + "/AWallpaper/" + wallPaper.getName() + ".mp4")){
                wallPaper.setType(1);
                wallPaper.setDataPath(Environment.getExternalStorageDirectory() + "/AWallpaper/" + wallPaper.getName() + ".mp4");
            }

        afterView();

    }

    void afterView() {
        cdv.setLayoutParams(new LinearLayout.LayoutParams(Application.getVideoWidth(), Application.getVideoHeight()));
        initView();
    }

    @Click(R.id.cdv)
    void crad() {
        AppExecutors.getInstance().scheduledExecutorService().schedule(()->{
            ivPreview.setVisibility(View.GONE);
        },500, TimeUnit.MILLISECONDS);
        switch (wallPaper.getType()) {
            //自带
            case 1:
                if (videoView.isPlaying())
                    videoView.pause();
                else
                    videoView.start();
                break;
            case 2:
                if(InternetUtil.isOnline(this)){
                    if (videoView.isPlaying())
                        videoView.pause();
                    else {
                        videoView.start();
                    }

                    if(!InternetUtil.isWifi(this)&&firstToast) {
                        toast("注意流量~~");
                        firstToast=false;
                    }

                }else {
                    toast("请打开网络后重试");
                }
                break;

        }
    }

    private void initView() {


        if(wallPaper.getType()==2){
            String path=Environment.getExternalStorageDirectory() + "/AWallpaper/" + wallPaper.getName() +wallPaper.getId()+ ".mp4";
            File file=new File(path);
            if(file.exists()){
                wallPaper.setDataPath(path);
                wallPaper.setType(1);
            }
        }



        switch (wallPaper.getType()) {
            //自带
            case 0:
                tv_action.setText("设为壁纸");
                iv_delete.setVisibility(View.GONE);
                break;
            //本地
            case 1:
                uri = Uri.parse(wallPaper.getDataPath());
                //Create media controller
                if (videoView != null && uri != null) {
                    videoView.setVideoURI(uri);
                } else {
                    toast("未找到该视频");
                    wallPaper.setType(2);
                    initView();
                }
                tv_action.setText("设为壁纸");
                iv_delete.setVisibility(View.VISIBLE);
                Bitmap bitmap;
                if (InternetUtil.isOnline(this)) {
                    getPreView();
                } else {
                    bitmap = ThumbnailUtils.createVideoThumbnail(wallPaper.getDataPath(), MediaStore.Images.Thumbnails.MICRO_KIND);
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, Application.getVideoWidth(), Application.getVideoHeight(), ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                    setPreView(bitmap);
                }
                break;
            //网络
            case 2:
                getPreView();
                tv_action.setText("下载视频");
                iv_delete.setVisibility(View.GONE);
                uri = Uri.parse(wallPaper.getMv());
                //Create media controller
                if (videoView != null && uri != null) {
                    videoView.setVideoURI(uri);
                } else {
                    toast("未找到该视频");
                }
                break;
        }
    }

    //设置预览图
    @UiThread
    void setPreView(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //todo换
            videoView.setBackground(ConvertUtil.bitmap2Drawable(bitmap));
            ivPreview.setImageBitmap(bitmap);
            relative.setBackground(ConvertUtil.bitmap2Drawable(BlurUtil.doBlur(bitmap, 10, 15)));
        }
    }

    //获取预览图
    @Background
    void getPreView() {
        Bitmap bitmap = ConvertUtil.url2Bitmap(wallPaper.getPreview());
        setPreView(bitmap);
    }

    //设置本地壁纸
    private void setWallpaper() {
        Application.setImageId(-1);
        Application.setImageDir(wallPaper.getDataPath());
        changePaper(VideoWallpaper.class.getCanonicalName());
        wallPaperNow=new WallPaperNow(1,-1,0,wallPaper.getDataPath());
        Application.getLiteOrm().save(wallPaperNow);
    }

    //设置壁纸
    private void changePaper(String name) {
        Intent chooseIntent;
        if (Build.VERSION.SDK_INT >= 16) {
            chooseIntent = new Intent();
            chooseIntent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            chooseIntent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(getPackageName(), name));
            startActivity(chooseIntent);
        } else {
            chooseIntent = new Intent(Intent.ACTION_SET_WALLPAPER);
            startActivity(Intent.createChooser(chooseIntent, "选择壁纸"));
        }
    }

    //设置自带
    private void setSelf() {
        switch (wallPaper.getId()) {
            case -1:
                changePaper(MallpaperService.class.getCanonicalName());
                wallPaperNow=new WallPaperNow(-1,-1,0,null);
                Application.getLiteOrm().save(wallPaperNow);
                break;
            case -2:
                changePaper(CameraLiveWallpaper.class.getCanonicalName());
                wallPaperNow=new WallPaperNow(-2,-1,0,null);
                Application.getLiteOrm().save(wallPaperNow);
                break;
            case -3:
                Application.setImageId(R.raw.bird);
                Application.setImageDir(null);
                changePaper(VideoWallpaper.class.getCanonicalName());
                wallPaperNow=new WallPaperNow(-3,-1,0,null);
                Application.getLiteOrm().save(wallPaperNow);
                break;
            case -4:
                Application.setImageId(R.raw.girl);
                Application.setImageDir(null);
                changePaper(VideoWallpaper.class.getCanonicalName());
                wallPaperNow=new WallPaperNow(-4,-1,0,null);
                Application.getLiteOrm().save(wallPaperNow);
                break;
            default:
                break;
        }
    }

    //下载视频
    private void download() {
        if(canDownload) {
            wallPaper.setDataPath(Environment.getExternalStorageDirectory() + "/AWallpaper/" + wallPaper.getName() +wallPaper.getId()+ ".mp4");
            DaggerPaperComponent.builder()
                    .showImageModule(new ShowImageModule(this,wallPaper.getMv(), wallPaper.getDataPath()))
                    .build()
                    .inject(this);
            presenter.downLoadToshow();
            canDownload=false;
        }
    }

    @Click(R.id.progress)
    void clickAction() {
        switch (wallPaper.getType()) {
            //自带
            case 0:
                setSelf();
                break;
            //本地
            case 1:
                setWallpaper();
                break;
            //网络
            case 2:
                download();
                break;
        }
    }

    @Click(R.id.iv_delete)
    void delete() {
        videoView.pause();
        File file = new File(wallPaper.getDataPath());
        if (file.exists())
            file.delete();
        Application.getLiteOrm().delete(wallPaper);
        toast("已删除");
        this.finish();
    }

    @Click(R.id.iv_back)
    void back() {
        this.finish();
    }

    @Override
    protected void onRestart() {
        this.finish();
        super.onRestart();
    }

    @Override
    public void downloadStart(int progres) {
        mProgress = progres;
        progress.setMax(progres);
    }

    @Background
    void savaWallpaper() {
        Application.getLiteOrm().save(wallPaper);
    }

    @Override
    public void downFinish(String progresss) {
        progress.setProgress(progress.getMax());
        toast("下载完成");
        iv_delete.setVisibility(View.VISIBLE);
        tv_action.setText("设为壁纸");
        wallPaper.setType(1);
        savaWallpaper();
        canDownload=true;
    }

    @Override
    public void downloading(int progres) {
        progress.setProgress(progres);
    }

    @Override
    public void downCancel(int progress) {
        canDownload=true;
    }

    @Override
    public void downLoadErro(String path) {

        File file=new File(path);
        if(file.exists())
            file.delete();

        toast("下载失败");
        tv_action.setText("重新下载");
        progress.setProgress(0);
        canDownload=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
