package retrofit.api;

import io.reactivex.Observable;
import model.entity.CallbackInfo;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Tyhj on 2017/6/14.
 */

public interface LeftNoteApi {
    @GET("callBack")
    Observable<CallbackInfo> getCallback(@Query("sign") String sign, @Query("data") String data, @Query("imei") String imei);
}
