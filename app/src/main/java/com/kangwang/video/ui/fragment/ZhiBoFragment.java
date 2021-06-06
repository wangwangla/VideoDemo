package com.kangwang.video.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.kangwang.video.R;
import com.kangwang.video.bean.Mp3Bean;
import com.kangwang.video.bean.VideoBean;
import com.kangwang.video.bean.Zhibo;
import com.kangwang.video.ui.activity.play.AudioPlayActivity;
import com.kangwang.video.ui.activity.play.VideoPlayActivity;
import com.kangwang.video.utils.FileUtils;

import java.util.ArrayList;

public class ZhiBoFragment extends BaseFragment{
    private ListView listView;
    @Override
    public int getLayout() {
        return R.layout.zhibo_list;
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
        ArrayList<Zhibo> beanList = FileUtils.getBeanList(getContext());
        listView.setAdapter(new ZhoBoArray(getContext(),R.layout.zhobo_item,beanList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
//                Intent intent = new Intent(getActivity(), AndoridVideoViewActivity.class);
                ArrayList<Zhibo> videoBean = beanList;
                intent.putExtra("bean",videoBean);//接收端需要变化
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }
}









