package com.practice.newscollector.ui.news_details;

import com.practice.newscollector.model.dao.ArticleSchema;
import com.practice.newscollector.model.dao.NewsDaoWorker;
import com.practice.newscollector.model.logger.ILog;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class NewsDetailsPresenterTest {
    private NewsDaoWorker dbWorker;
    private ILog logger;
    private NewsDetailsContract.View view;
    private NewsDetailsContract.Presenter presenter;

    @BeforeClass
    public static void setupClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                __ -> Schedulers.trampoline());
    }

    @Before
    public void setupPresenter(){
        dbWorker = Mockito.mock(NewsDaoWorker.class);
        logger = Mockito.mock(ILog.class);
        view = Mockito.mock(NewsDetailsFragment.class);
        presenter = new NewsDetailsPresenter(dbWorker, logger);
        presenter.subscribe(view);
    }

    @Test
    public void getArticlesTest(){
        List<ArticleSchema> articleSchemas = getTestList();
        when(dbWorker.getArticlesStartingFrom(Mockito.any(Integer.class))).thenReturn(Single.just(articleSchemas));
        when(dbWorker.getNextPage(Mockito.any(Long.class))).thenReturn(Single.just(articleSchemas));

        presenter.getArticles(1);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        verify(dbWorker).getArticlesStartingFrom(Mockito.any(Integer.class));
        verify(view).setArticles(Mockito.anyList());
        verify(view).getLastArticleTime();
        verify(dbWorker).getNextPage(Mockito.any(Long.class));
        verify(view).addArticlesToList(Mockito.anyList());
        verifyNoMore();
    }

    @Test
    public void getMoreArticlesTest(){
        List<ArticleSchema> articleSchemas = getTestList();
        when(dbWorker.getNextPage(Mockito.any(Long.class))).thenReturn(Single.just(articleSchemas));

        presenter.getMoreArticles(1);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        verify(dbWorker).getNextPage(Mockito.any(Long.class));
        verify(view).addArticlesToList(Mockito.anyList());
        verifyNoMore();
    }

    private List<ArticleSchema> getTestList(){
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
        verifyNoMoreInteractions(dbWorker);
        verifyNoMoreInteractions(view);
    }
}
