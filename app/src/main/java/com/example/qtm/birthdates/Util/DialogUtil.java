package com.example.qtm.birthdates.Util;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qtm.birthdates.R;
import com.example.qtm.birthdates.View.MyDialog;

import java.util.Calendar;

public class DialogUtil {

    private static MyDialog myDialog;
    private static Animation operatingAnim;

    public static void showLoadingDialog(final Context context, final String message){
        //显示加载dialog
        myDialog = new MyDialog(context, R.style.MyAlertDialog, Gravity.CENTER, false, R.layout.dialog_loding, null);
        myDialog.setShowDialogInterface(new MyDialog.ShowDialogInterface() {
            @Override
            public void showDialogAnim() {
                ImageView view = (ImageView) myDialog.findViewById(R.id.iv_loading_image);

                operatingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate);

                LinearInterpolator lin = new LinearInterpolator();
                operatingAnim.setInterpolator(lin);

                view.startAnimation(operatingAnim);

//                Animator animator = AnimatorInflater.loadAnimator(context, R.animator.rotate);
//                animator.setTarget(view);
//                LinearInterpolator lin = new LinearInterpolator();
//                animator.setInterpolator(lin);
//                animator.start();
            }

            @Override
            public void showDialogText() {
                ((TextView) myDialog.findViewById(R.id.tv_loading_text)).setText(message);
            }
        });
        myDialog.show();
    }

    public static void closeDialog(){
        operatingAnim.cancel();
        myDialog.dismiss();
    }

    //显示月日的日历
    public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        DatePickerDialog dialog = new DatePickerDialog(activity, themeResId,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                tv.setText((monthOfYear + 1) + "-" + dayOfMonth);
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

        //只显示年月，隐藏掉日
        DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow().getDecorView());
        if (dp != null) {
            ((ViewGroup)((ViewGroup)dp.getChildAt(0)).getChildAt(0))
                    .getChildAt(0).setVisibility(View.GONE);
        }
    }

    //显示年月日的日历
    public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, final TextView tv2, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        DatePickerDialog dialog = new DatePickerDialog(activity, themeResId,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                Solar solar = new Solar();
                solar.solarYear = year;
                solar.solarMonth = monthOfYear + 1;
                solar.solarDay = dayOfMonth;
                Lunar lunar = LunarSolar.SolarToLunar(solar);
                tv2.setText(lunar.toString());
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private static DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }
}
