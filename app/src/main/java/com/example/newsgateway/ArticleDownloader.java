package com.example.newsgateway;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.newsgateway.NewsArticle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ArticleDownloader implements Runnable {

    private static final String TAG = "ArticleDownloader";
    private static final String API_KEY = "10959e84d884497cab223871bea3bc19";
    private static final String BASE_URL = "https://newsapi.org/v2/top-headlines?pageSize=10";
    private static final String SOURCE_T = "&sources=";
    private static final String API_KEY_T = "&apiKey=";
    private static final String LANG_ID = "&language=";

    private String soID,langID;
    private List<NewsArticle> nar;
    private NewsService nser;

    public ArticleDownloader(NewsService nser, String soID,String langID) {
        this.nser = nser;
        this.soID = soID;
        this.langID = langID;
        nar = new ArrayList<>();
    }

    @Override
    public void run() {
        Log.d(TAG, "run: Downloading Article");
        Uri uri = Uri.parse(initURL());

        String li;
        StringBuilder sbui = new StringBuilder();

        try {
            URL u = new URL(uri.toString());

            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("GET");
            con.addRequestProperty("User-Agent", "");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.connect();

            InputStream ins = con.getInputStream();
            BufferedReader br = new BufferedReader((new InputStreamReader(ins)));

            while ((li = br.readLine()) != null) {
                sbui.append(li).append('\n');
            }
        } catch (Exception e) {
            Log.e(TAG, "run: Exception: ", e);
        }
        handleResults(sbui.toString());
    }

    private String initURL() {
        return BASE_URL + SOURCE_T + soID + LANG_ID +  API_KEY_T + API_KEY;
    }

    public void handleResults(final String jsonString) {
        Log.d(TAG, "handleResults: Updating Articles");
        parseJSON(jsonString);
        nser.populateArticles(nar);
    }

    private void parseJSON(String input) {
        Log.d(TAG, "parseJSON: Parsing Article data");
        try {
            JSONObject jOb = new JSONObject(input);
            JSONArray ja = jOb.getJSONArray("articles");

            for (int i = 0; i < ja.length(); i++) {
                JSONObject articleJsonObject = ja.getJSONObject(i);
                NewsArticle ar = new NewsArticle();
                ar.setAuthor(articleJsonObject.getString("author"));
                ar.setTitle(articleJsonObject.getString("title"));
                ar.setDescription(articleJsonObject.getString("description"));
                ar.setUrl(articleJsonObject.getString("url"));
                ar.setUrlToImage(articleJsonObject.getString("urlToImage"));
                ar.setPublishedAt(articleJsonObject.getString("publishedAt"));

                nar.add(ar);
            }
        } catch (Exception e) {
            Log.e(TAG, "parseJSON: Failed to parse JSON", e);
        }
    }
}
