package com.kangwang.video.factory;

import android.content.Context;

import com.example.player_base.AbstractPlayer;
import com.example.player_base.PlayerFactory;
import com.kangwang.androidmediaplayer.AndroidPlayFactory;
import com.kangwang.video.type.PlayerType;

import xyz.doikki.videoplayer.exo.ExoMediaPlayer;
import xyz.doikki.videoplayer.exo.ExoMediaPlayerFactory;

/**
 * @Auther jian xian si qi
 * @Date 2023/5/14 20:17
 */
public class PlayFactoryUtils {
    public static PlayerFactory<AbstractPlayer> playInstance(PlayerType type, Context context){
        PlayerFactory player = null;
        if (type == PlayerType.exo){
            player = new ExoMediaPlayerFactory();
        }else if (type == PlayerType.mediaplay){
            player = new AndroidPlayFactory();
        }
        if (player == null){
            player = new AndroidPlayFactory();
        }
        return player;
    }
}
