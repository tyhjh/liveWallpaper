package presenter.impl;

import javax.inject.Inject;

import model.impl.GetHomeImpl;

/**
 * Created by Tyhj on 2017/5/29.
 */

public class GetHomePresenter {
    GetHomeImpl home;

    @Inject
    public GetHomePresenter(GetHomeImpl home) {
        this.home=home;
    }

    public void getHome(){
        home.getHome();
    }

}
