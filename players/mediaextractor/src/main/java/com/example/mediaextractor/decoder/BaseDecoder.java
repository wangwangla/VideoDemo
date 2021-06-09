package com.example.mediaextractor.decoder;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mediaextractor.base.IDecoder;
import com.example.mediaextractor.state.DecodeState;
import com.example.mediaextractor.base.IDecoderStateListener;
import com.example.mediaextractor.base.IExtractor;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public abstract class BaseDecoder implements IDecoder {
    protected String mFilePath;

    //-------------线程相关------------------------
    /**
     * 解码器是否在运行
     */
    private boolean mIsRunning = true;
    /**
     * 线程等待锁
     */
    private Object mLock = new Object();
    /**
     * 是否可以进入解码
     */
    private boolean mReadyForDecode = false;
    //---------------解码相关-----------------------
    /**
     * 音视频解码器
     */
    protected MediaCodec mCodec = null;
    /**
     * 音视频数据读取器
     */
    protected IExtractor mExtractor = null;

    /**
     * 解解码输入缓存区
     */
    protected ByteBuffer[] mInputBuffers;

    /**
     * 解码输出缓存区
     */
    protected ByteBuffer[] mOutputBuffers;

    /**
     * 解码数据信息
     */
    protected MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();

    private DecodeState mState = DecodeState.STOP;


    /**
     * 流数据是否结束
     */
    private boolean mIsEOS = false;

    protected int mVideoWidth = 0;

    protected int mVideoHeight = 0;

    private long mDuration = 0;

    private long mStartPos = 0;

    private long mEndPos = 0;
    /**
     * 开始解码时间，用于音视频同步
     */
    private long mStartTimeForSync = -1L;

    public BaseDecoder(String path){
        this.mFilePath = path;
    }

    @SuppressLint("NewApi")
    public void run() {
        if (mState == DecodeState.STOP) {
            mState = DecodeState.START;
        }
        mStateListener.decoderPrepare(this);
        //【解码步骤：1. 初始化，并启动解码器】
        if (!init()) return;
        System.out.println("innitsss");

//                Log.i(TAG, "开始解码");

        while (mIsRunning) {
            if (mState != DecodeState.START &&
                    mState != DecodeState.DECODING &&
                    mState != DecodeState.SEEKING) {
//                Log.i(TAG, "进入等待：$mState")

                waitDecode();

                // ---------【同步时间矫正】-------------
                //恢复同步的起始时间，即去除等待流失的时间
                mStartTimeForSync = System.currentTimeMillis() - getCurTimeStamp();
            }

            if (!mIsRunning ||
                    mState == DecodeState.STOP) {
                mIsRunning = false;
                break;
            }
            if (mStartTimeForSync == -1L) {
                mStartTimeForSync = System.currentTimeMillis();
            }

            //如果数据没有解码完毕，将数据推入解码器解码
            if (!mIsEOS) {
                //【解码步骤：2. 见数据压入解码器输入缓冲】
                mIsEOS = pushBufferToDecoder();
            }

            //【解码步骤：3. 将解码好的数据从缓冲区拉取出来】
            int index = pullBufferFromDecoder();
            if (index >= 0) {
                // ---------【音视频同步】-------------
                if (mState == DecodeState.DECODING) {
                    sleepRender();
                }
                //【解码步骤：4. 渲染】
                render(mOutputBuffers[index], mBufferInfo);
                //【解码步骤：5. 释放输出缓冲】
                mCodec.releaseOutputBuffer(index, true);
                if (mState == DecodeState.START) {
                    mState = DecodeState.PAUSE;
                }
            }
            //【解码步骤：6. 判断解码是否完成】
            if (mBufferInfo.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
//                Log.i(TAG, "解码结束")
                mState = DecodeState.FINISH;
                mStateListener.decoderFinish(this);
            }
        }
        doneDecode();
        release();
    }

    protected abstract void doneDecode();

    /**
     * 渲染
     */
    abstract void render(ByteBuffer outputBuffer,MediaCodec.BufferInfo bufferInfo);

    private boolean init() {
        if (mFilePath.isEmpty()) {
            if (!mFilePath.startsWith("http")) {
                if (!new File(mFilePath).exists()) {
//            Log.w(TAG, "文件路径为空")
                    mStateListener.decoderError(this, "文件路径为空");

                    return false;
                }
            }
        }

        if (!check()) return false;

        //初始化数据提取器
        mExtractor = initExtractor(mFilePath);
        System.out.println(mExtractor+"+===================");
        if (mExtractor == null ||
                mExtractor.getFormat() == null) {
//            Log.w(TAG, "无法解析文件")
            return false;
        }

        //初始化参数
        if (!initParams()) return false;
        System.out.println("initParams");
        //初始化渲染器
        if (!initRender()) return false;

        System.out.println("iniRender");
        //初始化解码器
        if (!initCodec()) return false;
        return true;
    }

    protected abstract IExtractor initExtractor(String mFilePath);

    protected abstract boolean initRender();

    protected abstract boolean check();

    @SuppressLint("NewApi")
    private boolean initParams() {
        try {
            MediaFormat format = mExtractor.getFormat();
            mDuration = format.getLong(MediaFormat.KEY_DURATION) / 1000;
            System.out.println("dfhdfhdhfd");
            if (mEndPos == 0L) mEndPos = mDuration;
            initSpecParams(mExtractor.getFormat());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected abstract void initSpecParams(MediaFormat format);

    @SuppressLint("NewApi")
    private boolean initCodec() {
        try {
            String type = mExtractor.getFormat().getString(MediaFormat.KEY_MIME);
            try {
                mCodec = MediaCodec.createDecoderByType(type);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiii");
            if (!configCodec(mCodec, mExtractor.getFormat())) {
                System.out.println("222222222222222222222222222");
//                waitDecode();
            }
            mCodec.start();

            System.out.println("33333333333333333333333");

            mInputBuffers = mCodec.getInputBuffers();
            mOutputBuffers = mCodec.getOutputBuffers();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean pushBufferToDecoder(){
        int inputBufferIndex = mCodec.dequeueInputBuffer(2000);
        boolean isEndOfStream = false;

        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = mInputBuffers[inputBufferIndex];
            int sampleSize = mExtractor.readBuffer(inputBuffer);

            if (sampleSize < 0) {
                //如果数据已经取完，压入数据结束标志：MediaCodec.BUFFER_FLAG_END_OF_STREAM
                mCodec.queueInputBuffer(inputBufferIndex, 0, 0,
                        0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                isEndOfStream = true;
            } else {
                mCodec.queueInputBuffer(inputBufferIndex, 0,
                        sampleSize, mExtractor.getCurrentTimestamp(), 0);
            }
        }
        return isEndOfStream;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private int pullBufferFromDecoder(){
        // 查询是否有解码完成的数据，index >=0 时，表示数据有效，并且index为缓冲区索引
        int index = mCodec.dequeueOutputBuffer(mBufferInfo, 1000);
        if (index>=0) {
            mOutputBuffers = mCodec.getOutputBuffers();
                return index;
        } else {
            return -1;
        }
    }

    private void sleepRender() {
        long passTime = System.currentTimeMillis() - mStartTimeForSync;
        long curTime = getCurTimeStamp();
        if (curTime > passTime) {
            try {
                Thread.sleep(curTime - passTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("NewApi")
    private void release() {
        try {
            mState = DecodeState.STOP;
            mIsEOS = false;
            mExtractor.stop();
            mCodec.stop();
            mCodec.release();
            mStateListener.decoderDestroy(this);
        } catch ( Exception e) {
        }
    }

    /**
     * 解码线程进入等待
     */
    private void waitDecode() {
//        try {
//            if (mState == DecodeState.PAUSE) {
//                mStateListener.decoderPause(this);
//            }
//            synchronized(mLock) {
//                mLock.wait();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 暂停解码
     */
    @Override
    public void pause() {
        mState = DecodeState.DECODING;
    }

    /**
     * 继续解码
     */
    @Override
    public void goOn() {
        mState = DecodeState.DECODING;
        notifyDecode();
    }

    protected void notifyDecode() {

        /**
         * 通知解码线程继续运行
         */
        synchronized(mLock) {
            mLock.notifyAll();
        }
        if (mState == DecodeState.DECODING) {
//            mStateListener.decoderRunning(this);
        }

    }

    /**
     * 停止解码
     */
    @Override
    public void stop() {
        mState = DecodeState.STOP;
        mIsRunning = false;
        notifyDecode();
    }

    /**
     * 是否正在解码
     */
    @Override
    public boolean isDecoding() {
        return mState == DecodeState.DECODING;
    }

    /**
     * 是否正在快进
     */
    @Override
    public boolean isSeeking() {
        return mState == DecodeState.SEEKING;
    }

    /**
     * 是否停止解码
     */
    @Override
    public boolean isStop() {
        return mState == DecodeState.STOP;
    }

    protected IDecoderStateListener mStateListener;

    /**
     * 设置状态监听器
     *
     * @param iDecoderStateListener
     */
    @Override
    public void setStateListener(IDecoderStateListener iDecoderStateListener) {
        this.mStateListener = iDecoderStateListener;
    }

    /**
     * 获取视频宽
     */
    @Override
    public int getWidth() {
        return mVideoWidth;
    }

    /**
     * 获取视频高
     */
    @Override
    public int getHeight() {
        return mVideoHeight;
    }

    /**
     * 获取视频长度
     */
    @Override
    public Long getDuration() {
        return mDuration;
    }

    /**
     * 获取视频旋转角度
     */
    @Override
    public int getRotationAngle() {
        return 0;
    }

    /**
     * 获取音视频对应的格式参数
     */
    @Override
    public MediaFormat getMediaFormat() {
        return mExtractor.getFormat();
    }

    /**
     * 获取音视频对应的媒体轨道
     */
    @Override
    public int getTrack() {
        return 0;
    }

    /**
     * 获取解码的文件路径
     */
    @Override
    public String getFilePath() {
        return mFilePath;
    }

    /**
     * 无需音视频同步
     */
    @Override
    public IDecoder withoutSync() {
        return null;
    }


    abstract boolean configCodec(MediaCodec codec, MediaFormat format);

}

