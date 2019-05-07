package com.example.qtm.birthdates.Controll;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.qtm.birthdates.BaseActivity;
import com.example.qtm.birthdates.Model.Bean.UserBean;
import com.example.qtm.birthdates.Model.Dao.DBManger;
import com.example.qtm.birthdates.R;
import com.example.qtm.birthdates.Util.BirthdayUtil;
import com.example.qtm.birthdates.Util.Constant;
import com.example.qtm.birthdates.Util.SpUtils;
import com.necer.calendar.MonthCalendar;
import com.necer.entity.NDate;
import com.necer.listener.OnMonthSelectListener;
import com.necer.painter.InnerPainter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarActivity extends BaseActivity {
    private MonthCalendar ncalendar;
    private TextView current_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setStatusBarFullTransparent(this);
        setContentView(R.layout.activity_calendar);

        initView();

        initData();

        ncalendar.setOnMonthSelectListener(new OnMonthSelectListener() {
            @Override
            public void onMonthSelect(NDate date, boolean isClick) {
                current_day.setText(date.localDate.toString());
//                Toast.makeText(CalendarActivity.this, date.localDate.getYear() + "-" + date.localDate.getMonthOfYear() + "-" + date.localDate.getDayOfMonth(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CalendarActivity.this, BirthdayUserActivity.class);
                intent.putExtra(Constant.BIRTHDAY, date.localDate.toString());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        current_day.setText(BirthdayUtil.getInstance().todayToString());

        Map<String, String> strMap;
        Map<String, Integer> colorMap;
        List<String> pointList;

        if (MainActivity.userBeanList == null || MainActivity.userBeanList.size() <= 0){
            //初始化用户数据库
            String phoneId = SpUtils.getInstance().getString(SpUtils.PHONE_ID, null);
            if (phoneId == null)
                return;
            DBManger dbManger = new DBManger(this, phoneId);
            MainActivity.userBeanList.addAll(dbManger.getUserDAo().queryAllUser());
        }

        InnerPainter innerPainter = (InnerPainter) ncalendar.getCalendarPainter();
        strMap = new HashMap<>();
        colorMap = new HashMap<>();
        pointList = new ArrayList<>();

        for (UserBean userBean : MainActivity.userBeanList){
            //String birthday = BirthdayUtil.getInstance().getNextBirthday(userBean);
            String birthday = userBean.getNextBirthday();

            if (birthday != null){//设置日历的Painter数据
//                userBean.setNextBirthday(birthday);
                strMap.put(birthday, "生日");
                colorMap.put(birthday, Color.RED);
                pointList.add(birthday);
            }
        }

        innerPainter.setReplaceLunarStrMap(strMap);
        innerPainter.setReplaceLunarColorMap(colorMap);
        innerPainter.setPointList(pointList);
    }



    private void initView() {
        ncalendar = findViewById(R.id.ncalendar);
        current_day = findViewById(R.id.current_day);
    }
}
