package com.kangwang.ffmpeglibrary;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaExtractor;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.example.player_base.AbstractPlayer;
import com.kangwang.ffmpeglibrary.play.FFmepegVideo;

import java.io.IOException;
import java.util.Map;

public class FFmpegVideoPlayer extends AbstractPlayer {
    static {
        System.loadLibrary("play");
    }
    private FFmepegVideo fFmepegVideo;
    public FFmpegVideoPlayer(Context context) {
        super(context);
        fFmepegVideo = new FFmepegVideo();
    }

    @Override
    public void setVideoURI(Uri parse) {
//        uri和path之间的转换
    }

    @Override
    public void setDataSource(String path, Map<String, String> headers) {

    }

    @Override
    public void setDataSource(AssetFileDescriptor fd) {

    }

    @Override
    public void initPlayer() {
        
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void prepareAsync() {

    }

    @Override
    public void reset() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void seekTo(long time) {

    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 0;
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
