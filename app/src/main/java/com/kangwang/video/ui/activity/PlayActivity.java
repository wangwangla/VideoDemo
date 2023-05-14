package com.kangwang.video.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.player_base.AbstractPlayer;
import com.kangwang.androidmediaplayer.AndroidVideoPlayer;
import com.kangwang.video.R;
import com.kangwang.video.bean.VideoBean;
import com.kangwang.video.load.FileLoader;
import com.kangwang.video.ui.activity.base.BaseActivity;
import com.kangwang.video.utils.VideoUtils;
import com.kangwang.video.video.AndroidView;
import com.kangwang.video.window.FloatingWindow;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Auther jian xian si qi
 * @Date 2023/5/7 14:41
 */
public class PlayActivity extends BaseActivity {
    private long videoId;
    private VideoBean videoInfo;
    private AndroidView<AbstractPlayer> androidVideoPlayer;
    private SeekBar videoProcessSeekBar;
    private boolean isSmallWindow;
    private Handler handler = new Handler();

    @Override
    public int getLayout() {
        return R.layout.video_play;
    }

    @Override
    public void initView() {
        if (videoId==-1)return;
        long videoData = getIntent().getLongExtra("videoData", -1);
        if (videoData==-1)return;
        videoId = videoData;
        androidVideoPlayer = findViewById(R.id.android_player);
        videoInfo = FileLoader.findByIdVideoPath(this, videoId + "");
        androidVideoPlayer.setDataSource(videoInfo.getPath(),null);
        androidVideoPlayer.initPlayer(new Runnable() {
            @Override
            public void run() {
                updatePlayStatus();
            }
        });
        updateTime();
        View smallWindowBtn = findViewById(R.id.small_window_btn);
        smallWindowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        !Settings.canDrawOverlays(PlayActivity.this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + PlayActivity.this.getPackageName()));
                    PlayActivity.this.startActivity(intent);
                } else {
                    // 已经具有SYSTEM_ALERT_WINDOW权限，可以添加窗口
                    // 在这里执行添加窗口的操作
                    showFloatingWindow();
                }
            }
        });
    }


    private void showFloatingWindow() {
        if (isSmallWindow)return;
        this.isSmallWindow = true;
        FloatingWindow mFloatingWindow = new FloatingWindow();
        View view = initFloatView();
        AndroidView smallWindow= view.findViewById(R.id.small_window);
        smallWindow.initPlayer(new Runnable() {
            @Override
            public void run() {
//                updatePlayStatus();
                smallWindow.seekTo(androidVideoPlayer.getCurrentPosition());
            }
        });

        androidVideoPlayer.pause();
        mFloatingWindow.showFloatingWindowView(this, view);
        View closeSmall = view.findViewById(R.id.close_small);
        closeSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentPosition = smallWindow.getCurrentPosition();
                androidVideoPlayer.setCurrentPosition(currentPosition);
                mFloatingWindow.dismiss();
                androidVideoPlayer.start();
                PlayActivity.this.isSmallWindow = false;
            }
        });
    }

    private View initFloatView() {
        View view = View.inflate(this, R.layout.view_floating_window, null);
        AndroidView smallVideo = view.findViewById(R.id.small_window);
        smallVideo.setDataSource(videoInfo.getPath(),null);
        return view;
    }

    public void updateTime(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                PlayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (androidVideoPlayer != null) {
                            if (androidVideoPlayer.isPlaying()) {
                                TextView playTime = findViewById(R.id.play_time);
                                playTime.setText(VideoUtils.longToTimeFormat(androidVideoPlayer.getCurrentPosition()));
                                videoProcessSeekBar.setProgress((int) (androidVideoPlayer.getCurrentPosition()/1000.0f));
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(task, 5, 100);
    }

    @Override
    public void initData() {
        TextView videoTitle = findViewById(R.id.video_title);
        videoTitle.setText(videoInfo.getName());
        TextView playAllTime = findViewById(R.id.play_alltime);
        videoProcessSeekBar = findViewById(R.id.play_sb);
        videoProcessSeekBar.setMax((int) (videoInfo.getDuration()/1000.0f));
        playAllTime.setText(VideoUtils.longToTimeFormat(videoInfo.getDuration()));
    }

    @Override
    public void initListener() {
        View preVideo = findViewById(R.id.pre_video);
        ImageView playPause = findViewById(R.id.play_pause);
        View nextVideo = findViewById(R.id.next_video);
        View screenControl = findViewById(R.id.screen_control);
        screenControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoOri();
            }
        });
        View speed = findViewById(R.id.speed);
        speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    final PopupMenu popupMenu = new PopupMenu(PlayActivity.this, v);
                    popupMenu.inflate(R.menu.popup_song);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.oneS:{
                                    androidVideoPlayer.setSpeed(1);
                                    break;
                                }
                                case R.id.twoS:{
                                    androidVideoPlayer.setSpeed(2);
                                    break;
                                }
                                case R.id.threeS:{
                                    androidVideoPlayer.setSpeed(3);
                                    break;
                                }
                                case R.id.fourS:{
                                    androidVideoPlayer.setSpeed(4);
                                    break;
                                }
//                            case R.id.add_to_playlist:{
//                                break;
//                            }
//                            case R.id.add_to_album:{
//                                break;
//                            }
//                            case R.id.add_to_aritist:{
//                                break;
//                            }
//                            case R.id.delete_from_device:{
//                                break;
//                            }
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }else {
                    Toast.makeText(PlayActivity.this, "不支持！", Toast.LENGTH_SHORT).show();
                }
//                    androidVideoPlayer.setSpeed(2);
            }
        });
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (androidVideoPlayer.isPlaying()) {
                    androidVideoPlayer.pause();
                }else {
                    androidVideoPlayer.start();
                }
                updatePlayStatus();
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },3000);
    }

    private int screenOri = 0;

    /**
     * 横竖屏切换
     */
    public void videoOri(){
        RelativeLayout.LayoutParams lp;
        if (screenOri==0){
            screenOri=1;
        }else {
            screenOri = 0;
        }
        if (screenOri == 1){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 手动横屏
            lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT);
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 手动横屏
            lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
        }
        androidVideoPlayer.setLayoutParams(lp);
    }


    /**
     * dp2px  动态设置视频的宽高
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }


    private void updatePlayStatus() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updatePlayerIcon();
            }
        },100);
    }

    public void updatePlayerIcon(){
        ImageView playPause = findViewById(R.id.play_pause);
        if (androidVideoPlayer.isPlaying()){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_button_pause);
            playPause.setImageBitmap(bitmap);
        }else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_button_play);
            playPause.setImageBitmap(bitmap);
        }
    }

    @Override
    public void finish() {
        super.finish();
        androidVideoPlayer.release();
    }
}
