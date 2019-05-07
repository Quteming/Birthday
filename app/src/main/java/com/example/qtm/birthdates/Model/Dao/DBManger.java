package com.example.qtm.birthdates.Model.Dao;

import android.content.Context;

import com.example.qtm.birthdates.Model.Helper.UserHelper;

public class DBManger {

    private UserHelper mHelper;
    private UserDAo userDAo;

    public DBManger(Context context, String phoneId) {
        //创建数据库
        mHelper = new UserHelper(context, phoneId);

        userDAo = new UserDAo(mHelper);
    }

    public UserDAo getUserDAo() {
        return userDAo;
    }

    //关闭数据库
    public void close() {
        mHelper.close();
    }
}
