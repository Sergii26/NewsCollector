package com.practice.newscollector.ui.news_details;

import com.practice.newscollector.ui.news_list.NewsListFragment;
import com.practice.newscollector.ui.news_list.NewsListFragmentModule;

import dagger.Component;

@Component(modules = {NewsDetailsFragmentModule.class})
public interface NewsDetailsFragmentComponent {
    void injectNewsDetailsFragment(NewsDetailsFragment fragment);
}
