package com.example.mediaextractor.extractor;

import android.annotation.SuppressLint;
import android.media.MediaFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mediaextractor.utils.MediaExtractorVideoView;

import java.nio.ByteBuffer;

public class VideoExtractor implements IExtractor {
    private String path;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public VideoExtractor(String path){
        this.path = path;
        mMediaExtractor = new MediaExtractorVideoView(path);

    }
    @SuppressLint("NewApi")
    private MediaExtractorVideoView mMediaExtractor ;
    /**
     * 获取音视频格式参数
     */
    @SuppressLint("NewApi")
    @Override
    public MediaFormat getFormat() {
        return mMediaExtractor.getVideoMediaFormat();
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
     * @return
     */
    @Override
    public int getCurrentTimestamp() {
        return mMediaExtractor.getCurrentTimestamp();
    }

    @Override
    public int getSampleFlag() {
        return 0;
    }

    /**
     * Seek到指定位置，并返回实际帧的时间戳
     *
     * @param pos
     */
    @SuppressLint("NewApi")
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
