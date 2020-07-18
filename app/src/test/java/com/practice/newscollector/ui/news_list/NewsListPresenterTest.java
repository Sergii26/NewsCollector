package com.practice.newscollector.ui.news_list;

import com.practice.newscollector.R;
import com.practice.newscollector.model.dao.ArticleSchema;
import com.practice.newscollector.model.dao.NewsDaoWorker;
import com.practice.newscollector.model.logger.ILog;
import com.practice.newscollector.model.newtwork_api.ApiClient;
import com.practice.newscollector.model.newtwork_api.NetworkClient;

import com.google.common.base.Optional;
import com.practice.newscollector.model.pojo.Article;
import com.practice.newscollector.model.pojo.ResponseModel;
import com.practice.newscollector.model.pojo.SourceModel;
import com.practice.newscollector.model.utils.Utils;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class NewsListPresenterTest {
    private NetworkClient networkClient;
    private NewsDaoWorker dbWorker;
    private ILog logger;
    private NewsListContract.View view;
    private Utils androidUtils;
    private NewsListContract.Presenter presenter;

    @BeforeClass
    public static void setupClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                __ -> Schedulers.trampoline());
    }

    @Before
    public void setupPresenter(){
        networkClient = Mockito.mock(NetworkClient.class);
        dbWorker = Mockito.mock(NewsDaoWorker.class);
        logger = Mockito.mock(ILog.class);
        view = Mockito.mock(NewsListFragment.class);
        androidUtils = Mockito.mock(Utils.class);
        presenter = new NewsListPresenter(networkClient, dbWorker, logger, androidUtils);
        presenter.subscribe(view);
    }

    @Test
    public void setArticlesListTest_withInternetAndDataAtDatabase(){
        ArticleSchema article = new ArticleSchema(0, "", "", "",
                "", "", 0, "", "");
        List<ArticleSchema> articleSchemas = getArticleSchemaTestList();
        List<Article> articles = getArticleTestList();
        ResponseModel respModel = new ResponseModel();
        respModel.setArticles(articles);

        when(androidUtils.isConnectedToNetwork()).thenReturn(true);
        when(dbWorker.getLastArticle()).thenReturn(Single.just(Optional.of(article)));
        when(networkClient.getNewsFromDate(ApiClient.BBC_SOURCE, 100, ""))
                .thenReturn(Single.just(respModel));
        when(networkClient.getNewsFromDate(ApiClient.INDEPENDENT_SOURCE, 100, ""))
                .thenReturn(Single.just(respModel));
        when(dbWorker.insertArticle(Mockito.any())).thenReturn(Completable.complete());
        when(dbWorker.getFirstPage()).thenReturn(Single.just(articleSchemas));
        when(view.isRefreshingState()).thenReturn(true);

        presenter.setArticlesList();
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        verify(dbWorker).getLastArticle();
        verify(androidUtils, Mockito.times(3)).isConnectedToNetwork();
        verify(networkClient).getNewsFromDate(ApiClient.BBC_SOURCE, 100, "");
        verify(networkClient).getNewsFromDate(ApiClient.INDEPENDENT_SOURCE, 100, "");
        verify(dbWorker).insertArticle(Mockito.any());
        verify(dbWorker).getFirstPage();
        verify(view).isRefreshingState();
        verify(view).turnOffRefreshing();
        verify(view).setArticlesList(Mockito.any());
        verifyNoMore();
    }

    @Test
    public void setArticlesListTest_withoutInternetAndDataAtDatabase(){
        ArticleSchema article = new ArticleSchema(0, "", "", "",
                "", "", 0, "", "");
        List<ArticleSchema> articleSchemas = getArticleSchemaTestList();
        when(androidUtils.isConnectedToNetwork()).thenReturn(false);
        when(dbWorker.getLastArticle()).thenReturn(Single.just(Optional.of(article)));
        when(dbWorker.insertArticle(Mockito.any())).thenReturn(Completable.complete());
        when(dbWorker.getFirstPage()).thenReturn(Single.just(articleSchemas));
        when(view.isRefreshingState()).thenReturn(true);

        presenter.setArticlesList();
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        verify(dbWorker).getLastArticle();
        verify(androidUtils, Mockito.times(4)).isConnectedToNetwork();
        verify(dbWorker).insertArticle(Mockito.any());
        verify(dbWorker).getFirstPage();
        verify(view).isRefreshingState();
        verify(view).turnOffRefreshing();
        verify(view).setArticlesList(Mockito.any());
        verify(view).showToast(R.string.turn_on_internet);
        verifyNoMore();
    }

    @Test
    public void setArticlesListTest_withInternetAndEmptyDatabase(){
        List<ArticleSchema> articleSchemas = getArticleSchemaTestList();
        List<Article> articles = getArticleTestList();
        ResponseModel respModel = new ResponseModel();
        respModel.setArticles(articles);

        when(androidUtils.isConnectedToNetwork()).thenReturn(true);
        when(dbWorker.getLastArticle()).thenReturn(Single.just(Optional.absent()));
        when(networkClient.getNews(ApiClient.BBC_SOURCE, 100))
                .thenReturn(Single.just(respModel));
        when(networkClient.getNews(ApiClient.INDEPENDENT_SOURCE, 100))
                .thenReturn(Single.just(respModel));
        when(dbWorker.insertArticle(Mockito.any())).thenReturn(Completable.complete());
        when(dbWorker.getFirstPage()).thenReturn(Single.just(articleSchemas));
        when(view.isRefreshingState()).thenReturn(true);

        presenter.setArticlesList();
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        verify(dbWorker).getLastArticle();
        verify(androidUtils, Mockito.times(3)).isConnectedToNetwork();
        verify(networkClient).getNews(ApiClient.BBC_SOURCE, 100);
        verify(networkClient).getNews(ApiClient.INDEPENDENT_SOURCE, 100);
        verify(dbWorker).insertArticle(Mockito.any());
        verify(dbWorker).getFirstPage();
        verify(view).isRefreshingState();
        verify(view).turnOffRefreshing();
        verify(view).setArticlesList(Mockito.any());
        verifyNoMore();
    }

    @Test
    public void setArticlesListTest_withoutInternetAndEmptyDatabase(){
        when(androidUtils.isConnectedToNetwork()).thenReturn(false);
        when(dbWorker.getLastArticle()).thenReturn(Single.just(Optional.absent()));
        when(dbWorker.insertArticle(Mockito.any())).thenReturn(Completable.complete());
        when(dbWorker.getFirstPage()).thenReturn(Single.just(new ArrayList<>()));
        when(view.isRefreshingState()).thenReturn(true);

        presenter.setArticlesList();
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        verify(dbWorker).getLastArticle();
        verify(androidUtils, Mockito.times(5)).isConnectedToNetwork();
        verify(dbWorker).insertArticle(Mockito.any());
        verify(dbWorker).getFirstPage();
        verify(view).isRefreshingState();
        verify(view).turnOffRefreshing();
        verify(view).showToast(R.string.empty_database);
        verifyNoMore();
    }

    @Test
    public void getMoreArticlesTest_providesMoreArticles(){
        List<ArticleSchema> articleSchemas = getArticleSchemaTestList();

        when(dbWorker.getNextPage(Mockito.any(Long.class))).thenReturn(Single.just(articleSchemas));

        presenter.getMoreArticles(Mockito.any(Long.class));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        verify(dbWorker).getNextPage(Mockito.any(Long.class));
        verify(view).addArticlesToLIst(Mockito.any());
        verifyNoMore();
    }

    @Test
    public void getMoreArticlesTest_withoutMoreArticles(){
        when(dbWorker.getNextPage(Mockito.any(Long.class))).thenReturn(Single.just(new ArrayList<>()));

        presenter.getMoreArticles(Mockito.any(Long.class));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        verify(dbWorker).getNextPage(Mockito.any(Long.class));
        verifyNoMore();
    }

    private List<Article> getArticleTestList(){
        Article art1 = new Article(new SourceModel(""), "", "", "", "", "", "");
        Article art2 = new Article(new SourceModel(""), "", "", "", "", "", "");
        List<Article> articles = new ArrayList<>();
        articles.add(art1);
        articles.add(art2);
        return articles;
    }

    private List<ArticleSchema> getArticleSchemaTestList(){
        ArticleSchema article1 = new ArticleSchema(0, "", "", "",
                "", "", 0, "", "");
        ArticleSchema article2 = new ArticleSchema(0, "", "", "",
                "", "", 0, "", "");
        List<ArticleSchema> articleSchemas = new ArrayList<>();
        articleSchemas.add(article1);
        articleSchemas.add(article2);
        return articleSchemas;
    }

    private void verifyNoMore(){
        verifyNoMoreInteractions(networkClient);
        verifyNoMoreInteractions(dbWorker);
        verifyNoMoreInteractions(view);
        verifyNoMoreInteractions(androidUtils);
    }
}
