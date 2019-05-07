package com.example.qtm.birthdates.Model.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qtm.birthdates.Model.Bean.LoginUserBean;
import com.example.qtm.birthdates.Model.Helper.LoginUserHelper;
import com.example.qtm.birthdates.Model.Table.LoginUserTable;

public class LoginUserDao {

    private final LoginUserHelper mHelper;

    public LoginUserDao(Context context) {
        mHelper = new LoginUserHelper(context);
    }

    //添加用户
    public void addUser(LoginUserBean loginUserBean){
        SQLiteDatabase db = mHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(LoginUserTable.COL_PHOTOID, loginUserBean.getPhoneId());
        db.replace(LoginUserTable.TAB_NAME, null, values);
    }

    //查询用户
    public boolean queryUser(String phoneId){
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String sql = "select * from " + LoginUserTable.TAB_NAME + " where " + LoginUserTable.COL_PHOTOID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{phoneId});
        if (cursor.moveToNext()){
            //关闭资源
            cursor.close();
            return true;
        }
        //关闭资源
        cursor.close();
        return false;
    }
}
