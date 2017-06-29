package presenter.impl;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit.api.SignNoteApi;
import util.key.KeyUtil;

/**
 * Created by Tyhj on 2017/5/29.
 */

public class SignPresenter {
    String msg;
    String imei;


    @Inject
    public SignPresenter(@Named("IMEI") String IMEI) {
        this.msg="";
        imei=IMEI;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void Sign(){
        SignNoteApi api= com.tyhj.wallpaper.Application.getRetrofit().create(SignNoteApi.class);
        try {
            api.sign(KeyUtil.sign(KeyUtil.SIGN.getBytes(), KeyUtil.KEY),KeyUtil.SIGN,imei,msg)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String s) {
                            Log.e("留言",s);
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
