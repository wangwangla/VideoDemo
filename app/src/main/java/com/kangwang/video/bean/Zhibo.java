package com.kangwang.video.bean;

import java.io.Serializable;

public class Zhibo implements Serializable {
    private String name;
    private String uri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
