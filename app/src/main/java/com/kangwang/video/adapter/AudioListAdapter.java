package com.kangwang.video.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.kangwang.video.R;
import com.kangwang.video.bean.Mp3Bean;
import com.kangwang.video.bean.VideoBean;

public class AudioListAdapter extends CursorAdapter {
    private View mView;

    public AudioListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        mView = View.inflate(context, R.layout.adapter_video_item,null);
        mView.setTag(new AudioHolder());
        return mView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        AudioHolder holder =(AudioHolder) view.getTag();
        Mp3Bean bean = Mp3Bean.getInstance(cursor);
        holder.tvTitle.setText(bean.getTitle());
        holder.tvSize.setText(String.valueOf(bean.getSize()));
        holder.tvTime.setText(String.valueOf(bean.getTime()));
    }

    class AudioHolder{
        private TextView tvTitle;
        private TextView tvTime;
        private TextView tvSize;
        public AudioHolder(){
            tvTitle = mView.findViewById(R.id.tv_tile);
            tvTime = mView.findViewById(R.id.tv_time);
            tvSize = mView.findViewById(R.id.tv_size);
        }
    }
}
