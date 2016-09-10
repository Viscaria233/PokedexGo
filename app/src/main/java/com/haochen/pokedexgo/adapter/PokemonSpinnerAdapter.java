package com.haochen.pokedexgo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.common.Pokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Haochen on 2016/8/21.
 */
public class PokemonSpinnerAdapter extends DBAdapter<Pokemon> {

    public PokemonSpinnerAdapter(Context context, List<Pokemon> data) {
        super(context, data, "pokemon");
    }

    public PokemonSpinnerAdapter(Context context, Cursor cursor) {
        super(context, cursor, "pokemon");
    }

    @Override
    public void setData(Cursor cursor) {
        data = new ArrayList<>();
        Pokemon pokemon;
        while (cursor.moveToNext()) {
            pokemon = new Pokemon();
            pokemon.setId(cursor.getString(0));
            pokemon.addName(Locale.CHINA, cursor.getString(1));
            pokemon.addName(Locale.US, cursor.getString(2));
            data.add(pokemon);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_item_double_text_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.id = (TextView) convertView
                    .findViewById(R.id.text_field_1)
                    .findViewById(R.id.textView);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.text_field_2)
                    .findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Pokemon pokemon = data.get(position);
        viewHolder.id.setText(pokemon.getId());
        viewHolder.name.setText(pokemon.getName());
        return convertView;
    }

    private static class ViewHolder {
        TextView id;
        TextView name;
    }
}
