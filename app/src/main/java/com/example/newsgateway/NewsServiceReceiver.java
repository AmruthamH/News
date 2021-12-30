package com.example.newsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.newsgateway.MainActivity;
import com.example.newsgateway.ArticleDownloader;
import com.example.newsgateway.NewsService;

public class NewsServiceReceiver extends BroadcastReceiver {

    private static final String TAG = "NewsServiceReceiver";

    private final NewsService nService;

    public NewsServiceReceiver(NewsService newsService) {
        this.nService = newsService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        String ac = intent.getAction();
        if (ac == null)
            return;
        if (MainActivity.ACTION_MSG_TO_SERVICE.equals(ac)) {
            String sId = null;
            String lID = null;
            if (intent.hasExtra(MainActivity.SOURCE_ID)) {
                sId = intent.getStringExtra(MainActivity.SOURCE_ID);
                sId = sId.replaceAll(" ", "-");
                lID = intent.getStringExtra(MainActivity.LANG_ID);
                if(lID==null){
                    lID="";
                }
            }
            Log.d(TAG, "onReceive: ArticleDownloader");
            new Thread(new ArticleDownloader(nService, sId,lID)).start();
        }
    }
}