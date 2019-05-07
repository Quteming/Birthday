package com.example.qtm.birthdates.Model.Dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qtm.birthdates.Model.Bean.UserBean;
import com.example.qtm.birthdates.Model.Helper.UserHelper;
import com.example.qtm.birthdates.Model.Table.LoginUserTable;
import com.example.qtm.birthdates.Model.Table.UserTable;
import com.example.qtm.birthdates.Util.BirthdayUtil;

import java.util.ArrayList;
import java.util.List;

public class UserDAo {
    private UserHelper mHelper;

    public UserDAo(UserHelper dbHelper) {
        mHelper = dbHelper;
    }

    //保存单个联系人
    public void saveContact(UserBean userBean){
        if (userBean == null){
            return;
        }
        SQLiteDatabase database = mHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserTable.COL_ID, userBean.getId());
        values.put(UserTable.COL_NAME, userBean.getName());
        values.put(UserTable.COL_SEX, userBean.getSex());
        values.put(UserTable.COL_SOLAR_LUNAR, userBean.getSolarLunar());
        values.put(UserTable.COL_BIRTHDAY, userBean.getBirthday());
        database.replace(UserTable.TAB_NAME, null, values);
    }

    //保存多个联系人
    public void saveContacts(List<UserBean> userBeans){
        if (userBeans == null || userBeans.size() <= 0){
            return;
        }
        for (UserBean userBean : userBeans){
            saveContact(userBean);
        }
    }

    //查询联系人
    public UserBean queryUser(String objectId){
        if (objectId.isEmpty()){
            return null;
        }
        UserBean userBean = null;
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String sql = "select * from " + UserTable.TAB_NAME + " where " + UserTable.COL_ID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{objectId});
        if (cursor.moveToNext()){
            userBean = new UserBean();

            userBean.setName(cursor.getString(cursor.getColumnIndex(UserTable.COL_NAME)));
            userBean.setBirthday(cursor.getString(cursor.getColumnIndex(UserTable.COL_BIRTHDAY)));
            userBean.setId(cursor.getString(cursor.getColumnIndex(UserTable.COL_ID)));
            userBean.setSolarLunar(cursor.getInt(cursor.getColumnIndex(UserTable.COL_SOLAR_LUNAR)));
            userBean.setSex(cursor.getInt(cursor.getColumnIndex(UserTable.COL_SEX)));

            //用户下一次生日时间
            String solarBirthday = BirthdayUtil.getInstance().getNextBirthday(userBean);
            userBean.setNextBirthday(solarBirthday);
        }
        //关闭资源
        cursor.close();
        return userBean;
    }

    //查询所有联系人
    public List<UserBean> queryAllUser() {
        List<UserBean> userBeans = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String sql = "select * from " + UserTable.TAB_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            UserBean userBean = new UserBean();

            userBean.setName(cursor.getString(cursor.getColumnIndex(UserTable.COL_NAME)));
            userBean.setBirthday(cursor.getString(cursor.getColumnIndex(UserTable.COL_BIRTHDAY)));
            userBean.setId(cursor.getString(cursor.getColumnIndex(UserTable.COL_ID)));
            userBean.setSolarLunar(cursor.getInt(cursor.getColumnIndex(UserTable.COL_SOLAR_LUNAR)));
            userBean.setSex(cursor.getInt(cursor.getColumnIndex(UserTable.COL_SEX)));

            //用户下一次生日时间
            String solarBirthday = BirthdayUtil.getInstance().getNextBirthday(userBean);
            userBean.setNextBirthday(solarBirthday);

            userBeans.add(userBean);
        }
        //关闭资源
        cursor.close();
        return userBeans;
    }

    //删除联系人
    public void delectUser(UserBean userBean){
        SQLiteDatabase database = mHelper.getReadableDatabase();
        database.delete(UserTable.TAB_NAME, UserTable.COL_ID + "=?", new String[]{userBean.getId()});
    }
}
