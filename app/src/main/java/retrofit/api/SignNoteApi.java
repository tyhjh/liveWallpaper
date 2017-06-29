package retrofit.api;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Tyhj on 2017/6/14.
 */

public interface SignNoteApi {
    @POST("leftNote")
    Observable<String> sign(@Query("sign")String sign, @Query("data") String data, @Query("imei") String imei,@Query("leftNote") String msg);
}
