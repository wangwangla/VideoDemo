package com.kangwang.video.ui.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kangwang.video.R;
import com.kangwang.video.bean.Mp3Bean;
import com.kangwang.video.service.AudioService;
import com.kangwang.video.service.IAudioService;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class AudioPlayActivity extends BaseActivity implements View.OnClickListener{
    private ImageView zanting ;
    private BroadcastReceiver broadcastReceiver;
    private View back;
    private TextView title;
    private TextView showTime;

    @Override
    public int getLayout() {
        return R.layout.audio_play;
    }

    @Override
    public void initView() {
         zanting = findViewById(R.id.zanting);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        showTime = findViewById(R.id.show_time);
    }

    ServiceConnection serviceConnection;

    @Override
    public void initData() {
        //start 开启服务  保证服务长期运行在后台
         Intent intent = new Intent(getIntent());
         intent.setClass(this, AudioService.class);
         startService(intent); //会执行服务的start ff
         //activity 和 service的连接通道
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IAudioService music = (IAudioService)service;
                auidoService = music.getAuidoService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, serviceConnection,BIND_AUTO_CREATE);
        //bind绑定服务   调用服务里面的方法

        IntentFilter filter = new IntentFilter();
        filter.addAction(AudioService.ACTION_PRE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("------broad cast");
                //更新  按钮
                Mp3Bean bean = (Mp3Bean) intent.getSerializableExtra("bean");
                title.setText(bean.getTitle());
                int currentTime = auidoService.getCurrentTime();
                int during = auidoService.getDuring();
                String xx = currentTime+"/"+during;
                showTime.setText(xx);

            }
        };
        registerReceiver(broadcastReceiver,filter);
    }

    private AudioService auidoService;
    @Override
    public void initListener() {
        zanting.setOnClickListener(this);
        back.setOnClickListener(this::onClick);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //不解绑就会同生共死
        unbindService(serviceConnection);
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zanting:
                auidoService.stop();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
