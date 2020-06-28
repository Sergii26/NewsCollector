package com.practice.newscollector.ui.news_list;

import com.google.common.base.Optional;
import com.practice.newscollector.model.dao.ArticleSchema;
import com.practice.newscollector.model.dao.NewsDaoWorker;
import com.practice.newscollector.model.logger.ILog;
import com.practice.newscollector.model.newtwork_api.ApiClient;
import com.practice.newscollector.model.newtwork_api.ApiService;
import com.practice.newscollector.model.pojo.Article;
import com.practice.newscollector.ui.arch.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NewsListPresenter extends MvpPresenter<NewsListContract.View> implements NewsListContract.Presenter {

    private final ApiService networkService;
    private final NewsDaoWorker dbWorker;
    private final ILog logger;

    public NewsListPresenter(ApiService networkService, NewsDaoWorker dbWorker, ILog logger) {
        this.networkService = networkService;
        this.dbWorker = dbWorker;
        this.logger = logger;
    }

    private Single<List<Article>> getNewArticles() {
        logger.log("NewsListPresenter getNewArticles()");
        return dbWorker.getLastArticle()
                .subscribeOn(Schedulers.io())
                .flatMap((Function<Optional<ArticleSchema>, SingleSource<List<Article>>>) articleSchemaOptional -> {
                    if (articleSchemaOptional.isPresent()) {
                        final String sourceOfLastArticle = articleSchemaOptional.get().getSource();
                        return networkService.getNewsFromDate(ApiClient.BBC_SOURCE, 100, ApiClient.API_KEY, articleSchemaOptional.get().getPublishedAtAsString())
                                .zipWith(networkService.getNewsFromDate(ApiClient.INDEPENDENT_SOURCE, 100, ApiClient.API_KEY, articleSchemaOptional.get().getPublishedAtAsString()),
                                        (bbcResponse, independentResponse) -> {
                                            List<Article> articles;
                                            if(sourceOfLastArticle.equals("BBC News")){
                                                logger.log("NewsListPresenter getNewArticle()  BBC article for delete publishedAt: " + bbcResponse.getArticles().get(bbcResponse.getArticles().size() - 1).getPublishedAt());
                                                articles = bbcResponse.getArticles();
                                                articles.remove(articles.size() - 1);
                                                articles.addAll(independentResponse.getArticles());
                                            } else {
                                                logger.log("NewsListPresenter getNewArticle() Independent article for delete publishedAt: " + independentResponse.getArticles().get(independentResponse.getArticles().size() - 1).getPublishedAt());
                                                articles = independentResponse.getArticles();
                                                articles.remove(articles.size() - 1);
                                                articles.addAll(bbcResponse.getArticles());
                                            }
                                            logger.log("NewsListPresenter getNewArticle() isPresent articles size: " + articles.size());

                                            return articles;
                                        });
                    } else {
                        return networkService.getNews(ApiClient.BBC_SOURCE, 100, ApiClient.API_KEY)
                                .zipWith(networkService.getNews(ApiClient.INDEPENDENT_SOURCE, 100, ApiClient.API_KEY),
                                        (bbcResponse, independentResponse) -> {
                                            List<Article> articles = bbcResponse.getArticles();
                                            articles.addAll(independentResponse.getArticles());
                                            logger.log("NewsListPresenter getNewArticle() absent articles size: " + articles.size());
                                            return articles;
                                        });
                    }
                });
    }

    @Override
    public void setArticlesList() {
        logger.log("NewsListPresenter setArticlesList()");
        onStopDisposable.add(getNewArticles()
                .subscribeOn(Schedulers.io())
                .subscribe(articles -> {
                            logger.log("NewsListPresenter setNewsList() success - list size: " + articles.size());
                            onStopDisposable.add(dbWorker.insertArticle(convertArticles(articles))
                                    .subscribe(() -> {
                                        logger.log("NewsListPresenter setNewsList() insert to DB success");
                                        onStopDisposable.add(dbWorker.getLastArticles()
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(articles1 -> {
                                                    logger.log("NewsListPresenter setNewsList() on set success");
                                                    if (NewsListPresenter.this.hasView()) {
                                                        view.setNewsList(articles1);
                                                        if (view.isRefreshingState()) {
                                                            view.turnOffRefreshing();
                                                        }
                                                    } else {
                                                        logger.log("NewsListPresenter setNewsList() on set success - no view");
                                                    }
                                                }, throwable -> logger.log("NewsListPresenter setNewsList() on set error: " + throwable.getMessage())));

                                    }, throwable -> logger.log("NewsListPresenter setNewsList() insert to DB error: " + throwable.getMessage())));
                }, throwable -> logger.log("NewsListPresenter setNewsList() error: " + throwable.getMessage())));
    }

    @Override
    public void getMoreArticles(long lastArticle) {
        logger.log("NewsListPresenter getMoreArticles()");
        onStopDisposable.add(dbWorker.getMoreArticles(lastArticle)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articles -> {
                    logger.log("NewsListPresenter getMoreArticles() success");
                    if(!articles.isEmpty() && hasView()) {
                        view.addArticlesToLIst(articles);
                    }
                }, throwable -> logger.log("NewsListPresenter getMoreArticles() error: " + throwable.getMessage())));
    }

    @Override
    public void setupSubscriptions() {
        logger.log("NewsListPresenter setupSubscriptions()");
        if(hasView()){
            onStopDisposable.addAll(
                    view.getItemClickObservable()
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(articleId -> {
                        logger.log("NewsListPresenter setupSubscriptions() on item click position: " + articleId);
                        if (hasView()) {
                            view.showNewsDetailsFragment(articleId);
                        }
                    }),
                    view.getReachEndObservable()
                    .subscribe(publishedAt -> {
                        if(hasView()){
                            getMoreArticles(publishedAt);
                        }
                    }));
        }
    }

    private List<ArticleSchema> convertArticles(List<Article> articles) {
        List<ArticleSchema> convertedArticles = new ArrayList<>();
        for (Article article : articles) {
            convertedArticles.add(new ArticleSchema(article));
        }
        return convertedArticles;
    }

}
