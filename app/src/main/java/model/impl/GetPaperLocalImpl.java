package model.impl;

import com.tyhj.wallpaper.Application;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import model.entity.WallPaper;
import presenter.ShowWallPapers;

/**
 * Created by Tyhj on 2017/5/26.
 */

public class GetPaperLocalImpl {
    ShowWallPapers listener;
    @Inject
    public GetPaperLocalImpl(ShowWallPapers listener) {
        this.listener = listener;
    }

    public void getPaper() {
        Observable
                .create(new ObservableOnSubscribe<ArrayList<WallPaper>>() {
                    @Override
                    public void subscribe(ObservableEmitter<ArrayList<WallPaper>> e) throws Exception {
                        getLocaPaper(e);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<WallPaper>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<WallPaper> wallPapers) {
                        listener.toMainActivity(wallPapers);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        listener.hideLoading();
                    }
                });
    }

    private void getLocaPaper(ObservableEmitter<ArrayList<WallPaper>> e) {
        ArrayList<WallPaper> wallPapers = Application.getLiteOrm().query(WallPaper.class);
        e.onNext(wallPapers);
        try {
            Thread.sleep(2000);
            e.onComplete();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
