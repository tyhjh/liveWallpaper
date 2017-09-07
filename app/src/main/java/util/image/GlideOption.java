package util.image;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tyhj.wallpaper.R;

/**
 * Created by Tyhj on 2017/6/19.
 */

public class GlideOption {

    private volatile static RequestOptions options;
    private volatile static RequestOptions options2;

    public static RequestOptions getOption() {
        if (options == null) {
            synchronized (GlideOption.class) {
                if (options == null) {
                    options = new RequestOptions()
                            .error(R.mipmap.logo1)
                            .priority(Priority.LOW)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                }
            }
        }
        return options;
    }

    public static RequestOptions getOption2() {
        if (options2 == null) {
            synchronized (GlideOption.class) {
                if (options2 == null) {
                    options2 = new RequestOptions()
                            .error(R.mipmap.logo1)
                            .priority(Priority.NORMAL)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                }
            }
        }
        return options2;
    }


}
