package com.tyhj.wallpaper.ui.service;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Environment;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CameraLiveWallpaper extends WallpaperService {

    String TAG = "CameraLiveWallpaper";

    public Engine onCreateEngine() {
        return new CameraEngine();
    }


    class CameraEngine extends Engine {
        private Camera camera;
        private long time = 0;
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            startPreview();
            // 设置处理触摸事件  
            setTouchEventsEnabled(true);
            Log.d(TAG, "onCreate");
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (System.currentTimeMillis() - time < 500) {
                        //对焦
                        camera.autoFocus(new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, Camera camera) {
                                if (success) {
                                    //拍照
                                    //camera.takePicture(null, null, JpgmPicture);
                                }
                            }
                        });
                    } else {
                        camera.autoFocus(new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, Camera camera) {

                            }
                        });
                        time = System.currentTimeMillis();
                    }
                    break;
            }
        }


        //拍照的图片
        private Camera.PictureCallback JpgmPicture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                File file = getOutputMediaFile();
                if (file == null)
                    return;
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(data);
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();
            }
        };

        /**
         * Create a File for saving an image or video
         */
        private File getOutputMediaFile() {
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "MyCameraApp");
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
            return mediaFile;
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            stopPreview();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                startPreview();
            } else {
                stopPreview();
            }
        }

        /**
         * 开始预览
         */
        public void startPreview() {
            Log.d(TAG, "startPreview");
            stopPreview();
            camera = Camera.open();
            camera.setDisplayOrientation(90);
            Camera.Parameters parameter = camera.getParameters();
            parameter.setPreviewFormat(ImageFormat.NV21);
            List<Camera.Size> sizeList = parameter.getSupportedPreviewSizes();
            //设置分辨率为相机支持的最大的一个
            parameter.setPreviewSize(sizeList.get(sizeList.size() - 1).width, sizeList.get(sizeList.size() - 1).height);
            camera.setParameters(parameter);
            try {
                camera.setPreviewDisplay(getSurfaceHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
        }

        /**
         * 停止预览
         */
        public void stopPreview() {
            if (camera != null) {
                try {
                    camera.stopPreview();
                    camera.setPreviewCallback(null);
                    camera.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                camera = null;
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            stopPreview();
        }
    }
}