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
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.haochen.pokedexgo.common.BasicSkill;
import com.haochen.pokedexgo.fragment.BasicSkillInSkillDetailFragment;
import com.haochen.pokedexgo.fragment.ChargeSkillInSkillDetailFragment;
import com.haochen.pokedexgo.fragment.PokemonInBasicSkillDetailFragment;
import com.haochen.pokedexgo.sqlite.DBHelper;
import com.haochen.pokedexgo.util.ResUtil;
import com.haochen.pokedexgo.util.TypeUtil;

public class BasicSkillDetailActivity extends AppCompatActivity {

    private TextView name;
    private LinearLayout typeBg;
    private TextView type;
    private TextView style;
    private TextView powerText;
    private TextView durationText;
    private TextView energyText;
    private TextView power;
    private TextView duration;
    private TextView energy;
    private TextView dpsText;
    private TextView epsText;
    private TextView dps;
    private TextView eps;

    private LinearLayout[] typeBgs;
    private TextView[] typeNames;
    private LinearLayout[] scalarBgs;
    private TextView[] scalars;

    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_skill_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_basic_skill_detail);

        name = (TextView) findViewById(R.id.textView_name);
        typeBg = (LinearLayout) findViewById(R.id.type_background);
        type = (TextView) findViewById(R.id.textView_type);
        style = (TextView) findViewById(R.id.textView_style);

        powerText = (TextView) findViewById(R.id.textView_power_text);
        durationText = (TextView) findViewById(R.id.textView_duration_text);
        energyText = (TextView) findViewById(R.id.textView_energy_text);

        power = (TextView) findViewById(R.id.textView_power);
        duration = (TextView) findViewById(R.id.textView_duration);
        energy = (TextView) findViewById(R.id.textView_energy);

        dpsText = (TextView) findViewById(R.id.textView_dps_text);
        epsText = (TextView) findViewById(R.id.textView_eps_text);
        dps = (TextView) findViewById(R.id.textView_dps);
        eps = (TextView) findViewById(R.id.textView_eps);
        
        Bundle bundle = getIntent().getExtras();
        BasicSkill skill = (BasicSkill) bundle.getSerializable("basic_skill");
        name.setText(skill.getName());
        typeBg.setBackgroundResource(skill.getTypeFieldResId());
        type.setText(skill.getTypeNameResId());
        style.setText(R.string.basic);

        powerText.setText(R.string.power);
        durationText.setText(R.string.duration);
        energyText.setText(R.string.energy);
        power.setText(skill.getPower());
        duration.setText(skill.getDuration());
        energy.setText(skill.getEnergy());

        dpsText.setText(R.string.dps);
        epsText.setText(R.string.eps);
        int pow = Integer.parseInt(skill.getPower());
        int ener = Integer.parseInt(skill.getEnergy());
        int dura = Integer.parseInt(skill.getDuration());
        String format = "%.2f";
        dps.setText(String.format(format, pow / (dura / 1000.0)));
        eps.setText(String.format(format, ener / (dura / 1000.0)));

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
            cursor = db.rawQuery("SELECT def, scalar FROM type_chart WHERE atk = ?",
                    new String[]{"" + TypeUtil.toTypeId(skill.getTypeResName())});
            while (cursor.moveToNext()) {
                sca[cursor.getInt(0)] = cursor.getDouble(1);
            }
            cursor.close();

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

        String[] tags = {"pokemon", "same_type_basic_skill", "same_type_charge_skill"};
        int[] titles = {
                R.string.pokemon_with_this_skill,
                R.string.same_type_basic_skill,
                R.string.same_type_charge_skill
        };
        Class[] fragments = {
                PokemonInBasicSkillDetailFragment.class,
                BasicSkillInSkillDetailFragment.class,
                ChargeSkillInSkillDetailFragment.class
        };
        Bundle skillBundle = new Bundle();
        skillBundle.putString("id", skill.getId());
        Bundle typeBundle = new Bundle();
        typeBundle.putInt("id", skill.getTypeId());
        Bundle[] args = {skillBundle, typeBundle, typeBundle};
        for (int i = 0; i < tags.length; ++i) {
            View view = LayoutInflater.from(this).inflate(R.layout.tab_button_top, null);
            TextView textView = (TextView) view.findViewById(R.id.textView);
            textView.setText(titles[i]);

            TabHost.TabSpec spec = tabHost.newTabSpec(tags[i]);
            spec.setIndicator(view);
            tabHost.addTab(spec, fragments[i], args[i]);
        }
    }

}
