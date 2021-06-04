package com.kangwang.video.factory;

import android.content.Context;

import com.kangwang.androidmediaplayer.base.AbstractPlayer;

public abstract class PlayerFactory<T extends AbstractPlayer> {

    public abstract T createPlayer(Context context);
}
