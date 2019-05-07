package com.example.qtm.birthdates.Util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.qtm.birthdates.Controll.CalendarActivity;
import com.example.qtm.birthdates.R;

public class NotificationUtil {

    private static String channelid = "1";
    private static int notify_id = 1;//通知的id
    public static void openNotification8_0(Context context, String title, String content){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(channelid, "name", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, CalendarActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(context, channelid)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(context.getApplicationInfo().icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)//点击跳转后自动关闭通知
                .setContentIntent(pi)//设置跳转
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .build();

        manager.notify(notify_id, notification);
    }
}
