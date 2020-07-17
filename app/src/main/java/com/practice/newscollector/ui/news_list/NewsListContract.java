package com.practice.newscollector.ui.news_list;

import com.practice.newscollector.model.dao.ArticleSchema;
import com.practice.newscollector.ui.arch.Contract;

import java.util.List;

import io.reactivex.Observable;

public interface NewsListContract {
    interface View extends Contract.View{
        void setArticlesList(List<ArticleSchema> newsList);
        void addArticlesToLIst(List<ArticleSchema> articles);
        void turnOffRefreshing();
        boolean isRefreshingState();
        Observable<Integer> getItemClickObservable();
        void showNewsDetailsFragment(int articleId);
        boolean isConnectedToNetwork();
        List<ArticleSchema> getArticles();
    }

    interface Presenter extends Contract.Presenter<View>{
        void setArticlesList();
        void getMoreArticles(long lastArticle);
        void setupSubscriptions();
    }

    interface Host extends Contract.Host{
        void showNewsDetailsFragment(int articleId);
    }
}
