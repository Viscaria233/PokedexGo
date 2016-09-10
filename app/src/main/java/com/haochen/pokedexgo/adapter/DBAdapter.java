package com.haochen.pokedexgo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Haochen on 2016/7/20.
 */
public abstract class DBAdapter<T> extends MyAdapter<T> {

    protected String tableName;
    protected SQLiteDatabase db;

    public DBAdapter(Context context, List<T> list, String tableName) {
        super(context, list);
        this.tableName = tableName;
    }

    public DBAdapter(Context context, Cursor cursor, String tableName) {
        super(context, cursor);
        this.tableName = tableName;
    }
}
