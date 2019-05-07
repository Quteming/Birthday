package com.example.qtm.birthdates.Model.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.qtm.birthdates.Model.Table.UserTable;

public class UserHelper extends SQLiteOpenHelper {
    public UserHelper(Context context, String phoneId) {
        super(context, phoneId + ".db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserTable.CREAT_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
