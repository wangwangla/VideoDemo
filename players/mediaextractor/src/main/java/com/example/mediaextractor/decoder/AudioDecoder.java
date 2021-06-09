package com.example.mediaextractor.decoder;

import android.annotation.SuppressLint;
import android.media.*;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mediaextractor.base.IDecoder;
import com.example.mediaextractor.frame.Frame;
import com.example.mediaextractor.base.IDecoderProgress;
import com.example.mediaextractor.base.IDecoderStateListener;
import com.example.mediaextractor.extractor.AudioExtractor;
import com.example.mediaextractor.base.IExtractor;

import java.nio.ByteBuffer;

public class AudioDecoder extends BaseDecoder {
    /**采样率*/
    private int mSampleRate = -1;

    /**声音通道数量*/
    private int mChannels = 1;

    /**PCM采样位数*/
    private int mPCMEncodeBit = AudioFormat.ENCODING_PCM_16BIT;

    /**音频播放器*/
    private AudioTrack  mAudioTrack= null;

    /**音频数据缓存*/
    private short[] mAudioOutTempBuf = null;
    public AudioDecoder(String path) {
        super(path);
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
    protected IExtractor initExtractor(String mFilePath) {
        return new AudioExtractor(mFilePath);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initSpecParams(MediaFormat format) {
        try {
            mChannels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            mSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
            if (format.containsKey(MediaFormat.KEY_PCM_ENCODING)) {
                mPCMEncodeBit = format.getInteger(MediaFormat.KEY_PCM_ENCODING);
            } else {
                //如果没有这个参数，默认为16位采样
                mPCMEncodeBit = AudioFormat.ENCODING_PCM_16BIT;
            }
        } catch (Exception e) {
        }
    }

    @SuppressLint("NewApi")
    boolean configCodec(MediaCodec codec, MediaFormat format) {
        codec.configure(format, null , null, 0);
        return true;
    }

    public boolean initRender(){
        int channel = 0;
        if (mChannels == 1) {
            //单声道
            channel = AudioFormat.CHANNEL_OUT_MONO;
        } else {
            //双声道
            channel = AudioFormat.CHANNEL_OUT_STEREO;
        }

        //获取最小缓冲区
        int minBufferSize = AudioTrack.getMinBufferSize(mSampleRate, channel, mPCMEncodeBit);
        mAudioOutTempBuf = new short[(minBufferSize / 2)];

        mAudioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,//播放类型：音乐
                mSampleRate, //采样率
                channel, //通道
                mPCMEncodeBit, //采样位数
                minBufferSize, //缓冲区大小
                AudioTrack.MODE_STREAM); //播放模式：数据流动态写入，另一种是一次性写入

        mAudioTrack.play();
        return true;
    }

    @Override
    protected boolean check() {
        return true;
    }

    @SuppressLint("NewApi")
    void render(ByteBuffer outputBuffer,
                MediaCodec.BufferInfo bufferInfo) {
        if (mAudioOutTempBuf.length < bufferInfo.size / 2) {
            mAudioOutTempBuf = new short[(bufferInfo.size / 2)];
        }
        outputBuffer.position(0);
        outputBuffer.asShortBuffer().get(mAudioOutTempBuf, 0, bufferInfo.size/2);
        mAudioTrack.write(mAudioOutTempBuf, 0, bufferInfo.size / 2);
    }

    public void doneDecode() {
        mAudioTrack.stop();
        mAudioTrack.release();
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
        return 0L;
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
    @SuppressLint("NewApi")
    @Override
    public Long getCurTimeStamp() {
        return mBufferInfo.presentationTimeUs / 1000;
    }

    @Override
    public IDecoder asCropper() {
        return this;
    }
}
