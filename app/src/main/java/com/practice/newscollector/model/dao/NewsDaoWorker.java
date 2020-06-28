package com.practice.newscollector.model.dao;

import com.google.common.base.Optional;
import com.practice.newscollector.model.pojo.Article;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface NewsDaoWorker {
    Completable insertArticle(List<ArticleSchema> articles);

    Single<List<ArticleSchema>> getLastArticles();

    Single<List<ArticleSchema>> getMoreArticles(long publishedAt);

    Completable deleteAllArticles();

    Single<Optional<ArticleSchema>> getLastArticle();

    Single<List<ArticleSchema>> getNextArticles(int articleId);

    Completable clearDatabase();

}
