package com.example.qtm.birthdates.Model.Dao;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.example.qtm.birthdates.Model.Bean.UserBean;
import com.example.qtm.birthdates.Model.Table.UserTable;
import com.example.qtm.birthdates.MyCallBackInterface;
import com.example.qtm.birthdates.Util.SpUtils;

public class LeanCloundDao {
    private static final String TAB_NAME = "MyUser";
    private static final String BRLONG_TO = "belongto";//联系人信息属于者

    //注册
    public void regist(String phoneId, String psw, SignUpCallback callback){
        AVUser user = new AVUser();// 新建 AVUser 对象实例
        user.setUsername(phoneId);// 设置用户名
        user.setPassword(psw);// 设置密码
        user.signUpInBackground(callback);
    }

    //登陆
    public void login(String phoneId, String psw, LogInCallback<AVUser> callback){
        AVUser.logInInBackground(phoneId, psw, callback);
    }

    //保存联系人
    public void save(UserBean userBean, final MyCallBackInterface myCallBackInterface){
        final AVObject UserDetailInfo = new AVObject(TAB_NAME);// 构建对象
        UserDetailInfo.put(BRLONG_TO, SpUtils.getInstance().getString(SpUtils.PHONE_ID, ""));
        UserDetailInfo.put(UserTable.COL_NAME, userBean.getName());
        UserDetailInfo.put(UserTable.COL_SEX, userBean.getSex());
        UserDetailInfo.put(UserTable.COL_SOLAR_LUNAR, userBean.getSolarLunar());
        UserDetailInfo.put(UserTable.COL_BIRTHDAY, userBean.getBirthday());
        UserDetailInfo.saveInBackground(new SaveCallback() {// 保存到服务端
            @Override
            public void done(AVException e) {
                myCallBackInterface.saveCallBack(UserDetailInfo.getObjectId(), e);
            }
        });
    }

    //更新联系人信息
    public void update(UserBean userBean, SaveCallback callback){
        //去leanclound更新
        AVObject avObject = AVObject.createWithoutData(TAB_NAME, userBean.getId());
        avObject.put(UserTable.COL_NAME, userBean.getName());
        avObject.put(UserTable.COL_SEX, userBean.getSex());
        avObject.put(UserTable.COL_SOLAR_LUNAR, userBean.getSolarLunar());
        avObject.put(UserTable.COL_BIRTHDAY, userBean.getBirthday());
        avObject.saveInBackground(callback);
    }

    //删除联系人
    public void delete(UserBean userBean, DeleteCallback callback){
        AVObject avObject = AVObject.createWithoutData(TAB_NAME, userBean.getId());
        avObject.deleteInBackground(callback);
    }

    //批量获取联系人
    public void query(FindCallback<AVObject> callback){
        AVQuery<AVObject> query = new AVQuery<>(TAB_NAME);
        String phoneId = SpUtils.getInstance().getString(SpUtils.PHONE_ID, "");
        query.whereEqualTo(BRLONG_TO, phoneId);
        query.findInBackground(callback);
    }
}
