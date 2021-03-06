package com.kangwang.video.ui.activity.play;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kangwang.video.R;
import com.kangwang.video.bean.Mp3Bean;
import com.kangwang.video.lyirc.LyricBean;
import com.kangwang.video.lyirc.LyricUtils;
import com.kangwang.video.lyirc.LyricView;
import com.kangwang.video.service.AudioService;
import com.kangwang.video.service.IAudioService;
import com.kangwang.video.ui.activity.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;

public class AudioPlayActivity extends BaseActivity implements View.OnClickListener{
    private ImageView zanting ;
    private ImageView pre;
    private ImageView next;
    private ImageView model;
    private ImageView songList;
    private BroadcastReceiver broadcastReceiver;
    private View back;
    private TextView title;
    private TextView showTime;
    private final int UPDATE_TIME_PRO = 1;
    private SeekBar seekBar;
    private LyricView lyricView;
    private static final int MEG_ROLL = 2;

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
        seekBar = findViewById(R.id.jindutao);
        pre = findViewById(R.id.pre_song);
        next = findViewById(R.id.next);
        model = findViewById(R.id.play_model);
        songList = findViewById(R.id.song_list);
        lyricView = findViewById(R.id.lyric);
    }

    ServiceConnection serviceConnection;

    @Override
    public void initData() {
        //start ????????????  ?????????????????????????????????
         Intent intent = new Intent(getIntent());
         intent.setClass(this, AudioService.class);
         startService(intent); //??????????????????start ff
         //activity ??? service???????????????
         serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IAudioService music = (IAudioService)service;
                auidoService = music.getAuidoService();
                seekBar.setMax(auidoService.getDuring());
                LyricUtils utils = new LyricUtils();
                String audioPath = auidoService.getAudioPath();
                String lyricPath = getLyricPath(audioPath);
                if (lyricPath!=null){
                    File file = new File(lyricPath+".lrc");
                    if (!file.exists()){
                        file = new File(lyricPath+".txt");
                    }
                    utils.readLyricFile(file);
                    ArrayList<LyricBean> lyrics = utils.getLyrics();
                    lyricView.setLyricBeans(lyrics);
                }

//                audioPath.indexOf()
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, serviceConnection,BIND_AUTO_CREATE);
        //bind????????????   ???????????????????????????

        IntentFilter filter = new IntentFilter();
        filter.addAction(AudioService.ACTION_PRE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //??????  ??????
                updatePlayButton();
                Mp3Bean bean = (Mp3Bean) intent.getSerializableExtra("bean");
                title.setText(bean.getTitle());
                int currentTime = auidoService.getCurrentTime();
                int during = auidoService.getDuring();
                String xx = currentTime+"/"+during;
                showTime.setText(xx);
                //????????????
                updateTime();

                //
                roll(currentTime, during);
            }
        };
        registerReceiver(broadcastReceiver,filter);

    }

    public String getLyricPath(String musicNamePath){
        try {
            int i = musicNamePath.lastIndexOf("/");
            String musicRootPath = musicNamePath.substring(0, i);
            String musicName = musicNamePath.substring(i+1,musicNamePath.length());
            int i1 = musicName.lastIndexOf(".");
            String name = musicName.substring(0, i1);
            i = musicRootPath.lastIndexOf("/");
            musicRootPath = musicRootPath.substring(0, i)+"/lyric/";
            System.out.println(musicRootPath+name+".lyric");
            return musicRootPath+name;
        }catch (Exception e) {

        }
        return null;
    }

    private void roll(int currentTime, int during) {
        lyricView.roll(currentTime,during);
        mhandller.sendEmptyMessage(MEG_ROLL);
    }

    private void updateTime() {
        String s = auidoService.getCurrentTime() + "/" + auidoService.getDuring();
        showTime.setText(s);
        seekBar.setProgress(auidoService.getCurrentTime());
        mhandller.sendEmptyMessageDelayed(UPDATE_TIME_PRO,500);
    }

    private void updatePlayButton() {
        if (!auidoService.isPlaying()) {
            zanting.setImageResource(R.drawable.ic_play_btn_play);
        }else {
            zanting.setImageResource(R.drawable.ic_play_btn_pause);
        }
    }

    private AudioService auidoService;
    @Override
    public void initListener() {
        zanting.setOnClickListener(this);
        back.setOnClickListener(this::onClick);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser)return;
                auidoService.seekTo(progress);
                showTime.setText(auidoService.getCurrentTime()+"/"+auidoService.getDuring());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        pre.setOnClickListener(this::onClick);
        next.setOnClickListener(this::onClick);
        model.setOnClickListener(this::onClick);
        songList.setOnClickListener(this::onClick);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //???????????????????????????
        unbindService(serviceConnection);
        unregisterReceiver(broadcastReceiver);
        mhandller.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zanting:
                auidoService.stop();
                updatePlayButton();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.song_list:

                break;
            case R.id.pre_song:
                auidoService.pre();
                break;
            case R.id.next:
                auidoService.next();
                break;
            case R.id.play_model:
                switchMode();
                auidoService.switchMode();
                break;
        }
    }

    private void switchMode() {
        auidoService.switchMode();
        updatePlayStatus();
    }

    private void updatePlayStatus() {
        switch(auidoService.getCurrentMode()){
            case AudioService.PLAY_ALL:
                break;
            case AudioService.PLAY_RANDOM:
                break;
            case AudioService.PLAY_SINGLE:
                break;
        }
    }

    Handler mhandller = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_TIME_PRO:
                    updateTime();
                    break;
                case MEG_ROLL:
                    roll(auidoService.getCurrentTime(),auidoService.getDuring());
                    break;
            }
        }
    };
}
