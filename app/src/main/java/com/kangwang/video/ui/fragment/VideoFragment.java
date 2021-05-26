package com.kangwang.video.ui.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kangwang.video.R;
import com.kangwang.video.adapter.VideoListAdapter;
import com.kangwang.video.bean.VideoBean;
import com.kangwang.video.ui.activity.VideoPlayActivity;
import com.kangwang.video.utils.LogUtils;

import java.util.ArrayList;

public class VideoFragment extends BaseFragment{
    private ListView listView;
    @Override
    public int getLayout() {
        return R.layout.video_list_fragment;
    }

    @Override
    public void initView() {
        listView = (ListView) findById(R.id.lv);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor query = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Video.Media._ID,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.TITLE},
                null,
                null,
                null);
        listView.setAdapter(new VideoListAdapter(getActivity(),query));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
//                VideoBean instance = VideoBean.getInstance(query);
//                intent.putExtra("bean",instance);
                ArrayList<VideoBean> videoBean = VideoBean.getVideoBean(query);
                intent.putExtra("bean",videoBean);//接收端需要变化
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
//        while (query.moveToNext()){
//            VideoBean bean = VideoBean.getInstance(query);
//            LogUtils.e("xxxx",bean.toString());
//        }
    }
}
