package com.example.newsgateway;

import android.text.SpannableString;

import java.io.Serializable;

public class NewsSource implements Serializable {

    private String nsid;
    private String nsname;
    private String nscategory;
    private String nsurl;
    private String nscountry;
    private String nslanguage;
    private transient SpannableString nscoloredName;

    public String getId() {
        return nsid;
    }

    public void setId(String nsid) {
        this.nsid = nsid;
    }

    public String getName() {
        return nsname;
    }

    public void setName(String nsname) {
        this.nsname = nsname;
    }

    public String getCategory() {
        return nscategory;
    }

    public void setCategory(String nscategory) {
        this.nscategory = nscategory;
    }

    public String getCountry() {
        return nscountry;
    }

    public void setCountry(String nscountry) {
        this.nscountry = nscountry;
    }

    public String getLanguage() {
        return nslanguage;
    }

    public void setLanguage(String nslanguage) {
        this.nslanguage = nslanguage;
    }

    public String getUrl() {
        return nsurl;
    }

    public void setUrl(String url) {
        this.nsurl = nsurl;
    }

    public SpannableString getColoredName() {
        return nscoloredName;
    }

    public void setColoredName(SpannableString coloredName) {
        this.nscoloredName = coloredName;
    }

    @Override
    public String toString() {
        return "NewsSource{" +
                "id='" + nsid + '\'' +
                ", name='" + nsname + '\'' +
                ", category='" + nscategory + '\'' +
                ", url='" + nsurl + '\'' +
                '}';
    }
}
