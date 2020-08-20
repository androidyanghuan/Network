package com.example.classroom.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.classroom.BR;
import com.example.classroom.R;
import com.example.classroom.entitys.ClassifyBottomSheetInfo;
import com.example.classroom.listeners.OnItemClickListener;
import com.example.classroom.views.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/14
 * Description home页面bottom sheet适配器
 */
public class ClassifyBottomSheetAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private List<ClassifyBottomSheetInfo> mClassifyInfos = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public ClassifyBottomSheetAdapter(Context context) {
        this.context = context;
        String[] names = context.getResources().getStringArray(R.array.homeClassifyName);
        TypedArray ta = context.getResources().obtainTypedArray(R.array.homeClassifyIcon);
        if (ta.length() > 0) {
            for (int i = 0; i < ta.length(); i++) {
                mClassifyInfos.add(i,new ClassifyBottomSheetInfo(names[i],ta.getDrawable(i)));
            }
        }
        ta.recycle();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.classify_bottom_sheet_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        ClassifyBottomSheetInfo info = mClassifyInfos.get(position);
        holder.binding.setVariable(BR.classifyBottomSheetInfo,info);
        holder.binding.executePendingBindings();

        holder.itemView.setTag(info);
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickListener(view.getTag());
            }
        });
    }



    @Override
    public int getItemCount() {
        return mClassifyInfos.size();
    }


}
