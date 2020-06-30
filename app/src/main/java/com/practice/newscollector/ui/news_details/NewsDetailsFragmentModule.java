package com.practice.newscollector.ui.news_details;

import com.practice.newscollector.App;
import com.practice.newscollector.AppComponent;
import com.practice.newscollector.model.logger.Logger;
import com.practice.newscollector.ui.news_list.NewsListContract;
import com.practice.newscollector.ui.news_list.NewsListPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class NewsDetailsFragmentModule {
    private final AppComponent appComponent;

    NewsDetailsFragmentModule() {
        appComponent = App.getInstance().getAppComponent();
        appComponent.injectNewsDetailsFragment(this);
    }

    @Provides
    NewsDetailsContract.Presenter providePresenter() {
        return new NewsDetailsPresenter(appComponent.provideNewsDaoWorker(), Logger.withTag("MyLog"));
    }
}
