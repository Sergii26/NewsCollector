package com.practice.newscollector.model.dao;

import com.practice.newscollector.model.pojo.Article;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "news")
public class ArticleSchema {

    @PrimaryKey(autoGenerate = true)
    private int uniqueId = 0;

    private String source;

    private String title;

    private String description;

    private String url;

    private String urlToImage;

    private long publishedAt;

    private String publishedAtAsString;

    private String content;

    public ArticleSchema(int uniqueId, String source, String title, String description, String url, String urlToImage, long publishedAt, String publishedAtAsString, String content) {
        this.uniqueId = uniqueId;
        this.source = source;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.publishedAtAsString = publishedAtAsString;
        this.content = content;
    }

    @Ignore
    public ArticleSchema(Article article){
        this.uniqueId = 0;
        this.source = article.getSourceModel().getName();
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.url = article.getUrl();
        if(article.getUrlToImage() != null) {
            this.urlToImage = article.getUrlToImage().toString();
        } else {
            this.urlToImage = null;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        java.util.Date parsedTimeStamp = null;
        try {
            parsedTimeStamp = format.parse(article.getPublishedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (parsedTimeStamp != null) {
            this.publishedAt = parsedTimeStamp.getTime();
        }
        this.publishedAtAsString = article.getPublishedAt();
        this.content = article.getContent();
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public long getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(long publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getPublishedAtAsString() {
        return publishedAtAsString;
    }

    public void setPublishedAtAsString(String publishedAtAsString) {
        this.publishedAtAsString = publishedAtAsString;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
