package com.example.qtm.birthdates.Model.Table;

public class LoginUserTable {
    public static final String TAB_NAME = "tab_loginuser";
    public static final String COL_PHOTOID = "photoid";

    public static final String CREAT_TAB = "create table "
            + TAB_NAME + " ("
            + COL_PHOTOID + " text primary key);";
}
