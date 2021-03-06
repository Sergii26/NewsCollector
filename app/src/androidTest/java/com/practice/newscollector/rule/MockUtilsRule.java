package com.practice.newscollector.rule;

import android.app.Activity;

import com.practice.newscollector.App;
import com.practice.newscollector.AppComponent;

import com.practice.newscollector.DaggerTestAppComponent;
import com.practice.newscollector.model.dao.ArticleSchema;
import com.practice.newscollector.ui.arch.HostActivity;
import com.practice.newscollector.ui.news_list.NewsListFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.test.rule.ActivityTestRule;

public class MockUtilsRule<T extends Activity> extends ActivityTestRule {

    public MockUtilsRule(Class activityClass) {
        super(activityClass);
    }

    public List<ArticleSchema> getArticles(){
        HostActivity activity = (HostActivity)getActivity();
        List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
        NewsListFragment newsFragment = (NewsListFragment)fragments.get(fragments.size()-1);
        return newsFragment.getArticles();
    }

    @Override
    protected void beforeActivityLaunched() {
        super.beforeActivityLaunched();
        App.getInstance().setAppComponent(DaggerTestAppComponent.create());

    }
}
