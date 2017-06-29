package model.impl;

import android.util.Log;

import com.tyhj.wallpaper.Application;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import model.entity.WallPaper;
import presenter.ShowWallPapers;
import retrofit.api.WallpaperApi;
import util.key.KeyUtil;

/**
 * Created by Tyhj on 2017/5/25.
 */


public class GetPaperImpl {
    ShowWallPapers listener;

    @Inject
    public GetPaperImpl(ShowWallPapers listener) {
        this.listener = listener;
    }

    public void getPaper() {
        WallpaperApi service = Application.getRetrofit().create(WallpaperApi.class);
        try {
            service.getWallPapers(KeyUtil.sign(KeyUtil.SIGN.getBytes(), KeyUtil.KEY), KeyUtil.SIGN)
                    .subscribeOn(Schedulers.io())
                    //响应结果处理切换成主线程
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ArrayList<WallPaper>>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ArrayList<WallPaper> wallpapers) {
                            listener.toMainActivity(wallpapers);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("onError","onError");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
