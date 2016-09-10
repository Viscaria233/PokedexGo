package com.haochen.pokedexgo.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.haochen.pokedexgo.adapter.BasicSkillAdapter;
import com.haochen.pokedexgo.sqlite.DBHelper;

/**
 * Created by Haochen on 2016/8/5.
 */
public class BasicSkillInSkillDetailFragment extends BasicSkillFragment {

    @Override
    public void search(String key) {}

    @Override
    protected void show() {
        int typeId = getArguments().getInt("id");

        DBHelper helper = DBHelper.getInstance(null);
        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT basic_skill.name, name_zh_cn, name_en_us, type.name," +
//                " power, duration, energy" +
//                " FROM basic_skill LEFT JOIN type" +
//                " ON type_id = type._id" +
//                " WHERE type_id = ?", new String[]{"" + typeId});
        Cursor cursor = db.query(TABLE, COLUMNS, "type_id = ?",
                new String[]{"" + typeId}, null, null, null);
        adapter = new BasicSkillAdapter(getActivity(), cursor);
        cursor.close();
        db.close();
        listView.setAdapter(adapter);
        data = adapter.getData();
    }
}
