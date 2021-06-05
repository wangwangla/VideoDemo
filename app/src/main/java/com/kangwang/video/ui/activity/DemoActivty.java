package com.kangwang.video.ui.activity;

import android.os.Environment;
import android.view.SurfaceView;

import com.example.mediaextractor.decoder.AudioDecoder;
import com.example.mediaextractor.decoder.VideoDecoder;
import com.kangwang.video.R;
import com.kangwang.video.ui.activity.base.BaseActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DemoActivty extends BaseActivity {
    /**
     * 加载布局
     */
    @Override
    public int getLayout() {
        return R.layout.demo_activtiy;
    }

    /**
     * 初始化空间
     */
    @Override
    public void initView() {
        SurfaceView sfv = findViewById(R.id.sfv);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mvtest.mp4";
        //创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        //创建视频解码器
        VideoDecoder videoDecoder = new VideoDecoder(path, sfv, null);
        threadPool.execute(videoDecoder);

        //创建音频解码器
        AudioDecoder audioDecoder = new AudioDecoder(path);
        threadPool.execute(audioDecoder);

        //开启播放
        videoDecoder.goOn();
        audioDecoder.goOn();
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

    }

    /**
     * 设置监听
     */
    @Override
    public void initListener() {

    }
}
