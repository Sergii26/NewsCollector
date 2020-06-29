package com.practice.newscollector.model.newtwork_api;

import com.practice.newscollector.model.logger.ILog;
import com.practice.newscollector.model.pojo.ResponseModel;

import io.reactivex.Single;

public class NetworkWorker implements ApiWorker{
    private final ApiService apiService;
    private final ILog logger;

    public NetworkWorker(ApiService apiService, ILog logger) {
        this.apiService = apiService;
        this.logger = logger;
    }

    @Override
    public Single<ResponseModel> getNews(String source, int pageSize, String apiKey) {
        return apiService.getNews(source, pageSize, apiKey);
    }

    @Override
    public Single<ResponseModel> getNewsFromDate(String source, int pageSize, String apiKey, String fromDate) {
        return apiService.getNewsFromDate(source, pageSize, apiKey, fromDate);
    }
}
