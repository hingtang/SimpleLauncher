package com.hing.simplelauncher.servers;

import com.hing.simplelauncher.pojo.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by HingTang on 5/1/18.
 */
public interface CountryAPI {
    @GET("name/{name}")
    Call<List<Country>> getCountryInfo(@Path(value = "name", encoded = true) String name);
}
