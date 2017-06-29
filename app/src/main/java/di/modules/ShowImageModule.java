package di.modules;

import dagger.Module;
import dagger.Provides;
import model.entity.DownloadFile;
import presenter.ShowDownloadFile;

/**
 * Created by Tyhj on 2017/6/11.
 */

@Module
public class ShowImageModule {

    ShowDownloadFile downloadPaper;
    DownloadFile file;


    public ShowImageModule(ShowDownloadFile downloadPaper,String url,String path) {
        this.downloadPaper = downloadPaper;
        file=new DownloadFile(url,path);
    }

    @Provides
    ShowDownloadFile providerOnDownloadListener(){
        return downloadPaper;
    }

    @Provides
    DownloadFile providerFile(){
        return file;
    }

}
