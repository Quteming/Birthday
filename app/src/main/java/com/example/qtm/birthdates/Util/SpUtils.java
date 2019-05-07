package com.example.qtm.birthdates.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.qtm.birthdates.Model.Model;

public class SpUtils {
    public static final String PHONE_ID = "phone_id";

    private static SpUtils spUtils = new SpUtils();
    private static SharedPreferences mSp;

    private SpUtils() {
    }

    public static SpUtils getInstance(){
        if (mSp == null){
            mSp = Model.getInstance().getContext().getSharedPreferences("IM", Context.MODE_PRIVATE);
        }

        return spUtils;
    }

    //保存
    public void save(String key, Object value){
        if (value instanceof String){
            mSp.edit().putString(key, (String) value).commit();
        }else if (value instanceof Boolean){
            mSp.edit().putBoolean(key, (Boolean) value).commit();
        }else if (value instanceof Integer){
            mSp.edit().putInt(key, (Integer) value).commit();
        }
    }

    //获取数据的方法
    public String getString(String key, String defValue){
        return mSp.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue){
        return mSp.getBoolean(key, defValue);
    }

    public int getInt(String key, int defValue){
        return mSp.getInt(key, defValue);
    }
}
