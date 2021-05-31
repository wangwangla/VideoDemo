package com.kangwang.video.lyirc;

public class LyricBean {
    private int startTime;
    private String content;

    public LyricBean(int startTime, String content) {
        this.startTime = startTime;
        this.content = content;
    }

    @Override
    public String toString() {
        return "LyricBean{" +
                "startTime=" + startTime +
                ", content='" + content + '\'' +
                '}';
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
