package com.example.mediaextractor;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

class MMuxer {
    private String TAG = "MMuxer";
    private String mPath;
    private MediaMuxer mMediaMuxer = null;
    private int mVideoTrackIndex = -1;
    private int mAudioTrackIndex = -1;
    private boolean mIsAudioTrackAdd = false;
    private boolean mIsVideoTrackAdd = false;
    private boolean mIsStart = false;

    @SuppressLint("NewApi")
    void init (){
        String fileName = "LVideo_" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + ".mp4";
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/";
        mPath = filePath + fileName;
        try {
            mMediaMuxer = new MediaMuxer(mPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    void addVideoTrack(MediaFormat mediaFormat) {
        if (mIsVideoTrackAdd) return;
        if (mMediaMuxer != null) {
            try {
                mVideoTrackIndex = mMediaMuxer.addTrack(mediaFormat);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            Log.i(TAG, "添加视频轨道");
            mIsVideoTrackAdd = true;
            startMuxer();
        }
    }

    @SuppressLint("NewApi")
    void addAudioTrack(MediaFormat mediaFormat) {
        if (mIsAudioTrackAdd) return;
        if (mMediaMuxer != null) {
            try {
                mAudioTrackIndex = mMediaMuxer.addTrack(mediaFormat);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            Log.i(TAG, "添加音频轨道");
            mIsAudioTrackAdd = true;
            startMuxer();
        }
    }

    void setNoAudio() {
        if (mIsAudioTrackAdd) return;
                mIsAudioTrackAdd = true;
        startMuxer();
    }

    void setNoVideo() {
        if (mIsVideoTrackAdd) return;
                mIsVideoTrackAdd = true;
        startMuxer();
    }

    @SuppressLint("NewApi")
    void writeVideoData(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        if (mIsStart) {
            mMediaMuxer.writeSampleData(mVideoTrackIndex, byteBuffer, bufferInfo);
        }
    }

    void writeAudioData(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        if (mIsStart) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mMediaMuxer.writeSampleData(mAudioTrackIndex, byteBuffer, bufferInfo);
            }
        }
    }

    @SuppressLint("NewApi")
    private void startMuxer() {
        if (mIsAudioTrackAdd && mIsVideoTrackAdd) {
            mMediaMuxer.start();
            mIsStart = true;
            Log.i(TAG, "启动封装器");
        }
    }

    @SuppressLint("NewApi")
    void release() {
        mIsAudioTrackAdd = false;
        mIsVideoTrackAdd = false;
        try {
            mMediaMuxer.stop();
            mMediaMuxer.release();
            mMediaMuxer = null;
            Log.i(TAG, "退出封装器");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
