package com.example.classroom.adapters;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.classroom.views.BaseViewHolder;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by on 2018/5/3 15:13.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class RecycleAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<?> datas;

    private int layoutId,variable;

    public List<?> getData() {
        return datas;
    }

    public interface OnItemLongClickListener{
        void onItemLongClickListener(BaseViewHolder holder, int position);
    }

    private OnItemLongClickListener mListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mListener = onItemLongClickListener;
    }

    public RecycleAdapter(List<?> datas, int layoutId, int variable) {
        this.datas = datas;
        this.layoutId = layoutId;
        this.variable = variable;
    }

    @NotNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.binding.setVariable(variable,datas.get(position));
        holder.binding.executePendingBindings();
        holder.itemView.setOnLongClickListener(view -> {
            if (mListener != null) {
                mListener.onItemLongClickListener(holder,position);
            }
            return false;
        });

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


}
