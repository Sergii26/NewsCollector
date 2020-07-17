package com.practice.newscollector;

import com.practice.newscollector.ui.news_details.NewsDetailsPresenter;
import com.practice.newscollector.ui.news_details.NewsDetailsPresenterTest;
import com.practice.newscollector.ui.news_list.NewsListPresenterTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({NewsDetailsPresenterTest.class, NewsListPresenterTest.class})
public class TestSuite {
}
