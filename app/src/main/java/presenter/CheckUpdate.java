package presenter;

import model.entity.Update;

/**
 * Created by Tyhj on 2017/5/28.
 */

public interface CheckUpdate {
     void lastVersion();
     void preVersion(Update update);
     void onErro();
}
