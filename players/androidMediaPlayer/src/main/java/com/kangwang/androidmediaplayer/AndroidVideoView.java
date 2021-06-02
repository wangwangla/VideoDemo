package com.kangwang.androidmediaplayer;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

public class AndroidVideoView extends Surface implements SurfaceHolder.Callback {
    public AndroidVideoView(@NonNull SurfaceControl from) {
        super(from);
    }

    public AndroidVideoView(SurfaceTexture surfaceTexture) {
        super(surfaceTexture);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}
