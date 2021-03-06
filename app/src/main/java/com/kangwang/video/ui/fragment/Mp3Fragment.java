package com.kangwang.video.ui.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.kangwang.video.R;
import com.kangwang.video.adapter.AudioListAdapter;
import com.kangwang.video.bean.Mp3Bean;
import com.kangwang.video.ui.activity.play.AudioPlayActivity;

import java.util.ArrayList;

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
                        MediaStore.Video.Media.DISPLAY_NAME},
                null,
                null,
                null);
        listView.setAdapter(new AudioListAdapter(getActivity(),query));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AudioPlayActivity.class);
                ArrayList<Mp3Bean> mp3List = Mp3Bean.getMp3List(query);
                intent.putExtra("bean",mp3List);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }
}









