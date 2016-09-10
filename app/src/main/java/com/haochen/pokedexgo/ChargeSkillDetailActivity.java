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

import com.haochen.pokedexgo.common.ChargeSkill;
import com.haochen.pokedexgo.fragment.BasicSkillInSkillDetailFragment;
import com.haochen.pokedexgo.fragment.ChargeSkillInSkillDetailFragment;
import com.haochen.pokedexgo.fragment.PokemonInChargeSkillDetailFragment;
import com.haochen.pokedexgo.sqlite.DBHelper;
import com.haochen.pokedexgo.util.ResUtil;
import com.haochen.pokedexgo.util.TypeUtil;

public class ChargeSkillDetailActivity extends AppCompatActivity {

    private TextView name;
    private LinearLayout typeBg;
    private TextView type;
    private TextView style;
    private TextView powerText;
    private TextView skillBarText;
    private TextView damageScalarText;
    private TextView healScalarText;
    private TextView durationText;
    private TextView damageWindowText;
    private TextView ctText;
    private TextView power;
    private TextView skillBar;
    private TextView damageScalar;
    private TextView healScalar;
    private TextView duration;
    private TextView damageWindow;
    private TextView ct;
    private TextView dpsText;
    private TextView dffeText;
    private TextView dps;
    private TextView dffe;

    private LinearLayout[] typeBgs;
    private TextView[] typeNames;
    private LinearLayout[] scalarBgs;
    private TextView[] scalars;

    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_skill_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_charge_skill_detail);

        name = (TextView) findViewById(R.id.textView_name);
        typeBg = (LinearLayout) findViewById(R.id.type_background);
        type = (TextView) findViewById(R.id.textView_type);
        style = (TextView) findViewById(R.id.textView_style);

        powerText = (TextView) findViewById(R.id.textView_power_text);
        skillBarText = (TextView) findViewById(R.id.textView_skill_bar_text);
        damageScalarText = (TextView) findViewById(R.id.textView_damage_scalar_text);
        healScalarText = (TextView) findViewById(R.id.textView_heal_scalar_text);
        durationText = (TextView) findViewById(R.id.textView_duration_text);
        damageWindowText = (TextView) findViewById(R.id.textView_damage_window_text);
        ctText = (TextView) findViewById(R.id.textView_ct_text);

        power = (TextView) findViewById(R.id.textView_power);
        skillBar = (TextView) findViewById(R.id.textView_skill_bar);
        damageScalar = (TextView) findViewById(R.id.textView_damage_scalar);
        healScalar = (TextView) findViewById(R.id.textView_heal_scalar);
        duration = (TextView) findViewById(R.id.textView_duration);
        damageWindow = (TextView) findViewById(R.id.textView_damage_window);
        ct = (TextView) findViewById(R.id.textView_ct);

        dpsText = (TextView) findViewById(R.id.textView_dps_text);
        dffeText = (TextView) findViewById(R.id.textView_dffe_text);
        dps = (TextView) findViewById(R.id.textView_dps);
        dffe = (TextView) findViewById(R.id.textView_dffe);

        Bundle bundle = getIntent().getExtras();
        ChargeSkill skill = (ChargeSkill) bundle.getSerializable("charge_skill");
        name.setText(skill.getName());
        typeBg.setBackgroundResource(skill.getTypeFieldResId());
        type.setText(skill.getTypeNameResId());
        style.setText(R.string.charge);

        powerText.setText(R.string.power);
        skillBarText.setText(R.string.skill_bar);
        damageScalarText.setText(R.string.damage_scalar);
        healScalarText.setText(R.string.heal_scalar);
        durationText.setText(R.string.duration);
        damageWindowText.setText(R.string.damage_window);
        ctText.setText(R.string.ct);
        
        power.setText(skill.getPower());
        skillBar.setText(skill.getSkillBar());
        damageScalar.setText(skill.getDamageScalar());
        healScalar.setText(skill.getHealScalar());
        duration.setText(skill.getDuration());
        damageWindow.setText(skill.getDamageWindow());
        double critical = Double.parseDouble(skill.getCt());
        String format = "%.2f%%";
        ct.setText(String.format(format, 100 * critical));

        dpsText.setText(R.string.dps);
        dffeText.setText(R.string.dffe);
        int pow = Integer.parseInt(skill.getPower());
        int dura = Integer.parseInt(skill.getDuration());
        int bar = Integer.parseInt(skill.getSkillBar());
        format = "%.2f";
        dps.setText(String.format(format, (pow * (1 + 0.5 * critical) / (dura / 1000.0))));
        dffe.setText(String.format(format, (pow * (1 + 0.5 * critical) * bar)));

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
                PokemonInChargeSkillDetailFragment.class,
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
