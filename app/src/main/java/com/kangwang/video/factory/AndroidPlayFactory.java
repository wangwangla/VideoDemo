package com.kangwang.video.factory;

import android.content.Context;

import com.example.player_base.AbstractPlayer;
import com.example.player_base.PlayerFactory;
import com.kangwang.androidmediaplayer.AndroidVideoPlayer;

public class AndroidPlayFactory<T extends AbstractPlayer> extends PlayerFactory<AndroidVideoPlayer> {

    @Override
    public AndroidVideoPlayer createPlayer(Context context) {
        return new AndroidVideoPlayer(context);
    }
}
