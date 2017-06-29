package retrofit.converter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import model.entity.Home;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static com.tyhj.wallpaper.Application.log;

/**
 * Created by Tyhj on 2017/6/14.
 */

public class HomeConvert implements Converter<ResponseBody, Home> {

    public static final HomeConvert INSTANCE = new HomeConvert();

    @Override
    public Home convert(ResponseBody value) throws IOException {
        String home = value.string();
        log("getHome", home);
        try {
            JSONObject jsonObject = new JSONObject(home);
            if (jsonObject.getInt("code") == 200) {
                return new Home(jsonObject.getInt("type"), jsonObject.getString("imageUrl"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
