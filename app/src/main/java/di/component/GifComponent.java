package di.component;

import dagger.Component;
import di.modules.ShowImageModule;
import ui.activity.GifActivity;

/**
 * Created by Tyhj on 2017/9/5.
 */
@Component(modules = {ShowImageModule.class})
public interface GifComponent {
    void inject(GifActivity gifActivity);
}
