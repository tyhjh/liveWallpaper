package retrofit.converter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import model.entity.CallbackInfo;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static com.tyhj.wallpaper.Application.log;

/**
 * Created by Tyhj on 2017/6/14.
 */

public class CallbackConvert implements Converter<ResponseBody,CallbackInfo> {

    public static final CallbackConvert INSTANCE=new CallbackConvert();

    @Override
    public CallbackInfo convert(ResponseBody value) throws IOException {
        String callback=value.string();
        log("CallbackInfo",callback);
        try {
            JSONObject jsonObject=new JSONObject(callback);
            if(jsonObject.getInt("code")==200){
                return new CallbackInfo(jsonObject.getString("leftNote"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
