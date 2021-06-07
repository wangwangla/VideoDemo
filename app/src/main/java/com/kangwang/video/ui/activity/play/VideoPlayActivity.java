 package com.kangwang.video.ui.activity.play;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.player_base.AbstractPlayer;
import com.kangwang.video.R;
import com.kangwang.video.bean.VideoBean;
import com.kangwang.video.ui.activity.base.BaseActivity;
import com.kangwang.video.utils.LogUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class VideoPlayActivity extends BaseActivity implements View.OnClickListener{
    private static final int MSG_UPDATE = 1;
    private static final int MSG_UPDATE_TIME = 2;
    private AbstractPlayer videoView;
    private ImageView btnPlayer;
    private SeekBar sb_volum;
    private ImageView mute;
    private SeekBar seekBar;
    private ImageView btn_back;
    private ImageView btn_pre;
    private ImageView btn_next;
    private TextView title;
    private TextView zongshijian;
    private GestureDetector detector;
    private LinearLayout topLinear;
    private LinearLayout bottomLinear;
    private LinearLayout ll_loading;
    private ArrayList<VideoBean> beanList;
    private int position;
    private static final int UPDATE_SECOND = 5;
    private float screenHight;
    private AudioManager manager;
    private TextView vSpeed;
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
        title = findViewById(R.id.v_title);
        zongshijian = findViewById(R.id.zongshijian);
        topLinear = findViewById(R.id.video_top);
        bottomLinear = findViewById(R.id.video_bottom);
        ll_loading = findViewById(R.id.ll_loading);
        ll_loading.setVisibility(View.VISIBLE);
        vSpeed = findViewById(R.id.xxxxx);
    }

    @Override
    public void initData() {
        WindowManager wm =(WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenHight = wm.getDefaultDisplay().getHeight();
        updateSystemTime();
        manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //得到最大的音量
        int streamMaxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sb_volum.setMax(streamMaxVolume);
        int streamVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
        sb_volum.setProgress(streamVolume);
    }

    private int getCurrentVolumn(){
        manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int streamVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return streamVolume;
    }

    private int getMaxVolumn(){
        int streamMaxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return streamMaxVolume;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void initListener() {
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data == null){
            beanList = (ArrayList<VideoBean>) intent.getSerializableExtra("bean");
            position = intent.getIntExtra("position", -1);
            playPointVideo(position);
        }else {
//            videoView.setVideoURI();
        }
        videoView.setPlayerEventListener(new AbstractPlayer.PlayerEventListener() {
            @Override
            public void onError() {

            }

            @Override
            public void onCompletion() {
                //移除更新播放消息
                mHandler.removeMessages(MSG_UPDATE_TIME);
                //修改按钮状态
                //暂停的时候移除消息，   播放 的时候发送消息
                //更新时间为最大值
                startUpdateVideoPosition();
            }

            @Override
            public void onInfo(int what, int extra) {
                switch (what){
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        ll_loading.setVisibility(View.VISIBLE);
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        ll_loading.setVisibility(View.GONE);
                        break;
                }

            }

            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mp.getDuration());
                startUpdateVideoPosition();
                //初始化亿播放时间
                ll_loading.setVisibility(View.GONE);
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        seekBar.setSecondaryProgress(videoView.getBufferedPercentage());

                    }
                };
                timer.schedule(timerTask,0,1000);
            }

            @Override
            public void onVideoSizeChanged(int width, int height) {

            }
        });
//        videoView.setOnPreparedListener(new VideoPreparedListener(videoView));
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                //移除更新播放消息
//                mHandler.removeMessages(MSG_UPDATE_TIME);
//                //修改按钮状态
//                //暂停的时候移除消息，   播放 的时候发送消息
//                //更新时间为最大值
//                startUpdateVideoPosition();
//            }
//        });
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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //拖東之後的監聽
//        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                switch (what){
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                        ll_loading.setVisibility(View.VISIBLE);
//                        break;
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                        ll_loading.setVisibility(View.GONE);
//                        break;
//                }
//                return false;
//            }
//        });


        mute.setOnClickListener(this);

        btn_next.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_pre.setOnClickListener(this);
        vSpeed.setOnClickListener(this::onClick);

