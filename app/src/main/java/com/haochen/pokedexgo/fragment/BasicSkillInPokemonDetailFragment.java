package com.haochen.pokedexgo.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.haochen.pokedexgo.adapter.BasicSkillAdapter;
import com.haochen.pokedexgo.sqlite.DBHelper;

/**
 * Created by Haochen on 2016/8/3.
 */
public class BasicSkillInPokemonDetailFragment extends BasicSkillFragment {

    @Override
    public void search(String key) {}

    @Override
    protected void show() {
        String pokemonId = getArguments().getString("id");

        DBHelper helper = DBHelper.getInstance(null);
        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT basic_skill.name, name_zh_cn, name_en_us, type.name," +
//                " power, duration, energy" +
//                " FROM basic_skill LEFT JOIN type" +
//                " ON type_id = type._id" +
//                " WHERE basic_skill.name IN (" +
//                "                            SELECT skill_name" +
//                "                              FROM pokemon_basic_skill" +
//                "                              WHERE pm_id= ?)", new String[]{pokemonId});
        Cursor cursor = db.query(TABLE, COLUMNS,
                "basic_skill._id IN (" +
                        "            SELECT skill_id" +
                        "            FROM pokemon_basic_skill" +
                        "            WHERE pm_id = ?)", new String[]{pokemonId},
                null, null, null);
        adapter = new BasicSkillAdapter(getActivity(), cursor);
        cursor.close();
        db.close();
        listView.setAdapter(adapter);
        data = adapter.getData();
    }
}
