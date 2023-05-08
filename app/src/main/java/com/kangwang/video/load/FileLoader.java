package com.kangwang.video.load;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.kangwang.video.bean.VideoBean;

import java.util.ArrayList;

/**
 * @Auther jian xian si qi
 * @Date 2023/5/7 12:28
 */
public class FileLoader {
    public static ArrayList<VideoBean> loadFile(Context context){
        ArrayList<VideoBean> videoBeans = new ArrayList<>();
        videoBeans.clear();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                    MediaStore.Video.DEFAULT_SORT_ORDER);
        if (cursor!=null&&cursor.moveToFirst()){
            do{
                System.out.println("----------------------------------------------");
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)));
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
                String disPlayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)));
//                System.out.println(cursor.getString(cursor.getColumnIndexOrT
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)));
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST)));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
//                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.COMPOSER)));
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM)));
//                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.YEAR)));
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BOOKMARK)));
//                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM_ARTIST)));

                System.out.println("==================================");
                VideoBean bean = new VideoBean();
                bean.setId(id);
                bean.setName(disPlayName);
                bean.setSize(size);
                bean.setDuration(duration);
                videoBeans.add(bean);
            }while (cursor.moveToNext());
        }
        return videoBeans;
    }

    public static VideoBean findByIdVideoPath(Context context,String videoId){
        String selection = MediaStore.Video.Media._ID+ " = ?";
        String[] selectionArgs = {videoId};
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null
        );

        VideoBean bean = new VideoBean();
        if (cursor!=null&&cursor.moveToFirst()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            String disPlayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            String videoPath =  cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            bean.setId(id);
            bean.setName(disPlayName);
            bean.setSize(size);
            bean.setDuration(duration);
            bean.setPath(videoPath);
        }
        return bean;
    }

    public static String videoPath(Context context,String videoId){
        String selection = MediaStore.Video.Media._ID+ " = ?";
        String[] selectionArgs = {videoId};

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null
        );
        if (cursor!=null&&cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
        }
        return null;
    }
}
