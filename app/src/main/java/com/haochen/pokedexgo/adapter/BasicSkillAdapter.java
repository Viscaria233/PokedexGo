package com.haochen.pokedexgo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.common.BasicSkill;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Haochen on 2016/7/29.
 */
public class BasicSkillAdapter extends DBAdapter<BasicSkill> {
    public BasicSkillAdapter(Context context, List<BasicSkill> data) {
        super(context, data, "basic_skill");
    }

    public BasicSkillAdapter(Context context, Cursor cursor) {
        super(context, cursor, "basic_skill");
    }

    @Override
    public void setData(Cursor cursor) {
        data = new ArrayList<>();
        BasicSkill skill;
        while (cursor.moveToNext()) {
            skill = new BasicSkill();
            skill.setId(cursor.getString(0));
            skill.addName(Locale.CHINA, cursor.getString(1));
            skill.addName(Locale.US, cursor.getString(2));
            skill.setTypeResName(cursor.getString(3));
            skill.setPower(cursor.getString(4));
            skill.setDuration(cursor.getString(5));
            skill.setEnergy(cursor.getString(6));
            data.add(skill);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.adapter_item_basic_skill, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.textView_name);
            viewHolder.typeBg = (LinearLayout) convertView.findViewById(R.id.type_background);
            viewHolder.type = (TextView) convertView.findViewById(R.id.textView_type);
            viewHolder.power = (TextView) convertView.findViewById(R.id.textView_power);
            viewHolder.duration = (TextView) convertView.findViewById(R.id.textView_duration);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BasicSkill skill = data.get(position);
        viewHolder.name.setText(skill.getName());
        viewHolder.typeBg.setBackgroundResource(skill.getTypeFieldResId());
        viewHolder.type.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                context.getResources().getDimension(R.dimen.type_text_size));
        viewHolder.type.setText(skill.getTypeNameResId());
        viewHolder.power.setText(skill.getPower());
        viewHolder.duration.setText(skill.getDuration());
        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        LinearLayout typeBg;
        TextView type;
        TextView power;
        TextView duration;
    }
}
