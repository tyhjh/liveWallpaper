package retrofit.api;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Tyhj on 2017/6/14.
 */

public interface SavaUserApi {
    @POST("userInfo")
    Observable<String> savaUser(@Query("sign") String sign,
                                @Query("data") String data,
                                @Query("imei") String imei,
                                @Query("model") String model,
                                @Query("name") String name,
                                @Query("AndroidVersion") String AndroidVersion,
                                @Query("AppVersion") String AppVersion,
                                @Query("os") String os
    );
}
