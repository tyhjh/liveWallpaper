package retrofit.api;


import java.util.ArrayList;

import io.reactivex.Observable;
import model.entity.WallPaper;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface WallpaperApi {
    @GET("wallpapers")
    Observable<ArrayList<WallPaper>> getWallPapers(@Query("sign") String sign, @Query("data") String data);
}