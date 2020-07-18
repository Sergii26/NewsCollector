package com.practice.newscollector.ui.model.dao;

import com.google.common.base.Optional;
import com.practice.newscollector.model.dao.ArticleSchema;
import com.practice.newscollector.model.dao.NewsDatabaseWorker;
import com.practice.newscollector.model.dao.NewsRoomDao;
import com.practice.newscollector.model.logger.ILog;
import com.practice.newscollector.model.pojo.Article;
import com.practice.newscollector.model.pojo.SourceModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;

public class NewsDatabaseWorkerTest {
    private NewsDatabaseWorker dbWorker;
    private NewsRoomDao newsRoomDao;
    private ILog logger;
    private List<ArticleSchema> articles;


    @Before
    public void initWorker(){
        articles = new ArrayList<>();
        newsRoomDao = Mockito.mock(NewsRoomDao.class);
        logger = Mockito.mock(ILog.class);
        dbWorker = new NewsDatabaseWorker(newsRoomDao, logger);
        Mockito.when(newsRoomDao.getLastArticle()).thenReturn(articles);
    }

    @Test
    public void getLastArticleTest_withNull(){
        TestObserver<Boolean> testObserver = dbWorker.getLastArticle()
                .map(Optional::isPresent).test();
        testObserver.assertValue(false);
        testObserver.dispose();
    }

    @Test
    public void getLastArticleTest_withArticle(){
        articles.add(new ArticleSchema(0, "", "", "", "", "", 0,
                "", ""));
        TestObserver<Boolean> testObserver = dbWorker.getLastArticle()
                .map(Optional::isPresent).test();
        testObserver.assertValue(true);
        testObserver.dispose();
    }

    @Test
    public void convertArticlesTest(){
        List<ArticleSchema> articleSchemasList;
        List<Article> articlesList = new ArrayList<>();
        articlesList.add(new Article(new SourceModel("test"), "test", "test", "test",
                "test", "2020-07-17T16:52:37Z", "test"));
        articleSchemasList = dbWorker.convertArticles(articlesList);
        assertEquals("test", articleSchemasList.get(0).getContent());
        assertEquals("test", articleSchemasList.get(0).getDescription());
        assertEquals("2020-07-17T16:52:37Z", articleSchemasList.get(0).getPublishedAtAsString());
        assertEquals(1594993957000L, articleSchemasList.get(0).getPublishedAt());
        assertEquals("test", articleSchemasList.get(0).getSource());
        assertEquals("test", articleSchemasList.get(0).getTitle());
        assertEquals("test", articleSchemasList.get(0).getUrl());
        assertEquals("test", articleSchemasList.get(0).getUrlToImage());
    }
}
