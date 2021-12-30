package com.example.newsgateway;

import java.io.Serializable;

public class NewsArticle implements Serializable {

    private String articleAuthor;
    private String articleTitle;
    private String articleContent;
    private String url;
    private String articleImage;
    private String articleDate;

    public String getAuthor() {
        return articleAuthor;
    }

    public void setAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public String getTitle() {
        return articleTitle;
    }

    public void setTitle(String title) {
        this.articleTitle = title;
    }

    public String getDescription() {
        return articleContent;
    }

    public void setDescription(String description) {
        this.articleContent = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return articleImage;
    }

    public void setUrlToImage(String articleImage) {
        this.articleImage = articleImage;
    }

    public String getPublishedAt() {
        return articleDate;
    }

    public void setPublishedAt(String articleDate) {
        this.articleDate = articleDate;
    }

    @Override
    public String toString() {
        return "NewsArticle{" +
                "author='" + articleAuthor + '\'' +
                ", title='" + articleTitle + '\'' +
                ", description='" + articleContent + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + articleImage + '\'' +
                ", publishedAt='" + articleDate + '\'' +
                '}';
    }
}

