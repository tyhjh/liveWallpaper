package util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;

import java.util.HashMap;

/**
 * Created by Tyhj on 2017/5/26.
 */

public class ImageUtil {
    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }


    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;

    //获取屏幕大小
    public static void getWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        SCREEN_WIDTH = metrics.widthPixels;//获取到的是px，像素，绝对像素，需要转化为dpi
        SCREEN_HEIGHT = getRealHeight(context);
    }


    public static int getRealHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenHeight = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics dm = new DisplayMetrics();
            display.getRealMetrics(dm);
            screenHeight = dm.heightPixels;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            try {
                screenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception e) {
                DisplayMetrics dm = new DisplayMetrics();
                display.getMetrics(dm);
                screenHeight = dm.heightPixels;
            }
        }
        return screenHeight;
    }






    //获取bitmap颜色
    public static Palette.Swatch getColor(Bitmap bitmap) {
        // Palette的部分
        Palette palette = Palette.generate(bitmap);
        Palette.Swatch swatche = null;
        if (palette != null) {
            swatche = palette.getVibrantSwatch();
        }
        return swatche;
    }


    //设置控件轮廓
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static ViewOutlineProvider getOutline(boolean b, final int pading, final int circularBead) {
        if (b) {
            return new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, android.graphics.Outline outline) {
                    final int margin = Math.min(view.getWidth(), view.getHeight()) / pading;
                    outline.setOval(margin, margin, view.getWidth() - margin, view.getHeight() - margin);
                }
            };
        } else {
            return new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, android.graphics.Outline outline) {
                    final int margin = Math.min(view.getWidth(), view.getHeight()) / pading;
                    outline.setRoundRect(margin, margin, view.getWidth() - margin, view.getHeight() - margin, circularBead);
                }
            };
        }
    }

}
