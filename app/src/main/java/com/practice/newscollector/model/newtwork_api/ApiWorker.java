package com.practice.newscollector.model.newtwork_api;

import com.practice.newscollector.model.pojo.ResponseModel;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiWorker {
    Single<ResponseModel> getNews(String source, int pageSize, String apiKey);

    Single<ResponseModel> getNewsFromDate(String source, int pageSize, String apiKey, String fromDate);
}
