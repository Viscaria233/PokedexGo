package com.haochen.pokedexgo.fragment;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.haochen.pokedexgo.R;
import com.haochen.pokedexgo.adapter.AfterEvoAdapter;
import com.haochen.pokedexgo.adapter.MyAdapter;
import com.haochen.pokedexgo.adapter.PokemonSpinnerAdapter;
import com.haochen.pokedexgo.adapter.TypeSpinnerAdapter;
import com.haochen.pokedexgo.common.Pokemon;
import com.haochen.pokedexgo.sqlite.DBHelper;
import com.haochen.pokedexgo.util.TypeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Haochen on 2016/8/20.
 */
public class EditPokemonFragment extends BaseFragment {

    protected EditText no;
    protected EditText nameZhCN;
    protected EditText nameEnUS;
    protected Spinner type1;
    protected Spinner type2;
    protected EditText hp;
    protected EditText atk;
    protected EditText def;
    protected EditText catchPercent;
    protected EditText runPercent;
    protected Spinner selectBeforeEvo;
    protected EditText incubate;
    protected Spinner selectAfterEvo;
    protected Button addAfterEvo;
    protected ListView afterEvo;
    protected EditText candy;
    protected FloatingActionButton confirm;

//    protected MyAdapter<Pokemon> beforeEvoAdapter;
//    protected MyAdapter<Pokemon> afterEvoAdapter;
    protected MyAdapter<Pokemon> afterEvoAdapter;
    protected List<Pokemon> afterEvoData;


