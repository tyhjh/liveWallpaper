package presenter.impl;

import javax.inject.Inject;

import model.impl.GetPaperLocalImpl;

/**
 * Created by Tyhj on 2017/5/26.
 */

public class LocalPaperPresenter {
    private GetPaperLocalImpl getPaper;

    @Inject
    public LocalPaperPresenter(GetPaperLocalImpl getPaper) {
        this.getPaper=getPaper;
    }

    public void getLocalPaperToshow(){
        getPaper.getPaper();
    }
}
