package model.impl;

import com.tyhj.wallpaper.Application;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import model.entity.CallbackInfo;
import presenter.GetLeftNote;
import retrofit.api.LeftNoteApi;
import util.key.KeyUtil;

/**
 * Created by Tyhj on 2017/5/29.
 */

public class GetLfteNodeImpl {

    String IMEI;
    GetLeftNote lisenter;

    @Inject
    public GetLfteNodeImpl(@Named("IMEI") String IMEI, GetLeftNote lisenter) {
        this.IMEI = IMEI;
        this.lisenter = lisenter;
    }

    public void getLfetNode() {

        LeftNoteApi leftNoteApi = Application.getRetrofit().create(LeftNoteApi.class);
        try {
            leftNoteApi.getCallback(KeyUtil.sign(KeyUtil.SIGN.getBytes(), KeyUtil.KEY), KeyUtil.SIGN, IMEI)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<CallbackInfo>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(CallbackInfo s) {
                            if(s!=null){
                                lisenter.havaNote(s.getCallback());
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
