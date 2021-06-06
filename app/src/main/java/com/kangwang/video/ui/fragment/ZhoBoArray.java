package com.kangwang.video.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.kangwang.video.R;
import com.kangwang.video.bean.Zhibo;

import java.util.ArrayList;

public class ZhoBoArray extends ArrayAdapter<Zhibo> {
    private ArrayList<Zhibo> zhibos;
    private int resource;
    public ZhoBoArray(@NonNull Context context, int resource,ArrayList<Zhibo> zhibos) {
        super(context, resource,zhibos);
        this.zhibos = zhibos;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Zhibo item=getItem(position);   //获取当前项的实例
        View view= LayoutInflater.from(getContext()).inflate(resource,parent,false);
        TextView itemName= view.findViewById(R.id.title);
        itemName.setText(item.getName());
        return view;
    }

    @Nullable
    @Override
    public Zhibo getItem(int position) {
        return super.getItem(position);
    }

}
