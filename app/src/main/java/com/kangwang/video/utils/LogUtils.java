package com.kangwang.video.utils;

import android.util.Log;

import java.util.Date;

/**
 * 整个项目的日志
 */
public class LogUtils {
    private static boolean DEBUG = true;

    public static void v(String tag,String msg){
        if(DEBUG){
            Log.v(tag,msg);
        }
    }

    public static void e(String tag,String msg){
        if(DEBUG){
            Log.e(tag,msg);
        }
    }

    public static void w(String tag,String msg){
        if(DEBUG){
            Log.w(tag,msg);
        }
    }

    public static void d(String tag,String msg){
        if(DEBUG){
            Log.d(tag,msg);
        }
    }



}
