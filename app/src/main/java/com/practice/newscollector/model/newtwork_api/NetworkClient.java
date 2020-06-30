package com.practice.newscollector.model.newtwork_api;

import com.practice.newscollector.model.pojo.ResponseModel;

import io.reactivex.Single;

public interface NetworkClient {

    Single<ResponseModel> getNews(String source, int pageSize);

    Single<ResponseModel> getNewsFromDate(String source, int pageSize, String fromDate);
}
