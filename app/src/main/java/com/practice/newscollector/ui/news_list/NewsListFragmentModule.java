package com.practice.newscollector.ui.news_list;

import com.practice.newscollector.App;
import com.practice.newscollector.AppComponent;
import com.practice.newscollector.model.logger.Logger;
import com.practice.newscollector.model.utils.AndroidUtils;
import com.practice.newscollector.model.utils.Utils;

import dagger.Module;
import dagger.Provides;
@Module
public class NewsListFragmentModule {

    private final AppComponent appComponent;

    NewsListFragmentModule() {
        appComponent = App.getInstance().getAppComponent();
        appComponent.injectNewsListFragment(this);
    }

    @Provides
    NewsListContract.Presenter providePresenter() {
        return new NewsListPresenter(appComponent.provideNetworkClient(), appComponent.provideNewsDaoWorker(),
                Logger.withTag("MyLog"), appComponent.provideUtils());
    }

}
