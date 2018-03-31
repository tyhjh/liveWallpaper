package ui.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.tyhj.wallpaper.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.image.ImageUtil;

/**
 * Created by Tyhj on 2017/5/23.
 */

public class MallpaperService extends WallpaperService {

    private final Handler mHandler = new Handler();
    Bitmap newbm;

    @Override
    public void onCreate() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg);
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 设置想要的大小
        int newWidth = ImageUtil.SCREEN_WIDTH;
        int newHeight = ImageUtil.SCREEN_HEIGHT;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        super.onCreate();
    }

    @Override
    public Engine onCreateEngine() {
        return new Mngine();
    }


    //Engine是WallpaperService中的一个内部类，实现了壁纸窗口的创建以及Surface的维护工作
    class Mngine extends Engine {
        float touchX = 0;
        float touchY = 0;
        boolean isTouch;
        Paint paint;
        int width;
        int height;
        List<Mycircle> mycircleList;
        int distance = 100;
        float speed = 1.7f;
        int count = 55;
        float radius = 3.5f;
        int gap = 1;
        int length = 8;
        private boolean mVisible;

        private final Runnable mDrawCube = new Runnable() {
            public void run() {
                drawFrame();
            }
        };

        private void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            canvas = holder.lockCanvas();
            canvas.save();
            /*paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPaint(paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));*/
            canvas.drawBitmap(newbm, 0, 0, paint);

            for (int i = 0; i < mycircleList.size(); i++) {
                if (mycircleList.get(i).startX > width || mycircleList.get(i).startX < 0) {
                    mycircleList.get(i).speedX = -mycircleList.get(i).speedX;
                }
                if (mycircleList.get(i).startY > height || mycircleList.get(i).startY < 0) {
                    mycircleList.get(i).speedY = -mycircleList.get(i).speedY;
                }
                mycircleList.get(i).startX = mycircleList.get(i).startX + mycircleList.get(i).speedX;
                mycircleList.get(i).startY = mycircleList.get(i).startY + mycircleList.get(i).speedY;
                //画圆
                canvas.drawCircle(mycircleList.get(i).startX, mycircleList.get(i).startY, mycircleList.get(i).radius, mycircleList.get(i).paint);
            }
            //画连接线
            for (int i = 0; i < mycircleList.size() - 1; i++) {
                for (int j = i + 1; j < mycircleList.size(); j++) {
                    float x = mycircleList.get(i).startX - mycircleList.get(j).startX;
                    float y = mycircleList.get(i).startY - mycircleList.get(j).startY;
                    if (x * x + y * y < mycircleList.get(i).distance * distance * distance) {
                        canvas.drawLine(mycircleList.get(i).startX, mycircleList.get(i).startY,
                                mycircleList.get(j).startX, mycircleList.get(j).startY, mycircleList.get(i).paint);
                    }
                }
            }
            //点击时候处理
            if (isTouch) {
                List<Mycircle> connect = new ArrayList<Mycircle>();
                canvas.drawCircle(touchX, touchY, 5, paint);
                for (int i = 0; i < mycircleList.size(); i++) {
                    float x = mycircleList.get(i).startX - touchX;
                    float y = mycircleList.get(i).startY - touchY;
                    if (x * x + y * y < 8 * distance * distance) {
                        canvas.drawLine(mycircleList.get(i).startX, mycircleList.get(i).startY,
                                touchX, touchY, mycircleList.get(i).paint);
                        connect.add(mycircleList.get(i));
                    }
                }

                for (int i = 0; i < connect.size(); i++) {
                    for (int k = connect.size() - 1; k > i; k--) {
                        float x = connect.get(i).startX - connect.get(k).startX;
                        float y = connect.get(i).startY - connect.get(k).startY;
                        if (x*x+y*y<10*distance*distance) {
                            canvas.drawLine(connect.get(i).startX, connect.get(i).startY,
                                    connect.get(k).startX, connect.get(k).startY, connect.get(i).paint);
                        }
                    }
                }


            }
            int x = width / 4;
            int y = height / 4;
            canvas.restore();
            holder.unlockCanvasAndPost(canvas);
            mHandler.removeCallbacks(mDrawCube);
            if (mVisible) {
                mHandler.postDelayed(mDrawCube, 10);
            }

        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
        }


        public Mngine() {
            paint = new Paint();
            paint.setAntiAlias(true);
            //设置颜色
            paint.setColor(Color.WHITE);
            //设置背景
            //canvas.drawColor(Color.WHITE);
            mycircleList = new ArrayList<Mycircle>();
            Random random = new Random(System.currentTimeMillis());
            for (int i = 0; i < count; i++) {
                int distance = 1;
                int alpha = random.nextInt(60) + 40;
                float radius = this.radius * random.nextFloat() + 1.5f;
                float startX = random.nextInt(1080);
                float startY = random.nextInt(1920);
                float speedX = speed - 2 * speed * random.nextFloat();
                float speedY = speed - 2 * speed * random.nextFloat();

                if (speedX == 0)
                    speedX = 0.5f;
                if (speedY == 0)
                    speedY = -0.5f;

                if (i % 50 == 0)
                    distance = 2;
                else if (i % 30 == 0)
                    distance = 2;
                else if (i % 20 == 0)
                    distance = 2;


                if (radius < 0.7f)
                    distance = 0;
                Mycircle mycircle = new Mycircle(radius, startX, startY, speedX, speedY, alpha, distance);
                mycircleList.add(mycircle);
            }
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawCube);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            /*下面这个判断好玩，就是说，如果屏幕壁纸状态转为显式时重新绘制壁纸，否则黑屏幕，隐藏就可以*/
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(mDrawCube);
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isTouch = true;
                    touchX = event.getX();
                    touchY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    isTouch = true;
                    touchX = event.getX();
                    touchY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    isTouch = false;
                    touchX = -1;
                    touchY = -1;
                    break;
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            this.width = width;
            this.height = height;
            drawFrame();
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawCube);
        }
    }


    class Mycircle {
        int distance;
        Paint paint;
        float radius;
        float startX;
        float startY;
        float speedX;
        float speedY;
        int alpha;

        public Mycircle(float radius, float startX, float startY, float speedX, float speedY, int alpha, int distance) {
            this.radius = radius;
            this.startX = startX;
            this.startY = startY;
            this.speedX = speedX;
            this.speedY = speedY;
            this.alpha = alpha;
            paint = new Paint();
            paint.setAntiAlias(true);
            //设置颜色
            paint.setColor(Color.WHITE);
            paint.setAlpha(alpha);
            this.distance = distance;
        }
    }

}
