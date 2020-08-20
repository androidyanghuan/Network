package com.example.classroom.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.classroom.BR;
import com.example.classroom.R;
import com.example.classroom.activitys.BroswerActivity;
import com.example.classroom.entitys.BannerInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/12
 * Description banner适配器
 */
public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private List<BannerInfo> bannerInfos;

    public BannerAdapter(List<BannerInfo> bannerInfos) {
        this.bannerInfos = bannerInfos;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BannerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.banner_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        BannerInfo bannerInfo = bannerInfos.get(position);
        holder.binding.setVariable(BR.bannerInfo,bannerInfo);
        holder.binding.executePendingBindings();
        holder.itemView.setTag(bannerInfo);
        holder.itemView.setOnClickListener(view -> {
            Context context = view.getContext();
            Intent intent = new Intent(context, BroswerActivity.class);
            intent.putExtra("title",bannerInfo.getTitle());
            intent.putExtra("url", bannerInfo.getUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bannerInfos == null ? 0 : bannerInfos.size();
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;
        public BannerViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
