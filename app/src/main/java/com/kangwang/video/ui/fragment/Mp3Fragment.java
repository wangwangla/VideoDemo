package com.kangwang.video.ui.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.kangwang.video.R;
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
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.TITLE},
                null,
                null,
                null);
        while (query.moveToNext()){
            VideoBean bean = VideoBean.getInstance(query);
            LogUtils.e("xxxx",bean.toString());
        }
    }
}









