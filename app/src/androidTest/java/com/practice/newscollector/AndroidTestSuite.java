package com.practice.newscollector;

import com.practice.newscollector.ui.news_details.NewsDetailsFragmentTest;
import com.practice.newscollector.ui.news_list.NewsListFragmentTest;
import com.practice.newscollector.ui.news_list.NewsListFragmentWithoutNetworkTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({NewsDetailsFragmentTest.class, NewsListFragmentTest.class, NewsListFragmentWithoutNetworkTest.class})
public class AndroidTestSuite {
}
