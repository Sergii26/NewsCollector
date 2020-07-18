package com.practice.newscollector.ui.news_list;

import com.google.common.base.Optional;
import com.practice.newscollector.R;
import com.practice.newscollector.model.dao.ArticleSchema;
import com.practice.newscollector.model.dao.NewsDaoWorker;
import com.practice.newscollector.model.logger.ILog;
import com.practice.newscollector.model.newtwork_api.ApiClient;
import com.practice.newscollector.model.newtwork_api.NetworkClient;
import com.practice.newscollector.model.pojo.Article;
import com.practice.newscollector.model.pojo.SourceModel;
import com.practice.newscollector.model.utils.Utils;
import com.practice.newscollector.ui.arch.MvpPresenter;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NewsListPresenter extends MvpPresenter<NewsListContract.View> implements NewsListContract.Presenter {

    private final NetworkClient networkClient;
    private final NewsDaoWorker dbWorker;
    private final ILog logger;
    private final Utils androidUtils;

    public NewsListPresenter(NetworkClient networkClient, NewsDaoWorker dbWorker, ILog logger, Utils androidUtils) {
        this.networkClient = networkClient;
        this.dbWorker = dbWorker;
        this.logger = logger;
        this.androidUtils = androidUtils;
    }

    private Single<List<Article>> getNewArticles() {
        logger.log("NewsListPresenter getNewArticles() hasView = " + hasView());
        return dbWorker.getLastArticle()
                //Waiting for invoke subscribe method in MvpFragment
                .delay(1, TimeUnit.SECONDS)
                .flatMap((Function<Optional<ArticleSchema>, SingleSource<List<Article>>>) articleSchemaOptional -> {
                    logger.log("NewsListPresenter getNewArticles() hasInternet = " + androidUtils.isConnectedToNetwork());
                    if(!androidUtils.isConnectedToNetwork()){
                        return Single.just(new ArrayList<>());
                    } else {
                        if (articleSchemaOptional.isPresent()) {
                            final String sourceOfLastArticle = articleSchemaOptional.get().getSource();
                            return networkClient.getNewsFromDate(ApiClient.BBC_SOURCE, 100, articleSchemaOptional.get().getPublishedAtAsString())
                                    .zipWith(networkClient.getNewsFromDate(ApiClient.INDEPENDENT_SOURCE, 100, articleSchemaOptional.get().getPublishedAtAsString()),
                                            (bbcResponse, independentResponse) -> {
                                                List<Article> articles;
                                                if (sourceOfLastArticle.equals("BBC News")) {
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
                            return networkClient.getNews(ApiClient.BBC_SOURCE, 100)
                                    .zipWith(networkClient.getNews(ApiClient.INDEPENDENT_SOURCE, 100),
                                            (bbcResponse, independentResponse) -> {
                                                List<Article> articles = bbcResponse.getArticles();
                                                articles.addAll(independentResponse.getArticles());
                                                logger.log("NewsListPresenter getNewArticle() absent articles size: " + articles.size());
                                                return articles;
                                            });
                        }
                    }
                });
    }

    @Override
    public void setArticlesList() {
        logger.log("NewsListPresenter setArticlesList()");
        onStopDisposable.add(getNewArticles()
                .toObservable()
                .switchMapCompletable(dbWorker::insertArticle)
                .andThen(dbWorker.getFirstPage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articleSchemas -> {
                    logger.log("NewsListPresenter setArticlesList() on set success");
                    if(hasView()) {
                        if(view.isRefreshingState()) {
                            view.turnOffRefreshing();
                        }
                        if(androidUtils.isConnectedToNetwork() && !articleSchemas.isEmpty()) {
                            view.setArticlesList(articleSchemas);
                            return;
                        }
                        if(!androidUtils.isConnectedToNetwork() && !articleSchemas.isEmpty()){
                            view.setArticlesList(articleSchemas);
                            view.showToast(R.string.turn_on_internet);
                            return;
                        }
                        if(!androidUtils.isConnectedToNetwork() && articleSchemas.isEmpty()){
                            view.showToast(R.string.empty_database);
                        }
                    }
                }, throwable -> logger.log("NewsListPresenter setArticlesList() error: " + throwable.getMessage())));
    }

    @Override
    public void getMoreArticles(long lastArticle) {
        logger.log("NewsListPresenter getMoreArticles()");
        onStopDisposable.add(dbWorker.getNextPage(lastArticle)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articles -> {
                    logger.log("NewsListPresenter getMoreArticles() success");
                    if (!articles.isEmpty() && hasView()) {
                        view.addArticlesToLIst(articles);
                    }
                }, throwable -> logger.log("NewsListPresenter getMoreArticles() error: " + throwable.getMessage())));
    }

    @Override
    public void setupSubscriptions() {
        logger.log("NewsListPresenter setupSubscriptions()");
        if (hasView()) {
            onStopDisposable.add(
                    view.getItemClickObservable()
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe(articleId -> {
                                logger.log("NewsListPresenter setupSubscriptions() on item click position: " + articleId);
                                if (hasView()) {
                                    view.showNewsDetailsFragment(articleId);
                                }
                            }));
        }
    }

}
