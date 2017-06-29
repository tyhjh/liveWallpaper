package retrofit.api;

import io.reactivex.Observable;
import model.entity.Home;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Tyhj on 2017/6/14.
 */

public interface HomeApi {
    @GET("home")
    Observable <Home> getHome(@Query("sign") String sign,@Query("data") String data);
}
