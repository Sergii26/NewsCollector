package com.practice.newscollector.ui.news_details;

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
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;

public class NewsDetailsFragment extends MvpFragment<NewsDetailsContract.Presenter, NewsDetailsContract.Host> implements NewsDetailsContract.View {
    public static final String KEY_ARTICLE_ID = "key_article_id";

    @BindView(R.id.rvDetails)
    RecyclerView rvDetails;
    private Unbinder unbinder;
    private final DetailsRecyclerAdapter adapter = new DetailsRecyclerAdapter();
    private final ILog logger = Logger.withTag("MyLog");

    @Inject
    NewsDetailsContract.Presenter presenter;

    public static NewsDetailsFragment newInstance(int articleId){
        final Bundle b = new Bundle();
        b.putInt(KEY_ARTICLE_ID, articleId);
        final NewsDetailsFragment f = new NewsDetailsFragment();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.log("NewsDetailsFragment onCreate()");
        DaggerNewsDetailsFragmentComponent.builder()
                .newsDetailsFragmentModule(new NewsDetailsFragmentModule())
                .build()
                .injectNewsDetailsFragment(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logger.log("NewsDetailsFragment onCreateView()");
        if(adapter.getItemCount() == 0){
            Logger.withTag("MyLog").log("NewsDetailsFragment onCreateView() getInt from Bundle: " + (getArguments() != null ? getArguments().getInt(KEY_ARTICLE_ID) : -1));
            presenter.getArticles(getArguments() != null ? getArguments().getInt(KEY_ARTICLE_ID) : -1);
        }
        return inflater.inflate(R.layout.fragment_news_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logger.log("NewsDetailsFragment onViewCreated()");
        unbinder = ButterKnife.bind(this, view);
        rvDetails.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        rvDetails.setLayoutManager(layoutManager);
        snapHelper.attachToRecyclerView(rvDetails);
    }

    @Override
    public void onStart() {
        super.onStart();
        logger.log("NewsDetailsFragment onStart()");
        getPresenter().setupSubscriptions();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void setArticles(List<ArticleSchema> articles) {
        logger.log("NewsDetailsFragment setArticles()");
        adapter.setNewsList(articles);
        adapter.notifyDataSetChanged();
        rvDetails.scrollToPosition(adapter.getArticlesList().size() - 1);
    }

    @Override
    public void addArticlesToList(List<ArticleSchema> articles) {
        logger.log("NewsDetailsFragment addArticlesToList()");
        adapter.addArticlesToList(articles);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected NewsDetailsContract.Presenter getPresenter() {
        logger.log("NewsDetailsFragment getPresenter()");
        return presenter;
    }

    @Override
    public Observable<Long> getReachEndObservable() {
        logger.log("NewsDetailsFragment getReachEndObservable()");
        return adapter.getRichEndObservable();
    }

}
