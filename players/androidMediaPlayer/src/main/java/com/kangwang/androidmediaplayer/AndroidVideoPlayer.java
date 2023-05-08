package com.kangwang.androidmediaplayer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.example.player_base.AbstractPlayer;

import java.util.Map;

public class AndroidVideoPlayer
        extends AbstractPlayer
        implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnBufferingUpdateListener {

    private MediaPlayer mMediaPlayer;
    private Map<String, String> mHeaders;
    private MediaPlayer.OnPreparedListener preparedListener;
    private MediaPlayer.OnCompletionListener completionListener;
    private MediaPlayer.OnErrorListener errorListener;
    private MediaPlayer.OnInfoListener infoListener;
    private MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener;
    private int currentVideoPercent = 0;
    private int mBufferedPercent = 100;

    public AndroidVideoPlayer(Context context) {
        super(context);
    }

    public AndroidVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AndroidVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化播放器实例
     */
    @Override
    public void initPlayer() {
        init(context);
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
        try {
            mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
        } catch (Exception e) {
            mPlayerEventListener.onError();
        }
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

    public void openVideo() {
        if (mUri == null || surfaceHolder == null) {
            return;
        }
        release();
        try {
            mMediaPlayer = new MediaPlayer();
            setInitListener();
            mMediaPlayer.setDataSource(context, mUri, mHeaders);
            mMediaPlayer.setDisplay(surfaceHolder);
//            mMediaPlayer.setAudioAttributes(mAudioAttributes);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            prepareAsync();
        } catch (Exception ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            return;
        } finally {
        }
    }

    private void setInitListener() {
        mMediaPlayer.setOnPreparedListener(this::onPrepared);
        mMediaPlayer.setOnVideoSizeChangedListener(this::onVideoSizeChanged);
        mMediaPlayer.setOnCompletionListener(this::onCompletion);
        mMediaPlayer.setOnErrorListener(this::onError);
        mMediaPlayer.setOnInfoListener(this::onInfo);
        mMediaPlayer.setOnBufferingUpdateListener(this::onBufferingUpdate);
    }

    public MediaPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (preparedListener != null) {
            preparedListener.onPrepared(mp);
        }
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
        if (mPlayerEventListener != null) {
            mPlayerEventListener.onPrepared(mp);
        }
        if (currentVideoPercent != 0) {
            mMediaPlayer.seekTo(currentVideoPercent);
        }
        setSpeed(1);
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
    public void onCompletion(MediaPlayer mp) {
        if (completionListener != null) {
            completionListener.onCompletion(mp);
        }
        if (mPlayerEventListener != null) {
            mPlayerEventListener.onCompletion();
        }
        Log.i(TAG, "play complete");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (errorListener != null) {
            errorListener.onError(mp, what, extra);
        }
        if (mPlayerEventListener != null) {
            mPlayerEventListener.onError();
        }
        Log.i(TAG, "video error");
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (infoListener != null) {
            infoListener.onInfo(mp, what, extra);
        }
        if (mPlayerEventListener != null) {
            mPlayerEventListener.onInfo(what, extra);
        }
        return false;
    }

    /**
     * 仅仅在播放网络资源的时候调用
     * @param mp
     * @param percent
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (bufferingUpdateListener != null) {
            bufferingUpdateListener.onBufferingUpdate(mp, percent);
        }
        mBufferedPercent = percent;
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener preparedListener) {
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
        currentStatus = PAUSE;
        if (mMediaPlayer != null) {
            currentVideoPercent = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
        }
    }

    public void onResume() {
        currentStatus = RESUME;
        if (mMediaPlayer != null) {
            if (isPlaying()) return;
            openVideo();
        }

    }

    public void onDestroy() {
        if (mMediaPlayer != null) {
            release();
        }
    }

    public void release() {
        if (mMediaPlayer == null) return;
        mMediaPlayer.pause();
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                onDestroy();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public long getDuration() {
        if (mMediaPlayer!=null)
        return mMediaPlayer.getDuration();
        return 0;
    }

    /**
     * 获取缓冲百分比
     */
    @Override
    public int getBufferedPercentage() {
        return mBufferedPercent;
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
        mMediaPlayer.setVolume(v1, v2);
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
        }else {
            Toast.makeText(context, "不支持！", Toast.LENGTH_SHORT).show();
//            Toast.makeText(this,"不支持!".toString(),Toast.LENGTH_LONG).show();
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
        //not support
        return 0;
    }

    @Override
    public boolean isPlaying() {
        if (mMediaPlayer!=null){
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    /**
     * 调整进度
     *
     * @param time
     */
    @Override
    public void seekTo(long time) {
        seekTo((int) time);
    }

    @Override
    public int getCurrentPosition() {
        if (mMediaPlayer!=null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int pausePoaition) {
        mMediaPlayer.seekTo(pausePoaition);
    }

    public void start() {
        mMediaPlayer.start();
    }

    @Override
    public void pause() {
        onPause();
    }

    @Override
    public void stop() {
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

    public void setOnInfoListener(MediaPlayer.OnInfoListener onInfoListener) {
        setInfoListener(onInfoListener);
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        setCompletionListener(onCompletionListener);
    }
}
