package com.haochen.pokedexgo.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.haochen.pokedexgo.PokemonDetailActivity;
import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.adapter.EvoAdapter;
import com.haochen.pokedexgo.common.Pokemon;
import com.haochen.pokedexgo.sqlite.DBHelper;

import java.util.Locale;

/**
 * Created by Haochen on 2016/8/3.
 */
public class EvoFragment extends BaseFragment {

    private String pokemonId;
    private ListView listView;
    private BaseAdapter adapter;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_item_only_list_view, container, false);
    }

    @Override
    protected void initial() {
        pokemonId = getArguments().getString("id");
        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new EvoAdapter(getActivity(), pokemonId);
    }

    @Override
    protected void show() {
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String clickedId = ((EvoAdapter.EvoInfo) adapter.getItem(position)).getId();

                DBHelper helper = DBHelper.getInstance(null);
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT name_zh_cn, name_en_us, type1.name, type2.name," +
                        "hp, atk, def, sum, catch, run, incubate" +
                        " FROM type type1, pokemon LEFT JOIN type type2" +
                        "   ON type_id_2 = type2._id" +
                        "   WHERE type_id_1 = type1._id" +
                        "     AND pokemon._id = ?" +
                        "   ORDER BY pokemon._id ASC", new String[]{clickedId});
                final Pokemon pokemon = new Pokemon();
                if (cursor.moveToNext()) {
                    pokemon.setId(clickedId);
                    pokemon.addName(Locale.CHINA, cursor.getString(0));
                    pokemon.addName(Locale.US, cursor.getString(1));
                    pokemon.addType(cursor.getString(2));
                    String type2 = cursor.getString(3);
                    if (type2 != null) {
                        pokemon.addType(type2);
                    }

                    pokemon.setHp(cursor.getString(4));
                    pokemon.setAtk(cursor.getString(5));
                    pokemon.setDef(cursor.getString(6));
                    pokemon.setSum(cursor.getString(7));
                    pokemon.setCatchPercent(cursor.getString(8));
                    pokemon.setRunPercent(cursor.getString(9));
                    pokemon.setIncubate(cursor.getString(10));
                }
                cursor.close();
                db.close();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getActivity(), PokemonDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("pokemon", pokemon);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }).start();
            }
        });
    }

    @Override
    public void update() {
        adapter.notifyDataSetChanged();
    }
}
