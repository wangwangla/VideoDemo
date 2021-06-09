package com.kangwang.ffmpeglibrary.play;

public class FFmepegVideo {
    public FFmepegVideo(){
        System.loadLibrary("native-lib");
        System.out.println(native_version()+"==================");
    }

    private native String native_version();
}
