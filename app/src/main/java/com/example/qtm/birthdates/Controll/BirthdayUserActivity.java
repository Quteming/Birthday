package com.example.qtm.birthdates.Controll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.DeleteCallback;
import com.example.qtm.birthdates.BaseActivity;
import com.example.qtm.birthdates.Model.Bean.UserBean;
import com.example.qtm.birthdates.Model.Model;
import com.example.qtm.birthdates.R;
import com.example.qtm.birthdates.Util.Constant;
import com.example.qtm.birthdates.Util.Utils;
import com.example.qtm.birthdates.View.ActionBarBack;

import java.util.ArrayList;
import java.util.List;

public class BirthdayUserActivity extends BaseActivity {
    private ActionBarBack action_bar;
    private RecyclerView birthdays_list;
    private String stringExtra;
    private List<UserBean> userBeans;

    private int currentPosition;
    private String currentObjectId;
    private SearchListAdapter.AdapterInterface adapterInterface = new SearchListAdapter.AdapterInterface() {
        @Override
        public void getLongClickPosition(int position, String objectId) {
            currentPosition = position;
            currentObjectId = objectId;
        }

        @Override
        public void getOnClickPosition(int position, String objectId) {
            if (Utils.isFastClick())
                return;
            currentPosition = position;
            currentObjectId = objectId;
            Intent intent = new Intent(BirthdayUserActivity.this, DetailInformationActivity.class);
            intent.putExtra(Constant.INFORMATION_OBJECTID, currentObjectId);
            startActivity(intent);
        }
    };
    private SearchListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setStatusBarFullTransparent(this);
        setContentView(R.layout.activity_user_birthday);

        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        stringExtra = intent.getStringExtra(Constant.BIRTHDAY);
        if (TextUtils.isEmpty(stringExtra))
            return;

        userBeans = new ArrayList<>();
        for (UserBean userBean : MainActivity.userBeanList){
            if (stringExtra.equals(userBean.getNextBirthday())){
                userBeans.add(userBean);
            }
        }

        action_bar.setTitle(stringExtra);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        birthdays_list.setLayoutManager(layoutManager);
        adapter = new SearchListAdapter(this, userBeans);
        adapter.setAdapterInterface(adapterInterface);
        birthdays_list.setAdapter(adapter);
        //绑定ContextMenu
        birthdays_list.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                //添加布局
                getMenuInflater().inflate(R.menu.delete, menu);
            }
        });
    }

    private void initView() {
        action_bar = findViewById(R.id.action_bar);
        birthdays_list = findViewById(R.id.birthdays_list);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.user_editor:
                editUser();
                //消费事件
                return true;
            case R.id.user_delete:
                deleteUser();
                //消费事件
                return true;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void editUser() {
        Intent intent = new Intent(BirthdayUserActivity.this, CreateUserActivity.class);
        intent.putExtra(Constant.CREAT_OR_MODIFY, 1);
        intent.putExtra(Constant.MODIFY_OBJECTID, currentObjectId);
        startActivity(intent);
    }

    private void deleteUser() {
        //LeanClound删除
        final UserBean userBean = Model.getInstance().getDbManger().getUserDAo().queryUser(currentObjectId);
        Model.getInstance().getLeanCloundDao().delete(userBean, new DeleteCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    //Sqlite删除user信息
                    Model.getInstance().getDbManger().getUserDAo().delectUser(userBean);
                    //本地
                    int size = MainActivity.userBeanList.size();
                    for (int i = 0; i < size; i++){
                        if (MainActivity.userBeanList.get(i).getId().equals(currentObjectId)){
                            MainActivity.userBeanList.remove(i);
                            break;
                        }
                    }

                    userBeans.remove(currentPosition);

                    //刷新数据
                    adapter.refresh(userBeans);
                    Toast.makeText(BirthdayUserActivity.this, "删除" + userBean.getName() + "成功", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(BirthdayUserActivity.this, "删除" + userBean.getName() + "失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
