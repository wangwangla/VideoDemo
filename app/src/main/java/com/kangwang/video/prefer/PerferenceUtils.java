package com.kangwang.video.prefer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;

/**
 * @Auther jian xian si qi
 * @Date 2023/5/11 21:52
 */
public class PerferenceUtils {
    private static PerferenceUtils sInstance;
    private static SharedPreferences mPreferences;
    private static Context context;
    private ConnectivityManager connManager = null;

    public PerferenceUtils(final Context context) {
        this.context = context;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static final PerferenceUtils getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new PerferenceUtils(context.getApplicationContext());
        }
        return sInstance;
    }


//    public void setFullUnlocked(final boolean b) {
//        final SharedPreferences.Editor editor = mPreferences.edit();
//        editor.putBoolean(FULL_UNLOCKED, b);
//        editor.apply();
//    }

//    public boolean alwaysLoadAlbumImagesFromLastfm() {
//        return mPreferences.getBoolean(ALWAYS_LOAD_ALBUM_IMAGES_LASTFM, false);
//    }
}


