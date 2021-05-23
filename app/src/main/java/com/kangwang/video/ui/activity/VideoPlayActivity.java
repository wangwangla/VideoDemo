package com.kangwang.video.ui.activity;

import android.content.ContentQueryMap;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.kangwang.video.R;
import com.kangwang.video.bean.VideoBean;
import com.kangwang.video.utils.LogUtils;

import java.io.Serializable;

public class VideoPlayActivity extends BaseActivity implements View.OnClickListener{
    private static final int MSG_UPDATE = 1;
    private VideoView videoView;
    private Button btnPlayer;

    @Override
    public int getLayout() {
        return R.layout.video_play;
    }

    @Override
    public void initView() {
        videoView = findViewById(R.id.vv);
        btnPlayer = findViewById(R.id.play_pause);
        btnPlayer.setOnClickListener(this);
    }

    BatteryBroadcastReceiver receiver;

    @Override
    public void initData() {
        updateSystemTime();
        receiver = new BatteryBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver,filter); //注册

        unregisterReceiver(receiver);
    }

    @Override
    public void initListener() {
        Intent intent = getIntent();
        VideoBean bean = (VideoBean) intent.getSerializableExtra("bean");
        LogUtils.v("xxx",bean.toString());
        videoView.setVideoURI(Uri.parse(bean.getData()));
        videoView.setOnPreparedListener(new VideoPreparedListener(videoView));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_pause:
                if (videoView.isPlaying()){
                    videoView.pause();
                }else {
                    videoView.start();
                }
                updateButtonStatus();
                break;
            default:
                break;
        }
    }

    private void updateButtonStatus() {
        if (videoView.isPlaying()){
            btnPlayer.setBackgroundResource(R.drawable.ic_launcher_background);
        }else {

        }
    }

    private void updateSystemTime(){
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE,1000);
        mHandler.removeCallbacksAndMessages(null); //移除消息
    }
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case MSG_UPDATE:
                    break;
            }
        };
    };
}
