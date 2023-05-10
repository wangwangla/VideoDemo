package com.kangwang.video.ui.activity;

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
        androidVideoPlayer.initPlayer();
        updateTime();
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
        updatePlayStatus();
    }

    private void updatePlayStatus() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updatePlayerIcon();
            }
        },300);
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
