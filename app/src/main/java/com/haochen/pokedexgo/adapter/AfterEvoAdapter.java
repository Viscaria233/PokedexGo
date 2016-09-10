package com.haochen.pokedexgo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.common.Pokemon;

import java.util.List;

/**
 * Created by Haochen on 2016/8/23.
 */
public class AfterEvoAdapter extends MyAdapter<Pokemon> {
    public AfterEvoAdapter(Context context, List<Pokemon> data) {
        super(context, data);
    }

    public AfterEvoAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void setData(Cursor cursor) {}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_item_triple_text_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.id = (TextView) convertView
                    .findViewById(R.id.text_field_1).findViewById(R.id.textView);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.text_field_2).findViewById(R.id.textView);
            viewHolder.candy = (TextView) convertView
                    .findViewById(R.id.text_field_3).findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Pokemon pokemon = data.get(position);
        viewHolder.id.setText(pokemon.getId());
        viewHolder.name.setText(pokemon.getName());
        viewHolder.candy.setText(pokemon.getCandy());
        return convertView;
    }

    private static class ViewHolder {
        TextView id;
        TextView name;
        TextView candy;
    }
}
