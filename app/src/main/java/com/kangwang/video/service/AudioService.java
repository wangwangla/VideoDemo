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

/**
 * 上一曲  下一曲  播放模式  模板进度  通知栏
 */
public class AudioService extends Service {
    public static String ACTION_PRE = "ACTION_PRE";
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

        @Override
        public AudioService getAuidoService() {
            return AudioService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mediaPlayer!=null){
            mediaPlayer.reset();
        }else {
            mediaPlayer = new MediaPlayer();
        }
        ArrayList<Mp3Bean> bean = (ArrayList<Mp3Bean>)intent.getSerializableExtra("bean");
        int position = intent.getIntExtra("position", -1);
        System.out.println("======================");
        try {
            Mp3Bean bean1 = bean.get(position);
            mediaPlayer.setDataSource(bean1.getData());
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //发送广播
                    Intent intent1 = new Intent(ACTION_PRE);
                    intent1.putExtra("bean",bean1);
                    sendBroadcast(intent1);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }
    private MediaPlayer mediaPlayer;


    public int getCurrentTime(){
        if (mediaPlayer == null)return 0;
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuring(){
        if (mediaPlayer==null)return 0;
        return mediaPlayer.getDuration();
    }
}
