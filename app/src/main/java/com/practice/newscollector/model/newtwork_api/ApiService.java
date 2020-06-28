package com.practice.newscollector.model.newtwork_api;

import com.practice.newscollector.model.pojo.ResponseModel;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService{

    @GET("everything")
    Single<ResponseModel> getNews(@Query("sources") String source, @Query("pageSize") int pageSize, @Query("apiKey") String apiKey);

    @GET("everything")
    Single<ResponseModel> getNewsFromDate(@Query("sources") String source, @Query("pageSize") int pageSize, @Query("apiKey") String apiKey, @Query("from") String fromDate);
}
