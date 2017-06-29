package model.impl;

import com.tyhj.wallpaper.Application;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import model.entity.Home;
import presenter.ShowHome;
import retrofit.api.HomeApi;
import util.key.KeyUtil;

/**
 * Created by Tyhj on 2017/5/29.
 */

public class GetHomeImpl{
    ShowHome listener;

    @Inject
    public GetHomeImpl(ShowHome listener) {
        this.listener = listener;
    }

    public void getHome() {
        HomeApi homeApi= Application.getRetrofit().create(HomeApi.class);
        try {
            homeApi.getHome(KeyUtil.sign(KeyUtil.SIGN.getBytes(), KeyUtil.KEY), KeyUtil.SIGN)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Home>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Home home) {
                            if(home!=null){
                                listener.getHomeOk(home);
                            }else {
                                listener.getHomeFaile();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.getHomeFaile();
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
