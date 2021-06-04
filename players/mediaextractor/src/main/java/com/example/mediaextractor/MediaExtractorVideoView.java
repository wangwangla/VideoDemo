package com.example.mediaextractor;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;

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
//
//    https://zhuanlan.zhihu.com/p/145588749
}
