package com.kangwang.video.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.kangwang.video.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.kangwang.video.R.*;

public class FileUtils {
    private static final String path = "ipv6";
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
