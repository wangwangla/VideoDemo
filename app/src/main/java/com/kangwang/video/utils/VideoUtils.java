package com.kangwang.video.utils;

import java.text.DecimalFormat;

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

    public static String kbToM(long sizeKb){
//        long durationInMillis = 3600000; // 示例视频持续时间为1小时
        // 计算小时、分钟和秒数
        double sizeMB = (sizeKb / 1024.0 / 1024.0);
        int v = (int)sizeMB / 1024;
        if (v<=0){

            // 创建 DecimalFormat 对象，指定保留两位小数的格式
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
// 格式化大小值，并获取格式化后的结果
            String formattedSize = decimalFormat.format(sizeMB);
            return formattedSize+"M"+"  ";
//            return String.format("%02d   ",sizeMB);
        }else {
            // 创建 DecimalFormat 对象，指定保留两位小数的格式
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
// 格式化大小值，并获取格式化后的结果
            String formattedSize = decimalFormat.format((sizeMB / 1024.0f));
            return formattedSize+"  ";
        }
    }
}
