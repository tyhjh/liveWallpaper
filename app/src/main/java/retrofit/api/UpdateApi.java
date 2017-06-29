package retrofit.api;

import io.reactivex.Observable;
import model.entity.Update;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Tyhj on 2017/6/12.
 */


public interface UpdateApi {
    @GET("update")
    Observable<Update> update(@Query("sign") String sign,@Query("data") String data);
}
