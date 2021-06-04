package com.example.player_base;

import android.content.Context;

import com.example.player_base.AbstractPlayer;

public abstract class PlayerFactory<T extends AbstractPlayer> {

    public abstract T createPlayer(Context context);
}
