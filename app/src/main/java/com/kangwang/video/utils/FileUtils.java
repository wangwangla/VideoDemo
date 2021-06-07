package com.kangwang.video.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.kangwang.video.R;
import com.kangwang.video.bean.Zhibo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.kangwang.video.R.*;

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

    public static ArrayList<Zhibo> getBeanList(Context context){
        ArrayList<Zhibo> arrayList = new ArrayList<>();
        String s = readFile(context);
        String[] split = s.split("\n");
        for (int i = 0; i < split.length/2 - 1; i++) {
            String s1 = split[i*2];
            String s2 = split[i*2+1];
            String[] split1 = s1.split(",");
            System.out.println(split1[1]);
            System.out.println(s2);
            Zhibo zhibo = new Zhibo();
            zhibo.setName(split1[1]);
            zhibo.setUri(s2);
            arrayList.add(zhibo);
        }

        return arrayList;
    }
}
