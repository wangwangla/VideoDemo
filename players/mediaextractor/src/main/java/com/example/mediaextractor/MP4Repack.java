package com.example.mediaextractor;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mediaextractor.extractor.AudioExtractor;
import com.example.mediaextractor.extractor.VideoExtractor;

import java.nio.ByteBuffer;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class MP4Repack{
    private String TAG = "MP4Repack";
    private String path;

    private AudioExtractor mAExtractor = new AudioExtractor(path);
    private VideoExtractor mVExtractor = new VideoExtractor(path);
    private MMuxer mMuxer = new MMuxer();

    public MP4Repack(String path){
        this.path = path;
    }
    MediaFormat audioFormat;
    MediaFormat videoFormat;
    @SuppressLint("NewApi")
    void start1() {
        audioFormat = mAExtractor.getFormat();
        videoFormat = mVExtractor.getFormat();

        if (audioFormat != null) {
            mMuxer.addAudioTrack(audioFormat);
        } else {
            mMuxer.setNoAudio();
        }
        if (videoFormat != null) {
            mMuxer.addVideoTrack(videoFormat);
        } else {
            mMuxer.setNoVideo();
        }

//        new Thread() {
//            ByteBuffer buffer = ByteBuffer.allocate(500 * 1024);
//            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
//            if (audioFormat!= null) {
//                int size = mAExtractor.readBuffer(buffer)
//                while (size > 0) {
//                    bufferInfo.set(0, size, mAExtractor.getCurrentTimestamp(), mAExtractor.getSampleFlag())
//                    mMuxer.writeAudioData(buffer, bufferInfo)
//                    size = mAExtractor.readBuffer(buffer)
//                }
//            }
//            if (videoFormat != null) {
//                int size = mVExtractor.readBuffer(buffer)
//                while (size > 0) {
//                    bufferInfo.set(0, size, mVExtractor.getCurrentTimestamp(), mVExtractor.getSampleFlag())
//                    mMuxer.writeVideoData(buffer, bufferInfo)
//                    size = mVExtractor.readBuffer(buffer)
//                }
//            }
//            mAExtractor.stop();
//            mVExtractor.stop();
//            mMuxer.release();
////            Log.i(TAG, "MP4 重打包完成")
//        }.start();
    }
}