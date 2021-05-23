package com.kangwang.video.ui.activity;

import android.media.MediaPlayer;
import android.widget.VideoView;

public class VideoPreparedListener implements MediaPlayer.OnPreparedListener {
    private VideoView videoView;
    public VideoPreparedListener(VideoView videoView){
        this.videoView = videoView;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        videoView.start();
    }
}
