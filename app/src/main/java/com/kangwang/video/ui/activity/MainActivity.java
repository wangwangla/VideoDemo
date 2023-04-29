package com.kangwang.video.ui.activity;

import android.view.View;

import com.kangwang.video.R;
import com.kangwang.video.adapter.PlayerListAdapter;
import com.kangwang.video.ui.activity.base.BaseActivity;
import com.kangwang.video.ui.fragment.BaseFragment;
import com.kangwang.video.ui.fragment.VideoFragment;
import com.kangwang.video.ui.fragment.ZhiBoFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity{

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        setFragment();
    }

   public void setFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_content,new ZhiBoFragment())
                .commit();
   }

   @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}