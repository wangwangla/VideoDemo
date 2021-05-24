package com.kangwang.video.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kangwang.video.utils.LogUtils;

public class BatteryBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra("level", -1);
        LogUtils.e("电池电量",level+"");
        //根据level在值來設置
    }
}
