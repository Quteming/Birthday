package com.example.qtm.birthdates.Controll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.DeleteCallback;
import com.example.qtm.birthdates.BaseActivity;
import com.example.qtm.birthdates.Model.Bean.UserBean;
import com.example.qtm.birthdates.Model.Model;
import com.example.qtm.birthdates.R;
import com.example.qtm.birthdates.Util.Constant;
import com.example.qtm.birthdates.Util.MatcherUtil;
import com.example.qtm.birthdates.Util.Utils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    private EditText query;
    private ImageButton search_clear;
    private RecyclerView rv_search_list;
    private LinearLayoutManager layoutManager;
    private SearchListAdapter adapter;

    private List<UserBean> searchUserList = new ArrayList<>();//存储查询到的用户

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
            Intent intent = new Intent(SearchActivity.this, DetailInformationActivity.class);
            intent.putExtra(Constant.INFORMATION_OBJECTID, currentObjectId);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setStatusBarFullTransparent(this);
        setContentView(R.layout.activity_search);

        initView();
        initData();
        initListener();
    }

    private void initData() {
        layoutManager = new LinearLayoutManager(this);
        rv_search_list.setLayoutManager(layoutManager);
        adapter = new SearchListAdapter(this, searchUserList);
        adapter.setAdapterInterface(adapterInterface);
        rv_search_list.setAdapter(adapter);
        //绑定ContextMenu
        rv_search_list.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                //添加布局
                getMenuInflater().inflate(R.menu.delete, menu);
            }
        });
    }

    private void initView() {
        query = findViewById(R.id.query);
        search_clear = findViewById(R.id.search_clear);
        rv_search_list = findViewById(R.id.rv_search_list);
    }

    private void initListener() {

        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0){
                    search_clear.setVisibility(View.INVISIBLE);
                    searchUserList.clear();
                }else {
                    search_clear.setVisibility(View.VISIBLE);
                    searchUserList = updateUserList(s.toString());
                }
                adapter.refresh(searchUserList);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.setText("");
                search_clear.setVisibility(View.INVISIBLE);
            }
        });
    }

    private List<UserBean> updateUserList(String search) {
        List<UserBean> matchers = MatcherUtil.matchers(MainActivity.userBeanList, search);
        return matchers;
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
        Intent intent = new Intent(SearchActivity.this, CreateUserActivity.class);
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

                    searchUserList.remove(currentPosition);

                    //刷新数据
                    adapter.refresh(searchUserList);
                    Toast.makeText(SearchActivity.this, "删除" + userBean.getName() + "成功", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(SearchActivity.this, "删除" + userBean.getName() + "失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
