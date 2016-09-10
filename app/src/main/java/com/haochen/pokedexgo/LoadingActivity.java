package com.haochen.pokedexgo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.SQLException;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.haochen.pokedexgo.common.Config;
import com.haochen.pokedexgo.sqlite.DBHelper;
import com.haochen.pokedexgo.util.DataImporter;

import java.io.File;
import java.lang.ref.SoftReference;

public class LoadingActivity extends AppCompatActivity {

    private static final int DATA_IMPORT_FINISH = 1;
    private Handler handler;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Resources r = getResources();
        dialog = ProgressDialog.show(
                this,
                r.getString(R.string.loading),
                r.getString(R.string.loading_message), false, false);
        DBHelper.getInstance(this);
        handler = new MyHandler(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Config.locale = getResources().getConfiguration().locale;
                final SharedPreferences preferences= getSharedPreferences("config", Activity.MODE_PRIVATE);
                boolean imported = preferences.getBoolean("imported", false);
                if (!imported) {
                    try {
                        importData();
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("imported", true);
                        editor.apply();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(DATA_IMPORT_FINISH);
            }
        }).start();
    }

    private void importData() throws SQLException {
        DataImporter importer = new DataImporter();
        importer.load(new File(Environment.getExternalStorageDirectory(), "pmdata.xls"));
        importer.importAllData();
        importer.release();
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {}

    private static class MyHandler extends Handler {
        SoftReference<LoadingActivity> activity;

        public MyHandler(LoadingActivity activity) {
            this.activity = new SoftReference<LoadingActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DATA_IMPORT_FINISH:
                    activity.get().startMainActivity();
                    activity.get().finish();
                    activity.get().dialog.dismiss();
                    break;
            }
        }
    }
}
