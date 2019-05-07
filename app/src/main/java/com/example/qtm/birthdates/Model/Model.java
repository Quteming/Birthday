package com.example.qtm.birthdates.Model;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;
import com.example.qtm.birthdates.Model.Bean.LoginUserBean;
import com.example.qtm.birthdates.Model.Bean.UserBean;
import com.example.qtm.birthdates.Model.Dao.DBManger;
import com.example.qtm.birthdates.Model.Dao.LeanCloundDao;
import com.example.qtm.birthdates.Model.Dao.LoginUserDao;
import com.example.qtm.birthdates.Model.Table.UserTable;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Model {

    private static Model model = new Model();

    private Context mContext;
    private LoginUserDao loginUserDao;
    private DBManger dbManger;
    private ExecutorService executors = Executors.newCachedThreadPool();
    private LeanCloundDao leanCloundDao;

    private Model(){}

    public static Model getInstance(){
        return model;
    }

    public void init(Context context){
        mContext = context;

        //初始化登陆数据库
        loginUserDao = new LoginUserDao(mContext);

        //初始化leanclound操作类
        leanCloundDao = new LeanCloundDao();
    }

    public void loginSucceed(LoginUserBean loginUserBean){
        if (loginUserBean == null){
            return;
        }
        if (dbManger != null){//切换账号时重新登录，dbManager不为空
            dbManger.close();
        }
        //初始化用户数据库
        dbManger = new DBManger(mContext, loginUserBean.getPhoneId());
    }

    public DBManger getDbManger(){
        return dbManger;
    }

    public LoginUserDao getLoginUserDao(){
        return loginUserDao;
    }

    public LeanCloundDao getLeanCloundDao(){
        return leanCloundDao;
    }

    public ExecutorService getThreadPool(){
        return executors;
    }

    public Context getContext(){
        return mContext;
    }
}
