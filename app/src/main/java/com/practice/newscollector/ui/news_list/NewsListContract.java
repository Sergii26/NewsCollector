package com.practice.newscollector.ui.news_list;

import com.practice.newscollector.model.dao.ArticleSchema;
import com.practice.newscollector.ui.arch.Contract;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public interface NewsListContract {
    interface View extends Contract.View{
        void setNewsList(List<ArticleSchema> newsList);
        void addArticlesToLIst(List<ArticleSchema> articles);
        void turnOffRefreshing();
        boolean isRefreshingState();
        Observable<Integer> getItemClickObservable();
        Observable<Long> getReachEndObservable();
        void showNewsDetailsFragment(int articleId);
    }

    interface Presenter extends Contract.Presenter<View>{
        void setArticlesList();
        void getMoreArticles(long lastArticle);
        void setupSubscriptions();
    }

    interface Host extends Contract.Host{
        void showNewsDetailsFragment(int articleTime);
    }
}
