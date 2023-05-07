package com.kangwang.video.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileUtils {
    private static final String path = "languang";
    public static String readFile(Context context){
        InputStream inputStream;
        StringBuilder sb = new StringBuilder();
        try {
            AssetManager manager = context.getResources().getAssets();
            inputStream = manager.open(path);
            InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String length;
            while ((length = br.readLine()) != null) {
                sb.append(length + "\n");
            }
            //关流
            br.close();
            isr.close();
            inputStream.close();
            System.out.println("============================");
            System.out.println(sb.toString());
            return sb.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}
