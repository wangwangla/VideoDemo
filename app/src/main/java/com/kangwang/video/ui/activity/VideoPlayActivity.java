package com.kangwang.video.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.VideoView;

import com.kangwang.video.R;
import com.kangwang.video.bean.VideoBean;
import com.kangwang.video.utils.LogUtils;

import java.io.Serializable;

public class VideoPlayActivity extends BaseActivity{
    private VideoView videoView;
    @Override
    public int getLayout() {
        return R.layout.video_play;
    }

    @Override
    public void initView() {
        videoView = findViewById(R.id.vv);
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
        videoView.start();
    }
}
