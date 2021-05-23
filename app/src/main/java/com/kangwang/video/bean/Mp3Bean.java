package com.kangwang.video.bean;

import android.database.Cursor;
import android.provider.MediaStore;

import java.io.Serializable;

public class Mp3Bean implements Serializable {
    private String data;
    private String title;
    private int time;
    private long size;

    public static Mp3Bean getInstance(Cursor cursor){
        Mp3Bean bean = new Mp3Bean();
        if (cursor == null || cursor.getCount() == 0){
            return bean;
        }
        bean.data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        bean.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        bean.time =cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        bean.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
        return bean;
    }

    @Override
    public String toString() {
        return "Mp3Bean{" +
                "data='" + data + '\'' +
                ", title='" + title + '\'' +
                ", time=" + time +
                ", size=" + size +
                '}';
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
