package com.kangwang.video.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kangwang.androidmediaplayer.AndroidVideo;
import com.kangwang.androidmediaplayer.base.AbstractPlayer;
import com.kangwang.video.factory.AndroidPlayFactory;
import com.kangwang.video.factory.PlayerFactory;

public class AndroidView <P extends AbstractPlayer> extends FrameLayout implements UIContoller{
    private PlayerFactory<P> playerFactory;
    private P player;
    private Uri uri;

    public AndroidView(@NonNull Context context) {
        super(context);
    }

    public AndroidView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AndroidView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        switchEngine();
    }

    private void switchEngine(){
        playerFactory = new AndroidPlayFactory();
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setUri(String path){
        this.uri = Uri.parse(path);
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
}
