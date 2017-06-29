package presenter;

/**
 * Created by Tyhj on 2017/5/26.
 */

public interface ShowDownloadFile {
    public void downloadStart(int progress);
    public void downloading(int progress);
    public void downFinish(String path);
    public void downCancel(int progress);
    public void downLoadErro(String path);
}
