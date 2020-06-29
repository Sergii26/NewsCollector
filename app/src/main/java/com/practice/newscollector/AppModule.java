package com.practice.newscollector;

import com.practice.newscollector.model.dao.NewsDao;
import com.practice.newscollector.model.dao.NewsDaoWorker;
import com.practice.newscollector.model.dao.NewsDatabase;
import com.practice.newscollector.model.dao.NewsDatabaseWorker;
import com.practice.newscollector.model.logger.Logger;
import com.practice.newscollector.model.newtwork_api.ApiClient;
import com.practice.newscollector.model.newtwork_api.ApiService;
import com.practice.newscollector.model.newtwork_api.ApiWorker;
import com.practice.newscollector.model.newtwork_api.NetworkClient;
import com.practice.newscollector.model.newtwork_api.NetworkWorker;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @Singleton
    public NewsDao provideNewsDao() {
        return NewsDatabase.newInstance(App.getInstance());
    }

    @Provides
    public NewsDaoWorker provideNewsDaoWorker(){
        return new NewsDatabaseWorker(App.getInstance().getAppComponent().provideNewsDao().newsDao(), Logger.withTag("MyLog"));
    }

    @Provides
    @Singleton
    public NetworkClient provideNetworkClient(){
        return new ApiClient();
    }

    @Provides
    public ApiService provideApiService(){
        return App.getInstance().getAppComponent().provideNetworkClient().getApiService();
    }

    @Provides
    public ApiWorker provideApiWorker(){
        return new NetworkWorker(App.getInstance().appComponent.provideApiService(), Logger.withTag("MyLog"));
    }
}
