package com.example.qtm.birthdates.Model.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.qtm.birthdates.Model.Table.LoginUserTable;

public class LoginUserHelper extends SQLiteOpenHelper {
    public LoginUserHelper(Context context) {
        super(context, "loginuser.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LoginUserTable.CREAT_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
