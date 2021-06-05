package com.example.mediaextractor.utils;

import android.annotation.SuppressLint;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MediaExtractorVideoView {
    /**音视频分离器*/
    private MediaExtractor mExtractor = null;

    /**音频通道索引*/
    private int mAudioTrack = -1;

    /**视频通道索引*/
    private int mVideoTrack = -1;

    /**当前帧时间戳*/
    private int mCurSampleTime = 0;

    /**开始解码时间点*/
    private int mStartPos = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public MediaExtractorVideoView(String path){
        mExtractor = new MediaExtractor();
        try {
            mExtractor.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public MediaFormat getVideoMediaFormat(){
        for (int i = 0; i < mExtractor.getTrackCount(); i++) {
            MediaFormat trackFormat = mExtractor.getTrackFormat(i);
            String mime = trackFormat.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video/")) {
                mVideoTrack = i;
                break;
            }
        }
        if (mVideoTrack>=0)return mExtractor.getTrackFormat(mVideoTrack);
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public MediaFormat getVideoAudioFormat(){
        for (int i = 0; i < mExtractor.getTrackCount(); i++) {
            MediaFormat trackFormat = mExtractor.getTrackFormat(i);
            String mime = trackFormat.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
                mAudioTrack = i;
                break;
            }
        }
        if (mAudioTrack>=0)return mExtractor.getTrackFormat(mAudioTrack);
        return null;
    }

    @SuppressLint("NewApi")
    public int readBuffer(ByteBuffer byteBuffer){
        byteBuffer.clear();
        selectSourceTrack();
        int readSampleData = mExtractor.readSampleData(byteBuffer, 0);
        if (readSampleData < 0)return -1;
        mCurSampleTime = (int) mExtractor.getSampleTime();
        mExtractor.advance();
        return readSampleData;
    }

    @SuppressLint("NewApi")
    private void selectSourceTrack() {
        if (mVideoTrack >= 0) {
            mExtractor.selectTrack(mVideoTrack);
        } else if (mAudioTrack >= 0) {
            mExtractor.selectTrack(mAudioTrack);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Long seek(Long pos){
        mExtractor.seekTo(pos,MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
        return mExtractor.getSampleTime();
    }

    @SuppressLint("NewApi")
    public void stop(){
        mExtractor.release();
        mExtractor = null;
    }

    int getVdieoTrack(){
        return mVideoTrack;
    }

    int getmAudioTrack(){
        return mAudioTrack;
    }

    public void setStartPos(int pos){
        mStartPos = pos;
    }

    /**
     * 获取当前帧时间
     * @return
     */
    public int getCurrentTimestamp(){
        return mCurSampleTime;
    }
    /**当前帧标志*/
    private int mCurSampleFlag = 0;
    public int getSampleFlag(){
        return mCurSampleFlag;
    }
//
//    https://zhuanlan.zhihu.com/p/145588749
}
