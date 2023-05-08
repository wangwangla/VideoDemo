package com.kangwang.video.adpter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kangwang.video.R;
import com.kangwang.video.bean.VideoBean;
import com.kangwang.video.load.FileLoader;
import com.kangwang.video.ui.activity.PlayActivity;

import java.util.ArrayList;

/**
 * @Auther jian xian si qi
 * @Date 2023/5/7 14:04
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListHolder> {
    private ArrayList<VideoBean> videoBeans;
    private Activity activity;
    public VideoListAdapter(Activity activity,ArrayList<VideoBean> arrayList){
        this.activity = activity;
        this.videoBeans = arrayList;
    }

    @NonNull
    @Override
    public VideoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, null);
        return new VideoListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoListHolder holder, int position) {
        VideoBean bean = videoBeans.get(position);
        String s = FileLoader.videoPath(activity, bean.getId() + "");
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(s);
        Bitmap frameAtTime = media.getFrameAtTime();
        holder.prePic.setImageBitmap(frameAtTime);
        holder.zise.setText(bean.getSize()+"");
        holder.duration.setText(bean.getDuration()+"");
        holder.title.setText(bean.getName());
    }

    @Override
    public int getItemCount() {
        return videoBeans.size();
    }

    class VideoListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView prePic;
        private TextView title;
        private TextView zise;
        private TextView duration;

        public VideoListHolder(@NonNull View itemView) {
            super(itemView);
            prePic = itemView.findViewById(R.id.pre_pic);
            title = itemView.findViewById(R.id.title);
            zise = itemView.findViewById(R.id.size);
            duration = itemView.findViewById(R.id.duration);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, PlayActivity.class);
            int adapterPosition = getAdapterPosition();
            VideoBean bean = videoBeans.get(adapterPosition);
            intent.putExtra("videoData",bean.getId());
            activity.startActivity(intent);
        }
    }
}
