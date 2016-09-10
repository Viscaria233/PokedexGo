package com.haochen.pokedexgo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.common.Pokemon;
import com.haochen.pokedexgo.sqlite.DBHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

/**
 * Created by Haochen on 2016/8/3.
 */
public class EvoAdapter extends BaseAdapter {

    private Context context;
    private List<EvoInfo> list;

    public EvoAdapter(Context context, String pokemonId) {
        this.context = context;
        list = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(null);
        SQLiteDatabase db = helper.getReadableDatabase();

        //找到进化链中最低段精灵的id，存入pmId
        String pmId = pokemonId;
        Cursor cursor;
        while (true) {
            cursor = db.rawQuery("SELECT before_evo_pm_id FROM pokemon WHERE _id = ?",
                    new String[]{pmId == null ? "" : pmId});
            if (cursor.moveToNext()) {
                String s = cursor.getString(0);
                cursor.close();
                if (s != null) {
                    pmId = s;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        cursor = db.rawQuery("SELECT name_zh_cn, name_en_us FROM pokemon WHERE _id = ?",
                new String[]{pmId});
        cursor.moveToNext();
        //从最低段开始，搜索进化链中所有精灵，依次存入list
        Queue<EvoInfo> queue = new LinkedList<>();
        EvoInfo info = new EvoInfo();
        info.pokemon.setId(pmId);
        info.pokemon.addName(Locale.CHINA, cursor.getString(0));
        info.pokemon.addName(Locale.US, cursor.getString(1));
        queue.offer(info);
        int level = 0;
        queue.offer(null);
        while (!queue.isEmpty()) {
            info = queue.poll();
            if (info == null) {
                if (!queue.isEmpty()) {
                    ++level;
                    queue.offer(null);
                    continue;
                } else {
                    break;
                }
            }
            info.level = level;
            list.add(info);
            cursor = db.rawQuery("SELECT _id, name_zh_cn, name_en_us, candy" +
                            " FROM pokemon WHERE before_evo_pm_id = ?",
                    new String[]{info.getId()});
            EvoInfo temp;
            while (cursor.moveToNext()) {
                temp = new EvoInfo();
                temp.pokemon.setId(cursor.getString(0));
                temp.pokemon.addName(Locale.CHINA, cursor.getString(1));
                temp.pokemon.addName(Locale.US, cursor.getString(2));
                temp.candy = cursor.getInt(3);
                queue.offer(temp);
            }
            cursor.close();
        }
        db.close();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EvoInfo info = list.get(position);
        switch (info.level) {
            case 0: {
                convertView = LayoutInflater.from(context)
                        .inflate(R.layout.adapter_item_evo_0, parent, false);
                TextView candy = (TextView) convertView.findViewById(R.id.textView_candy);
                if (position + 1 < list.size()) {
                    candy.setText("" + list.get(position + 1).candy);
                }
            }
                break;
            case 1: {
                convertView = LayoutInflater.from(context)
                        .inflate(R.layout.adapter_item_evo_1, parent, false);
                TextView candy = (TextView) convertView.findViewById(R.id.textView_candy);
                candy.setText("" + info.candy);
            }
                break;
            case 2: {
                convertView = LayoutInflater.from(context)
                        .inflate(R.layout.adapter_item_evo_2, parent, false);
                TextView candy = (TextView) convertView.findViewById(R.id.textView_candy);
                candy.setText("" + info.candy);
            }
                break;
        }

        TextView candyText = (TextView) convertView.findViewById(R.id.textView_candy_text);
        candyText.setText(R.string.candy);

        ImageView icon = (ImageView) convertView.findViewById(R.id.imageView_icon);
        TextView id = (TextView) convertView.findViewById(R.id.textView_id);
        TextView name = (TextView) convertView.findViewById(R.id.textView_name);
        icon.setImageResource(info.pokemon.getIconResId());
        id.setText(info.getId());
        name.setText(info.getName());

        return convertView;
    }

    public static class EvoInfo {
        Pokemon pokemon = new Pokemon();
        int level;
        int candy;

        public String getId() {
            return pokemon.getId();
        }

        public String getName() {
            return pokemon.getName();
        }

        public int getLevel() {
            return level;
        }

        public int getCandy() {
            return candy;
        }
    }
}
