package com.practice.newscollector.ui.news_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.practice.newscollector.R;
import com.practice.newscollector.model.dao.ArticleSchema;
import com.practice.newscollector.model.logger.ILog;
import com.practice.newscollector.model.logger.Logger;
import com.practice.newscollector.ui.arch.fragments.MvpFragment;


import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;

public class NewsListFragment extends MvpFragment<NewsListContract.Presenter, NewsListContract.Host> implements NewsListContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rvNews)
    RecyclerView rvNews;
    private final NewsRecyclerAdapter adapter = new NewsRecyclerAdapter();
    private SwipeRefreshLayout swipeRefreshLayout;
    private final ILog logger = Logger.withTag("MyLog");
    private Unbinder unbinder;

    @Inject
    NewsListContract.Presenter presenter;

    public static NewsListFragment newInstance(){
        return new NewsListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.log("NewsListFragment onCreate()");
        DaggerNewsListFragmentComponent.builder()
                .newsListFragmentModule(new NewsListFragmentModule())
                .build()
                .injectNewsListFragment(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logger.log("NewsListFragment onCreateView()");
        View v = inflater.inflate(R.layout.fragment_news_list, container, false);
        if(adapter.getItemCount() == 0){
            presenter.setArticlesList();
        }
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logger.log("NewsListFragment onViewCreated()");
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        unbinder = ButterKnife.bind(this, view);
        rvNews.setAdapter(adapter);
        rvNews.setLayoutManager(new LinearLayoutManager(view.getContext()));
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        logger.log("NewsListFragment onStart()");
        getPresenter().setupSubscriptions();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        logger.log("NewsListFragment onRefresh()");
        presenter.setArticlesList();
    }

    @Override
    public void turnOffRefreshing() {
        logger.log("NewsListFragment turnOffRefreshing()");
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean isRefreshingState() {
        logger.log("NewsListFragment isRefreshingState()");
        return swipeRefreshLayout.isRefreshing();
    }

    @Override
    public Observable<Integer> getItemClickObservable() {
        return adapter.getClicksObservable();
    }

    @Override
    public Observable<Long> getReachEndObservable() {
        return adapter.getRichEndObservable();
    }

    @Override
    public void showNewsDetailsFragment(int articleId) {
        logger.log("NewsListFragment showNewsDetailsFragment()");
        if(hasCallBack()){
            getCallBack().showNewsDetailsFragment(articleId);
        }
    }

    @Override
    public void setNewsList(List<ArticleSchema> newsList) {
        adapter.setArticlesList(newsList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addArticlesToLIst(List<ArticleSchema> articles) {
        logger.log("NewsListFragment addArticlesToLIst()");
        adapter.addArticlesToList(articles);
    }

    @Override
    protected NewsListContract.Presenter getPresenter() {
        return presenter;
    }

}
