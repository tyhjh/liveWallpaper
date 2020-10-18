package com.tyhj.wallpaper.ui.activity;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dhht.annotation.Background;
import com.dhht.annotation.Click;
import com.dhht.annotation.UiThread;
import com.dhht.annotation.ViewById;
import com.dhht.annotationlibrary.ViewInjector;
import com.tyhj.wallpaper.Application;
import com.tyhj.wallpaper.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.inject.Inject;

import di.component.DaggerGifComponent;
import di.modules.ShowImageModule;
import model.entity.WallPaper;
import model.entity.WallPaperNow;
import presenter.ShowDownloadFile;
import presenter.impl.DownloadPresenter;
import com.tyhj.wallpaper.ui.common.BaseActivity;
import com.tyhj.wallpaper.ui.service.GifWallpaperService;
import util.ConvertUtil;
import util.image.BlurUtil;

public class GifActivity extends BaseActivity implements ShowDownloadFile {

    WallPaper wallPaper;

    WallPaperNow wallPaperNow;

    boolean finish;
    boolean start;

    @Inject
    DownloadPresenter presenter;

    @ViewById
    ImageView iv_gif;

    @ViewById
    CardView cdv;

    @ViewById
    RelativeLayout relative;


    @ViewById
    ProgressBar progress;

    @ViewById
    TextView tv_action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gif);
        ViewInjector.injectView(this);
        wallPaper = (WallPaper) getIntent().getSerializableExtra("wallPager");
        if (wallPaper.getDataPath() != null && new File(wallPaper.getDataPath()).exists()) {
            finish = true;
        } else {
            download();
            start = true;
        }
        afterViews();
    }


    void afterViews() {
        cdv.setLayoutParams(new LinearLayout.LayoutParams(Application.getVideoWidth(), Application.getVideoHeight()));
        tv_action.setText("设为壁纸");
        if (finish) {
            Glide.with(this).load(wallPaper.getDataPath()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv_gif);
        } else {
            Glide.with(this).load(wallPaper.getMv()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv_gif);
        }
        getBgBitmap();
    }

    @Background
    void getBgBitmap() {
        if (finish) {
            try {
                Bitmap bitmap = ConvertUtil.InputStream2Bitmap(new FileInputStream(new File(wallPaper.getDataPath())));
                setBg(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Bitmap bitmap = ConvertUtil.url2Bitmap(wallPaper.getPreview());
            setBg(bitmap);
        }
    }


    @Background
    void savaWallpaper() {
        Application.getLiteOrm().save(wallPaper);
    }

    @UiThread
    void setBg(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            relative.setBackground(ConvertUtil.bitmap2Drawable(BlurUtil.doBlur(bitmap, 10, 15)));
        }
    }


    @Click
    void progress() {
        if (!finish) {
            toast("下载中");
            if (!start) {
                download();
            }
        } else {//设置壁纸
            Application.setGifPath(wallPaper.getDataPath());
            log(wallPaper.getDataPath());
            changePaper(GifWallpaperService.class.getCanonicalName());
            wallPaperNow=new WallPaperNow(1,-1,0,wallPaper.getDataPath());
            Application.getLiteOrm().save(wallPaperNow);
        }

    }

    @Click
    void iv_back() {
        finish();
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

    void download() {
        wallPaper.setDataPath(Environment.getExternalStorageDirectory() + "/AWallpaper/" + wallPaper.getName()+wallPaper.getId()+ ".gif");
        DaggerGifComponent.builder()
                .showImageModule(new ShowImageModule(this, wallPaper.getMv(), wallPaper.getDataPath()))
                .build()
                .inject(this);
        presenter.downLoadToshow();
    }

    @Override
    public void downloadStart(int progress) {

    }

    @Override
    public void downloading(int progress) {

    }

    @Override
    public void downFinish(String path) {
        finish = true;
        savaWallpaper();
    }

    @Override
    public void downCancel(int progress) {
        start = false;
    }

    @Override
    public void downLoadErro(String path) {
        start = false;
    }

    @Override
    protected void onRestart() {
        finish();
        super.onRestart();
    }
}
