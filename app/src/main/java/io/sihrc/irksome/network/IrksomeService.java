package io.sihrc.irksome.network;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Chris on 9/2/15.
 */
public interface IrksomeService {
    @POST("/search")
    void search(@Body PostPackage query, Callback<List<Map<String, Object>>> callback);
}
