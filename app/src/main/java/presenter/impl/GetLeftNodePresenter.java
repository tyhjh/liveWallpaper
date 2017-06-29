package presenter.impl;

import javax.inject.Inject;

import model.impl.GetLfteNodeImpl;

/**
 * Created by Tyhj on 2017/5/29.
 */

public class GetLeftNodePresenter {
    GetLfteNodeImpl getLfteNodeImpl;

    @Inject
    public GetLeftNodePresenter(GetLfteNodeImpl getLfteNodeImpl) {
        this.getLfteNodeImpl = getLfteNodeImpl;
    }

    public void GetLeft() {
        getLfteNodeImpl.getLfetNode();
    }

}
