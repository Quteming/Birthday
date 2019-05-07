package com.example.qtm.birthdates.Controll;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.avos.avoscloud.AVUser;
import com.example.qtm.birthdates.BaseActivity;
import com.example.qtm.birthdates.Model.Bean.LoginUserBean;
import com.example.qtm.birthdates.Model.Model;
import com.example.qtm.birthdates.R;
import com.example.qtm.birthdates.Util.SpUtils;

public class WelcomeActivity extends BaseActivity {
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //如果当前activity已经退出，那么不处理handler的消息
           if (isFinishing()){
               return;
           }

           //判断进入登录界面还是主界面
            toMainOrLogin();
        }
    };

    private void toMainOrLogin() {
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null && currentUser.getUsername().equals(SpUtils.getInstance().getString(SpUtils.PHONE_ID, ""))) {
            // 跳转到首页
            boolean exit = Model.getInstance().getLoginUserDao().queryUser(currentUser.getUsername());
            if (!exit){
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }else {
                Model.getInstance().loginSucceed(new LoginUserBean(currentUser.getUsername()));
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        } else {
            //缓存用户对象为空时，可打开用户注册界面…
            Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        //结束当前页面
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        MainActivity.setStatusBarFullTransparent(this);

        handler.sendMessageDelayed(Message.obtain(),1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
