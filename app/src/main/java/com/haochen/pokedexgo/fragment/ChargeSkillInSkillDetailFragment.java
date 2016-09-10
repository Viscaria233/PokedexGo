package com.haochen.pokedexgo.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.adapter.ChargeSkillAdapter;
import com.haochen.pokedexgo.sqlite.DBHelper;

/**
 * Created by Haochen on 2016/8/5.
 */
public class ChargeSkillInSkillDetailFragment extends ChargeSkillFragment {
    @Override
    protected void show() {
        int typeId = getArguments().getInt("id");

        DBHelper helper = DBHelper.getInstance(null);
        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT charge_skill.name, type.name, power, skill_bar," +
//                "damage_scalar, heal_scalar, duration, damage_window, ct" +
//                " FROM charge_skill LEFT JOIN type" +
//                " ON type_id = type._id" +
//                " WHERE type_id = ?", new String[]{"" + typeId});
        Cursor cursor = db.query(TABLE, COLUMNS, "type_id = ?",
                new String[]{"" + typeId}, null, null, null);
        adapter = new ChargeSkillAdapter(getActivity(), cursor);
        cursor.close();
        db.close();
        listView.setAdapter(adapter);
        data = adapter.getData();
    }
}
