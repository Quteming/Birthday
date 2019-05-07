package com.example.qtm.birthdates.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qtm.birthdates.R;


public class ActionBar extends RelativeLayout implements View.OnClickListener {
    private CustomClickListener customClickListener;

    private ImageView action_bar_menu;
    private LinearLayout actionBarHeight;
    private TextView title;
    private LinearLayout search;
    private LinearLayout add;
    private int statusBarHeight = 0;

    public ActionBar(Context context) {
        super(context);
        initView(context);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context){
        View.inflate(context,R.layout.actionbar,ActionBar.this);

        actionBarHeight = (LinearLayout) findViewById(R.id.status_bar_height);
        action_bar_menu = findViewById(R.id.action_bar_menu);
        title = (TextView) findViewById(R.id.app_name);
        search = (LinearLayout) findViewById(R.id.action_bar_search);
        add = (LinearLayout) findViewById(R.id.action_bar_add);

        if (statusBarHeight == 0)
            statusBarHeight = getStatusBarHeight();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) actionBarHeight.getLayoutParams();
        layoutParams.height = statusBarHeight;

        actionBarHeight.setLayoutParams(layoutParams);

        initListener();
    }

    private void initListener() {
        search.setOnClickListener(this);
        add.setOnClickListener(this);
        action_bar_menu.setOnClickListener(this);
    }

    //获取状态栏高度
    private int getStatusBarHeight(){
        int result = 0;
        int resId = getResources().getIdentifier("status_bar_height","dimen","android");
        if(resId>0){
            result = getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_bar_search:
                customClickListener.searchClick();
                break;
            case R.id.action_bar_add:
                customClickListener.addClick();
                break;
            case R.id.action_bar_menu:
                customClickListener.menuClick();
                break;
                default:
                    break;
        }
    }

    /**
     * 给箭头图片添加监听 ——----回调接口
     */
    public interface CustomClickListener {
        void searchClick();
        void addClick();
        void menuClick();
    }

    public void setCustomClickListener(CustomClickListener customClickListener) {
        this.customClickListener = customClickListener;
    }
}

