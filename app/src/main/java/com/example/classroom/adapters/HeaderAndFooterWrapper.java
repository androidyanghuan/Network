package com.example.classroom.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.example.classroom.utils.WrapperUtil;
import com.example.classroom.views.ViewHolder;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by on 2018/4/4 15:23.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * https://github.com/hongyangAndroid/baseAdapter
 */

public class HeaderAndFooterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mInnerAdapter;

    public HeaderAndFooterWrapper(RecyclerView.Adapter<RecyclerView.ViewHolder> mInnerAdapter) {
        this.mInnerAdapter = mInnerAdapter;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null)
            return ViewHolder.createViewHolder(parent.getContext(), mHeaderViews.get(viewType));
        else if (mFooterViews.get(viewType) != null)
            return ViewHolder.createViewHolder(parent.getContext(), mFooterViews.get(viewType));
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        if (isHanderViewPosition(position) || isFootViewPosition(position))
            return;
        mInnerAdapter.onBindViewHolder(holder, position - getHeandersCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (isHanderViewPosition(position))
            return mHeaderViews.keyAt(position);
        else if (isFootViewPosition(position))
            return mFooterViews.keyAt(position - getHeandersCount() - getRealItemCount());
        return mInnerAdapter.getItemViewType(position - getHeandersCount());
    }

    @Override
    public int getItemCount() {
        return getHeandersCount() + getRealItemCount() + getFooterCount();
    }

    public int getFooterCount() {
        return mFooterViews.size();
    }

    @Override
    public void onViewAttachedToWindow(@NotNull RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHanderViewPosition(position) || isFootViewPosition(position))
            WrapperUtil.setFullSpan(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@Nullable RecyclerView recyclerView) {
        WrapperUtil.onAttachedToRecyclerView(mInnerAdapter, recyclerView, (layoutManager, oldLookup, position) -> {
            int viewType = getItemViewType(position);
            if (mHeaderViews.get(viewType) != null)
                return layoutManager.getSpanCount();
            else if (mFooterViews.get(viewType) != null)
                return layoutManager.getSpanCount();
            if (oldLookup != null)
                return oldLookup.getSpanSize(position);
            return 1;
        });
    }

    private boolean isHanderViewPosition(int position) {
        return position < getHeandersCount();
    }

    private boolean isFootViewPosition(int position) {
        return position >= getHeandersCount() + getRealItemCount();
    }

    public int getHeandersCount() {
        return mHeaderViews.size();
    }

    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFootView(View view) {
        if (getRealItemCount() == 0)
            return;
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public void showFootView(View view) {
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

}
