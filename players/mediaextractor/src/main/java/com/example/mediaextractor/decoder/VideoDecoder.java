package com.example.mediaextractor.decoder;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.mediaextractor.frame.Frame;
import com.example.mediaextractor.base.IDecoderProgress;
import com.example.mediaextractor.base.IDecoderStateListener;
import com.example.mediaextractor.extractor.IExtractor;
import com.example.mediaextractor.extractor.VideoExtractor;

import java.nio.ByteBuffer;

public class VideoDecoder extends BaseDecoder {
    private SurfaceView mSurfaceView;
    private Surface mSurface;
    public VideoDecoder(String path, SurfaceView surfaceView, Surface surface){
        super(path);
        this.mSurface = surface;
        this.mSurfaceView = surfaceView;
        setStateListener(new IDecoderStateListener() {
            @Override
            public void decoderPrepare(BaseDecoder baseDecoder) {

            }

            @Override
            public void decoderReady(BaseDecoder decoder) {

            }

            @Override
            public void decoderRunning(BaseDecoder decoder) {

            }

            @Override
            public void decoderPause(BaseDecoder decoder) {

            }

            @Override
            public void decodeOneFrame(BaseDecoder baseDecoder, Frame frame) {

            }

            @Override
            public void decoderFinish(BaseDecoder decodeJob) {

            }

            @Override
            public void decoderDestroy(BaseDecoder decodeJob) {

            }

            @Override
            public void decoderError(BaseDecoder baseDecoder, String msg) {

            }
        });
    }

    @Override
    protected void doneDecode() {

    }

    /**
     * 渲染
     *
     * @param outputBuffer
     * @param bufferInfo
     */
    @Override
    void render(ByteBuffer outputBuffer, MediaCodec.BufferInfo bufferInfo) {

    }

    @Override
    protected IExtractor initExtractor(String mFilePath) {
        return new VideoExtractor(mFilePath);
    }

    @Override
    protected boolean initRender() {
        return true;
    }

    public boolean check(){
        if (mSurfaceView == null && mSurface == null){
            mStateListener.decoderError(this,"没有显示器");
            return false;
        }
        return true;
    }

    @Override
    protected void initSpecParams(MediaFormat format) {

    }

    @SuppressLint("NewApi")
    boolean configCodec(final MediaCodec codec, final MediaFormat format) {
        if (mSurface != null) {
            codec.configure(format, mSurface , null, 0);
            notifyDecode();
        } else {
            mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder holder) {
                    mSurface = holder.getSurface();
                    configCodec(codec, format);
                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

                }
            });

            return false;
        }
        return true;
    }

    /**
     * 跳转到指定位置,并播放
     * 并返回实际帧的时间
     *
     * @param pos : 毫秒
     * @return 实际时间戳，单位：毫秒
     */
    @Override
    public Long seekAndPlay(Long pos) {
        return null;
    }

    /**
     * 设置尺寸监听器
     *
     * @param iDecoderProgress0
     */
    @Override
    public void setSizeListener(IDecoderProgress iDecoderProgress0) {

    }

    /**
     * 当前帧
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Long getCurTimeStamp() {
        return mBufferInfo.presentationTimeUs / 1000;
    }

    @Override
    public IDecoder asCropper() {
        return this;
    }
}
