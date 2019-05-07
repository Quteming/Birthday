package com.example.qtm.birthdates.Controll;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.DeleteCallback;
import com.example.qtm.birthdates.Model.Model;
import com.example.qtm.birthdates.Util.BirthdayUtil;
import com.example.qtm.birthdates.Util.Constant;
import com.example.qtm.birthdates.Util.LetterComparator;
import com.example.qtm.birthdates.Util.NotificationUtil;
import com.example.qtm.birthdates.Util.Utils;
import com.example.qtm.birthdates.View.ActionBar;
import com.example.qtm.birthdates.View.LetterList;
import com.example.qtm.birthdates.R;
import com.example.qtm.birthdates.View.TitleItemDecoration;
import com.example.qtm.birthdates.Model.Bean.UserBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView rv_user_list;
    public static List<UserBean> userBeanList = new ArrayList<>();
    private UserListAdapter adapter;
    private LetterList letterList;
    private TextView currentLetter;
    private ActionBar actionbar;

    public DrawerLayout drawer_layout;

    private TitleItemDecoration titleItemDecoration;

    private Map<String, Integer> positionForSection = new HashMap<>();//方便快速定位

    private int size;//userBeanList的数据量
    private int clickContextMenuPosition;

    private static final int SET_ADAPTER = 1;
    private static final int REFRESH_ADAPTER = 2;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SET_ADAPTER:
                    setAdapter();
                    //绑定ContextMenu
                    rv_user_list.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                            //添加布局
                            getMenuInflater().inflate(R.menu.delete, menu);
                        }
                    });
                    break;
                case REFRESH_ADAPTER:
                    refreshAdapter();
                    break;
                    default:
                        break;
            }
        }
    };

    private LetterList.OnTouchSelcetListener onTouchSelcetListener = new LetterList.OnTouchSelcetListener() {
        @Override
        public void touch(boolean isTouch, String select) {
            if (isTouch) {
                currentLetter.setText(select);
                currentLetter.setVisibility(View.VISIBLE);

                int positionForSection = getPositionForSection(select);

                if (positionForSection >= 0){
                    layoutManager.scrollToPositionWithOffset(positionForSection, 0);
                }
            }else {
                currentLetter.setText(select);
                currentLetter.setVisibility(View.GONE);
            }
        }
    };
    private LinearLayoutManager layoutManager;
    private UserListAdapter.AdapterInterface adapterInterface = new UserListAdapter.AdapterInterface() {
        @Override
        public void getLongClickPosition(int position) {
            clickContextMenuPosition = position;
        }

        @Override
        public void getOnClickPosition(String objectId) {
            if (Utils.isFastClick())
                return;
            Intent intent = new Intent(MainActivity.this, DetailInformationActivity.class);
            intent.putExtra(Constant.INFORMATION_OBJECTID, objectId);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarFullTransparent(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initListener();
    }

    private void initListener() {

        actionbar.setCustomClickListener(new ActionBar.CustomClickListener() {
            @Override
            public void searchClick() {
                if (Utils.isFastClick())
                    return;
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }

            @Override
            public void addClick() {
                if (Utils.isFastClick())
                    return;
                Intent intent = new Intent(MainActivity.this, CreateUserActivity.class);
                startActivity(intent);
            }

            @Override
            public void menuClick() {
                if (Utils.isFastClick())
                    return;
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });

        letterList.onTouchSelcetListener(onTouchSelcetListener);
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
        Intent intent = new Intent(MainActivity.this, CreateUserActivity.class);
        intent.putExtra(Constant.CREAT_OR_MODIFY, 1);
        String objectId = userBeanList.get(clickContextMenuPosition).getId();
        intent.putExtra(Constant.MODIFY_OBJECTID, objectId);
        startActivity(intent);
    }

    private void deleteUser() {
        //LeanClound删除
        final UserBean userBean = userBeanList.get(clickContextMenuPosition);
        Model.getInstance().getLeanCloundDao().delete(userBean, new DeleteCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    //Sqlite删除user信息
                    Model.getInstance().getDbManger().getUserDAo().delectUser(userBean);
                    //本地
                    userBeanList.remove(clickContextMenuPosition);

                    Toast.makeText(MainActivity.this, "删除" + userBean.getName() + "成功", Toast.LENGTH_SHORT).show();
                    //刷新数据
                    refreshAdapter();

                    return;
                }
                Toast.makeText(MainActivity.this, "删除" + userBean.getName() + "失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        drawer_layout = findViewById(R.id.drawer_layout);

        actionbar = findViewById(R.id.actionbar);

        rv_user_list = findViewById(R.id.rv_user_list);
        letterList = findViewById(R.id.letterlisr);
        currentLetter = findViewById(R.id.tv_letterlist_currentletter);
    }

    private void initData() {
        Model.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //本地获取数据
                if (userBeanList.size() > 0){
                    userBeanList.clear();
                }
                userBeanList.addAll(Model.getInstance().getDbManger().getUserDAo().queryAllUser());
                size = userBeanList.size();

                Message message = new Message();
                message.what = SET_ADAPTER;
                handler.sendMessage(message);
            }
        });
    }

    private void setPositionForSection(){
        int size = userBeanList.size();
        for (int position = 0; position < size; position++){
            if (position == 0 && userBeanList.get(position).getTag() != null){
                positionForSection.put(userBeanList.get(position).getTag(), position);
            }else {
                if (userBeanList.get(position).getTag() != null){
                    if (!userBeanList.get(position).getTag().equals(userBeanList.get(position - 1).getTag()))
                        positionForSection.put(userBeanList.get(position).getTag(), position);
                }
            }
        }
    }

    private int getPositionForSection(String tag){
        return positionForSection.get(tag) == null ? -1 : positionForSection.get(tag);
    }

    //全透状态栏
    public static void setStatusBarFullTransparent(Activity activity){
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void setAdapter(){
        //对数据进行排序
        Collections.sort(userBeanList, new LetterComparator());

        //划分数据块
        setPositionForSection();

        layoutManager = new LinearLayoutManager(MainActivity.this);
        rv_user_list.setLayoutManager(layoutManager);
        titleItemDecoration = new TitleItemDecoration(MainActivity.this, userBeanList);
        rv_user_list.addItemDecoration(titleItemDecoration);
        adapter = new UserListAdapter(MainActivity.this, userBeanList);
        adapter.setAdapterInterface(adapterInterface);
        rv_user_list.setAdapter(adapter);

        //通知
        String day = BirthdayUtil.getInstance().getNotificationDay(userBeanList);
        if (day == null)
            return;
        String content = dayToString(day);

        NotificationUtil.openNotification8_0(this, "朋友生日", content);
    }

    private void refreshAdapter(){
        if (adapter == null)
            return;
        //对数据进行排序
        Collections.sort(userBeanList, new LetterComparator());

        //划分数据块
        setPositionForSection();

        if (titleItemDecoration != null){
            rv_user_list.removeItemDecoration(titleItemDecoration);
        }
        titleItemDecoration = new TitleItemDecoration(MainActivity.this, userBeanList);
        rv_user_list.addItemDecoration(titleItemDecoration);

        adapter.notifyDataSetChanged();
    }

    private String dayToString(String day){
        if (day.equals("0")){
            return "有朋友今天生日，送个祝福吧。";
        }else if (day.equals("1")){
            return "有朋友明天生日，送个祝福吧。";
        }else if (day.equals("2")){
            return "有朋友后天生日，送个祝福吧。";
        }else if (day.equals("3")){
            return "有朋友三天后生日，送个祝福吧。";
        }else if (day.equals("4")){
            return "有朋友四天后生日，送个祝福吧。";
        }else if (day.equals("5")){
            return "有朋友五天后生日，送个祝福吧。";
        }else if (day.equals("6")){
            return "有朋友六天后生日，送个祝福吧。";
        }
        return null;
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        if (size != userBeanList.size()){
//            size = userBeanList.size();
//
//            Message message = new Message();
//            message.what = REFRESH_ADAPTER;
//            handler.sendMessage(message);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Message message = new Message();
        message.what = REFRESH_ADAPTER;
        handler.sendMessage(message);
    }
}
