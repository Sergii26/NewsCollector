package com.practice.newscollector.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.Ignore;


public class Article {

    @SerializedName("source")
    @Expose
    private SourceModel sourceModel;

    @SerializedName("author")
    @Expose
    @Ignore
    private Object author;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("urlToImage")
    @Expose
    private Object urlToImage;

    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;

    @SerializedName("content")
    @Expose
    private String content;

    public Article(SourceModel sourceModel, String title, String description, String url, Object urlToImage, String publishedAt, String content) {
        this.sourceModel = sourceModel;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public SourceModel getSourceModel() {
        return sourceModel;
    }

    public void setSourceModel(SourceModel sourceModel) {
        this.sourceModel = sourceModel;
    }

    public Object getAuthor() {
        return author;
    }

    public void setAuthor(Object author) {
        this.author = author;
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

    public Object getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(Object urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
