package com.kangwang.ffmpeglibrary.play;

public class FFmepegVideo {
    static {
        System.loadLibrary("play");
    }
    public FFmepegVideo(){
        System.out.println(native_version()+"==================");
    }

    private native String native_version();

}
