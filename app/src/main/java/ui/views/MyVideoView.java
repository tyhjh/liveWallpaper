package ui.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.VideoView;

import com.tyhj.wallpaper.Application;

/**
 * Created by Tyhj on 2017/5/26.
 */

public class MyVideoView extends VideoView{

    Handler handler=new Handler();

    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        // Log.i("@@@@", "onMeasure");

        //下面的代码是让视频的播放的长宽是根据你设置的参数来决定

        int width = getDefaultSize(Application.getVideoWidth(), widthMeasureSpec);
        int height = getDefaultSize(Application.getVideoHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    public void start() {
        super.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MyVideoView.this.setBackgroundColor(Color.parseColor("#00000000"));
            }
        },1000);
    }
}
