package com.practice.newscollector.ui.news_list;

import dagger.Component;

@Component(modules = {NewsListFragmentModule.class})
public interface NewsListFragmentComponent {
    void injectNewsListFragment(NewsListFragment fragment);
}
