package com.kangwang.video.bean;

/**
 * @Auther jian xian si qi
 * @Date 2023/5/7 13:44
 *
 * _ID:20
 * _DATA: /storage/emulated/0/$MuMu共享文件夹/oceans.mp4
 * DISPLAYNAME: oceans.mp4
 * SIZE: 23014356
 * MIMETYPE: video/mp4
 * DATEADDED: 1683437559
 * DATEMODIFIED: 1683436548
 * TITLE: oceans
 * DURATION: 46613
 * ARTIST: <unknown>
 * ALBUM: $MuMu共享文件夹
 * BOOKMARK: null
 * : ==================================
 */
public class VideoBean {
    private long id;
    private String name;
    private long size;
    private long duration;
    private String path;

    @Override
    public String toString() {
        return "VideoBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", duration=" + duration +
                ", path='" + path + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setPath(String path) {
//        this.path = path;http://badong-live21.cjyun.org/10095/s10095-bdtv1.m3u8?auth_key=1672502399-0-0-fd35f4e44c5974d838a03d4165643e7d
        this.path = "https://livedoc.cgtn.com/500d/prog_index.m3u8";
    }

    public String getPath() {
        return path;
    }
}
