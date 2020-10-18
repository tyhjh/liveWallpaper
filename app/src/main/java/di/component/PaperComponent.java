package di.component;

import dagger.Component;
import di.modules.ShowImageModule;
import com.tyhj.wallpaper.ui.activity.ShowImage;

/**
 * Created by Tyhj on 2017/6/11.
 */

@Component(modules = {ShowImageModule.class})
public interface PaperComponent {
    void inject(ShowImage showImage);
}
