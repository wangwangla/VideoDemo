package com.kangwang.video.ui.activity;

import android.content.Intent;
import android.net.Uri;

import com.kangwang.androidmediaplayer.AndroidVideo;
import com.kangwang.video.R;
import com.kangwang.video.bean.VideoBean;

import java.util.ArrayList;

public class AndoridVideoViewActivity extends BaseActivity {
    private AndroidVideo videoView;

    /**
     * 加载布局
     */
    @Override
    public int getLayout() {
        return R.layout.android_video_view;
    }

    /**
     * 初始化空间
     */
    @Override
    public void initView() {
        videoView = findViewById(R.id.android_video_view);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        Intent intent = getIntent();
        ArrayList<VideoBean> beanList = (ArrayList<VideoBean>) intent.getSerializableExtra("bean");
        int position = intent.getIntExtra("position", -1);
        videoView.setVideoURI(Uri.parse(beanList.get(position).getData()));
    }

    /**
     * 设置监听
     */
    @Override
    public void initListener() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.onDestroy();
    }

}
