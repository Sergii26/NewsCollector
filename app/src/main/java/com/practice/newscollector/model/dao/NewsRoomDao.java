package com.practice.newscollector.model.dao;

import com.practice.newscollector.model.pojo.Article;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Completable;

@Dao
public interface NewsRoomDao {
    @Query("SELECT * FROM news ORDER BY publishedAt DESC LIMIT 20")
    List<ArticleSchema> getLastArticles();

    @Query("SELECT * FROM news WHERE publishedAt < :publishedAt ORDER BY publishedAt DESC LIMIT 20")
    List<ArticleSchema> getMoreArticles(long publishedAt);

    @Query("SELECT * FROM news WHERE uniqueId = :uniqueId")
    ArticleSchema getArticleById(int uniqueId);

    @Insert
    Completable insertArticle(List<ArticleSchema> articles);

    @Query("DELETE FROM news")
    Completable deleteAllArticles();

    @Query("SELECT * FROM news ORDER BY publishedAt DESC LIMIT 1")
    List<ArticleSchema> getLastArticle();

    @Query("SELECT * FROM news WHERE publishedAt >= :publishedAt ORDER BY publishedAt DESC")
    List<ArticleSchema> getNextArticles(long publishedAt);

}
