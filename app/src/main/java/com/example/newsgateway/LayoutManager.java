package com.example.newsgateway;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LayoutManager implements Serializable {

    private int source,LMlan,LMcountry;
    private int arti;
    private List<String> categoryList = new ArrayList<>();
    private List<NewsSource> sourceList = new ArrayList<>();
    private List<NewsArticle> articleList = new ArrayList<>();
    private List<String> langlm = new ArrayList<>();
    private List<String> countrylm = new ArrayList<>();

    public void setSource(int source) {
        this.source = source;
    }

    public void setArticle(int arti) {
        this.arti = arti;
    }

    public List<String> getCategories() {
        return categoryList;
    }

    public void setLanglm(List<String> langlm) {
        this.langlm = langlm;
    }

    public List<String> getlangLM() {
        return langlm;
    }

    public void setCountrylm(List<String> countrylm) {
        this.countrylm = countrylm;
    }

    public List<String> getCountrylm() {
        return countrylm;
    }

    public void setLMlan(int LMlan) {
        this.LMlan = LMlan;
    }

    public void setLMcountry(int LMcountry) {
        this.LMcountry = LMcountry;
    }

    public void setCategories(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    public List<NewsSource> getSources() {
        return sourceList;
    }

    public void setSources(List<NewsSource> sourceList) {
        this.sourceList = sourceList;
    }

    public List<NewsArticle> getArticles() {
        return articleList;
    }

    public void setArticles(List<NewsArticle> articleList) {
        this.articleList = articleList;
    }
}
