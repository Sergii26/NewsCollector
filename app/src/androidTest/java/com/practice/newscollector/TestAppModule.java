package com.practice.newscollector;

import com.practice.newscollector.model.dao.NewsDao;
import com.practice.newscollector.model.dao.NewsDaoWorker;
import com.practice.newscollector.model.dao.NewsDatabase;
import com.practice.newscollector.model.dao.NewsDatabaseWorker;
import com.practice.newscollector.model.logger.Logger;
import com.practice.newscollector.model.newtwork_api.ApiClient;
import com.practice.newscollector.model.newtwork_api.NetworkClient;
import com.practice.newscollector.model.utils.TestAndroidUtils;
import com.practice.newscollector.model.utils.Utils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TestAppModule {
    @Provides
    @Singleton
    public NewsDao provideNewsDao() {
        return NewsDatabase.newInstance(App.getInstance());
    }

    @Provides
    public NewsDaoWorker provideNewsDaoWorker(){
        return new NewsDatabaseWorker(provideNewsDao().newsDao(), Logger.withTag("MyLog"));
    }

    @Provides
    @Singleton
    public NetworkClient provideNetworkClient(){
        return new ApiClient();
    }

    @Provides
    public Utils provideUtils(){
        return new TestAndroidUtils();
    }
}
