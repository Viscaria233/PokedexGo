package com.haochen.pokedexgo.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.haochen.pokedexgo.ChargeSkillDetailActivity;
import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.adapter.ChargeSkillAdapter;
import com.haochen.pokedexgo.adapter.MyAdapter;
import com.haochen.pokedexgo.common.ChargeSkill;
import com.haochen.pokedexgo.common.Pokemon;
import com.haochen.pokedexgo.sqlite.DBHelper;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Haochen on 2016/7/29.
 */
public class ChargeSkillFragment extends MyFragment {

    protected List<ChargeSkill> data;
    protected MyAdapter<ChargeSkill> adapter;

    protected LinearLayout name;
    protected LinearLayout type;
    protected LinearLayout power;
    protected LinearLayout duration;
    protected LinearLayout skillBar;

    protected static final String TABLE = "charge_skill LEFT JOIN type ON type_id = type._id";
    protected static final String[] COLUMNS = {
            "charge_skill._id",
            "name_zh_cn",
            "name_en_us",
            "type.name",
            "power",
            "skill_bar",
            "damage_scalar",
            "heal_scalar",
            "duration",
            "damage_window",
            "ct"
    };

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_item_charge_skill, container, false);
    }

    @Override
    protected void initial() {
        listView = (ListView) view.findViewById(R.id.listView);

        name = (LinearLayout) view.findViewById(R.id.background_name);
        type = (LinearLayout) view.findViewById(R.id.background_type);
        power = (LinearLayout) view.findViewById(R.id.background_power);
        duration = (LinearLayout) view.findViewById(R.id.background_duration);
        skillBar = (LinearLayout) view.findViewById(R.id.background_skill_bar);

        checked = new HashMap<>();
        checked.put(name, false);
        checked.put(type, false);
        checked.put(power, false);
        checked.put(duration, false);
        checked.put(skillBar, false);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CHANGE_ADAPTER_DATA:
                        if (msg.obj instanceof List) {
                            adapter.setData((List) msg.obj);
                        } else if (msg.obj instanceof Cursor) {
                            adapter.setData((Cursor) msg.obj);
                        }
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ChargeSkill skill = (ChargeSkill) adapter.getItem(position);
                        Intent intent = new Intent(getActivity(), ChargeSkillDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("charge_skill", skill);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }).start();
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Message msg = new Message();
                msg.what = CHANGE_ADAPTER_DATA;

                if (checked.get(v)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            msg.obj = reverse(adapter.getData());
                            handler.sendMessage(msg);
                        }
                    }).start();
                } else {
                    resetChecked();
                    setChecked(v, true);
                    final Resources r = getResources();
                    final Collator collator = Collator.getInstance(r.getConfiguration().locale);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            msg.obj = sort(adapter.getData(), new Comparator<ChargeSkill>() {
                                @Override
                                public int compare(ChargeSkill lhs, ChargeSkill rhs) {
                                    return collator.compare(lhs.getName(), rhs.getName());
                                }
                            });
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Message msg = new Message();
                msg.what = CHANGE_ADAPTER_DATA;

                if (checked.get(v)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            msg.obj = reverse(adapter.getData());
                            handler.sendMessage(msg);
                        }
                    }).start();
                } else {
                    resetChecked();
                    setChecked(v, true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            msg.obj = sort(adapter.getData(), new Comparator<ChargeSkill>() {
                                @Override
                                public int compare(ChargeSkill lhs, ChargeSkill rhs) {
                                    return lhs.getTypeId() - rhs.getTypeId();
                                }
                            });
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Message msg = new Message();
                msg.what = CHANGE_ADAPTER_DATA;

                if (checked.get(v)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            msg.obj = reverse(adapter.getData());
                            handler.sendMessage(msg);
                        }
                    }).start();
                } else {
                    resetChecked();
                    setChecked(v, true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            msg.obj = sort(adapter.getData(), new Comparator<ChargeSkill>() {
                                @Override
                                public int compare(ChargeSkill lhs, ChargeSkill rhs) {
                                    int left = Integer.parseInt(lhs.getPower());
                                    int right = Integer.parseInt(rhs.getPower());
                                    return right - left;
                                }
                            });
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Message msg = new Message();
                msg.what = CHANGE_ADAPTER_DATA;

                if (checked.get(v)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            msg.obj = reverse(adapter.getData());
                            handler.sendMessage(msg);
                        }
                    }).start();
                } else {
                    resetChecked();
                    setChecked(v, true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            msg.obj = sort(adapter.getData(), new Comparator<ChargeSkill>() {
                                @Override
                                public int compare(ChargeSkill lhs, ChargeSkill rhs) {
                                    int left = Integer.parseInt(lhs.getDuration());
                                    int right = Integer.parseInt(rhs.getDuration());
                                    return left - right;
                                }
                            });
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
        skillBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Message msg = new Message();
                msg.what = CHANGE_ADAPTER_DATA;

                if (checked.get(v)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            msg.obj = reverse(adapter.getData());
                            handler.sendMessage(msg);
                        }
                    }).start();
                } else {
                    resetChecked();
                    setChecked(v, true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            msg.obj = sort(adapter.getData(), new Comparator<ChargeSkill>() {
                                @Override
                                public int compare(ChargeSkill lhs, ChargeSkill rhs) {
                                    int left = Integer.parseInt(lhs.getSkillBar());
                                    int right = Integer.parseInt(rhs.getSkillBar());
                                    return right - left;
                                }
                            });
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
    }

    @Override
    protected void show() {
        DBHelper helper = DBHelper.getInstance(null);
        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT charge_skill.name, name_zh_cn, name_en_us, type.name," +
//                " power, skill_bar," +
//                "damage_scalar, heal_scalar, duration, damage_window, ct" +
//                " FROM charge_skill LEFT JOIN type" +
//                " ON type_id = type._id", null);
        Cursor cursor = db.query(TABLE, COLUMNS, null, null, null, null, null);
        adapter = new ChargeSkillAdapter(getActivity(), cursor);
        cursor.close();
        db.close();
        listView.setAdapter(adapter);
        data = adapter.getData();
    }

    @Override
    public void update() {
        adapter.notifyDataSetChanged();
        float sizePx = view.getResources().getDimension(R.dimen.header_text_size);

        TextView textView = (TextView) view.findViewById(R.id.textView_name);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx);
        textView.setText(R.string.name);

        textView = (TextView) view.findViewById(R.id.textView_type);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx);
        textView.setText(R.string.type);

        textView = (TextView) view.findViewById(R.id.textView_power);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx);
        textView.setText(R.string.power);

        textView = (TextView) view.findViewById(R.id.textView_duration);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx);
        textView.setText(R.string.duration);

        textView = (TextView) view.findViewById(R.id.textView_skill_bar);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx);
        textView.setText(R.string.skill_bar);
    }

    @Override
    public void search(String key) {
        super.search(key);
        List<ChargeSkill> data;
        if (key == null || key.equals("")) {
            data = this.data;
        } else {
            data = new ArrayList<>();
            for (ChargeSkill item : this.data) {
                if (item.getName().toLowerCase().contains(key.toLowerCase())) {
                    data.add(item);
                }
            }
        }

        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }
}
