package com.example.newsgateway;

import android.net.Uri;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.newsgateway.MainActivity;
import com.example.newsgateway.NewsSource;


public class SourceDownloader implements Runnable {
    private static final String TAG = "SourceDownloader";

    private static final String API_KEY = "10959e84d884497cab223871bea3bc19";
    private static final String BASE_URL = "https://newsapi.org/v2/sources?";
    private static final String CATEGORY_T = "&category=";
    private static final String API_KEY_T = "&apiKey=";
    private static final String LANGUAGE_T="&language=";
    private static final String COUNTRY_T="&country=";

    private String newsCategory;
    private String newsLanguage;
    private String newsCountry;
    private List<String> categoriesList;
    private List<NewsSource> sourcesList;
    private List<String> langList;
    private List<String> countryList;

    private MainActivity mainActivity;

    public SourceDownloader(MainActivity mainActivity, String cate, String langu, String coun) {
        this.mainActivity = mainActivity;
        newsCategory = cate;
        newsLanguage = langu;
        newsCountry = coun;
        sourcesList = new ArrayList<>();
        categoriesList = new ArrayList<>();
        countryList = new ArrayList<>();
        langList = new ArrayList<>();

    }

    @Override
    public void run() {
        Log.d(TAG, "run: Downloading Sources");
        String DOWNLOAD_LINK = initURL();

        Uri uri = Uri.parse(DOWNLOAD_LINK);
        String li;
        StringBuilder sbui = new StringBuilder();

        try {
            URL url = new URL(uri.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("User-Agent", "");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            InputStream inps = conn.getInputStream();
            BufferedReader bReader = new BufferedReader((new InputStreamReader(inps)));

            while ((li = bReader.readLine()) != null) {
                sbui.append(li).append('\n');
            }
        } catch (Exception e) {
            Log.e(TAG, "run: Exception: ", e);
        }
        handleResults(sbui.toString());
    }

    private String initURL() {
        String DOWNLOAD_LINK = BASE_URL + CATEGORY_T;

        if (!TextUtils.isEmpty(newsCategory) && !"all".equalsIgnoreCase(newsCategory))
            DOWNLOAD_LINK += newsCategory;
        DOWNLOAD_LINK += COUNTRY_T;

        if (!TextUtils.isEmpty(newsCountry) && !"all".equalsIgnoreCase(newsCountry))
            DOWNLOAD_LINK += newsCountry;
        DOWNLOAD_LINK += LANGUAGE_T;


        if (!TextUtils.isEmpty(newsLanguage) && !"all".equalsIgnoreCase(newsLanguage))
            DOWNLOAD_LINK += newsLanguage;

        DOWNLOAD_LINK += API_KEY_T + API_KEY;
        return DOWNLOAD_LINK;
    }




    private void parseJSON(String inp) {
        Log.d(TAG, "parseJSON: Parsing JSON data");
        try {
            JSONObject jsonObj = new JSONObject(inp);
            JSONArray jsonArr = jsonObj.getJSONArray("sources");
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject sourceJsonObj = jsonArr.getJSONObject(i);
                NewsSource nsource = new NewsSource();
                nsource.setId(sourceJsonObj.getString("id"));
                nsource.setName(sourceJsonObj.getString("name"));
                nsource.setCategory(sourceJsonObj.getString("category"));
                nsource.setUrl(sourceJsonObj.getString("url"));
                nsource.setLanguage(sourceJsonObj.getString("language"));
                nsource.setCountry(sourceJsonObj.getString("country"));
                nsource.setColoredName(new SpannableString(nsource.getName()));

                sourcesList.add(nsource);
                if (!categoriesList.contains(nsource.getCategory()))
                    categoriesList.add(nsource.getCategory());

                if (!langList.contains(nsource.getLanguage()))
                    langList.add(nsource.getLanguage());

                String nem=nsource.getCountry();

                if (!countryList.contains(nsource.getCountry()))
                    countryList.add(nsource.getCountry());
            }
        } catch (Exception e) {
            Log.e(TAG, "parseJSON: Failed to parse", e);
        }
    }
    public void handleResults(final String jsonString) {
        Log.d(TAG, "handleResults: Sources and Categories in MainActivity");
        parseJSON(jsonString);
        mainActivity.runOnUiThread(() -> mainActivity.SourceCategory(categoriesList, sourcesList,countryList,langList));
    }
}

