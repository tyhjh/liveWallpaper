package di.modules;

import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import model.entity.UserInfo;
import presenter.CheckUpdate;
import presenter.GetLeftNote;
import presenter.SetUserInfo;
import presenter.ShowHome;
import presenter.ShowWallPapers;
import ui.activity.MainActivity;
import util.CommonUtil;

/**
 * Created by Tyhj on 2017/6/10.
 */

@Module
public class MainModule {

    ShowWallPapers wallPapers;
    CheckUpdate checkUpdate;
    SetUserInfo setUserInfo;
    ShowHome showHome;
    GetLeftNote getLeftNote;
    Context context;

    public MainModule(MainActivity activity) {
        this.wallPapers = activity;
        this.checkUpdate = activity;
        this.setUserInfo = activity;
        this.showHome = activity;
        this.getLeftNote = activity;
        this.context = activity.getBaseContext();
    }


    @Provides
    ShowWallPapers provideShow() {
        return wallPapers;
    }

    @Provides
    @Named("version")
    String provideString() {
        return CommonUtil.getAppVersion(context);
    }

    //App升级监听
    @Provides
    CheckUpdate providerCheckupListener() {
        return checkUpdate;
    }

    @Provides
    SetUserInfo providerSetUserInfo() {
        return setUserInfo;
    }

    @Provides
    UserInfo provideUserInfo() {
        return new UserInfo(
                CommonUtil.getManufacturer(),
                CommonUtil.getModel(),
                CommonUtil.getDeviceIMEI(context),
                CommonUtil.getRomVersion(),
                CommonUtil.getAndroidVersion(),
                CommonUtil.getAppVersion(context));
    }

    @Provides
    ShowHome providerHome() {
        return showHome;
    }

    @Provides
    @Named("IMEI")
    String provideImei() {
        return CommonUtil.getDeviceIMEI(context);
    }

    @Provides
    GetLeftNote providerLfetNodeListener() {
        return getLeftNote;
    }


}
