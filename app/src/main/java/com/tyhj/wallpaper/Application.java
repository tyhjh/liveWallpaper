package com.tyhj.wallpaper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.litesuits.orm.LiteOrm;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit.converter.WallpaperFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import util.https.InternetUtil;

/**
 * Created by Tyhj on 2017/5/23.
 */

public class Application extends android.app.Application {
    static LiteOrm liteOrm;
    public static boolean isFirstCamera = true;
    private static Retrofit retrofit;
    private static boolean ISDEBUG=true;
    private static Context context;
    private static String gifPath=null;
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化参数依次为 this, AppId, AppKey
        context=getBaseContext();
        liteOrm = LiteOrm.newSingleInstance(getApplicationContext(), ".db");
        initDir();
        initPicasso();
        Fresco.initialize(getApplicationContext());
        initRetrofite();
    }

    private void initRetrofite() {
        //cache url
        File httpCacheDirectory = new File(getCacheDir(), "responses");

        int cacheSize = 50 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(getInterceptor())
                .addNetworkInterceptor(getNetWorkInterceptor())
                .cache(cache).build();

            retrofit = new Retrofit
                    .Builder()
                    .baseUrl("http://www.tyhj5.com/wallPaper/")
                    .client(client)
                    .addConverterFactory(WallpaperFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
    }

    private void initPicasso() {
        Picasso picasso = new Picasso.Builder(this)
                .memoryCache(new LruCache(10 << 20))//设置内存缓存大小10M
                .indicatorsEnabled(false) //设置左上角标记，主要用于测试
                .build();
        Picasso.setSingletonInstance(picasso);
    }

    public static LiteOrm getLiteOrm() {
        return liteOrm;
    }

    public void initDir() {
        File f1 = new File(Environment.getExternalStorageDirectory() + "/AWallpaper");
        if (!f1.exists()) {
            f1.mkdirs();
        }
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    private static int width, height;

    private static int imageId = R.raw.girl;

    private static String imageDir = null;

    public static int getImageId() {
        return imageId;
    }

    public static void setImageId(int imageId) {
        Application.imageId = imageId;
    }

    public static String getImageDir() {
        return imageDir;
    }

    public static void setImageDir(String imageDir) {
        Application.imageDir = imageDir;
    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        Application.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        Application.height = height;
    }

    public static int getVideoWidth() {
        return width * 3 / 5 + 5;
    }

    public static int getVideoHeight() {
        return height * 3 / 6;
    }

    public static String getGifPath() {
        return gifPath;
    }

    public static void setGifPath(String gifPath) {
        Application.gifPath = gifPath;
    }

    /**
     * 设置返回数据的  Interceptor  判断网络   没网读取缓存
     */
    public Interceptor getInterceptor(){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!InternetUtil.isOnline(getBaseContext())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                return chain.proceed(request);
            }
        };
    }

    /**
     * 设置连接器  设置缓存
     */
    public Interceptor getNetWorkInterceptor (){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                if (InternetUtil.isOnline(getBaseContext())) {
                    int maxAge = 0 * 60;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为1周
                    int maxStale = 60 * 60 * 24 * 7;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
    }

    public static void log(String key,String msg) {
        if (ISDEBUG)
            Log.e(key, msg);
    }
}
