package model.impl;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import model.entity.DownloadFile;
import presenter.ShowDownloadFile;

/**
 * Created by Tyhj on 2017/5/26.
 */

public class DownLoadImpl {

    ShowDownloadFile listener;
    DownloadFile paper;

    @Inject
    public DownLoadImpl(ShowDownloadFile listener, DownloadFile paper) {
        this.listener = listener;
        this.paper = paper;
    }

    public void setPaper(String url) {
        paper.setUrl(url);
    }

    int finish = 0;

    //开始或者暂停下载
    private boolean downLoading = true;
    //用于保存下载线程（每段为一个线程）
    private List<HashMap<String, Integer>> threadList = new ArrayList<>();

    //文件长度
    private int leanth;
    //下载进度
    private int total = 0;
    //下载地址
    private URL url;
    //下载的文件
    private File file;

    public void onDownload() {
        Observable down = Observable.create(new ObservableOnSubscribe<Msg>() {
            @Override
            public void subscribe(ObservableEmitter<Msg> emitter) throws Exception {
                url = new URL(paper.getUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(500);
                //获取文件的大小
                leanth = connection.getContentLength();
                Log.e("开始长度：", leanth + "");
                //设置进度条的长度
                emitter.onNext(new Msg(0, null, leanth));
                //文件不存在
                if (leanth < 0) {
                    emitter.onError(new Throwable(paper.getPath()));
                    emitter.onComplete();
                    return;
                }
                //保存文件地址
                file = new File(paper.getPath());
                if (file.exists())
                    file.delete();
                //设置分割下载的文件
                RandomAccessFile random = new RandomAccessFile(file, "rw");
                random.setLength(leanth);
                //分为三段下载
                int blockSize = leanth / 3;

                for (int i = 0; i < 3; i++) {
                    int begin = i * blockSize;
                    int end = (i + 1) * blockSize;
                    if (i == 2)
                        end = leanth;
                    //设置每段下载的长度
                    HashMap<String, Integer> map = new HashMap<String, Integer>();
                    map.put("begin", begin);
                    map.put("end", end);
                    map.put("finished", 0);
                    threadList.add(map);
                    //创建线程下载文件
                    Thread t = new Thread(new Download(begin, end, file, url, i, emitter));
                    t.start();
                }
            }
        });

        //创建一个下游 Observer
        Observer<Msg> observer = new Observer<Msg>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Msg value) {
                switch (value.getId()) {
                    case 0:
                        listener.downloadStart(value.getProgress());
                        break;
                    case 1:
                        listener.downloading(value.getProgress());
                        break;
                    case 2:
                        listener.downFinish(value.getMsg());
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
                listener.downLoadErro(paper.getPath());
            }

            @Override
            public void onComplete() {

            }
        };

        down.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    //下载类
    class Download implements Runnable {
        private int begin;
        private int end;
        private File file;
        private URL url;
        private int id;
        private ObservableEmitter emitter;

        public Download(int begin, int end, File file, URL url, int id, ObservableEmitter emitter) {
            this.begin = begin;
            this.end = end;
            this.file = file;
            this.url = url;
            this.id = id;
            this.emitter = emitter;
        }

        @Override
        public void run() {
            try {
                //判断是否下载完成
                if (begin > end)
                    return;

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(500);
                //设置需要下载的位置
                connection.setRequestProperty("Range", "bytes=" + begin + "-" + end);

                InputStream is = connection.getInputStream();
                byte[] buff = new byte[1024 * 1024];
                RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
                //设置写入文件的位置
                accessFile.seek(begin);
                int len = 0;
                HashMap<String, Integer> map = threadList.get(id);
                //开始下载
                while ((len = is.read(buff)) != -1 && downLoading) {
                    accessFile.write(buff, 0, len);
                    //更新总下载进度
                    total = total + len;
                    emitter.onNext(new Msg(1, null, total));
                    //更新每段的下载进度
                    map.put("finished", map.get("finished") + len);
                }
                finish++;
                if (finish == 3) {
                    emitter.onNext(new Msg(2, file.getPath(), 0));
                    emitter.onComplete();
                }
                is.close();
                accessFile.close();

            } catch (IOException e) {
                listener.downLoadErro(paper.getPath());
                emitter.onError(new Throwable(paper.getPath()));
                e.printStackTrace();
            }
        }
    }

    class Msg {
        String msg;
        int progress;
        int id;

        public Msg(int id, String msg, int progress) {
            this.msg = msg;
            this.progress = progress;
            this.id = id;
        }

        public String getMsg() {
            return msg;
        }

        public int getProgress() {
            return progress;
        }

        public int getId() {
            return id;
        }
    }

}
