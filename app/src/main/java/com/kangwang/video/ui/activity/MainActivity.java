package com.kangwang.video.ui.activity;

import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kangwang.video.R;
import com.kangwang.video.adapter.PlayerListAdapter;
import com.kangwang.video.ui.fragment.BaseFragment;
import com.kangwang.video.ui.fragment.Mp3Fragment;
import com.kangwang.video.ui.fragment.VideoFragment;

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
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        viewPager = findViewById(R.id.vp);
        audioView = findViewById(R.id.audio_id);
        videoView = findViewById(R.id.video_id);
    }

    @Override
    public void initData() {
        updataTextColor(0);
    }

    @Override
    public void initListener() {
        List<BaseFragment> list = new ArrayList<>();
        list.add(new VideoFragment());
        list.add(new Mp3Fragment());
        viewPager.setAdapter(new PlayerListAdapter(getSupportFragmentManager(),list));
        viewPager.addOnPageChangeListener(new MyPageChangeListener());
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

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            int width = indictor.getWidth();
//            float translastx = positionOffset * width;
//            int startIdex = position * width;
//            int offsetX = (int)(startIdex * translastx);
        }

        @Override
        public void onPageSelected(int position) {
            updataTextColor(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @SuppressLint("ResourceAsColor")
    private void updataTextColor(int position) {
        if (position == TAB_AUDIO){
            audioView.setBackgroundColor(R.color.bgBhui);
            audioView.setTextColor(Color.RED);
            videoView.setBackgroundColor(Color.WHITE);
            videoView.setTextColor(Color.GREEN);
        }else {
            audioView.setBackgroundColor(Color.WHITE);
            videoView.setTextColor(Color.RED);
            videoView.setBackgroundColor(R.color.bgBhui);
            audioView.setTextColor(Color.GREEN);
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