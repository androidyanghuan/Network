package com.example.classroom.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.classroom.BR;
import com.example.classroom.R;
import com.example.classroom.activitys.BroswerActivity;
import com.example.classroom.entitys.ClassifyInfo;
import com.example.classroom.views.BaseViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/13
 * Description banner适配器
 */
/*public class ClassifyAdapter extends BaseQuickAdapter<ClassifyInfo, BaseViewHolder> {

    public ClassifyAdapter(int layoutResId, @Nullable List<ClassifyInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ClassifyInfo classifyInfo) {
        holder.setText(R.id.item_desc, String.format("%s", classifyInfo.getDesc()));
        holder.setText(R.id.item_name, String.format("%s", classifyInfo.getAuthor()));
        holder.setText(R.id.item_type, String.format("%s", classifyInfo.getType()));
        holder.setText(R.id.item_date, String.format("%s", classifyInfo.getCreatedAt()));
    }

}*/
public class ClassifyAdapter extends RecyclerView.Adapter<BaseViewHolder>{

    private List<ClassifyInfo> classifyInfos;

    public ClassifyAdapter(List<ClassifyInfo> classifyInfos) {
        this.classifyInfos = classifyInfos;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.classify_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        ClassifyInfo classifyInfo = classifyInfos.get(position);
        holder.binding.setVariable(BR.classifyInfo,classifyInfo);
        holder.binding.executePendingBindings();

        holder.itemView.setOnClickListener(view -> {
            Context context = view.getContext();
            Intent intent = new Intent(context, BroswerActivity.class);
            intent.putExtra("title",classifyInfo.getTitle());
            intent.putExtra("url",classifyInfo.getUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return classifyInfos.size();
    }

}
