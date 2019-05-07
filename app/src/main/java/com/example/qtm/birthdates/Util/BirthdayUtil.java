package com.example.qtm.birthdates.Util;

import com.avos.avoscloud.utils.StringUtils;
import com.example.qtm.birthdates.Model.Bean.UserBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BirthdayUtil {

    private static BirthdayUtil birthdayUtil = new BirthdayUtil();
    private static Solar today;

    private BirthdayUtil(){}

    public static BirthdayUtil getInstance(){
        if (today == null){
            Calendar calendar = Calendar.getInstance();
            today = new Solar();
            today.solarYear = calendar.get(Calendar.YEAR);
            today.solarMonth = calendar.get(Calendar.MONTH) + 1;
            today.solarDay = calendar.get(Calendar.DAY_OF_MONTH);
        }
        return birthdayUtil;
    }

    //获取未过的生日的日期
    public String getNextBirthday(UserBean userBean){
        String birthday;
        if (userBean.getSolarLunar() == 0){//阳历
            birthday = parseSolar(userBean);
        }else {//阴历
            birthday = parseLunar(userBean);
        }
        return birthday;
    }

    private String parseLunar(UserBean userBean) {
        String[] split = userBean.getBirthday().split("-");//得到月  日
        if (split == null || split.length < 2){
            return null;
        }
        int month = Integer.parseInt(split[0]);
        int day = Integer.parseInt(split[1]);
        return judge(today.solarYear - 1, month, day);//从-1年开始
    }

    private String judge(int year, int month, int day) {//阴历转阳历然后判断该日期是否已过
        Lunar lunar = new Lunar();
        lunar.lunarYear = year;
        lunar.lunarMonth = month;
        lunar.lunarDay = day;
        lunar.isleap = false;
        Solar new_solar = LunarSolar.LunarToSolar(lunar);
        if (new_solar.solarYear < today.solarYear) {
            return judge(year + 1, month, day);
        }else if (new_solar.solarYear > today.solarYear){
            return new_solar.toString();
        }else if (new_solar.solarYear == today.solarYear){//年等于时对月进行判断
            if (new_solar.solarMonth < today.solarMonth){
                return judge(year + 1, month, day);
            }else if (new_solar.solarMonth > today.solarMonth){
                return new_solar.toString();
            }else if (new_solar.solarMonth == today.solarMonth){//月等于时对日进行判断
                if (new_solar.solarDay < today.solarDay){
                    return judge(year + 1, month, day);
                }else if (new_solar.solarDay >= today.solarDay){
                    return new_solar.toString();
                }
            }
        }
        return null;
    }

    private String parseSolar(UserBean userBean) {
        String[] split = userBean.getBirthday().split("-");//得到月  日
        if (split == null || split.length < 2){
            return null;
        }
        int month = Integer.parseInt(split[0]);
        int day = Integer.parseInt(split[1]);
        if (month > today.solarMonth){
            return calendarToString(today.solarYear, month, day);
        }else if (month < today.solarMonth){
            return calendarToString(today.solarYear + 1, month, day);
        }else if (month == today.solarMonth){//月等于时对日进行判断
            if (day < today.solarDay){
                return calendarToString(today.solarYear + 1, month, day);
            }else if (day >= today.solarDay){
                return calendarToString(today.solarYear, month, day);
            }
        }
        return null;
    }

    private String calendarToString(int year, int month, int day){
        if (month <= 9){
            if (day <= 9)
                return year+"-0"+month+"-0"+day;
            return year+"-0"+month+"-"+day;
        }else {
            if (day <= 9)
                return year+"-"+month+"-0"+day;
            return year+"-"+month+"-"+day;
        }
    }

    public String todayToString(){
        return today.toString();
    }

    public String getNotificationDay(List<UserBean> userBeans){
        int size = userBeans.size();
        String iDay;//第i天
        for (int i = 0; i < 7; i++){
            Calendar now = Calendar.getInstance();
            now.add(now.DATE, i);//当前时间＋i天
            for (int j = 0; j < size; j++){
                Date currentTime = now.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = formatter.format(currentTime);
                if (dateString.equals(userBeans.get(j).getNextBirthday())){
                    iDay = String.valueOf(i);
                    return iDay;
                }
            }
        }
        return null;
    }
}
