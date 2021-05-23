package com.kangwang.video.ui.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.kangwang.video.ui.fragment.BaseFragment;

import java.util.List;

public class PlayerListAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> baseFragmentList;
    /**
     * 构造方法
     * @param fm
     */
    public PlayerListAdapter(@NonNull FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm);
        this.baseFragmentList = fragmentList;
    }

    /**
     * 加载 fragment
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return baseFragmentList.get(position);
    }

    //显示几个页面
    @Override
    public int getCount() {
        return baseFragmentList == null ? 0 : baseFragmentList.size();
    }
}
