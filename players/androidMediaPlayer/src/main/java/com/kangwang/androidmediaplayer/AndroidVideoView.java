package com.kangwang.androidmediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import java.util.Map;

public class AndroidVideoView
        extends SurfaceView
        implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnBufferingUpdateListener{
    private final String TAG = getClass().getName();
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder surfaceHolder;
    private Uri mUri;
    private Map<String, String> mHeaders;
    private Context context;
    private MediaPlayer.OnPreparedListener preparedListener;
    private MediaPlayer.OnCompletionListener completionListener;
    private MediaPlayer.OnErrorListener errorListener;
    private MediaPlayer.OnInfoListener infoListener;
    private MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener;
    private int mCurrentBufferPercentage = 0;
    private int currentVideoPercent = 0;
    private int mVideoWidth;
    private int mVideoHeight;
    private final int PAUSE = 1;
    private final int PLAY = 2;
    private int current = 0;

    public AndroidVideoView(Context context) {
        super(context);
        init(context);
    }

    public AndroidVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AndroidVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                surfaceHolder = holder;
                if (current == PAUSE)return;
                holder.getSurface();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                surfaceHolder = holder;
                if (current == PAUSE)return;
                openVideo();
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
            }
        });
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
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
        open();
    }

    private void open() {
        openVideo();
        requestLayout();
        invalidate();
    }

    private void openVideo() {
        System.out.println("open video");
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
            mCurrentBufferPercentage = 0;
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
            current = PLAY;
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
        current = PAUSE;
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

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public void pause() {
        onResume();
    }

    public void stop(){
        mMediaPlayer.pause();
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
