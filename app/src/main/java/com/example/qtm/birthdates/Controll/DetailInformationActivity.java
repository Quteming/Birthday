package com.example.qtm.birthdates.Controll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qtm.birthdates.BaseActivity;
import com.example.qtm.birthdates.Model.Bean.UserBean;
import com.example.qtm.birthdates.Model.Model;
import com.example.qtm.birthdates.R;
import com.example.qtm.birthdates.Util.BirthdayUtil;
import com.example.qtm.birthdates.Util.Constant;
import com.example.qtm.birthdates.View.ActionBarBack;

public class DetailInformationActivity extends BaseActivity {
    private ActionBarBack action_bar;
    private TextView name;
    private ImageView sex;
    private TextView solar_lunar;
    private TextView birthday;
    private TextView next_birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setStatusBarFullTransparent(this);
        setContentView(R.layout.activity_detail_information);

        initView();
        initData();
    }

    private void initView() {
        action_bar = findViewById(R.id.action_bar);
        name = findViewById(R.id.name);
        sex = findViewById(R.id.sex);
        solar_lunar = findViewById(R.id.solar_lunar);
        birthday = findViewById(R.id.birthday);
        next_birthday = findViewById(R.id.next_birthday);
    }

    private void initData() {
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra(Constant.INFORMATION_OBJECTID);

        UserBean userBean = Model.getInstance().getDbManger().getUserDAo().queryUser(stringExtra);
        if (userBean == null)
            return;

        action_bar.setTitle("详情信息");
        name.setText(userBean.getName());
        if (userBean.getSex() == 0){
            sex.setImageResource(R.drawable.sex_boy);
        }else {
            sex.setImageResource(R.drawable.sex_gril);
        }
        if (userBean.getSolarLunar() == 0){
            solar_lunar.setText("阳历");
        }else {
            solar_lunar.setText("阴历");
        }
        birthday.setText(userBean.getBirthday());

//        String solarBirthday = BirthdayUtil.getInstance().getNextBirthday(userBean);
//        if (!TextUtils.isEmpty(solarBirthday)){
//            next_birthday.setText(solarBirthday);
//        }
        next_birthday.setText(userBean.getNextBirthday());
    }
}
