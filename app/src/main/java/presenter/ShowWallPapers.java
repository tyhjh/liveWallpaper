package presenter;

import java.util.ArrayList;

import model.entity.WallPaper;

/**
 * Created by Tyhj on 2017/5/25.
 */

public interface ShowWallPapers {
    void showLoading();
    void hideLoading();
    void toMainActivity(ArrayList<WallPaper> wallPapers);
    void showFailedError();
}
