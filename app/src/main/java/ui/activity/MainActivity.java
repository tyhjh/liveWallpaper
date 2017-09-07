package ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tyhj.wallpaper.Application;
import com.tyhj.wallpaper.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import di.component.DaggerMainComponent;
import di.modules.MainModule;
import di.modules.ShowImageModule;
import model.entity.Home;
import model.entity.Update;
import model.entity.WallPaper;
import presenter.CheckUpdate;
import presenter.GetLeftNote;
import presenter.SetUserInfo;
import presenter.ShowDownloadFile;
import presenter.ShowHome;
import presenter.ShowWallPapers;
import presenter.impl.CheckUpdatePresenter;
import presenter.impl.DownloadPresenter;
import presenter.impl.GetHomePresenter;
import presenter.impl.GetLeftNodePresenter;
import presenter.impl.LocalPaperPresenter;
import presenter.impl.SetUserInfoPresenter;
import presenter.impl.SignPresenter;
import presenter.impl.SyncWallpaper;
import presenter.impl.WallpapersPresenter;
import tyrantgit.widget.HeartLayout;
import ui.adpter.PaperAdapter;
import ui.common.BaseActivity;
import ui.views.MyDialog;
import util.CommonUtil;
import util.ConvertUtil;
import util.app.AppUtil;
import util.image.BlurUtil;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements ShowWallPapers, SetUserInfo, CheckUpdate, ShowDownloadFile, ShowHome, GetLeftNote {

    int position = 0;
    boolean getLfte = true;
    MediaPlayer player;
    int maxProgress = 100;
    boolean canDownload = true;

    Timer mTimer;
    Random mRandom = new Random();

    ArrayList<WallPaper> papers;
    PaperAdapter adpter;

    TextView tv_progress;
    AppCompatCheckBox checkBox;

    @Inject
    WallpapersPresenter presenter;

    @Inject
    LocalPaperPresenter localPaperPresenter;

    @Inject
    CheckUpdatePresenter checkUpdatePresenter;

    @Inject
    DownloadPresenter downloadPresenter;

    @Inject
    SetUserInfoPresenter userInfoPresenter;

    @Inject
    GetHomePresenter getHomePresenter;

    @Inject
    GetLeftNodePresenter leftNodePresenter;

    @Inject
    SignPresenter sign;


    SurfaceHolder.Callback callBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            log("surfaceCreated");
            if (player == null) {
                initMediaPlayer();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            log("surfaceChanged");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            log("surfaceDestroyed");
            if (player != null) {
                position = player.getCurrentPosition();
                player.stop();
                player.release();
                player = null;
            }
        }
    };

    @ViewById
    SurfaceView surFace;

    @ViewById
    ImageView iv_bg;

    @ViewById
    RecyclerView rcyle_wallpaper;

    @ViewById
    HeartLayout heart;

    @ViewById
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSrcWidth();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onStop() {
        log("onStop");
        if (player != null) {
            position = player.getCurrentPosition();
            player.pause();
        }
        super.onStop();
    }

    @Override
    protected void onRestart() {
        if (player != null) {
            player.seekTo(position);
            player.start();
        }
        log("onRestart");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        getLfte = false;
        log("onDestroy");
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @AfterViews
    void afterView() {
        initHeart();
        surFace.setVisibility(View.GONE);
        Picasso.with(this).load(R.mipmap.home_bg).into(iv_bg);
        initRecycleView();

        DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .showImageModule(new ShowImageModule(this, " ", Environment.getExternalStorageDirectory() + "/AWallpaper/wallpaper.apk"))
                .build()
                .inject(this);


        presenter.getWallPaperToShow();
        localPaperPresenter.getLocalPaperToshow();
        checkUpdatePresenter.showCheckUpdate();
        userInfoPresenter.setUserInfoToShow();
        getHomePresenter.getHome();

        log(CommonUtil.getAndroidVersion());


        getLeftNote();
    }

    @Background
    void getLeftNote() {
        while (getLfte) {
            leftNodePresenter.GetLeft();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //加载心的动画
    private void initHeart() {
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                heart.post(new Runnable() {
                    @Override
                    public void run() {
                        heart.addHeart(randomColor());
                    }
                });
            }
        }, 500, 400);
    }

    private int randomColor() {
        return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
    }

    private void initRecycleView() {
        papers = new ArrayList<WallPaper>();
        setInitList();
        adpter = new PaperAdapter(papers, MainActivity.this);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        rcyle_wallpaper.setLayoutManager(manager);
        rcyle_wallpaper.setAdapter(adpter);
    }

    private void setInitList() {
        papers.add(new WallPaper(-1, 0, null, "粒子", null, null, null, null));
        papers.add(new WallPaper(-2, 0, null, "相机", null, null, null, null));
        papers.add(new WallPaper(-3, 0, null, "小鸟", null, null, null, null));
        papers.add(new WallPaper(-4, 0, null, "女孩", null, null, null, null));
    }

    @Click(R.id.fab)
    void fab() {
        changePaper();
    }

    private void changePaper() {
        Intent chooseIntent;
        chooseIntent = new Intent(Intent.ACTION_SET_WALLPAPER);
        startActivity(Intent.createChooser(chooseIntent, "选择壁纸"));
    }

    private void initMediaPlayer() {
        player = MediaPlayer.create(MainActivity.this, R.raw.girl);
        player.setSurface(surFace.getHolder().getSurface());
        player.setVolume(0, 0);
        player.setLooping(true);
        player.seekTo(position);
        player.start();
    }

    @Override
    public void showLoading() {
        //toast("加载中");
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public synchronized void toMainActivity(ArrayList<WallPaper> papers) {
        for (int i = 0; i < papers.size(); i++) {
            if (!this.papers.contains(papers.get(i))) {
                this.papers.add(papers.get(i));
            }
        }
        adpter.notifyDataSetChanged();
    }

    @Override
    public void showFailedError() {
        toast("网络出错");
    }

    @Override
    public void showSetInfoOk() {

    }

    @Override
    public void showSetInfoError() {

    }

    private void getSrcWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidthDip = dm.widthPixels;// 屏幕宽（dip，如：320dip）
        int screenheightDip = dm.heightPixels;
        Application.setHeight(screenheightDip);
        Application.setWidth(screenWidthDip);

        log("setWidth："+Application.getWidth()+"   +setHeight："+Application.getHeight());
    }

    @Override
    public void lastVersion() {

    }

    @Override
    public void preVersion(Update update) {
        downloadPresenter.setDownLoadUrl(update.getApkUrl());
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.dialog_update, null);
        CardView cdv_update = (CardView) layout.findViewById(R.id.cdv_update);
        //getPreView(imageUrl,cdv_update);
        final MyDialog dialog = new MyDialog(this, layout, R.style.dialog);
        dialog.setCancelable(update.isCancelable());
        TextView tv_version = (TextView) layout.findViewById(R.id.tv_version);
        TextView tv_why = (TextView) layout.findViewById(R.id.tv_why);
        tv_version.setText(update.getVersionCode());
        tv_why.setText(update.getInfo());
        checkBox = (AppCompatCheckBox) layout.findViewById(R.id.ck_download);
        tv_progress = (TextView) layout.findViewById(R.id.tv_progress);
        tv_progress.setText(0 + "%");
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && canDownload) {
                    downloadPresenter.downLoadToshow();
                    canDownload = false;
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onErro() {

    }

    @Override
    public void downloadStart(int progress) {
        maxProgress = progress;
    }

    @Override
    public void downloading(int progress) {
        tv_progress.setText(100 * progress / maxProgress + "%");
    }

    @Override
    public void downFinish(String path) {
        tv_progress.setText(100 + "%");
        AppUtil.installApk(path, this);
    }

    @Override
    public void downCancel(int progress) {

    }

    @Override
    public void downLoadErro(String path) {
        checkBox.setChecked(false);
        canDownload = true;
        File file = new File(path);
        if (file.exists())
            file.delete();
        toast("下载失败");
    }

    @Override
    public void getHomeOk(Home home) {
        switch (home.getType()) {
            case 0:
                surFace.setVisibility(View.GONE);
                Picasso.with(this).load(R.mipmap.home_bg).into(iv_bg);
                break;
            case 1:
                iv_bg.setVisibility(View.GONE);
                surFace.setVisibility(View.VISIBLE);
                surFace.getHolder().addCallback(callBack);
                break;
            case 2:
                surFace.setVisibility(View.GONE);
                Picasso.with(this).load(home.getImageUrl()).into(iv_bg);
                break;
        }
    }

    @Override
    public void getHomeFaile() {
        surFace.setVisibility(View.GONE);
        Picasso.with(this).load(R.mipmap.home_bg).into(iv_bg);
    }

    @Click(R.id.iv_sign)
    void heart() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View layout = inflater.inflate(R.layout.dialog_sign, null);
        final MyDialog dialog = new MyDialog(this, layout, R.style.dialog);
        final EditText et_sign = (EditText) layout.findViewById(R.id.et_sign);
        AppCompatCheckBox ck_sava = (AppCompatCheckBox) layout.findViewById(R.id.ck_sava);
        ck_sava.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (et_sign.getText().toString().trim().length() > 0) {
                        sign.setMsg(et_sign.getText().toString());
                        sign.Sign();
                        toast("已发送");
                    } else {
                        toast("你什么都没有留下");
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCancelable(true);
    }

    @LongClick(R.id.fab)
    void root() {
        Snackbar snackbar = Snackbar.make(iv_bg, "确认同步壁纸资源", Snackbar.LENGTH_SHORT).setAction("whyNot", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtil.getDeviceIMEI(MainActivity.this).equals("869011025169639")) {
                    sycnPaper();
                } else {
                    Toast.makeText(MainActivity.this, "你没有该权限", Toast.LENGTH_LONG).show();
                }
            }
        });
        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(R.id.snackbar_action);
        textView.setAllCaps(false);
        snackbar.show();
    }

    @Background
    void sycnPaper() {
        SyncWallpaper.syncWallpaper();
        SyncWallpaper.syncApp();
    }

    //获取预览图
    @Background
    void getPreView(String url, View view) {
        Bitmap bitmap = ConvertUtil.url2Bitmap(url);
        setbg(bitmap, view);
    }

    @UiThread
    void setbg(Bitmap bitmap, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(ConvertUtil.bitmap2Drawable(BlurUtil.doBlur(bitmap, 10, 15)));
        }
    }

    @Override
    public void havaNote(final String msg) {
        Snackbar.make(iv_bg, "Tyhj回复了你：" + msg, Snackbar.LENGTH_INDEFINITE).setAction("额", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast(msg);
            }
        }).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
