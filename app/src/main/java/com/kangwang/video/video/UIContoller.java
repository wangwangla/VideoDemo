package com.kangwang.video.video;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Map;

public interface UIContoller {

        void start();

        void pause();

        long getDuration();

        long getCurrentPosition();

        void seekTo(long pos);

        boolean isPlaying();

        int getBufferedPercentage();

        void startFullScreen();

        void stopFullScreen();

        boolean isFullScreen();

        void setMute(boolean isMute);

        boolean isMute();

        void setScreenScaleType(int screenScaleType);

        void setSpeed(float speed);

        float getSpeed();

        long getTcpSpeed();

        void replay(boolean resetPosition);

        void setMirrorRotation(boolean enable);

        Bitmap doScreenShot();

        int[] getVideoSize();

        void setRotation(float rotation);

        void startTinyScreen();

        void stopTinyScreen();

        boolean isTinyScreen();
    }
