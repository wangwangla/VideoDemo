package com.kangwang.video.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {
    /**
     * 设置 时间
     */
    public  static String formatSystemTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }
}
