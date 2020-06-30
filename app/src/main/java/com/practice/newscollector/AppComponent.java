package com.practice.newscollector;

import com.practice.newscollector.model.dao.NewsDao;
import com.practice.newscollector.model.dao.NewsDaoWorker;
import com.practice.newscollector.model.newtwork_api.NetworkClient;
import com.practice.newscollector.ui.news_details.NewsDetailsFragmentModule;
import com.practice.newscollector.ui.news_list.NewsListFragmentModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void injectNewsListFragment(NewsListFragmentModule module);

    void injectNewsDetailsFragment(NewsDetailsFragmentModule module);

    NewsDao provideNewsDao();

    NewsDaoWorker provideNewsDaoWorker();

    NetworkClient provideNetworkClient();
}
