package com.haochen.pokedexgo;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    private TextView languageText;
    private Spinner languageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                Locale locale = null;
                switch (languageSpinner.getSelectedItemPosition()) {
                    case 0:
                        locale = Locale.CHINA;
                        break;
                    case 1:
                        locale = Locale.US;
                        break;
                }
                bundle.putSerializable("language", locale);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        languageText = (TextView) findViewById(R.id.textView_language);
        languageSpinner = (Spinner) findViewById(R.id.spinner_language);

        Resources r = getResources();
        languageText.setText(R.string.language);
        languageSpinner.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{
                        r.getString(R.string.zh_cn),
                        r.getString(R.string.en_us)
                }));

        Bundle bundle = getIntent().getExtras();
        Locale locale = (Locale) bundle.getSerializable("language");
        String str = locale.toString();
        switch (str) {
            case "zh_CN":
                languageSpinner.setSelection(0);
                break;
            case "en_US":
                languageSpinner.setSelection(1);
                break;
        }
    }

}
