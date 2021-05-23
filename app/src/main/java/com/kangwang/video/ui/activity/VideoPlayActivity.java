package com.kangwang.video.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.kangwang.video.R;
import com.kangwang.video.bean.VideoBean;
import com.kangwang.video.utils.LogUtils;

import java.io.Serializable;

public class VideoPlayActivity extends BaseActivity implements View.OnClickListener{
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

    @Override
    public void initData() {

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
}
