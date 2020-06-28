package com.practice.newscollector.ui.news_details;

import com.practice.newscollector.model.dao.ArticleSchema;
import com.practice.newscollector.ui.arch.Contract;

import java.util.List;

import io.reactivex.Observable;

public interface NewsDetailsContract {
    interface View extends Contract.View {
        void setArticles(List<ArticleSchema> articles);

        void addArticlesToList(List<ArticleSchema> articles);

        Observable<Long> getReachEndObservable();
    }

    interface Presenter extends Contract.Presenter<NewsDetailsContract.View> {
        void getArticles(int articleId);

        void getMoreArticles(long lastArticle);

        void setupSubscriptions();
    }

    interface Host extends Contract.Host {

    }
}
