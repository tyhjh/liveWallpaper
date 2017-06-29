package presenter.impl;

import javax.inject.Inject;

import model.impl.SetInfoModelImpl;

/**
 * Created by Tyhj on 2017/5/25.
 */

public class SetUserInfoPresenter {
    private SetInfoModelImpl updateUserInfo;

    @Inject
    public SetUserInfoPresenter(SetInfoModelImpl updateUserInfo) {
        this.updateUserInfo=updateUserInfo;
    }

    public void setUserInfoToShow(){
        updateUserInfo.onSetInfo();
    }

}
