package com.haochen.pokedexgo;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.haochen.pokedexgo.common.Config;
import com.haochen.pokedexgo.common.Request;
import com.haochen.pokedexgo.fragment.BasicSkillFragment;
import com.haochen.pokedexgo.fragment.ChargeSkillFragment;
import com.haochen.pokedexgo.fragment.ISearch;
import com.haochen.pokedexgo.fragment.BaseFragment;
import com.haochen.pokedexgo.fragment.MyFragment;
import com.haochen.pokedexgo.fragment.PokemonFragment;
import com.haochen.pokedexgo.sqlite.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentTabHost tabHost;
    private List<View> tabs;
    private List<Integer> titleIds;

    private TextView searchText;
    private ImageButton clear;
    private ImageButton search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        printData();
        initialTab();
        initialSearch();
    }

    private void initialSearch() {
        searchText = (TextView) findViewById(R.id.textView_search);
        clear = (ImageButton) findViewById(R.id.clear_search);
        search = (ImageButton) findViewById(R.id.search);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                MyFragment fragment = (MyFragment) fm.findFragmentByTag(tabHost.getCurrentTabTag());
                String key = searchText.getText().toString();
                fragment.search(key == null ? "" : key);
            }
        });
    }

    private void initialTab() {
        tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.tab_content);
        tabs = new ArrayList<>();
        titleIds = new ArrayList<>();

        String[] tags = {"pokemon", "basic_skill", "charge_skill"};
        int[] titles = {
                R.string.pokemon,
                R.string.basic_skill,
                R.string.charge_skill
        };
        int[] icons = {
                R.drawable.ball_colour,
                R.drawable.flash,
                R.drawable.fire
        };
        Class[] fragments = {
                PokemonFragment.class,
                BasicSkillFragment.class,
                ChargeSkillFragment.class
        };

        View view;
        ImageView imageView;
        TextView textView;
        for (int i = 0; i < tags.length; ++i) {
            view = LayoutInflater.from(this).inflate(R.layout.tab_button, null);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            textView = (TextView) view.findViewById(R.id.textView);
            imageView.setImageResource(icons[i]);
            textView.setText(titles[i]);
            tabs.add(view);
            titleIds.add(titles[i]);

            TabHost.TabSpec spec = tabHost.newTabSpec(tags[i]);
            spec.setIndicator(view);
            tabHost.addTab(spec, fragments[i], null);
        }
        tabHost.requestFocus();
    }

    public void printData() {
        DBHelper helper = DBHelper.getInstance(null);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM type_chart", null);
        while (cursor.moveToNext()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < cursor.getColumnCount(); ++i) {
                builder.append(cursor.getString(i));
                builder.append('\t');
            }
            Log.v("haochen", builder.toString());
        }
        cursor.close();
        cursor = db.rawQuery("SELECT * FROM pokemon", null);
        while (cursor.moveToNext()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < cursor.getColumnCount(); ++i) {
                builder.append(cursor.getString(i));
                builder.append('\t');
            }
            Log.v("haochen", builder.toString());
        }
        cursor.close();
        cursor = db.rawQuery("SELECT * FROM basic_skill", null);
        while (cursor.moveToNext()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < cursor.getColumnCount(); ++i) {
                builder.append(cursor.getString(i));
                builder.append('\t');
            }
            Log.v("haochen", builder.toString());
        }
        cursor.close();
        cursor = db.rawQuery("SELECT * FROM charge_skill", null);
        while (cursor.moveToNext()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < cursor.getColumnCount(); ++i) {
                builder.append(cursor.getString(i));
                builder.append('\t');
            }
            Log.v("haochen", builder.toString());
        }
        cursor.close();
        cursor = db.rawQuery("SELECT * FROM pokemon_basic_skill", null);
        while (cursor.moveToNext()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < cursor.getColumnCount(); ++i) {
                builder.append(cursor.getString(i));
                builder.append('\t');
            }
            Log.v("haochen", builder.toString());
        }
        cursor.close();
        cursor = db.rawQuery("SELECT * FROM pokemon_charge_skill", null);
        while (cursor.moveToNext()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < cursor.getColumnCount(); ++i) {
                builder.append(cursor.getString(i));
                builder.append('\t');
            }
            Log.v("haochen", builder.toString());
        }
        cursor.close();
        db.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (id == R.id.menu_item_pokemon) {
                    Intent intent = new Intent(MainActivity.this, AddActivity.class);
                    startActivity(intent);
                } else if (id == R.id.menu_item_skill) {
                } else if (id == R.id.menu_item_tools) {
                } else if (id == R.id.menu_item_setting) {
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("language", getResources().getConfiguration().locale);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, Request.SETTING);
                } else if (id == R.id.menu_item_about) {
                }
            }
        }).start();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Request.SETTING: {
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    switchLanguage((Locale) bundle.getSerializable("language"));
                }
            }
            break;
        }
    }

    private void switchLanguage(Locale locale) {
        Resources res = getResources();
        Configuration config = res.getConfiguration();
        if (config.locale.equals(locale)) {
            return;
        }
        DisplayMetrics metrics = res.getDisplayMetrics();
        config.locale = locale;
        res.updateConfiguration(config, metrics);
        Config.locale = locale;
        for (int i = 0; i < tabs.size(); ++i) {
            TextView textView = (TextView) tabs.get(i).findViewById(R.id.textView);
            textView.setText(titleIds.get(i));
        }
        String tag = tabHost.getCurrentTabTag();
        FragmentManager manager = getSupportFragmentManager();
        MyFragment fragment = (MyFragment) manager.findFragmentByTag(tag);
        if (fragment != null) {
            fragment.update();
        }
    }
}
