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

import com.haochen.pokedexgo.PokemonDetailActivity;
import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.adapter.MyAdapter;
import com.haochen.pokedexgo.adapter.PokemonAdapter;
import com.haochen.pokedexgo.common.Pokemon;
import com.haochen.pokedexgo.sqlite.DBHelper;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Haochen on 2016/7/22.
 */
public class PokemonFragment extends MyFragment {

    protected List<Pokemon> data;
    protected MyAdapter<Pokemon> adapter;

    protected LinearLayout no;
    protected LinearLayout name;
    protected LinearLayout hp;
    protected LinearLayout atk;
    protected LinearLayout def;
    protected LinearLayout sum;
            
    protected static final String TABLE = "type t1, pokemon LEFT JOIN type t2 ON type_id_2 = t2._id";
    protected static final String[] COLUMNS = {
            "pokemon._id",
            "name_zh_cn",
            "name_en_us",
            "t1.name",
            "t2.name",
            "hp",
            "atk",
            "def",
            "sum",
            "catch",
            "run",
            "incubate"
    };
    protected static final String ORDER = "pokemon._id ASC";

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_item_pokemon, container, false);
    }

    @Override
    protected void initial() {
        listView = (ListView) view.findViewById(R.id.listView);

        no = (LinearLayout) view.findViewById(R.id.background_no);
        name = (LinearLayout) view.findViewById(R.id.background_name);
        hp = (LinearLayout) view.findViewById(R.id.background_hp);
        atk = (LinearLayout) view.findViewById(R.id.background_atk);
        def = (LinearLayout) view.findViewById(R.id.background_def);
        sum = (LinearLayout) view.findViewById(R.id.background_sum);

        checked = new HashMap<>();
        checked.put(no, false);
        checked.put(name, false);
        checked.put(hp, false);
        checked.put(atk, false);
        checked.put(def, false);
        checked.put(sum, false);

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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Pokemon pokemon = (Pokemon) adapter.getItem(pos);
                        Intent intent = new Intent(getActivity(), PokemonDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("pokemon", pokemon);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }).start();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
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
                            msg.obj = sort(adapter.getData(), new Comparator<Pokemon>() {
                                @Override
                                public int compare(Pokemon lhs, Pokemon rhs) {
                                    return lhs.getId().compareTo(rhs.getId());
                                }
                            });
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
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
                            msg.obj = sort(adapter.getData(), new Comparator<Pokemon>() {
                                @Override
                                public int compare(Pokemon lhs, Pokemon rhs) {
                                    return collator.compare(lhs.getName(), rhs.getName());
                                }
                            });
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
        hp.setOnClickListener(new View.OnClickListener() {
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
                            msg.obj = sort(adapter.getData(), new Comparator<Pokemon>() {
                                @Override
                                public int compare(Pokemon lhs, Pokemon rhs) {
                                    int left = Integer.parseInt(lhs.getHp());
                                    int right = Integer.parseInt(rhs.getHp());
                                    return right - left;
                                }
                            });
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
        atk.setOnClickListener(new View.OnClickListener() {
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
                            msg.obj = sort(adapter.getData(), new Comparator<Pokemon>() {
                                @Override
                                public int compare(Pokemon lhs, Pokemon rhs) {
                                    int left = Integer.parseInt(lhs.getAtk());
                                    int right = Integer.parseInt(rhs.getAtk());
                                    return right - left;
                                }
                            });
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
        def.setOnClickListener(new View.OnClickListener() {
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
                            msg.obj = sort(adapter.getData(), new Comparator<Pokemon>() {
                                @Override
                                public int compare(Pokemon lhs, Pokemon rhs) {
                                    int left = Integer.parseInt(lhs.getDef());
                                    int right = Integer.parseInt(rhs.getDef());
                                    return right - left;
                                }
                            });
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
        sum.setOnClickListener(new View.OnClickListener() {
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
                            msg.obj = sort(adapter.getData(), new Comparator<Pokemon>() {
                                @Override
                                public int compare(Pokemon lhs, Pokemon rhs) {
                                    int left = Integer.parseInt(lhs.getSum());
                                    int right = Integer.parseInt(rhs.getSum());
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
//        Cursor cursor = db.rawQuery("SELECT pokemon._id, type1.name, type2.name," +
//                "hp, atk, def, sum, catch, run, incubate" +
//                " FROM type type1, pokemon LEFT JOIN type type2" +
//                "   ON type_id_2 = type2._id" +
//                "   WHERE type_id_1 = type1._id" +
//                "   ORDER BY pokemon._id ASC", null);
        Cursor cursor = db.query(TABLE, COLUMNS, "type_id_1 = t1._id", null, null, null, ORDER);
        adapter = new PokemonAdapter(getActivity(), cursor);
        cursor.close();
        db.close();
        listView.setAdapter(adapter);
        data = adapter.getData();
    }

    @Override
    public void update() {
        adapter.notifyDataSetChanged();
        resetChecked();
        float sizePx = getActivity().getResources().getDimension(R.dimen.header_text_size);

        TextView textView = (TextView) view.findViewById(R.id.textView_no);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx);
        textView.setText(R.string.no);

        textView = (TextView) view.findViewById(R.id.textView_name);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx);
        textView.setText(R.string.name);

        textView = (TextView) view.findViewById(R.id.textView_hp);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx);
        textView.setText(R.string.hp);

        textView = (TextView) view.findViewById(R.id.textView_atk);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx);
        textView.setText(R.string.atk);

        textView = (TextView) view.findViewById(R.id.textView_def);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx);
        textView.setText(R.string.def);

        textView = (TextView) view.findViewById(R.id.textView_sum);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx);
        textView.setText(R.string.sum);
    }

    @Override
    public void search(String key) {
        super.search(key);
        List<Pokemon> data;
        if (key == null || key.equals("")) {
            data = this.data;
        } else {
            data = new ArrayList<>();
            for (Pokemon item : this.data) {
                if (item.getName().toLowerCase().contains(key.toLowerCase())
                        || item.getId().matches("[0]*" + key)
                        || key.matches("[0]*" + item.getId())) {
                    data.add(item);
                }
            }
        }

        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }
}
