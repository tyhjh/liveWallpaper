package presenter.impl;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Tyhj on 2017/5/30.
 */

public class SyncWallpaper {

    public static void syncWallpaper(){
        final Statement statement=init();
        AVQuery<AVObject> query = new AVQuery<AVObject>("Wallpaper");
        query.whereNotEqualTo("id",-1);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> list, final AVException e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(e==null&&list.size()>0){
                            Log.e("查询",list.size()+"长度");
                            for(int i=0;i<list.size();i++){
                                int id=list.get(i).getInt("id");
                                String name=list.get(i).getString("name");
                                String icon=list.get(i).getAVFile("icon").getUrl();
                                String wallPaper=list.get(i).getAVFile("wallPaper").getUrl();
                                String sql="select * from wallpaper where id = '"+id+"'";
                                try {
                                    ResultSet resultSet=statement.executeQuery(sql);
                                    if(!resultSet.next()){
                                        sql="insert into wallpaper values('"+id+"','"+name+"','"+icon+"','"+icon+"','"+wallPaper+"','"+0+"')";
                                        statement.execute(sql);
                                    }
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }else if(e!=null){
                            Log.e("查询",e.getMessage().toString());
                        }
                    }
                }).start();
            }
        });
    }

    public static void syncApp(){
        final Statement statement=init();
        AVQuery<AVObject> query = new AVQuery<AVObject>("App");
        query.orderByDescending("version");
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject avObject, AVException e) {
                if(avObject!=null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String sql="select * from App where versionCode ='"+avObject.getString("versionCode")+"'";
                                ResultSet set=statement.executeQuery(sql);
                                if(!set.next()){
                                    sql="insert into App values('"+avObject.getString("versionCode")+"','" +
                                            avObject.getAVFile("app").getUrl()+"','"+
                                            avObject.getAVFile("image").getUrl()+"','"+
                                            avObject.getString("info")+"','"+
                                            avObject.getInt("version")+"')";
                                    statement.execute(sql);
                                }
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }).start();
                }else if(e!=null){
                    e.printStackTrace();
                }
            }
        });
    }

    static String url3 = "jxxxxxxxxxxxxxxxxxx";


    //初始化
    private static synchronized Statement init() {
        Connection conn=null;
        Statement statement=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // System.out.println("成功加载MySQL驱动！");
        } catch (Exception e) {
            System.out.println("找不到MySQL驱动!");
            e.printStackTrace();
        }
        try {
            conn = (Connection) DriverManager.getConnection(url3, "txxxyx", "xxxxxxx");
            // System.out.println("成功加载conn！");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("找不到conn!");
        }
        try {
            if (conn != null) {
                statement = (Statement) conn.createStatement();
            } else {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }

}
