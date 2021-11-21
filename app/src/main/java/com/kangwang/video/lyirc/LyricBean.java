package com.kangwang.video.lyirc;

public class LyricBean {
    private long startTime;
    private String content;
    private long sleepTime;

    public LyricBean(long startTime, String content) {
        this.startTime = startTime;
        this.content = content;
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public String toString() {
        return "LyricBean{" +
                "startTime=" + startTime +
                ", content='" + content + '\'' +
                '}';
    }

    public long getStartTime() {
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
