package com.example.mediaextractor;

import android.media.MediaCodec;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.ByteBuffer;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class Frame {
    ByteBuffer buffer;
    MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setBufferInfo(MediaCodec.BufferInfo info) {
        bufferInfo.set(info.offset, info.size, info.presentationTimeUs, info.flags);
    }
}