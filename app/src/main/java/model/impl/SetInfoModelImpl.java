package model.impl;

import android.util.Log;

import com.tyhj.wallpaper.Application;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import model.entity.UserInfo;
import presenter.SetUserInfo;
import retrofit.api.SavaUserApi;
import util.key.KeyUtil;

/**
 * Created by Tyhj on 2017/5/25.
 */

public class SetInfoModelImpl {

    UserInfo userInfo;
    SetUserInfo lisenter;

    @Inject
    public SetInfoModelImpl(SetUserInfo setUserInfo, UserInfo userInfo) {
        this.lisenter = setUserInfo;
        this.userInfo = userInfo;
    }


    public void onSetInfo() {

        SavaUserApi api = Application.getRetrofit().create(SavaUserApi.class);
        try {
            api.savaUser(KeyUtil.sign(KeyUtil.SIGN.getBytes(), KeyUtil.KEY), KeyUtil.SIGN, userInfo.getPhoneIMEI(),
                    userInfo.getPhoneModel(), userInfo.getPhoneName(),
                    userInfo.getVersion(), userInfo.getAppVersion(), userInfo.getOs())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String s) {
                            Log.e("savaUserInfo", s);
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getInt("code") == 200) {
                                    lisenter.showSetInfoOk();
                                } else {
                                    lisenter.showSetInfoError();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            lisenter.showSetInfoError();
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
