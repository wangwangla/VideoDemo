package com.kangwang.video.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.kangwang.video.bean.Mp3Bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * 上一曲  下一曲  播放模式  模板进度  通知栏
 */
public class AudioService extends Service {
    public static final int PLAY_ALL = 1;
    public static final int PLAY_SINGLE = 2;
    public static final int PLAY_RANDOM = 3;

    private int currentMode = PLAY_ALL;


    public static String ACTION_PRE = "ACTION_PRE";
    private int position;
    private ArrayList<Mp3Bean> beanlIst;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Music();
    }

    public void stop() {
        if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }else {
            mediaPlayer.start();
        }
    }

    private class Music extends Binder implements IAudioService{
        private AudioService service = AudioService.this;
        @Override
        public AudioService getAuidoService() {
            return AudioService.this;
        }

        @Override
        public String getAudioPath() {
            return service.getAudioPath();
        }
    }

    public String getAudioPath() {
        return bean1.getData();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mediaPlayer!=null){
            mediaPlayer.reset();
        }else {
            mediaPlayer = new MediaPlayer();
        }
        beanlIst = (ArrayList<Mp3Bean>)intent.getSerializableExtra("bean");
        position = intent.getIntExtra("position", -1);
        playItem();
        return super.onStartCommand(intent, flags, startId);
    }

    Mp3Bean bean1;

    private void playItem() {
        if(mediaPlayer!=null){
            mediaPlayer.reset();
        }else {
            mediaPlayer = new MediaPlayer();
        }
        try {
            bean1 = beanlIst.get(position);
            mediaPlayer.setDataSource(bean1.getData());

            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    autoPlay();
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //发送广播
                    Intent intent1 = new Intent(ACTION_PRE);
                    intent1.putExtra("bean",bean1);
                    sendBroadcast(intent1);
                }
            });

        }catch (Exception e){

        }
    }

    private MediaPlayer mediaPlayer;

    public boolean isPlaying(){
        if (mediaPlayer!=null)
        return mediaPlayer.isPlaying();
        return false;
    }

    public int getCurrentTime(){
        if (mediaPlayer == null)return 0;
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuring(){
        if (mediaPlayer==null)return 0;
        return mediaPlayer.getDuration();
    }

    private int getMaxDuring(){
        if (mediaPlayer != null){
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    private int getCurrent(){
        if (mediaPlayer != null){
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int position){
        if (mediaPlayer!=null)
        mediaPlayer.seekTo(position);
    }

    public void next(){
        if (position!=beanlIst.size()-1) {
            position++;
        }else {
            position = 0;
        }
        playItem();
    }

    public void pre(){
        if (position != 0){
            position -- ;
        }else {
            position = beanlIst.size() - 1;
        }
        playItem();
    }

    /**
     * 切换标志位
     */
    public void switchMode(){
        switch (currentMode){
            case PLAY_ALL:
                currentMode = PLAY_ALL;
                break;
            case PLAY_RANDOM:
                currentMode = PLAY_RANDOM;
                break;
            case PLAY_SINGLE:
                currentMode = PLAY_SINGLE;
                break;
        }
    }

    public void autoPlay(){
        switch (currentMode){
            case PLAY_ALL:
                if(currentMode == beanlIst.size()-1){
                    position = 0;
                }else {
                    position ++;
                }
                break;
            case PLAY_RANDOM:

                break;
            case PLAY_SINGLE:
                Random random = new Random();
                int i = random.nextInt(beanlIst.size());
                position = i;
                break;
        }
    }


    public int getCurrentMode() {
        return currentMode;
    }
}
