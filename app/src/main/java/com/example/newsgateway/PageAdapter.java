package com.example.newsgateway;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.newsgateway.NewsFragment;

import java.util.List;

public class PageAdapter extends FragmentPagerAdapter {
    private long padapbaseId = 0;
    private List<NewsFragment> nFragments;

    public PageAdapter(FragmentManager fm, List<NewsFragment> newsFragments) {
        super(fm);
        this.nFragments = newsFragments;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Fragment getItem(int pos) {
        return nFragments.get(pos);
    }

    @Override
    public int getCount() {
        return nFragments.size();
    }

    @Override
    public long getItemId(int pos) {

        return padapbaseId + pos;
    }

    public void notifyChangeInPosition(int ne) {

        padapbaseId += getCount() + ne;
    }
}
