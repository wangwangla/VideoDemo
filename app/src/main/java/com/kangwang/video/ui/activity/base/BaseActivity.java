package com.kangwang.video.ui.activity.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.kangwang.video.ui.base.UIInterface;

/**
 * 基类   规范结构
 * （模板设计模式）
 * 提供公共的方法
 */
public abstract class BaseActivity extends FragmentActivity implements UIInterface {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initView();
        initData();
        initListener();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
