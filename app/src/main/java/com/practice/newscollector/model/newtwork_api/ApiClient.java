package com.practice.newscollector.model.newtwork_api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.practice.newscollector.model.pojo.ResponseModel;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient implements NetworkClient{
    public static final String API_KEY = "bd88a6dfe2a14f73b4f46c220fc69523";
    public static final String  BBC_SOURCE= "bbc-news";
    public static final String  INDEPENDENT_SOURCE= "independent";

    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static Retrofit retrofit = null;
    private final ApiService apiService;

    public ApiClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    @Override
    public Single<ResponseModel> getNews(String source, int pageSize) {
        return apiService.getNews(source, pageSize, ApiClient.API_KEY);
    }

    @Override
    public Single<ResponseModel> getNewsFromDate(String source, int pageSize, String fromDate) {
        return apiService.getNewsFromDate(source, pageSize, ApiClient.API_KEY, fromDate);
    }

}
