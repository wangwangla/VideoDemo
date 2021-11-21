package com.kangwang.video.lyirc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class LyricUtils {
    private ArrayList<LyricBean> lyrics;
    public void readLyricFile(File file){
        if (file == null || !file.exists()){
            lyrics = null;
        }else {
            lyrics = new ArrayList<>();
//            1.解析
//            2.排序
//            3.计算美剧高亮
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
                String line = null;
                while ((line = reader.readLine())!=null){
                    line = parseLyric(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String parseLyric(String line){
        int pos1 = line.indexOf("[");
        int pos2 = line.indexOf("]");
        if (pos1 == 0 & pos2 !=-1){
            long[] timePoints = new long[getCountTag(line)];
            String startTime  = line.substring(pos1+1,pos2);
            timePoints[0] = strtime2long(startTime);
            if (timePoints[0]==-1){
                return "";
            }
            int i = 1;
            String content = line;
            while (pos1 == 0 & pos2 != -1){
                content = content.substring(pos2 +1);
                pos1 = content.indexOf("[");
                pos2 = content.indexOf("]");
                if (pos2!=-1){
                    startTime = content.substring(pos1+1,pos2);
                    timePoints[i] = strtime2long(startTime);
                    if (timePoints[i] == -1){
                        return "";
                    }
                    i++;
                }
            }

            for (int i1 = 0; i1 < timePoints.length; i1++) {
                lyrics.add(new LyricBean(timePoints[i1],content));
            }

//            排序
            Collections.sort(lyrics,new Sort());

//            计算高亮
            for (int i1 = 0; i1 < lyrics.size(); i1++) {
                LyricBean lyricBean = lyrics.get(i);
                if (i1+1<lyrics.size()){
                    LyricBean t = lyrics.get(i1+1);
                    lyricBean.setSleepTime(t.getStartTime()-lyricBean.getStartTime());
                }
            }
        }
        return null;
    }

    class Sort implements Comparator<LyricBean>{

        @Override
        public int compare(LyricBean o1, LyricBean o2) {
            if (o1.getStartTime() < o2.getStartTime()){
                return -1;
            }else if (o1.getStartTime() > o2.getStartTime()){
                return 1;
            }else {
                return 0;
            }
        }
    }

    private long strtime2long(String startTime) {
        //1.根据
        try {
            String s1[] = startTime.split(":");
            String s2[] = s1[1].split("\\.");
            long min = Long.parseLong(s1[0]);
            long second = Long.parseLong(s2[0]);
            long mil = Long.parseLong(s2[1]);
            return mil * 60 * 1000 + second * 1000 + min * 10;
        }catch (Exception e) {
            return -1;
        }
    }

    //判断有多少句子
    private int getCountTag(String line) {
        int result = -1;
        String[] left = line.split("\\[");
        String[] right = line.split("\\]");
        if (left.length == 0&&right.length == 0){
            return 1;
        }else if (left.length > right.length){
            return left.length;
        }else {
            return right.length;
        }
    }
}



















