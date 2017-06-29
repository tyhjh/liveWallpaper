package util.file;

import java.io.File;

/**
 * Created by Tyhj on 2017/5/29.
 */

public class FileUtil {


    public static boolean isFile(String path){
        if(path==null){
            return false;
        }else {
            File file=new File(path);
            if(file.exists()){
                return true;
            }
        }
        return false;
    }





}
