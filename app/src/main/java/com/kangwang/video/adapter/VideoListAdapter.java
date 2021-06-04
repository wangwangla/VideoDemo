package com.kangwang.video.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.kangwang.video.R;
import com.kangwang.video.bean.VideoBean;
import com.kangwang.video.utils.StringUtils;

/**
 * 视频数据列表的适配器
 */
public class VideoListAdapter extends CursorAdapter {
    private View mView;
    public VideoListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        mView = View.inflate(context, R.layout.adapter_video_item,null);
        mView.setTag(new VideoHolder());
        return mView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        VideoHolder holder =(VideoHolder) view.getTag();
        VideoBean bean = VideoBean.getInstance(cursor);
        holder.tvTitle.setText(bean.getTitle());
        holder.tvSize.setText(StringUtils.formatSize(bean.getSize()));
        holder.tvTime.setText(StringUtils.formatTime(bean.getTime()));
    }

    class VideoHolder{
        private TextView tvTitle;
        private TextView tvTime;
        private TextView tvSize;
        public VideoHolder(){
            tvTitle = mView.findViewById(R.id.tv_tile);
            tvTime = mView.findViewById(R.id.tv_time);
            tvSize = mView.findViewById(R.id.tv_size);
        }
    }
}
