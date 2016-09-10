package com.haochen.pokedexgo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.common.Pokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Haochen on 2016/7/20.
 */
public class PokemonAdapter extends DBAdapter<Pokemon> {

    public PokemonAdapter(Context context, List<Pokemon> data) {
        super(context, data, "pokemon");
    }

    public PokemonAdapter(Context context, Cursor cursor) {
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
            pokemon.addType(cursor.getString(3));
            String type2 = cursor.getString(4);
            if (type2 != null) {
                pokemon.addType(type2);
            }

            pokemon.setHp(cursor.getString(5));
            pokemon.setAtk(cursor.getString(6));
            pokemon.setDef(cursor.getString(7));
            pokemon.setSum(cursor.getString(8));
            pokemon.setCatchPercent(cursor.getString(9));
            pokemon.setRunPercent(cursor.getString(10));
            pokemon.setIncubate(cursor.getString(11));
            data.add(pokemon);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.adapter_item_pokemon, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.id = (TextView) convertView.findViewById(R.id.textView_id);
            viewHolder.name = (TextView) convertView.findViewById(R.id.textView_name);
            viewHolder.typeBg = new LinearLayout[2];
            viewHolder.typeBg[0] = (LinearLayout) convertView.findViewById(R.id.type_background1);
            viewHolder.typeBg[1] = (LinearLayout) convertView.findViewById(R.id.type_background2);
            viewHolder.types = new TextView[2];
            viewHolder.types[0] = (TextView) convertView.findViewById(R.id.textView_type1);
            viewHolder.types[1] = (TextView) convertView.findViewById(R.id.textView_type2);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.hp = (TextView) convertView.findViewById(R.id.textView_hp);
            viewHolder.atk = (TextView) convertView.findViewById(R.id.textView_atk);
            viewHolder.def = (TextView) convertView.findViewById(R.id.textView_def);
            viewHolder.sum = (TextView) convertView.findViewById(R.id.textView_sum);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.typeBg[1].setBackgroundResource(0);
        viewHolder.types[1].setText(null);

        Pokemon pokemon = data.get(position);
        viewHolder.id.setText(pokemon.getId());
        viewHolder.name.setText(pokemon.getName());
        for (int i = 0; i < pokemon.getTypeSize(); ++i) {
            viewHolder.typeBg[i].setBackgroundResource(pokemon.getTypeFieldResId(i));
            viewHolder.types[i].setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen.type_text_size));
            viewHolder.types[i].setText(pokemon.getTypeResId(i));
        }
        viewHolder.icon.setBackgroundResource(pokemon.getIconResId());
        viewHolder.hp.setText(pokemon.getHp());
        viewHolder.atk.setText(pokemon.getAtk());
        viewHolder.def.setText(pokemon.getDef());
        viewHolder.sum.setText(pokemon.getSum());
        return convertView;
    }

    private static class ViewHolder {
        TextView id;
        TextView name;
        LinearLayout[] typeBg;
        TextView[] types;
        ImageView icon;
        TextView hp;
        TextView atk;
        TextView def;
        TextView sum;
    }

}
