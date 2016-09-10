package com.haochen.pokedexgo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.haochen.pokedexgo.common.Pokemon;
import com.haochen.pokedexgo.fragment.EvoFragment;
import com.haochen.pokedexgo.fragment.BasicSkillInPokemonDetailFragment;
import com.haochen.pokedexgo.fragment.ChargeSkillInPokemonDetailFragment;
import com.haochen.pokedexgo.sqlite.DBHelper;
import com.haochen.pokedexgo.util.ResUtil;
import com.haochen.pokedexgo.util.TypeUtil;

public class PokemonDetailActivity extends AppCompatActivity {

    private TextView id;
    private TextView name;
    private ImageView image;
    private LinearLayout[] typeFields;
    private TextView[] types;
    private TextView hpText;
    private TextView atkText;
    private TextView defText;
    private TextView sumText;
    private TextView hp;
    private TextView atk;
    private TextView def;
    private TextView sum;
    private TextView catchText;
    private TextView runText;
    private TextView incubateText;
    private TextView catchPercent;
    private TextView runPercent;
    private TextView incubate;

    private LinearLayout[] typeBgs;
    private TextView[] typeNames;
    private LinearLayout[] scalarBgs;
    private TextView[] scalars;

    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_pokemon_detail);

        id = (TextView) findViewById(R.id.textView_id);
        name = (TextView) findViewById(R.id.textView_name);
        image = (ImageView) findViewById(R.id.imageView);
        typeFields = new LinearLayout[2];
        types = new TextView[2];
        View type1 = findViewById(R.id.type_1);
        View type2 = findViewById(R.id.type_2);
        typeFields[0] = (LinearLayout) type1.findViewById(R.id.background);
        types[0] = (TextView) type1.findViewById(R.id.textView);
        typeFields[1] = (LinearLayout) type2.findViewById(R.id.background);
        types[1] = (TextView) type2.findViewById(R.id.textView);
        hpText = (TextView) findViewById(R.id.textView_hp_text);
        atkText = (TextView) findViewById(R.id.textView_atk_text);
        defText = (TextView) findViewById(R.id.textView_def_text);
        sumText = (TextView) findViewById(R.id.textView_sum_text);
        hp = (TextView) findViewById(R.id.textView_hp);
        atk = (TextView) findViewById(R.id.textView_atk);
        def = (TextView) findViewById(R.id.textView_def);
        sum = (TextView) findViewById(R.id.textView_sum);
        catchText = (TextView) findViewById(R.id.textView_catch_text);
        runText = (TextView) findViewById(R.id.textView_run_text);
        incubateText = (TextView) findViewById(R.id.textView_incubate_text);
        catchPercent = (TextView) findViewById(R.id.textView_catch);
        runPercent = (TextView) findViewById(R.id.textView_run);
        incubate = (TextView) findViewById(R.id.textView_incubate);

        Bundle bundle = getIntent().getExtras();
        Pokemon pokemon = (Pokemon) bundle.getSerializable("pokemon");
        id.setText(pokemon.getId());
        name.setText(pokemon.getName());
        image.setImageResource(pokemon.getImageResId());

        typeFields[1].setBackgroundColor(0x00000000);
        types[1].setText("");
        for (int i = 0; i < pokemon.getTypeSize(); ++i) {
            typeFields[i].setBackgroundResource(pokemon.getTypeFieldResId(i));
            types[i].setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.type_text_size));
            types[i].setText(pokemon.getTypeResId(i));
        }

        hpText.setText(ResUtil.getResId(R.string.class, "hp"));
        atkText.setText(ResUtil.getResId(R.string.class, "atk"));
        defText.setText(ResUtil.getResId(R.string.class, "def"));
        sumText.setText(ResUtil.getResId(R.string.class, "sum"));
        hp.setText(pokemon.getHp());
        atk.setText(pokemon.getAtk());
        def.setText(pokemon.getDef());
        sum.setText(pokemon.getSum());
        
        catchText.setText(R.string.catch_percent);
        runText.setText(R.string.run_percent);
        incubateText.setText(R.string.incubate);
        double catchP = Double.parseDouble(pokemon.getCatchPercent());
        double runP = Double.parseDouble(pokemon.getRunPercent());
        String format = "%.2f%%";
        catchPercent.setText(String.format(format, 100 * catchP));
        runPercent.setText(String.format(format, 100 * runP));
        if (pokemon.getIncubate() != null) {
            String text = pokemon.getIncubate() + getResources().getString(R.string.meter);
            incubate.setText(text);
        }

        DBHelper helper = DBHelper.getInstance(null);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM type", null);
        if (cursor.moveToNext()) {
            int typeNumber = cursor.getInt(0);
            cursor.close();
            typeBgs = new LinearLayout[typeNumber];
            scalarBgs = new LinearLayout[typeNumber];
            typeNames = new TextView[typeNumber];
            scalars = new TextView[typeNumber];
            for (int i = 0; i < typeNumber; ++i) {
                View typeView = findViewById(ResUtil.getResId(
                        R.id.class, "text_field_type_" + (i + 1)));
                View scalarView = findViewById(ResUtil.getResId(
                        R.id.class, "text_field_scalar_" + (i + 1)));

                typeBgs[i] = (LinearLayout) typeView.findViewById(R.id.background);
                typeNames[i] = (TextView) typeView.findViewById(R.id.textView);
                scalarBgs[i] = (LinearLayout) scalarView.findViewById(R.id.background);
                scalars[i] = (TextView) scalarView.findViewById(R.id.textView);

                typeBgs[i].setBackgroundResource(
                        ResUtil.getResId(R.drawable.class, TypeUtil.toTypeResName(i) + "_field"));
                typeNames[i].setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.type_text_size_small));
                typeNames[i].setText(
                        ResUtil.getResId(R.string.class, TypeUtil.toTypeResName(i)));
            }

            double[] sca = new double[typeNumber];
            for (int i = 0; i < sca.length; ++i) {
                sca[i] = 1;
            }
            for (int i = 0; i < pokemon.getTypeSize(); ++i) {
                cursor = db.rawQuery("SELECT atk, scalar FROM type_chart WHERE def = ?",
                        new String[]{"" + TypeUtil.toTypeId(pokemon.getTypeName(i))});
                while (cursor.moveToNext()) {
                    sca[cursor.getInt(0)] *= cursor.getDouble(1);
                }
                cursor.close();
            }

            for (int i = 0; i < sca.length; ++i) {
                double s = sca[i];
                int color = 0;
                if (s == 1) {
                    color = 0xff888888;
                } else if (s > 1) {
                    color = 0xffff0000;
                } else {
                    color = 0xff00ff00;
                }
                scalarBgs[i].setBackgroundColor(color);
                scalars[i].setText(String.format("%.2f", s));
            }
        }
        db.close();

        tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.tab_content);
        String[] tags = {"evo", "basic_skill", "charge_skill"};
        int[] titles = {R.string.evo, R.string.basic_skill, R.string.charge_skill};
        Class[] fragments = {EvoFragment.class, BasicSkillInPokemonDetailFragment.class, ChargeSkillInPokemonDetailFragment.class};
        Bundle arg = new Bundle();
        arg.putString("id", pokemon.getId());
        View view;
        for (int i = 0; i < tags.length; ++i) {
            view = LayoutInflater.from(this).inflate(R.layout.tab_button_top, null);
            TextView textView = (TextView) view.findViewById(R.id.textView);
            textView.setText(titles[i]);

            TabHost.TabSpec spec = tabHost.newTabSpec(tags[i]);
            spec.setIndicator(view);
            tabHost.addTab(spec, fragments[i], arg);
        }
    }
}
