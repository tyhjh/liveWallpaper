package model.entity;

/**
 * @author tyhj
 * @date 2020/10/18
 * @Description: java类作用描述
 */

public class UserInfo {

    private String phoneName;

    private String phoneModel;

    private String phoneIMEI;

    private String Os;

    private String version;

    private String appVersion;

    public UserInfo(String phoneName, String phoneModel, String phoneIMEI, String os, String version, String appVersion) {
        this.phoneName = phoneName;
        this.phoneModel = phoneModel;
        this.phoneIMEI = phoneIMEI;
        Os = os;
        this.version = version;
        this.appVersion = appVersion;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getPhoneIMEI() {
        return phoneIMEI;
    }

    public void setPhoneIMEI(String phoneIMEI) {
        this.phoneIMEI = phoneIMEI;
    }

    public String getOs() {
        return Os;
    }

    public void setOs(String os) {
        Os = os;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
