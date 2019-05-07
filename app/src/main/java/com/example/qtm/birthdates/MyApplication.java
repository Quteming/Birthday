package com.example.qtm.birthdates;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.example.qtm.birthdates.Model.Model;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"McUNtKL71xbB4exmXGD9VHcJ-gzGzoHsz","OWA6g1PQWgnlwq1TTf7F3m89");

        Model.getInstance().init(this);
    }
}
