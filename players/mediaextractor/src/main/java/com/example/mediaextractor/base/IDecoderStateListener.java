package com.example.mediaextractor.base;

import com.example.mediaextractor.decoder.BaseDecoder;
import com.example.mediaextractor.frame.Frame;

public interface IDecoderStateListener {
    void decoderPrepare(BaseDecoder baseDecoder);
    void decoderReady(BaseDecoder decoder);
    void decoderRunning(BaseDecoder decoder);
    void decoderPause(BaseDecoder decoder);
    void decodeOneFrame(BaseDecoder baseDecoder, Frame frame);
    void decoderFinish(BaseDecoder decodeJob);
    void decoderDestroy(BaseDecoder decodeJob);
    void decoderError(BaseDecoder baseDecoder, String msg);
}
