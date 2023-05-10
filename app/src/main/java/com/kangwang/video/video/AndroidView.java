package com.kangwang.video.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.player_base.AbstractPlayer;
import com.kangwang.video.factory.AndroidPlayFactory;
import com.example.player_base.PlayerFactory;

public class AndroidView <P extends AbstractPlayer> extends FrameLayout implements UIContoller{
    private PlayerFactory<P> playerFactory;
    private P player;

    public AndroidView(@NonNull Context context) {
        this(context,null);
    }

    public AndroidView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        switchEngine(context);
    }

    public AndroidView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        switchEngine(context);
    }

    private void switchEngine(Context context){
        playerFactory = new AndroidPlayFactory();
        player = playerFactory.createPlayer(context);
        addView(player);
        LayoutParams params = (LayoutParams) player.getLayoutParams();
        params.gravity = Gravity.CENTER;
        player.setLayoutParams(params);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public long getDuration() {
        return player.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(long pos) {
        player.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getBufferedPercentage() {
        return player.getBufferedPercentage();
    }

    @Override
    public void startFullScreen() {

    }

    @Override
    public void stopFullScreen() {

    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void setMute(boolean isMute) {

    }

    @Override
    public boolean isMute() {
        return false;
    }

    @Override
    public void setScreenScaleType(int screenScaleType) {

    }

    @Override
    public void setSpeed(float speed) {
        player.setSpeed(speed);
    }

    @Override
    public float getSpeed() {
        return player.getSpeed();
    }

    @Override
    public long getTcpSpeed() {
        return 0;
    }

    @Override
    public void replay(boolean resetPosition) {

    }

    @Override
    public void setMirrorRotation(boolean enable) {

    }

    @Override
    public Bitmap doScreenShot() {
        return null;
    }

    @Override
    public int[] getVideoSize() {
        return new int[2];
    }

    @Override
    public void setRotation(float rotation) {
        player.setRotation(rotation);
    }

    @Override
    public void startTinyScreen() {

    }

    @Override
    public void stopTinyScreen() {

    }

    @Override
    public boolean isTinyScreen() {
        return false;
    }

    public void release() {
    }

    public void setDataSource(String path, Object o) {
        player.setDataSource(path,null);
    }

    public void initPlayer(Runnable runnable) {
        player.initPlayer();
        player.setPlayerEventListener(new AbstractPlayer.PlayerEventListener() {
            @Override
            public void onError() {
                Log.d("play","error");
            }

            @Override
            public void onCompletion() {

            }

            @Override
            public void onInfo(int what, int extra) {

            }

            @Override
            public void onPrepared(MediaPlayer mp) {
                runnable.run();
            }

            @Override
            public void onVideoSizeChanged(int width, int height) {

            }
        });
    }
}
