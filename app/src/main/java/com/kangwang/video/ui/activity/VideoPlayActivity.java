package com.kangwang.video.ui.activity;

import android.content.ContentQueryMap;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.kangwang.video.R;
import com.kangwang.video.bean.VideoBean;
import com.kangwang.video.utils.LogUtils;

import java.io.Serializable;

public class VideoPlayActivity extends BaseActivity implements View.OnClickListener{
    private static final int MSG_UPDATE = 1;
    private static final int MSG_UPDATE_TIME = 2;
    private VideoView videoView;
    private Button btnPlayer;
    private SeekBar sb_volum;
    BatteryBroadcastReceiver receiver;
    private ImageView mute;

    private TextView all_time;
    private TextView ready_play_time;
    private SeekBar seekBar;

    @Override
    public int getLayout() {
        return R.layout.video_play;
    }

    @Override
    public void initView() {
        videoView = findViewById(R.id.vv);
        btnPlayer = findViewById(R.id.play_pause);
        btnPlayer.setOnClickListener(this);
        sb_volum = findViewById(R.id.sb_volum);
        mute = findViewById(R.id.mute);

        all_time = findViewById(R.id.all_time);
        ready_play_time = findViewById(R.id.time_already);
        seekBar = findViewById(R.id.pro_bar);
    }

    private float screenHight;

    @Override
    public void initData() {
        WindowManager wm =(WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenHight = wm.getDefaultDisplay().getHeight();
        updateSystemTime();
//        receiver = new BatteryBroadcastReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
//        registerReceiver(receiver,filter); //注册

//        unregisterReceiver(receiver);
        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //得到最大的音量
        int streamMaxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sb_volum.setMax(streamMaxVolume);
        int streamVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
        sb_volum.setProgress(streamVolume);
        sb_volum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //第三个参数就是是不是用户在波动进度条 1.显示系统的   0，不显示
                manager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mute.setOnClickListener(this);
    }
    AudioManager manager;
    private int getCurrentVolumn(){
        manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int streamVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return streamVolume;
    }

    private int getMaxVolumn(){
        int streamMaxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return streamMaxVolume;
    }

    @Override
    public void initListener() {
        Intent intent = getIntent();
        VideoBean bean = (VideoBean) intent.getSerializableExtra("bean");
        LogUtils.v("xxx",bean.toString());
        videoView.setVideoURI(Uri.parse(bean.getData()));
        videoView.setOnPreparedListener(new VideoPreparedListener(videoView));
    }

    int currentVolumn = 0;

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
            case R.id.mute:
                int current = getCurrentVolumn();
                if(current == 0){
                    setSystemVolumn(currentVolumn);
                }else {
                    currentVolumn = getCurrentVolumn();
                    setSystemVolumn(0);
                }
                break;
            default:
                break;
        }
    }

    float startVolumn = 0;
    float startY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                startVolumn = getCurrentVolumn();
                break;
            case MotionEvent.ACTION_MOVE:
                //屏幕划过的距离
                float offsetY = event.getY() - startY;
                //屏幕划过的百分比
                float percent = offsetY / screenHight;
                float changVolumn = -(percent * getMaxVolumn());
                int finalVolumn =(int) (startVolumn + changVolumn);
                setSystemVolumn(finalVolumn);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void setSystemVolumn(int volumn){
        manager.setStreamVolume(AudioManager.STREAM_MUSIC,volumn,0);
        sb_volum.setProgress(volumn );
    }

    private void updateButtonStatus() {
        if (videoView.isPlaying()){

            btnPlayer.setText("暂停");
        }else {

            btnPlayer.setText("播放");
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
                case MSG_UPDATE_TIME:
                    startUpdateVideoPosition();
                    break;
            }
        };
    };

    class VideoPreparedListener implements MediaPlayer.OnPreparedListener {
        private VideoView videoView;
        public VideoPreparedListener(VideoView videoView){
            this.videoView = videoView;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            all_time.setText(mp.getDuration());

            seekBar.setMax(mp.getDuration());
            startUpdateVideoPosition();

//            videoView.start();
            //初始化亿播放时间
        }
    }

    private void startUpdateVideoPosition() {
        ready_play_time.setText(videoView.getCurrentPosition());
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME,500);
        seekBar.setProgress(videoView.getCurrentPosition());
    }
}
