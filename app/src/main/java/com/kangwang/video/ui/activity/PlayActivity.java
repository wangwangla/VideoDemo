package com.kangwang.video.ui.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showFloatingWindow();
            }
        },1000);

    }

    private void showFloatingWindow() {
        FloatingWindow mFloatingWindow = new FloatingWindow();
        View view = findViewById(R.id.android_player);
        mFloatingWindow.showFloatingWindowView(this, view);
    }

//    private View initFloatView() {
//        View view = View.inflate(this, R.layout.view_floating_window, null);
//        // 设置视频封面
////        final ImageView mThumb = (ImageView) view.findViewById(R.id.thumb_floating_view);
////        Glide.with(this).load(R.drawable.thumb).into(mThumb);
//        // 悬浮窗关闭
////        view.findViewById(R.id.close_floating_view).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                mFloatingWindow.dismiss();
////            }
////        });
//        // 返回前台页面
////        view.findViewById(R.id.back_floating_view).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                mFloatingWindow.setTopApp(FloatingWindowActivity.this);
////            }
////        });
//        final VideoView videoView = view.findViewById(R.id.video_view);
//        //视频内容设置
//        videoView.setVideoPath("https://stream7.iqilu.com/10339/article/202002/18/2fca1c77730e54c7b500573c2437003f.mp4");
//        // 视频准备完毕，隐藏正在加载封面，显示视频
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mThumb.setVisibility(View.GONE);
//            }
//        });
//        // 循环播放
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                videoView.start();
//            }
//        });
//        // 开始播放视频
//        videoView.start();
//        return view;
//    }

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
