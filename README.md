![anita-austvika-1132037-unsplash.jpg](https://upload-images.jianshu.io/upload_images/4906791-620cc99ac0cddaac.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/640)

最近搞了一下Android的动态壁纸，像实现“萤火视频桌面”那样，本来以为很难的，但是了解了一下感觉还是很容易的。

> 效果图：http://lc-fgtnb2h8.cn-n1.lcfile.com/82f4e474384b28b5739a.gif


### Android壁纸的实现和管理分为三层：
*只想了解动态壁纸的看第一个就好了*
#### WallpaperService与Engine
壁纸运行在一个Android服务之中，这个服务的名字叫做WallpaperService。当用户选择了一个壁纸之后，此壁纸所对应的WallpaperService便会启动并开始进行壁纸的绘制工作。Engine是WallpaperService中的一个内部类，实现了壁纸窗口的创建以及Surface的维护工作。这一层次的内容主要体现了壁纸的实现原理。

#### WallpaperManagerService
这个系统服务用于管理壁纸的运行与切换，并通过WallpaperManager类向外界提供操作壁纸的接口。这一层次主要体现了Android对壁纸的管理方式。

#### WindowManagerService
用于计算壁纸窗口的Z序、可见性以及为壁纸应用窗口动画。这一层次主要体现了Android对壁纸窗口的管理方式。


### 实现
首先静态壁纸是很简单的，大概就是如下几种方法，我也没有试过，

>* 使用WallpaperManager的setResource(int ResourceID)方法
>* 使用WallpaperManager的setBitmap(Bitmap bitmap)方法
>* 使用WallpaperManager的setStream(InputStream data)方法

```java

//需要权限
<uses-permission android:name = "android.permission.SET_WALLPAPER"/>
WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
try {
	wallpaperManager.setResource(R.drawable.picture);
} catch (IOException e) {
	e.printStackTrace();
}
```

### 动态壁纸
刚才讲了，动态壁纸就是一个服务，我们先创建一个服务并继承**WallpaperService**。这个服务里面有个内部类**Engine**，实现了壁纸窗口的创建以及Surface的维护工作。就是说我们可以获取到一个**SurfaceHolder**，拿到这个东西就好办了，我们可以在上面画自己想要的东西或者把视频输出到上面去。

我就直接上代码了
#### 设置视频桌面

```java
public class VideoWallpaper extends WallpaperService {

    private MediaPlayer mp;
    private int progress = 0;
    
    //这里就是返回我们自定义的Engine
    @Override
    public Engine onCreateEngine() {
        return new VideoEngine();
    }
    //自定义Engine
    class VideoEngine extends Engine {
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            //可以设置点击事件
            setTouchEventsEnabled(true);
        }

    
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
        //把视频输出到SurfaceHolder上面
            if (mp != null && mp.isPlaying())
                return;
            //可以设置SD卡的视频
            mp = MediaPlayer.create(getApplicationContext(), R.raw.bird);
            //这句话并不简单
            mp.setSurface(holder.getSurface());
            //重复播放
            mp.setLooping(true);
            mp.start();
        }


//当桌面不可见的时候的处理
        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                if (mp != null)
                    return;
                mp = MediaPlayer.create(getApplicationContext(), R.raw.bird);
                mp.setSurface(getSurfaceHolder().getSurface());
                mp.setLooping(true);
                //获取进度播放
                mp.seekTo(progress);
                mp.start();
            } else {
                if (mp != null && mp.isPlaying()) {
                    //保存进度
                    progress = mp.getCurrentPosition();
                    mp.stop();
                    mp.release();
                    mp = null;
                }
            }

        }

        @Override
        public void onDestroy() {
            if (mp != null) {
                mp.stop();
                mp.release();
            }
            super.onDestroy();
        }
    }

}

```
上面的代码并不复杂，只是自己做的时候会遇到一些问题，首在不能在Engine的onCreate的方法里面设置视频播放，应该是SurfaceHolder还没有创建吧，还有mediaPlayer设置输出的Surface

```java
//正确设置代码
mediaPlayer.setSurface(holder.getSurface());
//一般是这样设置，这里这样设置报错
mediaPlayer.setDisplay(holder);
```
是Service那肯定要注册的
```xml
<service
            android:name="ui.service.VideoWallpaper"
            android:permission="android.permission.BIND_WALLPAPER">
            <!-- 为动态壁纸配置intent-filter -->
            <intent-filter>
                <action android:name="android.service.wallpaper.WallpaperService" />
            </intent-filter>
            <!-- 为动态壁纸配置meta-data -->
            <meta-data
                android:name="android.service.wallpaper"
                android:resource="@xml/livewallpapervideo" />
        </service>
```

然后关于"livewallpapervideo.xml"，就是一个正常的布局文件
```xml

<?xml version="1.0" encoding="utf-8"?>
<wallpaper xmlns:android="http://schemas.android.com/apk/res/android"
    android:settingsActivity="ui.activity.LiveWallPreference"
    android:thumbnail="@mipmap/ic_video"
    android:description="@string/wallpaper_description4"
    />

```
里面有个settingsActivity，这个东西我不知道有什么用，就是随便一个Activity，你也要注册这个Activity，


#### 其他玩法
除了将视频作为壁纸以为还可以将GIF作为壁纸，将摄像头获取到的图象作为壁纸，甚至直接自己在SurfaceHolder自己画东西都可以，道理都是一样的

服务器挂了，可以看看本地壁纸
> 项目地址：https://github.com/tyhjh/liveWallpaper


