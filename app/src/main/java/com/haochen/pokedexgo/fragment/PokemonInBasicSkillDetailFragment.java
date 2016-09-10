package com.haochen.pokedexgo.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.adapter.PokemonAdapter;
import com.haochen.pokedexgo.sqlite.DBHelper;

/**
 * Created by Haochen on 2016/8/5.
 */
public class PokemonInBasicSkillDetailFragment extends PokemonFragment {

    @Override
    protected void show() {
        String skillId = getArguments().getString("id");

        DBHelper helper = DBHelper.getInstance(null);
        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT pokemon._id, type1.name, type2.name," +
//                "hp, atk, def, sum, catch, run, incubate" +
//                " FROM type type1, pokemon LEFT JOIN type type2" +
//                "   ON type_id_2 = type2._id" +
//                "   WHERE type_id_1 = type1._id" +
//                "     AND pokemon._id IN (" +
//                "                         SELECT pm_id" +
//                "                           FROM pokemon_basic_skill" +
//                "                           WHERE skill_name = ?)" +
//                "   ORDER BY pokemon._id ASC", new String[]{skillName});
        Cursor cursor = db.query(TABLE, COLUMNS,
                "type_id_1 = t1._id" +
                        " AND pokemon._id IN (" +
                        "                     SELECT pm_id" +
                        "                     FROM pokemon_basic_skill" +
                        "                     WHERE skill_id = ?)", new String[]{skillId},
                null, null, ORDER);
        adapter = new PokemonAdapter(getActivity(), cursor);
        cursor.close();
        db.close();
        listView.setAdapter(adapter);
        data = adapter.getData();
    }
}
