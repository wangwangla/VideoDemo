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
import java.util.ArrayList;
import java.util.Queue;

public class VideoPlayActivity extends BaseActivity implements View.OnClickListener{
    private static final int MSG_UPDATE = 1;
    private static final int MSG_UPDATE_TIME = 2;
    private VideoView videoView;
    private ImageView btnPlayer;
    private SeekBar sb_volum;
    BatteryBroadcastReceiver receiver;
    private ImageView mute;

//    private TextView all_time;
//    private TextView ready_play_time;
    private SeekBar seekBar;
    private ImageView btn_back;
    private ImageView btn_pre;
    private ImageView btn_next;
//    private Button btn_full;

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
        seekBar = findViewById(R.id.pro_bar);

        btn_back = findViewById(R.id.btn_back);
        btn_pre = findViewById(R.id.btn_pre);
        btn_next = findViewById(R.id.btn_next);
//        btn_full = findViewById(R.id.full_screen);

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
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser)return;
                videoView.seekTo(progress);
                seekBar.setProgress(progress);
//                ready_play_time.setText(progress+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mute.setOnClickListener(this);

        btn_next.setOnClickListener(this);
//        btn_full.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_pre.setOnClickListener(this);
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

    private ArrayList<VideoBean> beanList;
    private int position;

    @Override
    public void initListener() {
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data == null){
            beanList = (ArrayList<VideoBean>) intent.getSerializableExtra("bean");
            position = intent.getIntExtra("position", -1);
            playPointVideo(position);
        }else {
            videoView.setVideoURI(data);

        }
        videoView.setOnPreparedListener(new VideoPreparedListener(videoView));
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //移除更新播放消息
                mHandler.removeMessages(MSG_UPDATE_TIME);
                //修改按钮状态
                //暂停的时候移除消息，   播放 的时候发送消息
                //更新时间为最大值
                startUpdateVideoPosition();
            }
        });
    }

    private void playPointVideo(int position){
        btn_pre.setEnabled(position!=0);
        btn_next.setEnabled(position!=beanList.size()-1);
        VideoBean bean = beanList.get(position);
        LogUtils.v("xxx",bean.toString());

        videoView.setVideoURI(Uri.parse(bean.getData()));

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
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_pre:
                if (position!=0){
                    position--;
                }
                playPointVideo(position);
                break;
            case R.id.btn_next:
                if (position!=beanList.size()-1){
                    position++;
                }
                playPointVideo(position);
                break;
//            case R.id.full_screen:
//                finish();
//                break;

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
            btnPlayer.setImageResource(R.drawable.ic_play_btn_play);

        }else {

            btnPlayer.setImageResource(R.drawable.ic_play_btn_pause);
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
//            all_time.setText(mp.getDuration()+"");
//
            seekBar.setMax(mp.getDuration());
            startUpdateVideoPosition();

            videoView.start();
            //初始化亿播放时间
        }
    }

    private void startUpdateVideoPosition() {
//        ready_play_time.setText(videoView.getCurrentPosition()+"");
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME,500);
        seekBar.setProgress(videoView.getCurrentPosition());
    }
}
