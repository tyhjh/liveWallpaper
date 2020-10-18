package model.entity;

/**
 * @author tyhj
 * @date 2020/10/18
 * @Description: java类作用描述
 */

public class Update {

    private String apkUrl;

    private String info;

    private String versionCode;

    private String imageUrl;

    private int version;


    public Update(String apkUrl, String info, String versionCode, String imageUrl, int version) {
        this.apkUrl = apkUrl;
        this.info = info;
        this.versionCode = versionCode;
        this.imageUrl = imageUrl;
        this.version = version;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isCancelable(){
        return (version==0);
    }

}
