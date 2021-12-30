package com.example.newsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.newsgateway.MainActivity;
import com.example.newsgateway.NewsArticle;

import java.util.List;

import static com.example.newsgateway.MainActivity.ARTICLE_LIST;
public class MainActivityReceiver extends BroadcastReceiver {

    private static final String TAG = "MainActivityReceiver";

    private final MainActivity ma;

    public MainActivityReceiver(MainActivity mainActivity) {
        this.ma = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent inte) {
        Log.d(TAG, "onReceive: ");
        String a = inte.getAction();
        if (a == null)
            return;
        if (MainActivity.ACTION_NEWS_STORY.equals(a)) {
            List<NewsArticle> articlesLi;
            if (inte.hasExtra(ARTICLE_LIST)) {
                articlesLi = (List<NewsArticle>) inte.getSerializableExtra(ARTICLE_LIST);
                ma.updateFragments(articlesLi);
            }
        }
    }
}