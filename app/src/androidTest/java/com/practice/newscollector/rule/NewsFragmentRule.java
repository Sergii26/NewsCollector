package com.practice.newscollector.rule;

import android.app.Activity;

import com.practice.newscollector.model.dao.ArticleSchema;
import com.practice.newscollector.ui.arch.HostActivity;
import com.practice.newscollector.ui.news_list.NewsListFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

public class NewsFragmentRule<T extends Activity> extends ActivityTestRule {
    public NewsFragmentRule(Class activityClass) {
        super(activityClass);
    }

    public List<ArticleSchema> getArticles(){
        HostActivity activity = (HostActivity)getActivity();
        List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
        NewsListFragment newsFragment = (NewsListFragment)fragments.get(fragments.size()-1);
        return newsFragment.getArticles();
    }

}
