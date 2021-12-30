package com.example.newsgateway;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    public static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    public static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";
    public static final String ARTICLE_LIST = "ARTICLE_LIST";
    public static final String SOURCE_ID = "SOURCE_ID";
    public static final String LANG_ID = "LANG_ID";
    private static final String TAG = "MainActivity";
    private String newsSource,csm,lsm,catsm;

    private int currentSP;
    private boolean state;
    private boolean serviceStatus = false;

    private List<String> sourceList;
    private List<NewsSource> newsSourcesList;
    private List<String> categoriesList;
    private List<String> langua;
    private List<String> coun;
    private List<NewsArticle> articlesList;
    private List<NewsFragment> fragmentList;
    private Map<String, NewsSource> sourceStore;

    private MainActivityReceiver mareceiver;
    private SourceAdapter sa;

    private Menu catMenu;

    private List<Drawer> drawerList;
    private DrawerLayout drawerLay;
    private ListView drawerLV;
    private ActionBarDrawerToggle drawerTog;
    private PageAdapter pageAdap;
    private ViewPager viewP;

    private int[] topicCol;
    private Map<String, Integer> topicMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sourceList = new ArrayList<>();
        newsSourcesList = new ArrayList<>();
        categoriesList = new ArrayList<>();
        articlesList = new ArrayList<>();

        drawerList = new ArrayList<>();
        fragmentList = new ArrayList<>();

        sourceStore = new HashMap<>();

        topicMap = new HashMap<>();
        topicCol = getResources().getIntArray(R.array.topicColors);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mareceiver = new MainActivityReceiver(this);
        drawerLay = findViewById(R.id.drawerLayout);
        drawerLV = findViewById(R.id.drawerList);
        sa = new SourceAdapter(this, drawerList);
        drawerLV.setAdapter(sa);
        pageAdap = new PageAdapter(getSupportFragmentManager(), fragmentList);
        viewP = findViewById(R.id.viewPage);
        viewP.setAdapter(pageAdap);


        if (savedInstanceState == null && !serviceStatus) {
            Log.d(TAG, "onCreate: Starting News Service");
            Intent sIIntent = new Intent(MainActivity.this, NewsService.class);
            startService(sIIntent);
            serviceStatus = true;
        }
        IntentFilter filter = new IntentFilter(MainActivity.ACTION_NEWS_STORY);
        registerReceiver(mareceiver, filter);
        if (sourceStore.isEmpty() && savedInstanceState == null)
            new Thread(new SourceDownloader(this, "","","")).start();


        drawerLV.setOnItemClickListener((parent, view, pos, id) -> {
            viewP.setBackgroundResource(0);
            currentSP = pos;
            selectListItem(pos);
        });


        drawerTog = new ActionBarDrawerToggle(this, drawerLay, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        //updateTitleTotal();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.categories, menu);
        catMenu = menu;
        if (state) {
            SubMenu topicsm = catMenu.addSubMenu("Topics");
            SubMenu countrysm = catMenu.addSubMenu("Country");
            SubMenu langsm = catMenu.addSubMenu("Languages");
            //categoryMenu.add("all");
            for (String category : categoriesList)
                topicsm.add(category);
            for (String languages : langua)
                langsm.add(languages);
            for (String countries : coun)
                countrysm.add(countries);

        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");

        if (drawerTog.onOptionsItemSelected(item)) {  // <== Important!

            Log.d(TAG, "onOptionsItemSelected: " + item);
            //drawerLayout.openDrawer(drawerListView);
            return true;
        }
        if(item.hasSubMenu()) {
            return true;
        }
        if (item.getGroupId() == 0) {
            catsm=item.getTitle().toString();
            lsm="";
            csm="";

        }
        if (item.getGroupId() == 1) {
            csm=item.getTitle().toString();
            try {
                JSONObject obj = new JSONObject(jsonCountriesFileLoading());
                //String jsonLocation = jsonCountriesFileLoading();
                JSONArray jsonLocation = obj.getJSONArray("countries");
                ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> m_li;
                for (int l = 0; l < jsonLocation.length(); l++) {
                    JSONObject jo_inside = jsonLocation.getJSONObject(l);
                    String cc = jo_inside.getString("code");
                    String cn = jo_inside.getString("name");
                    //String cUpperCase=csm.toUpperCase();

                    if(csm.equals(cn)){
                        //csm="";
                        csm=cc;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            lsm="";

        }
        if (item.getGroupId() == 2) {
            lsm=item.getTitle().toString();
            try {
                JSONObject obj = new JSONObject(jsonLanguagesFileLoading());
                //String jsonLocation = jsonCountriesFileLoading();
                JSONArray jsonLocation = obj.getJSONArray("languages");

                for (int l = 0; l < jsonLocation.length(); l++) {
                    JSONObject jo_inside = jsonLocation.getJSONObject(l);
                    String lc = jo_inside.getString("code");
                    String ln = jo_inside.getString("name");
                    //String cUpperCase=csm.toUpperCase();

                    if(lsm.equals(ln)){
                        //csm="";
                        lsm=lc;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            csm="";
        }
        Log.d(TAG, "onOptionsItemSelected: Starting Source thread");
        new Thread(new SourceDownloader(this, catsm,lsm,csm)).start();
        drawerLay.openDrawer(drawerLV);

        return super.onOptionsItemSelected(item);
    }





    @Override
    protected void onSaveInstanceState(@NonNull Bundle outS) {
        Log.d(TAG, "onSaveInstanceState: ");
        LayoutManager layoutRestore = new LayoutManager();

        Log.d(TAG, "categories: " + categoriesList);
        layoutRestore.setCategories(categoriesList);

        Log.d(TAG, "sources: " + newsSourcesList);
        layoutRestore.setSources(newsSourcesList);

        Log.d(TAG, "languages: " + langua);
        layoutRestore.setLanglm(langua);

        Log.d(TAG, "countries: " + coun);
        layoutRestore.setCountrylm(coun);

        layoutRestore.setArticle(viewP.getCurrentItem());

        Log.d(TAG, "currentSP : " + currentSP);
        layoutRestore.setSource(currentSP);

        Log.d(TAG, "articles : " + articlesList);
        layoutRestore.setArticles(articlesList);

        outS.putSerializable("state", layoutRestore);

        super.onSaveInstanceState(outS);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: ");
        super.onRestoreInstanceState(savedInstanceState);

        setTitle(R.string.app_name);
        LayoutManager layoutManager = (LayoutManager) savedInstanceState.getSerializable("state");
        state = true;

        articlesList = layoutManager.getArticles();
        Log.d(TAG, "articles: " + articlesList);

        categoriesList = layoutManager.getCategories();
        Log.d(TAG, "categories: " + categoriesList);

        newsSourcesList = layoutManager.getSources();
        Log.d(TAG, "sources: " + newsSourcesList);

        langua = layoutManager.getlangLM();
        Log.d(TAG, "languages: " + langua);

        coun = layoutManager.getCountrylm();
        Log.d(TAG, "Countries: " + coun);

        for (int i = 0; i < newsSourcesList.size(); i++) {
            sourceList.add(newsSourcesList.get(i).getName());
            sourceStore.put(newsSourcesList.get(i).getName(), newsSourcesList.get(i));
        }

        drawerLV.clearChoices();
        sa.notifyDataSetChanged();
        drawerLV.setOnItemClickListener((parent, view, posi, id) -> {
            viewP.setBackgroundResource(0);
                    currentSP = posi;
                    selectListItem(posi);
                }
        );
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onPostCreate: ");
        super.onPostCreate(savedInstanceState);
        drawerTog.syncState();
    }


    private String jsonCountriesFileLoading() {

        Log.d(TAG, "loadFile: Loading JSON File ");
        String json=null;
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.country_codes);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer, "UTF-8");

        }
        catch (FileNotFoundException e) {
            Toast.makeText(this, "No JSON file", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: Stopping News Service");
        unregisterReceiver(mareceiver);
        Intent intent = new Intent(MainActivity.this, MainActivityReceiver.class);
        stopService(intent);
        super.onDestroy();
    }

    private String jsonLanguagesFileLoading() {

        Log.d(TAG, "loadFile: Loading JSON File ");
        String json2=null;
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.language_codes);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            json2 = new String(buffer, "UTF-8");

        }
        catch (FileNotFoundException e) {
            Toast.makeText(this, "No JSON file", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json2;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged: ");
        super.onConfigurationChanged(newConfig);
        drawerTog.onConfigurationChanged(newConfig);
    }


    public void updateTitleTotal()
    {
        int totalNumOfNews = newsSourcesList.size();
        if (totalNumOfNews != 0)
            setTitle(getString(R.string.app_name)+ " (" + totalNumOfNews + ")");
        else

            setTitle(getString(R.string.app_name));
    }


    public void SourceCategory(List<String> newsCategories, List<NewsSource> nSource,List<String> newsCountry,List<String> newsLanguage) {
        Log.d(TAG, "SourceCategory: ");
        Log.d(TAG, "size: " + nSource.size() + ", Categories size: " + newsCategories.size());
        sourceStore.clear();
        sourceList.clear();
        newsSourcesList.clear();
        drawerList.clear();
        newsSourcesList.addAll(nSource);
        updateTitleTotal();

        if (!catMenu.hasVisibleItems()) {
            categoriesList.clear();
            SubMenu topicsSubmenu = catMenu.addSubMenu("Topics ");
            SubMenu countriesMenu = catMenu.addSubMenu("Countries ");
            SubMenu languagesMenu = catMenu.addSubMenu("Languages ");
            categoriesList = newsCategories;
            topicsSubmenu.add("all");
            languagesMenu.add("all");
            countriesMenu.add("all");
            Collections.sort(newsCategories);
            int i = 0;
            int p=0;
            for (String cat : newsCategories) {
                SpannableString catString = new SpannableString(cat);
                catString.setSpan(new ForegroundColorSpan(topicCol[i]), 0, catString.length(), 0);
                topicMap.put(cat, topicCol[i++]);
                //topicsSubmenu.add(categoryString);
                topicsSubmenu.add(0,p++,p++,catString);

            }
            int j=0;
            String cs="";
            for (String c : newsCountry){
                try {
                    JSONObject obj = new JSONObject(jsonCountriesFileLoading());
                    //String jsonLocation = jsonCountriesFileLoading();
                    JSONArray jsonLocation = obj.getJSONArray("countries");
                    ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
                    HashMap<String, String> m_li;
                    for (int l = 0; l < jsonLocation.length(); l++) {
                        JSONObject jo_inside = jsonLocation.getJSONObject(l);
                        String cc = jo_inside.getString("code");
                        String cn = jo_inside.getString("name");
                        String cUpperCase=c.toUpperCase();

                        if(cUpperCase.equals(cc)){
                            cs=cn;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                countriesMenu.add(1,j++,j++,cs);

            }
            int k =0;
            String ls="";
            for (String lan : newsLanguage){
                try {
                    JSONObject obj = new JSONObject(jsonLanguagesFileLoading());

                    JSONArray jsonLocation = obj.getJSONArray("languages");
                    ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
                    HashMap<String, String> m_li;
                    for (int l = 0; l < jsonLocation.length(); l++) {
                        JSONObject jo_inside = jsonLocation.getJSONObject(l);
                        String lc = jo_inside.getString("code");
                        String ln = jo_inside.getString("name");
                        String langUpperCase=lan.toUpperCase();

                        if(langUpperCase.equals(lc)){
                            ls=ln;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                languagesMenu.add(2,k++,k++,ls);

            }
        }
        for (NewsSource sour : nSource) {
            if (topicMap.containsKey(sour.getCategory())) {
                int color = topicMap.get(sour.getCategory());
                SpannableString coloredString = new SpannableString(sour.getName());
                coloredString.setSpan(new ForegroundColorSpan(color), 0, sour.getName().length(), 0);
                sour.setColoredName(coloredString);
                sourceList.add(sour.getName());
                sourceStore.put(sour.getName(), sour);
            }
        }


        for (NewsSource sour : nSource) {
            Drawer drawerContent = new Drawer();
            drawerContent.setItemName(sour.getColoredName());
            drawerList.add(drawerContent);
        }
        sa.notifyDataSetChanged();

        if(drawerList.isEmpty()){
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            b.setMessage("Invalid selection for Topic:"+catsm+" , "+"Country:"+csm+" , "+"Language:"+lsm);
            AlertDialog dialog = b.create();
            dialog.show();
        }


    }

    public void updateFragments(List<NewsArticle> articles) {
        Log.d(TAG, "updateFragments: ");
        setTitle(newsSource);

        for (int i = 0; i < pageAdap.getCount(); i++)
            pageAdap.notifyChangeInPosition(i);

        fragmentList.clear();

        for (int artic = 0; artic < articles.size(); artic++) {
            fragmentList.add(NewsFragment.newInstance(articles.get(artic), artic, articles.size()));
        }
        pageAdap.notifyDataSetChanged();
        viewP.setCurrentItem(0);
        this.articlesList = articles;
    }

    private void selectListItem(int position) {
        Log.d(TAG, "selectListItem => selected pos: " + position + ", sourceList size: " + sourceList.size());
        newsSource = sourceList.get(position);
        Intent sintent = new Intent(MainActivity.ACTION_MSG_TO_SERVICE);
        sintent.putExtra(SOURCE_ID, newsSource);
        sendBroadcast(sintent);
        drawerLay.closeDrawer(drawerLV);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "The back button pressed", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}