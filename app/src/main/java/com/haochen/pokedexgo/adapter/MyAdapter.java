package com.haochen.pokedexgo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Haochen on 2016/7/20.
 */
public abstract class MyAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> data;

    public MyAdapter(Context context, List<T> data) {
        this.context = context;
        this.data = data;
    }

    public MyAdapter(Context context, Cursor cursor) {
        this.context = context;
        setData(cursor);
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public abstract void setData(Cursor cursor);

    public List<T> getData() {
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}