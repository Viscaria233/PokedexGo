package com.haochen.pokedexgo.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.haochen.pokedexgo.util.DataImporter;

/**
 * Created by Haochen on 2016/7/19.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "com.haochen.pokedexgo.db";
    private static int DB_VERSION = 1;

    private static DBHelper instance;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS type (" +
                "_id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL UNIQUE)");
        db.execSQL("CREATE TABLE IF NOT EXISTS type_chart (" +
                "atk INTEGER CONSTRAINT fk_chart_atk REFERENCES type (_id)," +
                "def INTEGER CONSTRAINT fk_chart_def REFERENCES type (_id)," +
                "scalar DOUBLE NOT NULL," +
                "PRIMARY KEY (atk, def))");
        db.execSQL("CREATE TABLE IF NOT EXISTS pokemon (" +
                "_id TEXT PRIMARY KEY," +
                "name_zh_cn TEXT," +
                "name_en_us TEXT," +
                "type_id_1 INTEGER CONSTRAINT fk_pm_type_1 REFERENCES type (_id)," +
                "type_id_2 INTEGER CONSTRAINT fk_pm_type_2 REFERENCES type (_id)," +
                "hp INTEGER," +
                "atk INTEGER," +
                "def INTEGER," +
                "sum INTEGER," +
                "catch DOUBLE," +
                "run DOUBLE," +
                "incubate INTEGER," +
                "before_evo_pm_id TEXT CONSTRAINT fk_pm_before_evo_pm REFERENCES pokemon (_id)," +
                "candy INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS basic_skill (" +
                "_id TEXT PRIMARY KEY," +
                "name_zh_cn TEXT," +
                "name_en_us TEXT," +
                "type_id INTEGER CONSTRAINT fk_basic_skill_type REFERENCES type (_id)," +
                "power INTEGER," +
                "duration INTEGER," +
                "energy INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS charge_skill (" +
                "_id TEXT PRIMARY KEY," +
                "name_zh_cn TEXT," +
                "name_en_us TEXT," +
                "type_id INTEGER CONSTRAINT fk_charge_skill_type REFERENCES type (_id)," +
                "power INTEGER," +
                "skill_bar INTEGER," +
                "damage_scalar DOUBLE," +
                "heal_scalar DOUBLE," +
                "duration INTEGER," +
                "damage_window INTEGER," +
                "ct DOUBLE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS pokemon_basic_skill (" +
                "pm_id TEXT CONSTRAINT fk_pm_skill_pm REFERENCES pokemon (_id)," +
                "skill_id TEXT CONSTRAINT fk_pm_basic_skill_skill REFERENCES basic_skill (_id)," +
                "PRIMARY KEY (pm_id, skill_id))");
        db.execSQL("CREATE TABLE IF NOT EXISTS pokemon_charge_skill (" +
                "pm_id TEXT CONSTRAINT fk_pm_skill_pm REFERENCES pokemon (_id)," +
                "skill_id TEXT CONSTRAINT fk_pm_charge_skill_skill REFERENCES charge_skill (_id)," +
                "PRIMARY KEY (pm_id, skill_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
