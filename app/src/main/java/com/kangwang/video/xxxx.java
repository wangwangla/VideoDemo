package com.kangwang.video;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class xxxx extends SurfaceView {

    public xxxx(Context context) {
        super(context);
    }

    public xxxx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public xxxx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getHolder().addCallback(mSHCallback);
    }

    private MediaPlayer player;
    private SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            player.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        }
    };
}
