package com.kangwang.video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

//import com.kangwang.ffmpeglibrary.FFmpegVideoPlayer;

public class FFmpegActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg);
        SurfaceView surfaceView = findViewById(R.id.ffmpeg_surface);
        holder = surfaceView.getHolder();
        holder.addCallback(this);

    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

    }
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * This is called immediately before a surface is being destroyed. After
     * returning from this call, you should no longer try to access this
     * surface.  If you have a rendering thread that directly accesses
     * the surface, you must ensure that thread is no longer touching the
     * Surface before returning from this function.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
    private void realse(){
        holder.removeCallback(this);
    }
}