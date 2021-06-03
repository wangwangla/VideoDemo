package com.kangwang.video.factory;

import com.kangwang.video.video.IjkPlayer;
import com.kangwang.video.video.VideoMeidePlayer;

public class PlayFactory {
    public static VideoMeidePlayer getVideoPlayer(){
        return new VideoMeidePlayer();
    }

    public static IjkPlayer getIjkPlayer(){
        return new IjkPlayer();
    }
}
