package com.kangwang.video.ui.notifa;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.kangwang.video.R;
import com.kangwang.video.ui.activity.base.BaseActivity;

public class Notifi extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //system  notifi
        Notification.Builder builder = new Notification.Builder(this);
        builder.setTicker("正在xxxx");
        builder.setSmallIcon(R.drawable.video_default_icon);
        builder.setContentTitle("xxxxxxxxxxx");
        builder.setContentText("xxxxxxxx");
        builder.setWhen(System.currentTimeMillis());

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }

    @Override
    public int getLayout() {
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
