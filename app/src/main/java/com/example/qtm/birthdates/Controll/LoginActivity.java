package com.example.qtm.birthdates.Controll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.example.qtm.birthdates.BaseActivity;
import com.example.qtm.birthdates.Model.Bean.LoginUserBean;
import com.example.qtm.birthdates.Model.Bean.UserBean;
import com.example.qtm.birthdates.Model.Model;
import com.example.qtm.birthdates.Model.Table.UserTable;
import com.example.qtm.birthdates.R;
import com.example.qtm.birthdates.Util.DialogUtil;
import com.example.qtm.birthdates.Util.SpUtils;
import com.example.qtm.birthdates.Util.Utils;

import java.util.List;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private Button login;
    private Button register;
    private EditText phoneId;
    private EditText passwrod;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MainActivity.setStatusBarFullTransparent(this);

        initView();
        initListener();
    }

    private void initView(){
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        phoneId = findViewById(R.id.login_phoneId);
        passwrod = findViewById(R.id.login_passwrod);
    }

    private void initListener(){
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                if (Utils.isFastClick())
                    return;
                userLogin();
                break;
            case R.id.register:
                if (Utils.isFastClick())
                    return;
                userRegister();
        }
    }

    private void userRegister() {
        final String registPhoneId = phoneId.getText().toString();   // 获取 登录界面输入的用户名
        final String registPasswrod = passwrod.getText().toString();    //获取 登录界面输入的密码
        //校验
        if (TextUtils.isEmpty(registPhoneId) || TextUtils.isEmpty(registPasswrod)){
            Toast.makeText(LoginActivity.this,"输入的用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        //显示加载dialog
       DialogUtil.showLoadingDialog(this, "正在注册");

        //注册
        Model.getInstance().getLeanCloundDao().regist(registPhoneId, registPasswrod, new SignUpCallback() {
            @Override
            public void done(final AVException e) {
                if (e == null) {
                    // 注册成功
                    DialogUtil.closeDialog();
                    Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                } else {
                    // 失败的原因可能有多种，常见的是用户名已经存在。
                    DialogUtil.closeDialog();
                    switch (e.getCode()){
                        case 202:
                            Toast.makeText(LoginActivity.this,"用户已被注册",Toast.LENGTH_SHORT).show();
                            break;
                        case 203:
                            Toast.makeText(LoginActivity.this,"邮箱已被注册",Toast.LENGTH_SHORT).show();
                            break;
                        case 214:
                            Toast.makeText(LoginActivity.this,"手机号已被注册",Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private void userLogin() {
        final String loginPhoneId = phoneId.getText().toString();   // 获取 登录界面输入的用户名
        final String loginPasswrod = passwrod.getText().toString();    //获取 登录界面输入的密码
        //校验
        if (TextUtils.isEmpty(loginPhoneId) || TextUtils.isEmpty(loginPasswrod)) {
            Toast.makeText(LoginActivity.this, "输入的用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //显示加载dialog
        DialogUtil.showLoadingDialog(this, "正在登录");

        Model.getInstance().getLeanCloundDao().login(loginPhoneId, loginPasswrod, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser user, AVException e) {
                if (e == null) {
                    Model.getInstance().getThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            //登录成功
                            SpUtils.getInstance().save(SpUtils.PHONE_ID, loginPhoneId);
                            //添加登录用户到数据库
                            LoginUserBean loginUserBean = new LoginUserBean(loginPhoneId);
                            Model.getInstance().getLoginUserDao().addUser(loginUserBean);
                            //创建联系人数据库
                            Model.getInstance().loginSucceed(loginUserBean);
                            //从leanclound拉取数据到本地数据库
                            loadDateFromLCToSqlite();
                        }
                    });

                } else {
                    DialogUtil.closeDialog();
                    Toast.makeText(LoginActivity.this, "输入手机号与密码不匹配", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //从leanclound加载数据到本地数据库
    public void loadDateFromLCToSqlite(){
        Model.getInstance().getLeanCloundDao().query(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> avObjects, AVException avException) {
                Model.getInstance().getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (avObjects != null && 0 < avObjects.size()){
                            for (AVObject object : avObjects){
                                saveToSQLite(object);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DialogUtil.closeDialog();
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }

    private void saveToSQLite(AVObject object) {
        UserBean userBean = new UserBean();
        userBean.setId(object.getObjectId());
        userBean.setName(object.getString(UserTable.COL_NAME));
        userBean.setSex(object.getInt(UserTable.COL_SEX));
        userBean.setSolarLunar(object.getInt(UserTable.COL_SOLAR_LUNAR));
        userBean.setBirthday(object.getString(UserTable.COL_BIRTHDAY));
        Model.getInstance().getDbManger().getUserDAo().saveContact(userBean);
    }
}
