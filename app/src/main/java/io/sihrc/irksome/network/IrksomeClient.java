package io.sihrc.irksome.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Chris on 9/2/15.
 */
public class IrksomeClient {
    final static private String ADDRESS = "http://45.55.45.67";
    public IrksomeService api;

    public IrksomeClient() {
        Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new ItemFactory())
            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setEndpoint(ADDRESS)
            .setConverter(new GsonConverter(gson))
            .build();

        api = restAdapter.create(IrksomeService.class);
    }
}