//        topLinear.setOnClickListener(this);
//        bottomLinear.setOnClickListener(this::onClick);
//

        //第二缓冲进度



        //设置手势的监听
        detector = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                topAndBottomCtrl();
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                //快进
                enterTinyScreen();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (videoView.isPlaying()){
                    pausePoaition = (int) videoView.getCurrentPosition();
                    videoView.pause();
                }else {
                    videoView.resume();
                    videoView.seekTo(pausePoaition);
                }
                return super.onDoubleTap(e);
            }
        });
    }

    private void playPointVideo(int position){
        btn_pre.setEnabled(position!=0);
        btn_next.setEnabled(position!=beanList.size()-1);
        VideoBean bean = beanList.get(position);
        LogUtils.v("xxx",bean.toString());
        title.setText(bean.getTitle());
//        videoView.setVideoURI(Uri.parse(bean.getData()));
//        http://39.134.168.76/PLTV/1/224/3221225556/index.m3u8
        videoView.setVideoURI(Uri.parse("http://39.134.168.76/PLTV/1/224/3221225556/index.m3u8"));
        videoView.initPlayer();

//        videoView.setVideoURI(Uri.parse("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8"));
//        videoView.setVideoPath("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
//        videoView.setVideoPath(
//                "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8"
//        );
    }
    int currentVolumn = 0;

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.play_pause:
                if (videoView.isPlaying()){
                    videoView.stop();
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
            case R.id.xxxxx:
                switchSpeed();
                break;

            default:
                break;
        }
    }

    private void switchSpeed(){
        CharSequence text = vSpeed.getText();
        float speed = 1.0f;
        if (text==null){
            vSpeed.setText("1.0x");
            setVideoSpeed(speed);
            return;
        }
        if (text.equals("1.0x")){
            vSpeed.setText("2.0x");
            speed = 2.0f;
        }else if (text.equals("2.0x")){
            vSpeed.setText("1.0x");
            speed = 1.0f;
        }
        setVideoSpeed(speed);
    }

    private void setVideoSpeed(float speed){
        videoView.setSpeed(speed);
    }
    float startVolumn = 0;
    float startY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
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

//                videoView.setX(event.getX());
//                videoView.setY(event.getY());
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
            btnPlayer.setImageResource(R.drawable.ic_play_btn_pause);

        }else {

            btnPlayer.setImageResource(R.drawable.ic_play_btn_play);
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
                case UPDATE_SECOND:
                    updateSecond();
                    break;
            }
        };
    };

    public void updateSecond(){
        int duration = (int) videoView.getDuration();
        float pre = duration  /100f;
        int sp = (int) (pre * duration);
//        int bufferPercentage = videoView.getBufferPercentage();
        seekBar.setSecondaryProgress(20);
        int secondaryProgress = seekBar.getSecondaryProgress();

        mHandler.sendEmptyMessageDelayed(UPDATE_SECOND,1000);
    }

    private void startUpdateVideoPosition() {
//        ready_play_time.setText(videoView.getCurrentPosition()+"");
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME,500);
        seekBar.setProgress(videoView.getCurrentPosition());
        zongshijian.setText(videoView.getCurrentPosition() +" /" + videoView.getDuration());

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView!=null){
            videoView.resume();
        }
    }

    private int pausePoaition = 0;

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView!=null){
            videoView.pause();
            pausePoaition = (int) videoView.getCurrentPosition();
        }
    }

    private boolean isShowBottomAndTop = true;
    private void topAndBottomCtrl(){
        if (isShowBottomAndTop) {
            //隐藏
            isShowBottomAndTop = false;
            topLinear.animate().translationY(-topLinear.getHeight()).start();
            bottomLinear.animate().translationY(bottomLinear.getHeight()).start();
        }else {
            isShowBottomAndTop = true;
            //出现
            topLinear.animate().translationY(0).start();
            bottomLinear.animate().translationY(0).start();
        }
    }

    @Override
    public void finish() {
        super.finish();
        videoView.onDestroy();
    }


    public void enterTinyScreen() {
        ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
        layoutParams.width = 300;
        layoutParams.height = 300;
        videoView.setLayoutParams(layoutParams);
    }

    public static Activity scanForActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }
    /**
     *  获取屏幕宽度
     */
    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     *  获取屏幕高
     */
    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
