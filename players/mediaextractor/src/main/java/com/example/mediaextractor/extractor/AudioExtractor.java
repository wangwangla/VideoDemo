package com.example.mediaextractor.extractor;

import android.media.MediaFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mediaextractor.base.IExtractor;
import com.example.mediaextractor.utils.MediaExtractorVideoView;

import java.nio.ByteBuffer;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class AudioExtractor implements IExtractor {
    private MediaExtractorVideoView mMediaExtractor;

    public AudioExtractor(String path){

        mMediaExtractor = new MediaExtractorVideoView(path);
    }

    /**
     * 获取音视频格式参数
     */
    @Override
    public MediaFormat getFormat() {
        return mMediaExtractor.getVideoAudioFormat();
    }

    /**
     * 读取音视频数据
     *
     * @param byteBuffer
     */
    @Override
    public int readBuffer(ByteBuffer byteBuffer) {
        return mMediaExtractor.readBuffer(byteBuffer);
    }

    /**
     * 获取当前帧时间
     *
     * @return
     */
    @Override
    public int getCurrentTimestamp() {
        return mMediaExtractor.getCurrentTimestamp();
    }

    @Override
    public int getSampleFlag() {
        return mMediaExtractor.getSampleFlag();
    }

    /**
     * Seek到指定位置，并返回实际帧的时间戳
     *
     * @param pos
     */
    @Override
    public Long seek(Long pos) {
        return mMediaExtractor.seek(pos);
    }

    @Override
    public void setStartPos(int pos) {
        mMediaExtractor.setStartPos(pos);
    }

    /**
     * 停止读取数据
     */
    @Override
    public void stop() {
        mMediaExtractor.stop();
    }

}
