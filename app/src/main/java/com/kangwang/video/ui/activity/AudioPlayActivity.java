package com.kangwang.video.ui.activity;

import android.content.Intent;
import android.media.MediaPlayer;

import com.kangwang.video.R;
import com.kangwang.video.bean.Mp3Bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class AudioPlayActivity extends BaseActivity{
    @Override
    public int getLayout() {
        return R.layout.audio_play;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        ArrayList<Mp3Bean> bean = (ArrayList<Mp3Bean>)intent.getSerializableExtra("bean");
        int position = intent.getIntExtra("position", -1);
        System.out.println("======================");
        try {

            Mp3Bean bean1 = bean.get(position);
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(bean1.getData());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initListener() {

    }
}
