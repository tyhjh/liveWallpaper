package di.component;

import dagger.Component;
import di.modules.MainModule;
import di.modules.ShowImageModule;
import com.tyhj.wallpaper.ui.activity.MainActivity;

/**
 * Created by Tyhj on 2017/6/10.
 */

@Component(modules = {MainModule.class, ShowImageModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
