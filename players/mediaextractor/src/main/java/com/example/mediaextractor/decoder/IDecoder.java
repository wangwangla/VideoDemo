package com.example.mediaextractor.decoder;

import android.media.MediaFormat;

import com.example.mediaextractor.base.IDecoderProgress;
import com.example.mediaextractor.base.IDecoderStateListener;

/**
 * 操作是一个费时操作，所以放入线程来操作
 */
public interface IDecoder extends Runnable {
    /**
     * 暂停解码
     */
    void pause();

    /**
     * 继续解码
     */
    void goOn();

    /**
     * 停止解码
     */
    void stop();

    /**
     * 是否正在解码
     */
    boolean isDecoding();
    /**
     * 跳转到指定位置,并播放
     * 并返回实际帧的时间
     *
     * @param pos: 毫秒
     * @return 实际时间戳，单位：毫秒
     */
    Long seekAndPlay(Long pos);
    /**
     * 是否正在快进
     */
    boolean isSeeking();

    /**
     * 是否停止解码
     */
    boolean isStop();
    /**
     * 设置尺寸监听器
     */
    void setSizeListener(IDecoderProgress iDecoderProgress0);
    /**
     * 设置状态监听器
     */
    void setStateListener(IDecoderStateListener iDecoderStateListener);

    /**
     * 获取视频宽
     */
    int getWidth();

    /**
     * 获取视频高
     */
    int getHeight();

    /**
     * 获取视频长度
     */
    Long getDuration();

    /**
     * 当前帧
     * @return
     */
    Long getCurTimeStamp();

    /**
     * 获取视频旋转角度
     */
    int getRotationAngle();
    /**
     * 获取音视频对应的格式参数
     */
    MediaFormat getMediaFormat();
    /**
     * 获取音视频对应的媒体轨道
     */
    int getTrack();
    /**
     * 获取解码的文件路径
     */
    String getFilePath();
    /**
     * 无需音视频同步
     */
    IDecoder withoutSync();

    IDecoder asCropper();
}
