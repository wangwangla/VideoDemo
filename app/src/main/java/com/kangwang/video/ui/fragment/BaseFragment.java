package com.kangwang.video.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kangwang.video.ui.UIInterface;

public abstract class BaseFragment extends Fragment implements UIInterface {
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayout(),null);
        initView();
        initListener();
        initData();
        return view;
    }

    /**
     * 找出fragment里面的view
     * @param resId
     * @return
     */
    public View findById(int resId){
        return view.findViewById(resId);
    }
}
