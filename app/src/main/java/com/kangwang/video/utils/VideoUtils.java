package com.kangwang.video.utils;

/**
 * @Auther jian xian si qi
 * @Date 2023/5/8 9:15
 */
public class VideoUtils {
    public static String longToTimeFormat(long durationInMillis){
//        long durationInMillis = 3600000; // 示例视频持续时间为1小时
        // 计算小时、分钟和秒数
        int hours = (int) (durationInMillis / (1000 * 60 * 60));
        int minutes = (int) ((durationInMillis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) ((durationInMillis % (1000 * 60)) / 1000);
        // 格式化为时分秒格式
        String formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        System.out.println(formattedDuration); // 输出结果：01:00:00
        return formattedDuration;
    }
}
