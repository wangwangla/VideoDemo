package com.kangwang.video.factory;

import android.content.Context;

import com.kangwang.androidmediaplayer.AndroidVideoPlayer;
import com.kangwang.androidmediaplayer.base.AbstractPlayer;
import com.kangwang.video.video.VideoMeidePlayer;

public class AndroidPlayFactory<T extends AbstractPlayer> extends PlayerFactory<AndroidVideoPlayer>{
    public static VideoMeidePlayer getVideoPlayer(){
        return new VideoMeidePlayer();
    }

    @Override
    public AndroidVideoPlayer createPlayer(Context context) {
        return new AndroidVideoPlayer(context);
    }
}
