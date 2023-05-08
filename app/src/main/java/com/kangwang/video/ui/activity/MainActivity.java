package com.kangwang.video.ui.activity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kangwang.video.R;
import com.kangwang.video.adpter.VideoListAdapter;
import com.kangwang.video.bean.VideoBean;
import com.kangwang.video.load.FileLoader;
import com.kangwang.video.ui.activity.base.BaseActivity;
import com.kangwang.video.utils.PermissionUtils;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView videoListRV;
    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        videoListRV = findViewById(R.id.video_list);
    }

    @Override
    public void initData() {
        PermissionUtils.checkPermission(this);
        FileLoader.loadFile(this);
        videoListRV.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<VideoBean> videoBeans = FileLoader.loadFile(this);
        videoListRV.setAdapter(new VideoListAdapter(this,videoBeans));
    }

    @Override
    public void initListener() {
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 切换播放列表
     */
    private void switchTabs(int position) {

    }

    private void updataTextColor(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}