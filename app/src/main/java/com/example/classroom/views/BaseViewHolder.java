package com.example.classroom.views;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/14
 * Description ViewHolder 基类
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    public ViewDataBinding binding;

    public BaseViewHolder(@NonNull ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
