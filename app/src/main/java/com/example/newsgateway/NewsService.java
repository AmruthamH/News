package com.example.newsgateway;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.newsgateway.MainActivity;
import com.example.newsgateway.NewsArticle;
import com.example.newsgateway.NewsServiceReceiver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewsService extends Service {
    private static final String TAG = "NewsService";

    private boolean isRun = true;
    private NewsServiceReceiver nsReceiver;
    private List<NewsArticle> liArticles = new ArrayList<>();

    public NewsService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        nsReceiver = new NewsServiceReceiver(this);
        IntentFilter intentF = new IntentFilter(MainActivity.ACTION_MSG_TO_SERVICE);
        registerReceiver(nsReceiver, intentF);

        new Thread(() -> {
            while (isRun) {
                while (liArticles.isEmpty()) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                sendArticles();
            }
        }).start();
        return Service.START_STICKY;
    }

    private void sendArticles() {
        Log.d(TAG, "sendArticles: Broadcasting Article");
        Intent inte = new Intent();
        inte.setAction(MainActivity.ACTION_NEWS_STORY);
        inte.putExtra(MainActivity.ARTICLE_LIST, (Serializable) liArticles);
        sendBroadcast(inte);
        liArticles.clear();
    }

    public void populateArticles(List<NewsArticle> articles) {
        this.liArticles.clear();
        this.liArticles.addAll(articles);
    }

    @Override
    public void onDestroy() {
        isRun = false;
        super.onDestroy();
    }
}