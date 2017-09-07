package retrofit.converter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import model.entity.WallPaper;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static com.tyhj.wallpaper.Application.log;

/**
 * Created by Tyhj on 2017/6/11.
 */

public class WallpapeprConvert implements Converter<ResponseBody, ArrayList<WallPaper>> {

    public static final WallpapeprConvert INSTANCE = new WallpapeprConvert();

    @Override
    public ArrayList<WallPaper> convert(ResponseBody responseBody) throws IOException {
        String msg=responseBody.string();
        log("ArrayList<WallPaper>", msg);
        JSONObject object = null;
        try {
            object = new JSONObject(msg);
            if (object.getInt("code") == 200) {
                ArrayList<WallPaper> papers = new ArrayList<WallPaper>();
                JSONArray array = object.getJSONArray("wallpapers");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    papers.add(0,new WallPaper(json.getInt("id"), 2,
                            json.getString("image"),
                            json.getString("name"),
                            json.getString("mv"), null, null,
                            json.getString("preview")));
                }
                return papers;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
