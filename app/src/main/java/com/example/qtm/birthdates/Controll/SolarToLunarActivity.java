package com.example.qtm.birthdates.Controll;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.qtm.birthdates.BaseActivity;
import com.example.qtm.birthdates.R;
import com.example.qtm.birthdates.Util.DialogUtil;
import com.example.qtm.birthdates.Util.Utils;

import java.util.Calendar;

public class SolarToLunarActivity extends BaseActivity {
    private TextView lunar;
    private TextView solar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setStatusBarFullTransparent(this);
        setContentView(R.layout.activity_solar_to_lunar);

        lunar = findViewById(R.id.lunar);
        solar = findViewById(R.id.solar);

        solar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastClick())
                    return;
                DialogUtil.showDatePickerDialog(SolarToLunarActivity.this, DatePickerDialog.THEME_HOLO_LIGHT, solar, lunar, Calendar.getInstance());
            }
        });
    }
}
