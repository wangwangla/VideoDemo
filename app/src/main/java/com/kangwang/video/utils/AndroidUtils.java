package com.kangwang.video.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/29 17:32
 */
public class AndroidUtils {
    public static void fullScreen(Activity activity){
        // 全屏
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static void noTitle(Activity activity){
        // 无title
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
