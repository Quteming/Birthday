package com.example.qtm.birthdates.Controll;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.example.qtm.birthdates.BaseActivity;
import com.example.qtm.birthdates.Model.Bean.UserBean;
import com.example.qtm.birthdates.Model.Model;
import com.example.qtm.birthdates.MyCallBackInterface;
import com.example.qtm.birthdates.R;
import com.example.qtm.birthdates.Util.BirthdayUtil;
import com.example.qtm.birthdates.Util.Constant;
import com.example.qtm.birthdates.Util.DialogUtil;
import com.example.qtm.birthdates.Util.SpUtils;
import com.example.qtm.birthdates.Util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateUserActivity extends BaseActivity implements View.OnClickListener {
    private Button title_back;
    private TextView title_save;

    private EditText et_creat_name;

    private RadioButton rb_creat_boy;
    private RadioButton rb_creat_gril;

    private Spinner spinner;
    private TextView tv_creat_brithday;

    private TextView title;

    private static final int CREATE_USER = 0;
    private static final int MODIFICATION_USER = 1;
    private int intExtra;
    private String objectId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        initView();
        initData();
        initListener();
    }

    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private void initData() {
        //数据
        data_list = new ArrayList<String>();
        data_list.add("阳历：");
        data_list.add("阴历：");
        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);

        Intent intent = getIntent();
        intExtra = intent.getIntExtra(Constant.CREAT_OR_MODIFY, 0);
        //初始化布局数据显示
        if (intExtra == 1){
            objectId = intent.getStringExtra(Constant.MODIFY_OBJECTID);
            UserBean userBean = Model.getInstance().getDbManger().getUserDAo().queryUser(objectId);
            if (userBean == null)
                return;

            title.setText("编辑");
            et_creat_name.setText(userBean.getName());
            if (userBean.getSex() == 1){
                rb_creat_gril.setChecked(true);
            }else {
                rb_creat_boy.setChecked(true);
            }
            if (userBean.getSolarLunar() == 1){
                spinner.setSelection(1);
            }else {
                spinner.setSelection(0);
            }
            tv_creat_brithday.setText(userBean.getBirthday());
        }
    }

    private void initListener() {
        title_back.setOnClickListener(this);
        title_save.setOnClickListener(this);
        tv_creat_brithday.setOnClickListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                solarLunar = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView() {
        title_back = findViewById(R.id.title_back);
        title_save = findViewById(R.id.title_save);

        et_creat_name = findViewById(R.id.et_creat_name);

        rb_creat_boy = findViewById(R.id.rb_creat_boy);
        rb_creat_gril = findViewById(R.id.rb_creat_gril);

        spinner = findViewById(R.id.spinner);
        tv_creat_brithday = findViewById(R.id.tv_creat_brithday);

        title = findViewById(R.id.title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_save:
                if (Utils.isFastClick())
                    return;
                saveCreateUser();
                break;
            case R.id.tv_creat_brithday:
                if (Utils.isFastClick())
                    return;
                DialogUtil.showDatePickerDialog(this, DatePickerDialog.THEME_HOLO_LIGHT, tv_creat_brithday, Calendar.getInstance());
                break;
                default:
                    break;
        }
    }

    private int solarLunar = 0;//记录选择了阳历还是阴历
    private void saveCreateUser() {
        String name = et_creat_name.getText().toString();
        String brithday = tv_creat_brithday.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(brithday)){
            Toast.makeText(this, "请填写姓名和生日", Toast.LENGTH_SHORT).show();
            return;
        }

        if (intExtra == 1){//编辑
            modification(name, brithday);
        }else {//创建
            //保存到leanclound
            saveToLeanClound(name, brithday);
        }
    }

    private void modification(String name,  String brithday) {
        DialogUtil.showLoadingDialog(this, "编辑联系人");
        final UserBean userBean = new UserBean();
        userBean.setId(objectId);
        userBean.setName(name);
        userBean.setSex(rb_creat_boy.isChecked() ? 0 : 1);
        userBean.setSolarLunar(solarLunar);
        userBean.setBirthday(brithday);
        userBean.setNextBirthday(BirthdayUtil.getInstance().getNextBirthday(userBean));

        //保存到leanclound
        Model.getInstance().getLeanCloundDao().update(userBean, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null){
                    DialogUtil.closeDialog();
                    Toast.makeText(CreateUserActivity.this, "编辑失败", Toast.LENGTH_SHORT).show();
                    return;
                }

                //保存到本地数据库
//                Model.getInstance().getDbManger().getUserDAo().saveContact(userBean);
                int size = MainActivity.userBeanList.size();
                for (int i = 0; i < size; i++){
                    if (MainActivity.userBeanList.get(i).getId().equals(objectId)){
                        MainActivity.userBeanList.remove(i);
                        break;
                    }
                }
                MainActivity.userBeanList.add(userBean);

                DialogUtil.closeDialog();
                Toast.makeText(CreateUserActivity.this, "编辑成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void saveToLeanClound(String name,  String brithday) {
        DialogUtil.showLoadingDialog(this, "创建联系人");
        final UserBean userBean = new UserBean();
        userBean.setId(SpUtils.getInstance().getString(SpUtils.PHONE_ID, ""));
        userBean.setName(name);
        userBean.setSex(rb_creat_boy.isChecked() ? 0 : 1);
        userBean.setSolarLunar(solarLunar);
        userBean.setBirthday(brithday);
        userBean.setNextBirthday(BirthdayUtil.getInstance().getNextBirthday(userBean));
        Model.getInstance().getLeanCloundDao().save(userBean, new MyCallBackInterface() {
            @Override
            public void saveCallBack(String objectId, AVException e) {
                if (e != null || TextUtils.isEmpty(objectId)){
                    DialogUtil.closeDialog();
                    Toast.makeText(CreateUserActivity.this, "创建失败", Toast.LENGTH_SHORT).show();
                    return;
                }

                //保存到本地数据库
                userBean.setId(objectId);
                Model.getInstance().getDbManger().getUserDAo().saveContact(userBean);

                MainActivity.userBeanList.add(userBean);

                DialogUtil.closeDialog();
                Toast.makeText(CreateUserActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
