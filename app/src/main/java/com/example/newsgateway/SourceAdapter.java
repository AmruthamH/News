package com.example.newsgateway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SourceAdapter extends BaseAdapter {

    private final Context context;
    private final List<Drawer> drawerList;

    public SourceAdapter(Context context, List<Drawer> drawerList) {
        this.context = context;
        this.drawerList = drawerList;
    }

    @Override
    public int getCount() {
        return drawerList.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {
        if (cv == null)
            cv = (LayoutInflater.from(context).inflate(R.layout.drawer_list_item, parent, false));

        Drawer d = drawerList.get(position);
        TextView textView = cv.findViewById(R.id.listItem);
        textView.setText(d.getItemName());
        return cv;
    }
}
