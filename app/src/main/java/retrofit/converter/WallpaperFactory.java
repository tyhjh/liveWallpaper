package retrofit.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import model.entity.CallbackInfo;
import model.entity.Home;
import model.entity.Update;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Tyhj on 2017/6/12.
 */

public class WallpaperFactory extends Converter.Factory {
    public static final WallpaperFactory INSTANCE = new WallpaperFactory();

    public static WallpaperFactory create() {
        return INSTANCE;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if(type.toString().equals("java.util.ArrayList<model.entity.WallPaper>")){
            return WallpapeprConvert.INSTANCE;
        }else if(type== Update.class){
            return UpdateConvert.INSTANCE;
        }else if(type== Home.class){
            return HomeConvert.INSTANCE;
        }else if(type==CallbackInfo.class){
            return CallbackConvert.INSTANCE;
        }else if(type==String.class){
            return StringConvert.INSTANCE;
        }

        return StringConvert.INSTANCE;
    }
}
