package com.example.mediaextractor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;

import com.example.mediaextractor.decoder.AudioDecoder;
import com.example.mediaextractor.decoder.VideoDecoder;
import com.example.mediaextractor.utils.StringUtils;
import com.example.player_base.AbstractPlayer;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MediaCodePlayer extends AbstractPlayer {

    public MediaCodePlayer(Context context) {
        super(context);
    }

    public MediaCodePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaCodePlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private VideoDecoder videoDecoder;
    private AudioDecoder audioDecoder;
    @SuppressLint("NewApi")
    public void xx(String path){
        mUri = Uri.parse(path);
    }

    @Override
    public void setVideoURI(Uri parse) {
        mUri = parse;
    }



    @Override
    public void setDataSource(String path, Map<String, String> headers) {

    }

    @Override
    public void setDataSource(AssetFileDescriptor fd) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initPlayer() {
        //        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mvtest.mp4";
        //创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        //创建视频解码器
        videoDecoder = new VideoDecoder(StringUtils.getRealFilePath(getContext(),mUri), this, null);
        threadPool.execute(videoDecoder);
        //创建音频解码器
        audioDecoder = new AudioDecoder(StringUtils.getRealFilePath(getContext(),mUri));
        threadPool.execute(audioDecoder);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void start() {
        //开启播放
        videoDecoder.goOn();
        audioDecoder.goOn();
    }

    @SuppressLint("NewApi")
    @Override
    public void pause() {
        videoDecoder.pause();
        audioDecoder.pause();
    }

    @Override
    public void stop() {
        videoDecoder.stop();
        audioDecoder.stop();
    }

    @Override
    public void prepareAsync() {
        videoDecoder.withoutSync();
        audioDecoder.withoutSync();
    }

    @Override
    public void reset() {
        stop();
        start();
    }

    @Override
    public boolean isPlaying() {
        return !videoDecoder.isStop();
//        return false;
    }

    @Override
    public void seekTo(long time) {
        videoDecoder.seekAndPlay(time);
        audioDecoder.seekAndPlay(time);
    }

    @Override
    public int getCurrentPosition() {
        long curTimeStamp = videoDecoder.getCurTimeStamp();
        return (int)curTimeStamp;
    }

    @Override
    public long getDuration() {
        return videoDecoder.getDuration();
    }

    @Override
    public int getBufferedPercentage() {
        return 0;
    }

    @Override
    public void setSurface(Surface surface) {
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {

    }

    @Override
    public void setVolume(float v1, float v2) {

    }

    @Override
    public void setLooping(boolean isLooping) {

    }

    @Override
    public void setOptions() {

    }

    @Override
    public void setSpeed(float speed) {

    }

    @Override
    public float getSpeed() {
        return 0;
    }

    @Override
    public long getTcpSpeed() {
        return 0;
    }

    @Override
    protected void openVideo() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void release() {

    }
}
