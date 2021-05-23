package com.kangwang.video.bean;

import android.database.Cursor;
import android.provider.MediaStore;

import java.io.Serializable;

public class VideoBean implements Serializable {
    private String title;
    private String data;
    private int time;
    private long size;

    @Override
    public String toString() {
        return "VideoBean{" +
                "title='" + title + '\'' +
                ", data='" + data + '\'' +
                ", time=" + time +
                ", size=" + size +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public static VideoBean getInstance(Cursor cursor){
        VideoBean bean = new VideoBean();
        if (cursor == null || cursor.getCount() == 0){
            return bean;
        }
        bean.time = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
        bean.title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
        bean.data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        bean.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));

        return bean;
    }
}
