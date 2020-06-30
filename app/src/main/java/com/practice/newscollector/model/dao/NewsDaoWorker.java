package com.practice.newscollector.model.dao;

import com.google.common.base.Optional;
import com.practice.newscollector.model.pojo.Article;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface NewsDaoWorker {
    Completable insertArticle(List<Article> articles);

    Single<List<ArticleSchema>> getFirstPage();

    Single<List<ArticleSchema>> getNextPage(long publishedAt);

    Completable deleteAllArticles();

    Single<Optional<ArticleSchema>> getLastArticle();

    Single<List<ArticleSchema>> getArticlesStartingFrom(int articleId);

    Completable clearDatabase();

}
