package retrofit.converter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import model.entity.Update;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static android.R.attr.version;
import static com.tyhj.wallpaper.Application.log;

/**
 * Created by Tyhj on 2017/6/12.
 */

public class UpdateConvert implements Converter<ResponseBody, Update> {

    public static final UpdateConvert INSTANCE = new UpdateConvert();

    @Override
    public Update convert(ResponseBody value) throws IOException {
        String msg = value.string();
        log("Update",msg);
        try {
            JSONObject jsonObject = new JSONObject(msg);
            if (jsonObject.getInt("code") == 200) {
                if (!jsonObject.getString("versionCode").equals(version)) {
                    return new Update(jsonObject.getString("apkUrl"),
                            jsonObject.getString("info"),
                            jsonObject.getString("versionCode"),
                            jsonObject.getString("imageUrl"),
                            jsonObject.getInt("version"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
