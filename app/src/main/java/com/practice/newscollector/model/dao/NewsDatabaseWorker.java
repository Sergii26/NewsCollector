package com.practice.newscollector.model.dao;

import com.google.common.base.Optional;
import com.practice.newscollector.model.logger.ILog;
import com.practice.newscollector.model.pojo.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Single;

public class NewsDatabaseWorker implements NewsDaoWorker {

    private ILog logger;
    private NewsRoomDao dao;

    public NewsDatabaseWorker(NewsRoomDao dao, ILog logger) {
        this.dao = dao;
        this.logger = logger;
    }

    @Override
    public Completable insertArticle(List<Article> articles) {
        logger.log("NewsDatabaseWorker insertArticle()");
        return dao.insertArticle(convertArticles(articles));
    }

    @Override
    public Single<List<ArticleSchema>> getFirstPage() {
        logger.log("NewsDatabaseWorker getLastArticles()");
        return Single.fromCallable(() -> dao.getFirstPage());
    }

    @Override
    public Single<List<ArticleSchema>> getNextPage(long publishedAt) {
        logger.log("NewsDatabaseWorker getMoreArticles()");
        return Single.fromCallable(() -> dao.getNextPage(publishedAt));
    }

    @Override
    public Completable deleteAllArticles() {
        logger.log("NewsDatabaseWorker deleteAllArticles()");
        return dao.deleteAllArticles();
    }

    @Override
    public Single<Optional<ArticleSchema>> getLastArticle() {
        logger.log("NewsDatabaseWorker getLastArticle()");
        return Single.fromCallable(() -> dao.getLastArticle())
                .map(articleSchemas -> {
                    if(articleSchemas.isEmpty()) {
                        return Optional.absent();
                    } else {
                        return Optional.of(articleSchemas.get(0));
                    }
                });
    }

    @Override
    public Single<List<ArticleSchema>> getArticlesStartingFrom(int articleId) {
        logger.log("NewsDatabaseWorker getNextArticles()");
        return Single.fromCallable(() -> dao.getArticleById(articleId))
                .map(articleSchema -> dao.getArticleStartingFrom(articleSchema.getPublishedAt()));
    }

    @Override
    public Completable clearDatabase() {
        return Completable.fromCallable((Callable<Completable>) () -> dao.deleteAllArticles());
    }

    public List<ArticleSchema> convertArticles(List<Article> articles) {
        List<ArticleSchema> convertedArticles = new ArrayList<>();
        for (Article article : articles) {
            convertedArticles.add(new ArticleSchema(article));
        }
        return convertedArticles;
    }
}
