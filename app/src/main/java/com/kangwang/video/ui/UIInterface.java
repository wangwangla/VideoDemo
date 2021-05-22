package com.kangwang.video.ui;

public interface UIInterface {
    /**
     * 加载布局
     */
    public int getLayout();

    /**
     * 初始化空间
     *
     */
    public void initView();

    /**
     * 初始化数据
     *
     *
     */
    public void initData();

    /**
     * 设置监听
     */
    public void initListener();

}
