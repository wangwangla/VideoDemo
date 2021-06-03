package com.kangwang.ffmpeglibrary;

import android.media.MediaExtractor;

import java.io.IOException;

public class xx {
    private void xx (){
        MediaExtractor extractor = new MediaExtractor();
        try {
            extractor.setDataSource("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
