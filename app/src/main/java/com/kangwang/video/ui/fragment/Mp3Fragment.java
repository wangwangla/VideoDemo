package com.kangwang.video.ui.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.kangwang.video.R;
import com.kangwang.video.adapter.AudioListAdapter;
import com.kangwang.video.bean.VideoBean;
import com.kangwang.video.utils.LogUtils;

public class Mp3Fragment extends BaseFragment{
    private ListView listView;
    @Override
    public int getLayout() {
        return R.layout.mp3_list_fragment;
    }

    @Override
    public void initView() {
        listView = (ListView) findById(R.id.lv);
    }

    @Override
    public void initData() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void initListener() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor query = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Video.Media._ID,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.TITLE},
                null,
                null,
                null);
        listView.setAdapter(new AudioListAdapter(getActivity(),query));
//        listView.setAdapter(new CursorAdapter() {
//            @Override
//            public View newView(Context context, Cursor cursor, ViewGroup parent) {
//                return null;
//            }
//
//            @Override
//            public void bindView(View view, Context context, Cursor cursor) {
//
//            }
//        });
//
//        while (query.moveToNext()){
//            VideoBean bean = VideoBean.getInstance(query);
//            LogUtils.e("xxxx",bean.toString());
//        }
    }
}









