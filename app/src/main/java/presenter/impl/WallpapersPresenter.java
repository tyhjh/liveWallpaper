package presenter.impl;

import javax.inject.Inject;

import model.impl.GetPaperImpl;

/**
 * Created by Tyhj on 2017/5/25.
 */

public class WallpapersPresenter{

    GetPaperImpl getPaper;

    @Inject
    public WallpapersPresenter(GetPaperImpl getPaper) {
        this.getPaper = getPaper;
    }

    public void getWallPaperToShow(){
        //getPaper.getPaper();
    }

}
