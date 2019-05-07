package com.example.qtm.birthdates.Model.Table;

public class UserTable {
    public static final String TAB_NAME = "tab_user";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_SEX = "sex";
    public static final String COL_SOLAR_LUNAR = "solarLunar";
    public static final String COL_BIRTHDAY = "birthday";

    public static final String CREAT_TAB = "create table "
            + TAB_NAME + " ("
            + COL_ID + " text primary key,"
            + COL_SEX + " integer,"
            + COL_NAME + " text,"
            + COL_SOLAR_LUNAR + " integer,"
            + COL_BIRTHDAY + " text);";
}
