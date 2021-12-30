package com.example.newsgateway;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsFragment extends Fragment {

    private static final String TAG = "NewsFragment";

    private static final String ARTICLE = "ARTICLE";
    private static final String INDEX = "INDEX";
    private static final String TOTAL = "TOTAL";
    private static final String DATE_FORMAT = "MMM dd, yyyy HH:mm";
    private static final String DATE_FORMAT_PARSE = "yyyy-MM-dd'T'HH:mm:ss";

    private static final SimpleDateFormat sdfF = new SimpleDateFormat(DATE_FORMAT);
    private static final SimpleDateFormat sdfP = new SimpleDateFormat(DATE_FORMAT_PARSE);

    private TextView articleHeadLine;
    private TextView articleDate;
    private TextView articleAuthor;
    private TextView articleText;
    private ImageView articleImage;
    private TextView articleCount;

    private NewsArticle newsarticle;

    private View v;

    public NewsFragment() {

    }

    public static NewsFragment newInstance(NewsArticle art, int ind, int tot) {
        Log.d(TAG, "newInstance: Creating News Fragment instance");
        NewsFragment nf = new NewsFragment();
        Bundle arg = new Bundle(1);
        arg.putSerializable(ARTICLE, art);
        arg.putInt(INDEX, ind);
        arg.putInt(TOTAL, tot);
        nf.setArguments(arg);
        return nf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater lInflater, ViewGroup vgContainer, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        v = lInflater.inflate(R.layout.fragment_news, vgContainer, false);

        articleHeadLine = v.findViewById(R.id.articleHeadline);
        articleDate = v.findViewById(R.id.articleDate);
        articleAuthor = v.findViewById(R.id.articleAuthor);
        articleText = v.findViewById(R.id.articleText);
        articleImage = v.findViewById(R.id.articleImage);
        articleCount = v.findViewById(R.id.articleCount);

        newsarticle = (NewsArticle) getArguments().getSerializable(ARTICLE);


        if (isNull(newsarticle.getTitle()))
            articleHeadLine.setVisibility(View.GONE);
        else
            articleHeadLine.setText(newsarticle.getTitle());


        if (isNull(newsarticle.getAuthor()))
            articleAuthor.setVisibility(View.GONE);
        else
            articleAuthor.setText(newsarticle.getAuthor());


        if (isNull(newsarticle.getDescription()))
            articleText.setVisibility(View.GONE);
        else
            articleText.setText(newsarticle.getDescription());


        if (!isNull(newsarticle.getPublishedAt())) {
            try {
                Date pDate = sdfP.parse(newsarticle.getPublishedAt());
                if (pDate != null) {
                    articleDate.setText(sdfF.format(pDate));
                }
            } catch (ParseException e) {
                Log.e(TAG, "onCreateView: Failed to parse date", e);
            }
        }

        if (isNull(newsarticle.getUrlToImage()))
            articleImage.setVisibility(View.GONE);
        else
            showImage(newsarticle.getUrlToImage());

        articleCount.setText(String.format("%d of %d", getArguments().getInt(INDEX) + 1, getArguments().getInt(TOTAL)));

        articleHeadLine.setOnClickListener(v -> startIntent());

        articleImage.setOnClickListener(v -> startIntent());

        articleText.setOnClickListener(v -> startIntent());

        return v;
    }

    private void startIntent() {
        Log.d(TAG, "startIntent: Opening news in browser");
        Intent sIntent = new Intent();
        sIntent.setAction(Intent.ACTION_VIEW);
        sIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        sIntent.setData(Uri.parse(newsarticle.getUrl()));
        startActivity(sIntent);
    }

    private void showImage(final String imageURL) {
        Log.d(TAG, "showImage: Displaying News image. Image URL : " + imageURL);
        Picasso p = new Picasso.Builder(getActivity()).listener((picasso1, uri, exception) -> exception.printStackTrace()).build();

        p.setLoggingEnabled(true);

        p.load(imageURL).placeholder(R.drawable.loading).error(R.drawable.noimage).into(articleImage);


    }

    private boolean isNull(String input) {
        return TextUtils.isEmpty(input) || input.trim().equals("null");
    }
}
