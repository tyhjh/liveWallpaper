package model.impl;

import com.tyhj.wallpaper.Application;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import model.entity.Update;
import presenter.CheckUpdate;
import retrofit.api.UpdateApi;
import util.key.KeyUtil;

/**
 * Created by Tyhj on 2017/5/28.
 */

public class CheckUpdateImpl {
    CheckUpdate listener;
    String version;

    @Inject
    public CheckUpdateImpl(CheckUpdate listener, @Named("version") String version) {
        this.listener = listener;
        this.version = version;
    }

    public void onCheckUpdate() {

        UpdateApi api = Application.getRetrofit().create(UpdateApi.class);
        try {
            api.update(KeyUtil.sign(KeyUtil.SIGN.getBytes(), KeyUtil.KEY), KeyUtil.SIGN)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Update>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }
                        @Override
                        public void onNext(Update update) {
                            if (update != null && !update.getVersionCode().equals(version)) {
                                listener.preVersion(update);
                            } else if (update.getVersionCode().equals(version)) {
                                listener.lastVersion();
                            } else {
                                listener.onErro();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

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
