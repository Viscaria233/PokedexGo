package com.haochen.pokedexgo.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.adapter.ChargeSkillAdapter;
import com.haochen.pokedexgo.sqlite.DBHelper;

/**
 * Created by Haochen on 2016/8/3.
 */
public class ChargeSkillInPokemonDetailFragment extends ChargeSkillFragment {

    @Override
    protected void show() {
        String pokemonId = getArguments().getString("id");

        DBHelper helper = DBHelper.getInstance(null);
        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT charge_skill.name, type.name, power, skill_bar," +
//                "damage_scalar, heal_scalar, duration, damage_window, ct" +
//                " FROM charge_skill LEFT JOIN type" +
//                " ON type_id = type._id" +
//                " WHERE charge_skill.name IN (" +
//                "                            SELECT skill_name" +
//                "                              FROM pokemon_charge_skill" +
//                "                              WHERE pm_id= ?)", new String[]{pokemonId});
        Cursor cursor = db.query(TABLE, COLUMNS,
                "charge_skill._id IN (" +
                        "             SELECT skill_id" +
                        "             FROM pokemon_charge_skill" +
                        "             WHERE pm_id = ?)", new String[]{pokemonId},
                null, null, null);
        adapter = new ChargeSkillAdapter(getActivity(), cursor);
        cursor.close();
        db.close();
        listView.setAdapter(adapter);
        data = adapter.getData();
    }
}
