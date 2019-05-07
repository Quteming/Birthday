package com.example.qtm.birthdates.View;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qtm.birthdates.R;

public class ActionBarBack extends RelativeLayout implements View.OnClickListener {

    private ImageView back;
    private TextView title;
    private LinearLayout actionBarHeight;
    private Context mContext;

    private int statusBarHeight = 0;

    public ActionBarBack(Context context) {
        super(context);
        initView(context);
    }

    public ActionBarBack(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ActionBarBack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public ActionBarBack(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }


    private void initView(Context context) {
        mContext = context;
        View.inflate(context,R.layout.actionbarback,ActionBarBack.this);

        actionBarHeight = (LinearLayout) findViewById(R.id.status_bar_height);
        if (statusBarHeight == 0)
            statusBarHeight = getStatusBarHeight();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) actionBarHeight.getLayoutParams();
        layoutParams.height = statusBarHeight;

        actionBarHeight.setLayoutParams(layoutParams);

        back = findViewById(R.id.title_back);
        back.setOnClickListener(this);

        title = findViewById(R.id.title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
//                clickListener.backClick();
                ((Activity)mContext).onBackPressed();
                break;
            default:
                break;
        }
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

    public void setTitle(String message){
        title.setText(message);
    }

//    private ActionBarBackClickListener clickListener;
//    public interface ActionBarBackClickListener {
//        void backClick();
//    }
//
//    public void setActionBarBackClickListener(ActionBarBackClickListener customClickListener) {
//        this.clickListener = customClickListener;
//    }
}
