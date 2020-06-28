package com.practice.newscollector.ui.news_details;

import com.practice.newscollector.model.dao.NewsDaoWorker;
import com.practice.newscollector.model.logger.ILog;
import com.practice.newscollector.ui.arch.MvpPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class NewsDetailsPresenter extends MvpPresenter<NewsDetailsContract.View> implements NewsDetailsContract.Presenter {
    private final NewsDaoWorker dbWorker;
    private final ILog logger;

    public NewsDetailsPresenter(NewsDaoWorker dbWorker, ILog logger) {
        this.dbWorker = dbWorker;
        this.logger = logger;
    }

    @Override
    public void getArticles(int articleId) {
        logger.log("NewsDetailsPresenter getArticles()");
        onStopDisposable.add(dbWorker.getNextArticles(articleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articleSchema -> {
                    logger.log("NewsDetailsPresenter getArticles() size: " + articleSchema.size());
                    if (hasView()) {
                        view.setArticles(articleSchema);
                    }
                }, throwable -> logger.log("NewsDetailsPresenter getMoreArticles() error: " + throwable.getMessage())));
    }

    @Override
    public void getMoreArticles(long lastArticle) {
        logger.log("NewsDetailsPresenter getMoreArticles()");
        onStopDisposable.add(dbWorker.getMoreArticles(lastArticle)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articleSchemas -> {
                    logger.log("NewsDetailsPresenter getMoreArticles() success");
                    if (!articleSchemas.isEmpty() && hasView()) {
                        view.addArticlesToList(articleSchemas);
                    }
                }, throwable -> logger.log("NewsDetailsPresenter getMoreArticles() error: " + throwable.getMessage())));
    }

    @Override
    public void setupSubscriptions() {
        logger.log("NewsDetailsPresenter setupSubscriptions()");
        if (hasView()) {
            onStopDisposable.add(view.getReachEndObservable()
                    .subscribe(publishedAt -> {
                        if (hasView()) {
                            getMoreArticles(publishedAt);
                        }
                    }));
        }
    }
}
