package com.kangwang.video.ui.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import com.kangwang.video.R;
import com.kangwang.video.ui.activity.base.BaseActivity;

/**
 * 显示logo
 *  初始化数据
 *  展示广告
 */
public class SplashActivity extends BaseActivity {

    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {


    }

    @Override
    public void initData() {
        //延迟跳转
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }

    @Override
    public void initListener() {

    }
}
