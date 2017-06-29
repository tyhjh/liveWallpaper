package presenter.impl;

import javax.inject.Inject;

import model.impl.CheckUpdateImpl;

/**
 * Created by Tyhj on 2017/5/28.
 */

public class CheckUpdatePresenter {
    CheckUpdateImpl checkUpdateImpl;

    @Inject
    public CheckUpdatePresenter(CheckUpdateImpl checkUpdateImpl) {
        this.checkUpdateImpl = checkUpdateImpl;
    }

    public void showCheckUpdate() {
        checkUpdateImpl.onCheckUpdate();
    }

}
