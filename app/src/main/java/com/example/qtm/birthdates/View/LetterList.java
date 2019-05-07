package com.example.qtm.birthdates.View;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LetterList extends View {
    private Paint mPaint;
    private Paint mPaint2;
    private String currentLetter = "";
    private OnTouchSelcetListener mListener;
    // 26个字母
    public static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    public LetterList(Context context) {
        this(context, null);
    }

    public LetterList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);init();
    }

    public LetterList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setTextSize(30);
        mPaint.setColor(Color.BLACK);

        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setTextSize(30);
        mPaint2.setColor(Color.RED);
    }

    int height;
    int width;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        height = getHeight();
        width = getWidth();
        int letterHeight = height / letters.length;
        for (int i = 0; i < letters.length; i++) {
            //单个字母的宽度
            float measureWidht = mPaint.measureText(letters[i]);
            int dy = (int) (letterHeight * i + letterHeight - measureWidht / 2);
            int dx = (int) (width / 2 - measureWidht / 2);

            canvas.drawText(letters[i], dx, dy, mPaint);
            //判断字符是不是选中的
            if (letters[i].equals(currentLetter)) {
                canvas.drawText(letters[i], dx, dy, mPaint2);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //这里需要判断一下，滑动的区域是不是在我们26 个字母的范围内。
        if ((event.getY() / height) * letters.length < 0 || (event.getY() / height) * letters.length > letters.length) {
            return false;
        }
        switch (event.getAction()) {
            //监听按下和移动事件
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //获取当前字母（按下或移动的高度除总高度乘以总个数=按下或移动的数组的下标）
                currentLetter = letters[(int) ((event.getY() / height) * letters.length)];
                mListener.touch(true,currentLetter);
                //刷新界面，就是重新调用 Draw 方法
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //抬起的时候给下标设置 为空
                currentLetter = "";
                //延时回调
                this.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListener.touch(false,currentLetter);
                    }
                },1000);
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    public void onTouchSelcetListener(OnTouchSelcetListener listener) {
        mListener = listener;
    }

    public interface OnTouchSelcetListener {
        void touch(boolean isTouch, String select);
    }
}
