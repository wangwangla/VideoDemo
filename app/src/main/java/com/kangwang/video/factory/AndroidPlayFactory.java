package com.kangwang.video.factory;

import android.content.Context;

import com.kangwang.androidmediaplayer.AndroidVideo;
import com.kangwang.androidmediaplayer.base.AbstractPlayer;
import com.kangwang.video.video.IjkPlayer;
import com.kangwang.video.video.VideoMeidePlayer;

public class AndroidPlayFactory<T extends AbstractPlayer> extends PlayerFactory<AndroidVideo>{
    public static VideoMeidePlayer getVideoPlayer(){
        return new VideoMeidePlayer();
    }

    @Override
    public AndroidVideo createPlayer(Context context) {
        return new AndroidVideo(context);
    }
}
