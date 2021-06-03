package com.kangwang.androidmediaplayer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.kangwang.androidmediaplayer.base.AbstractPlayer;
import com.kangwang.androidmediaplayer.base.UIContoller;

import java.util.Map;

public class AndroidVideoView
        extends AbstractPlayer
        implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnBufferingUpdateListener,
        UIContoller {

    private MediaPlayer mMediaPlayer;
    private Map<String, String> mHeaders;
    private MediaPlayer.OnPreparedListener preparedListener;
    private MediaPlayer.OnCompletionListener completionListener;
    private MediaPlayer.OnErrorListener errorListener;
    private MediaPlayer.OnInfoListener infoListener;
    private MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener;
    private int currentVideoPercent = 0;
    private int mVideoWidth;
    private int mVideoHeight;


    public AndroidVideoView(Context context) {
        super(context);
    }

    public AndroidVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 初始化播放器实例
     */
    @Override
    public void initPlayer() {

    }

    /**
     * 设置播放地址
     *
     * @param path    播放地址
     * @param headers 播放地址请求头
     */
    @Override
    public void setDataSource(String path, Map<String, String> headers) {
        setVideoURI(Uri.parse(path), null);
    }

    /**
     * 用于播放raw和asset里面的视频文件
     *
     * @param fd
     */
    @Override
    public void setDataSource(AssetFileDescriptor fd) {

    }

    /**
     * set uri
     *
     * @param uri
     */
    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    public void setVideoURI(Uri uri, Map<String, String> headers) {
        mUri = uri;
        mHeaders = headers;
    }

//    openVideo();
//    requestLayout();
//    invalidate();

    public void openVideo() {
        if (mUri == null || surfaceHolder == null) {
            return;
        }
        release();
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(this::onPrepared);
            mMediaPlayer.setOnVideoSizeChangedListener(this::onVideoSizeChanged);
            mMediaPlayer.setOnCompletionListener(this::onCompletion);
            mMediaPlayer.setOnErrorListener(this::onError);
            mMediaPlayer.setOnInfoListener(this::onError);
            mMediaPlayer.setOnBufferingUpdateListener(this::onBufferingUpdate);
            mMediaPlayer.setDataSource(context, mUri, mHeaders);
            mMediaPlayer.setDisplay(surfaceHolder);
//            mMediaPlayer.setAudioAttributes(mAudioAttributes);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();
        } catch (Exception ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            return;
        } finally {
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (preparedListener != null) {
            preparedListener.onPrepared(mp);
        }
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
        if (currentVideoPercent != 0){
            mMediaPlayer.seekTo(currentVideoPercent);
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        mVideoWidth = mp.getVideoWidth();
        mVideoHeight = mp.getVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            getHolder().setFixedSize(mVideoWidth, mVideoHeight);
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        if (mVideoWidth > 0 && mVideoHeight > 0) {
            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;

                // for compatibility, we adjust size based on aspect ratio
                if ( mVideoWidth * height  < width * mVideoHeight ) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if ( mVideoWidth * height  > width * mVideoHeight ) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize;
                height = width * mVideoHeight / mVideoWidth;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    height = heightSpecSize;
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize;
                width = height * mVideoWidth / mVideoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    width = widthSpecSize;
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = mVideoWidth;
                height = mVideoHeight;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize;
                    width = height * mVideoWidth / mVideoHeight;
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize;
                    height = width * mVideoHeight / mVideoWidth;
                }
            }
        } else {
            // no size yet, just adopt the given spec sizes
        }
        setMeasuredDimension(width, height);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (completionListener!=null){
            completionListener.onCompletion(mp);
        }
        Log.i(TAG,"play complete");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (errorListener!=null){
            errorListener.onError(mp,what,extra);
        }
        Log.i(TAG,"video error");
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (infoListener!=null){
            infoListener.onInfo(mp,what,extra);
        }
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (bufferingUpdateListener!=null){
            bufferingUpdateListener.onBufferingUpdate(mp,percent);
        }
    }

    public void  setOnPreparedListener(MediaPlayer.OnPreparedListener preparedListener) {
        this.preparedListener = preparedListener;
    }


    public void setPreparedListener(MediaPlayer.OnPreparedListener preparedListener) {
        this.preparedListener = preparedListener;
    }

    public void setCompletionListener(MediaPlayer.OnCompletionListener completionListener) {
        this.completionListener = completionListener;
    }

    public void setErrorListener(MediaPlayer.OnErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    public void setInfoListener(MediaPlayer.OnInfoListener infoListener) {
        this.infoListener = infoListener;
    }

    public void setBufferingUpdateListener(MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener) {
        this.bufferingUpdateListener = bufferingUpdateListener;
    }

    public void onPause() {
        if (mMediaPlayer!=null){
            currentVideoPercent = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
            System.out.println("currentVideoPercent save"+ currentVideoPercent);
        }
    }

    public void onResume() {
        if (mMediaPlayer!=null){
            if (isPlaying())return;
            openVideo();
            System.out.println("currentVideoPercent  read"+currentVideoPercent);
        }

    }

    public void onDestroy() {
        if (mMediaPlayer!=null){
            release();
        }
    }

    public void release(){
        if (mMediaPlayer==null)return;
        mMediaPlayer.pause();
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                onDestroy();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public long getDuration() {
        return mMediaPlayer.getDuration();
    }

    /**
     * 获取缓冲百分比
     */
    @Override
    public int getBufferedPercentage() {
        return 0;
    }

    @Override
    public void startFullScreen() {

    }

    @Override
    public void stopFullScreen() {

    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void setMute(boolean isMute) {

    }

    @Override
    public boolean isMute() {
        return false;
    }

    @Override
    public void setScreenScaleType(int screenScaleType) {

    }

    /**
     * 设置渲染视频的View,主要用于TextureView
     *
     * @param surface
     */
    @Override
    public void setSurface(Surface surface) {
        try {
            mMediaPlayer.setSurface(surface);
        } catch (Exception e) {
            mPlayerEventListener.onError();
        }
    }

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     *
     * @param holder
     */
    @Override
    public void setDisplay(SurfaceHolder holder) {
        try {
            mMediaPlayer.setDisplay(holder);
        } catch (Exception e) {
            mPlayerEventListener.onError();
        }
    }

    /**
     * 设置音量
     *
     * @param v1
     * @param v2
     */
    @Override
    public void setVolume(float v1, float v2) {
        mMediaPlayer.setVolume(v1,v2);
    }

    /**
     * 设置是否循环播放
     *
     * @param isLooping
     */
    @Override
    public void setLooping(boolean isLooping) {
        mMediaPlayer.setLooping(isLooping);
    }

    /**
     * 设置其他播放配置
     */
    @Override
    public void setOptions() {

    }

    /**
     * 设置播放速度
     *
     * @param speed
     */
    @Override
    public void setSpeed(float speed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setSpeed(speed));
            } catch (Exception e) {
                mPlayerEventListener.onError();
            }
        }
    }

    /**
     * 获取播放速度
     */
    @Override
    public float getSpeed() {
        // only support above Android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                return mMediaPlayer.getPlaybackParams().getSpeed();
            } catch (Exception e) {
                mPlayerEventListener.onError();
            }
        }
        return 1f;
    }

    /**
     * 获取当前缓冲的网速
     */
    @Override
    public long getTcpSpeed() {
        return 0;
    }

    @Override
    public void replay(boolean resetPosition) {

    }

    @Override
    public void setMirrorRotation(boolean enable) {

    }

    @Override
    public Bitmap doScreenShot() {
        return null;
    }

    @Override
    public int[] getVideoSize() {
        return new int[]{mVideoWidth,mVideoHeight};
    }

    @Override
    public void startTinyScreen() {

    }

    @Override
    public void stopTinyScreen() {

    }

    @Override
    public boolean isTinyScreen() {
        return false;
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    /**
     * 调整进度
     *
     * @param time
     */
    @Override
    public void seekTo(long time) {
        seekTo((int)time);
    }

    @Override
    public long getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void pause() {
        onResume();
    }

    @Override
    public void stop(){
        mMediaPlayer.pause();
    }

    /**
     * 准备开始播放（异步）
     */
    @Override
    public void prepareAsync() {
        mMediaPlayer.prepareAsync();
    }

    /**
     * 重置播放器
     */
    @Override
    public void reset() {
        mMediaPlayer.reset();
    }

    public void resume() {
        onResume();
    }

    public void seekTo(int pausePoaition) {
        mMediaPlayer.seekTo(pausePoaition);
    }

    public void start() {
        mMediaPlayer.start();
    }

    public void setOnInfoListener(MediaPlayer.OnInfoListener onInfoListener) {
        setInfoListener(onInfoListener);
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        setCompletionListener(onCompletionListener);
    }
}
