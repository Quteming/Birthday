package com.example.qtm.birthdates.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;


public class MyDialog extends Dialog implements View.OnClickListener {
    private Context context;      // 上下文
    private boolean cancelable;     //区域外是否可点击
    private int gravity;            //Gravity.BOTTOM  Gravity.CENTER....
    private int layoutResID;      // 布局文件id
    private int[] listenedItems;  // 要监听的控件id

    public MyDialog(Context context, int style, int gravity, boolean cancelable, int layoutResID, int[] listenedItems) {
        super(context, style);
        this.context = context;
        this.gravity = gravity;
        this.cancelable = cancelable;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);

        getWindow().setGravity(gravity);

        setCanceledOnTouchOutside(cancelable);// 点击Dialog外部消失

        initView();

        //遍历控件id,添加点击事件
        if (listenedItems != null && listenedItems.length > 0) {
            for (int id : listenedItems) {
                findViewById(id).setOnClickListener(this);
            }
        }
    }

    private void initView() {
        if (showDialogInterface != null) {
            showDialogInterface.showDialogAnim();
            showDialogInterface.showDialogText();
        }
    }

    @Override
    public void onClick(View view) {
        dismiss();//注意：我在这里加了这句话，表示只要按任何一个控件的id,弹窗都会消失，不管是确定还是取消。
        listener.OnCenterItemClick(this, view);
    }

//    //显示加载dialog动画
//    public void showLoadingDialogAnim(int viewId, int anim){
//        Animation animation = AnimationUtils.loadAnimation(context, anim);
//        findViewById(viewId).startAnimation(animation);
//    }
//
//    public void closeAnim(int viewId){
//        findViewById(viewId).clearAnimation();
//    }

    private ShowDialogInterface showDialogInterface;
    public interface ShowDialogInterface{
        void showDialogAnim();
        void showDialogText();
    }
    public void setShowDialogInterface(ShowDialogInterface showDialogInterface){
        this.showDialogInterface = showDialogInterface;
    }

    private OnCenterItemClickListener listener;
    public interface OnCenterItemClickListener {
        void OnCenterItemClick(MyDialog dialog, View view);
    }
    public void setOnCenterItemClickListener(OnCenterItemClickListener listener) {
        this.listener = listener;
    }
}
