package com.practice.newscollector.ui;

import android.os.Bundle;

import com.practice.newscollector.R;
import com.practice.newscollector.model.logger.ILog;
import com.practice.newscollector.model.logger.Logger;
import com.practice.newscollector.ui.arch.HostActivity;
import com.practice.newscollector.ui.news_details.NewsDetailsContract;
import com.practice.newscollector.ui.news_details.NewsDetailsFragment;
import com.practice.newscollector.ui.news_list.NewsListContract;
import com.practice.newscollector.ui.news_list.NewsListFragment;

import androidx.fragment.app.FragmentManager;

public class MainActivity extends HostActivity implements NewsListContract.Host,
        NewsDetailsContract.Host {

    private final ILog logger = Logger.withTag("MyLog");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportFragmentManager().getFragments().size() == 0) {
            logger.log("MainActivity in onCreate() add NewsListFragment");
            addFragment(NewsListFragment.newInstance());
        }
    }

    @Override
    protected int getFragmentContainerId() {
        logger.log("MainActivity in getFragmentContainerId()");
        return R.id.fragment_container;
    }

    @Override
    public void showNewsDetailsFragment(int articleTime) {
        logger.log("MainActivity in showNewsDetailsFragment()");
        replaceFragmentToBackStack(NewsDetailsFragment.newInstance(articleTime));
    }

    private void clearBackStack() {
        logger.log("MainActivity in clearBackStack()");
        final FragmentManager manager = getSupportFragmentManager();
        while (manager.getBackStackEntryCount() > 0) {
            manager.popBackStackImmediate();
        }
    }

}
