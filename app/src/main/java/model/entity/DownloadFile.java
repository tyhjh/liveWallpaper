package model.entity;

/**
 * @author tyhj
 * @date 2020/10/18
 * @Description: java类作用描述
 */

public class DownloadFile {

    private String url;

    private String path;

    public DownloadFile(String url, String path) {
        this.url = url;
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