    protected static final String TABLE = "pokemon";
    protected static final String[] COLUMNS = {
            "_id",
            "name_zh_cn",
            "name_en_us"
    };
    protected static final String ORDER = "_id ASC";

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_pokemon, container, false);
    }

    @Override
    protected void initial() {
        initialComponents();
//        initialData();
        no.setEnabled(false);
    }

    protected void initialComponents() {
        LinearLayout no = (LinearLayout) view.findViewById(R.id.no);
        LinearLayout nameZhCN = (LinearLayout) view.findViewById(R.id.name_zh_cn);
        LinearLayout nameEnUS = (LinearLayout) view.findViewById(R.id.name_en_us);
        LinearLayout type1 = (LinearLayout) view.findViewById(R.id.type_1);
        LinearLayout type2 = (LinearLayout) view.findViewById(R.id.type_2);
        LinearLayout hp = (LinearLayout) view.findViewById(R.id.hp);
        LinearLayout atk = (LinearLayout) view.findViewById(R.id.atk);
        LinearLayout def = (LinearLayout) view.findViewById(R.id.def);
        LinearLayout catchPercent = (LinearLayout) view.findViewById(R.id.catch_percent);
        LinearLayout runPercent = (LinearLayout) view.findViewById(R.id.run_percent);
        LinearLayout selectBeforeEvo = (LinearLayout) view.findViewById(R.id.select_before_evo);
        LinearLayout incubateOrCandy = (LinearLayout) view.findViewById(R.id.incubate_or_candy);
        LinearLayout selectAfterEvo = (LinearLayout) view.findViewById(R.id.select_after_evo);
        LinearLayout candy = (LinearLayout) view.findViewById(R.id.candy);
        confirm = (FloatingActionButton) view.findViewById(R.id.confirm);

        this.no = (EditText) no.findViewById(R.id.editText);
        this.nameZhCN = (EditText) nameZhCN.findViewById(R.id.editText);
        this.nameEnUS = (EditText) nameEnUS.findViewById(R.id.editText);
        this.type1 = (Spinner) type1.findViewById(R.id.spinner);
        this.type2 = (Spinner) type2.findViewById(R.id.spinner);
        this.hp = (EditText) hp.findViewById(R.id.editText);
        this.atk = (EditText) atk.findViewById(R.id.editText);
        this.def = (EditText) def.findViewById(R.id.editText);
        this.catchPercent = (EditText) catchPercent.findViewById(R.id.editText);
        this.runPercent = (EditText) runPercent.findViewById(R.id.editText);
        this.selectBeforeEvo = (Spinner) selectBeforeEvo.findViewById(R.id.spinner);
        this.incubate = (EditText) incubateOrCandy.findViewById(R.id.editText);
        this.selectAfterEvo = (Spinner) selectAfterEvo.findViewById(R.id.spinner);
        this.addAfterEvo = (Button) view.findViewById(R.id.button_add_after_evo);
        this.afterEvo = (ListView) view.findViewById(R.id.listView_after_evo);
        this.candy = (EditText) candy.findViewById(R.id.editText);

        Resources r = getResources();

        ((TextView) no.findViewById(R.id.textView)).setText(R.string.no);
        ((TextView) nameZhCN.findViewById(R.id.textView)).setText(R.string.name_zh_cn);
        ((TextView) nameEnUS.findViewById(R.id.textView)).setText(R.string.name_en_us);
        String type = r.getString(R.string.type);
        ((TextView) type1.findViewById(R.id.textView)).setText(type + " 1");
        ((TextView) type2.findViewById(R.id.textView)).setText(type + " 2");
        ((TextView) hp.findViewById(R.id.textView)).setText(R.string.hp);
        ((TextView) atk.findViewById(R.id.textView)).setText(R.string.atk);
        ((TextView) def.findViewById(R.id.textView)).setText(R.string.def);
        ((TextView) catchPercent.findViewById(R.id.textView)).setText(R.string.catch_percent);
        ((TextView) runPercent.findViewById(R.id.textView)).setText(R.string.run_percent);
        ((TextView) selectBeforeEvo.findViewById(R.id.textView)).setText(R.string.before_evo);
        ((TextView) incubateOrCandy.findViewById(R.id.textView)).setText(R.string.incubate);
        ((TextView) selectAfterEvo.findViewById(R.id.textView)).setText(R.string.after_evo);
        ((TextView) candy.findViewById(R.id.textView)).setText(R.string.candy);

        this.hp.setInputType(InputType.TYPE_CLASS_NUMBER);
        this.atk.setInputType(InputType.TYPE_CLASS_NUMBER);
        this.def.setInputType(InputType.TYPE_CLASS_NUMBER);
        this.incubate.setInputType(InputType.TYPE_CLASS_NUMBER);
        this.candy.setInputType(InputType.TYPE_CLASS_NUMBER);

        this.catchPercent.setInputType(
                InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        this.runPercent.setInputType(
                InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        String[] names = TypeUtil.getTypeResNameArray();
        ArrayList<String> types1 = new ArrayList<>();
        ArrayList<String> types2 = new ArrayList<>();
        types2.add(null);
        for (int i = 0; i < names.length; ++i) {
            types1.add(names[i]);
            types2.add(names[i]);
        }
        this.type1.setAdapter(new TypeSpinnerAdapter(getActivity(), types1));
        this.type2.setAdapter(new TypeSpinnerAdapter(getActivity(), types2));

        DBHelper helper = DBHelper.getInstance(null);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;
        cursor = db.query(TABLE, COLUMNS, null, null, null, null, ORDER);
        MyAdapter<Pokemon> selectBeforeEvoAdapter = new PokemonSpinnerAdapter(getActivity(), cursor);
        selectBeforeEvoAdapter.getData().add(0, new Pokemon());
        cursor.close();
        cursor = db.query(TABLE, COLUMNS, "before_evo_pm_id IS NULL", null, null, null, ORDER);
        MyAdapter<Pokemon> selectAfterEvoAdapter = new PokemonSpinnerAdapter(getActivity(), cursor);
        selectAfterEvoAdapter.getData().add(0, new Pokemon());
        cursor.close();
        db.close();

        this.selectBeforeEvo.setAdapter(selectBeforeEvoAdapter);
        this.selectAfterEvo.setAdapter(selectAfterEvoAdapter);

        LinearLayout header = (LinearLayout) view.findViewById(R.id.header);
        ((TextView) header.findViewById(R.id.text_field_1).findViewById(R.id.textView)).setText(R.string.no);
        ((TextView) header.findViewById(R.id.text_field_2).findViewById(R.id.textView)).setText(R.string.name);
        ((TextView) header.findViewById(R.id.text_field_3).findViewById(R.id.textView)).setText(R.string.candy);

        this.afterEvoData = new ArrayList<>();
        this.afterEvoAdapter = new AfterEvoAdapter(getActivity(), afterEvoData);
        afterEvo.setAdapter(afterEvoAdapter);
    }

    protected void initialData() {
        Bundle bundle = getArguments();
        Pokemon pokemon = (Pokemon) bundle.getSerializable("pokemon");
        String beforeEvoPmId = bundle.getString("before_evo_pm_id");
        int candy = bundle.getInt("candy");

        no.setText(pokemon.getId());
        nameZhCN.setText(pokemon.getName(Locale.CHINA));
        nameEnUS.setText(pokemon.getName(Locale.US));
        type1.setSelection(TypeUtil.toTypeId(pokemon.getTypeName(0)) - 1);
        type2.setSelection(TypeUtil.toTypeId(pokemon.getTypeName(1)));
        hp.setText(pokemon.getHp());
        atk.setText(pokemon.getAtk());
        def.setText(pokemon.getDef());
        catchPercent.setText(pokemon.getCatchPercent().replace("%", ""));
        runPercent.setText(pokemon.getRunPercent().replace("%", ""));
        selectBeforeEvo.setSelection(Integer.parseInt(pokemon.getId()));
        incubate.setText(pokemon.getId() == null ? pokemon.getIncubate() : "" + candy);

        DBHelper helper = DBHelper.getInstance(null);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, name_zh_cn, name_en_us, candy" +
                        "              FROM pokemon" +
                        "              WHERE before_evo_pm_id = ?", new String[]{pokemon.getId()});
        Pokemon p;
        while (cursor.moveToNext()) {
            p = new Pokemon();
            p.setId(cursor.getString(0));
            p.addName(Locale.CHINA, cursor.getString(1));
            p.addName(Locale.US, cursor.getString(2));
            p.setCandy(cursor.getString(3));
            afterEvoData.add(p);
        }
        db.close();
        afterEvoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void show() {
        addAfterEvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pokemon pokemon = (Pokemon) selectAfterEvo.getSelectedItem();
                String c = candy.getText().toString();
                int error = 0;
                if (pokemon.getId() == null) {
                    error = R.string.error_empty_after_evo;
                } else if ("".equals(c)) {
                    error = R.string.error_empty_candy;
                } else if (afterEvoData.contains(pokemon)) {
                    error = R.string.error_exists_after_evo;
                }

                if (error != 0) {
                    Snackbar.make(confirm, error, Snackbar.LENGTH_LONG).show();
                } else {
                    pokemon.setCandy(c);
                    afterEvoData.add(pokemon);
                    afterEvoAdapter.notifyDataSetChanged();
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int error = checkError();
                if (error != 0) {
                    Snackbar.make(confirm, error, Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(confirm, "DONE", Snackbar.LENGTH_LONG).show();

//                    int h = Integer.parseInt(hp.getText().toString());
//                    int a = Integer.parseInt(atk.getText().toString());
//                    int d = Integer.parseInt(def.getText().toString());
//                    int sum = h + a + d;
//                    String format = "%.2f%";
//                    DBHelper helper = DBHelper.getInstance(null);
//                    SQLiteDatabase db = helper.getWritableDatabase();
//                    db.execSQL("INSERT INTO pokemon VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
//                            new Object[] {
//                                    no.getText().toString(),
//                                    nameZhCN.getText().toString(),
//                                    nameEnUS.getText().toString(),
//                                    type1.getSelectedItem(),
//                                    "".equals(type2.getSelectedItem()) ? null : type2.getSelectedItem(),
//                                    h,
//                                    a,
//                                    d,
//                                    sum,
//                                    String.format(format, Double.parseDouble(catchPercent.getText().toString())),
//                                    String.format(format, Double.parseDouble(runPercent.getText().toString())),
//                                    "".equals(incubate.getText()) ? null : incubate.getText(),
//                                    "".equals(beforeEvo.getSelectedItem()) ? null : beforeEvo.getSelectedItem(),
//                                    candy.getText(),
//                            });
//                    db.close();
                }
            }
        });
    }

    /**
     * @return res ID of the error message
     */
    protected int checkError() {
        String noStr = no.getText().toString();
        String nameZhCNStr = nameZhCN.getText().toString();
        String nameEnUSStr = nameEnUS.getText().toString();

//        if ("".equals(noStr)) {
//            return R.string.error_empty_no;
//        }
        if ("".equals(nameZhCNStr)) {
            return R.string.error_empty_name_zh_cn;
        }
        if ("".equals(nameEnUSStr)) {
            return R.string.error_empty_name_en_us;
        }
        if ("".equals(hp.getText().toString())) {
            return R.string.error_empty_hp;
        }
        if ("".equals(atk.getText().toString())) {
            return R.string.error_empty_atk;
        }
        if ("".equals(def.getText().toString())) {
            return R.string.error_empty_def;
        }
        if ("".equals(catchPercent.getText().toString())) {
            return R.string.error_empty_catch;
        }
        if ("".equals(runPercent.getText().toString())) {
            return R.string.error_empty_run;
        }

        if (((Pokemon) selectBeforeEvo.getSelectedItem()).getId() == null) {
            if ("".equals(incubate.getText().toString())) {
                return R.string.error_empty_incubate;
            }
        }
//
//        if (!"".equals(afterEvo.getSelectedItem())) {
//            if ("".equals(candy.getText().toString())) {
//                return R.string.error_empty_candy;
//            }
//        }

        double n = Double.parseDouble(catchPercent.getText().toString());
        if (n < 0 || n > 100) {
            return R.string.error_invalid_catch;
        }
        n = Double.parseDouble(runPercent.getText().toString());
        if (n < 0 || n > 100) {
            return R.string.error_invalid_run;
        }

        int exists = checkExists();
        if (exists != 0) {
            return exists;
        }
        return 0;
    }

    protected int checkExists() {
        String noStr = no.getText().toString();
        String nameZhCNStr = nameZhCN.getText().toString();
        String nameEnUSStr = nameEnUS.getText().toString();

        DBHelper helper = DBHelper.getInstance(null);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;
//      cursor = db.rawQuery("SELECT * FROM pokemon WHERE _id = ?",
//                new String[]{noStr});
//        if (cursor.moveToNext()) {
//            cursor.close();
//            return R.string.error_exists_no;
//        }
//        cursor.close();
        cursor = db.rawQuery("SELECT * FROM pokemon WHERE name_zh_cn = ? AND _id <> ?",
                new String[]{nameZhCNStr, noStr});
        if (cursor.moveToNext()) {
            cursor.close();
            return R.string.error_exists_name_zh_cn;
        }
        cursor.close();
        cursor = db.rawQuery("SELECT * FROM pokemon WHERE name_en_us = ? AND _id <> ?",
                new String[]{nameEnUSStr, noStr});
        if (cursor.moveToNext()) {
            cursor.close();
            return R.string.error_exists_name_en_us;
        }
        cursor.close();
        db.close();
        return 0;
    }

    @Override
    public void update() {

    }
}
