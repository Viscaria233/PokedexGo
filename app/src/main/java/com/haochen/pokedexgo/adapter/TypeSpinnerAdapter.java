package com.haochen.pokedexgo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.common.BasicSkill;

import java.util.List;

/**
 * Created by Haochen on 2016/8/21.
 */
public class TypeSpinnerAdapter extends MyAdapter<String> {

    public TypeSpinnerAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public void setData(Cursor cursor) {}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_item_single_text_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.bg = (LinearLayout) convertView.findViewById(R.id.background);
            viewHolder.type = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String resName = data.get(position);
        if (resName != null) {
            BasicSkill skill = new BasicSkill();
            skill.setTypeResName(resName);
            viewHolder.bg.setBackgroundResource(skill.getTypeFieldResId());
            viewHolder.type.setText(skill.getTypeNameResId());
        }
        return convertView;
    }

    private static class ViewHolder {
        LinearLayout bg;
        TextView type;
    }
}
