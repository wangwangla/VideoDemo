package com.kangwang.video.ui.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.kangwang.video.ui.UIInterface;

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
}
