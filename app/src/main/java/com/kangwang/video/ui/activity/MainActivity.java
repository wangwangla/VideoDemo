package com.kangwang.video.ui.activity;

import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.kangwang.video.R;
import com.kangwang.video.adapter.PlayerListAdapter;
import com.kangwang.video.ui.activity.base.BaseActivity;
import com.kangwang.video.ui.fragment.BaseFragment;
import com.kangwang.video.ui.fragment.Mp3Fragment;
import com.kangwang.video.ui.fragment.VideoFragment;
import com.kangwang.video.ui.fragment.ZhiBoFragment;
import com.kangwang.video.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TextView audioView;
    private TextView videoView;
    private final int TAB_VIDEO = 0;
    private final int TAB_AUDIO = 1;

    @Override
    public int getLayout() {
//        FFmepegVideo fFmepegVideo = new FFmepegVideo();
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        viewPager = findViewById(R.id.vp);
        audioView = findViewById(R.id.audio_id);
        videoView = findViewById(R.id.video_id);
    }

    private RemoteViews getContentView() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notify_audio);
        return remoteViews;
    }


    @Override
    public void initData() {
        updataTextColor(0);
    }

    @Override
    public void initListener() {
        List<BaseFragment> list = new ArrayList<>();
        list.add(new ZhiBoFragment());
        list.add(new Mp3Fragment());
        viewPager.setAdapter(new PlayerListAdapter(getSupportFragmentManager(),list));
        audioView.setOnClickListener(this);
        videoView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.audio_id:
                switchTabs(TAB_AUDIO);
                break;
            case R.id.video_id:
                switchTabs(TAB_VIDEO);
                break;
            default:
                break;
        }
    }

    /**
     * 切换播放列表
     */
    private void switchTabs(int position) {
        updataTextColor(position);
        viewPager.setCurrentItem(position);
    }

    private void updataTextColor(int position) {
        if (position == TAB_AUDIO){
            audioView.setTextColor(Color.BLUE);
            videoView.setTextColor(Color.GRAY);
        }else {
            videoView.setTextColor(Color.BLUE);
            audioView.setTextColor(Color.GRAY);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}