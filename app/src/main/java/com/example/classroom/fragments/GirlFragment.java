package com.example.classroom.fragments;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.classroom.BR;
import com.example.classroom.R;
import com.example.classroom.adapters.RecycleAdapter;
import com.example.classroom.apis.BaseObserver;
import com.example.classroom.app.App;
import com.example.classroom.databinding.FragmentGirlBinding;
import com.example.classroom.entitys.BaseEntity;
import com.example.classroom.entitys.GirlInfo;
import com.example.classroom.utils.ACache;
import com.example.classroom.utils.EmptyUtil;
import com.example.classroom.utils.MyLog;
import com.example.classroom.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.Disposable;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/14
 * Description
 */
public class GirlFragment extends BaseFragment implements OnRefreshLoadMoreListener {
    private static final String TAG = "GirlFragment";

    private FragmentGirlBinding mBinding;
    private List<GirlInfo> mItems = new ArrayList<>();
    private int mPage = 1;
    private Disposable mDisposable;
    private RecycleAdapter mAdapter;
    private RefreshLayout mRefreshLayout;
    private boolean isRefresh,isLoadMore,hasLoadMore,isScrolling;

    public GirlFragment() {
    }

    public static GirlFragment newInstance(String title) {
        GirlFragment gf = new GirlFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        gf.setArguments(args);
        return gf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_girl,container,false);
        Bundle bundle = getArguments();
        String title = "";
        if (bundle != null) {
            title = bundle.getString("title", "");
        }
        mBinding.setTitle(title);

        mBinding.fragmentGirlSrl.setOnRefreshLoadMoreListener(this);
        mBinding.fragmentGirlSrl.setEnableAutoLoadMore(false);

        initViews();

        return mBinding.getRoot();
    }

    private void initViews() {
        //读取缓存数据
        String s = ACache.get(requireActivity()).getAsString("girls");
        if (!EmptyUtil.isEmpty(s)) {
            List<GirlInfo>items = new Gson().fromJson(s,new TypeToken<List<GirlInfo>>(){}.getType());
            if (items != null) {
                mItems.addAll(items);
            } else
                getData(true,mPage);
            hasLoadMore = true;
        } else
            getData(true,mPage);

        //创建helper对象
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);

        mAdapter = new RecycleAdapter(mItems,R.layout.fragment_girl_item, BR.welfare);
        mBinding.fragmentGirlRv.setLayoutManager(new GridLayoutManager(requireActivity(),2));
        mAdapter.setOnItemLongClickListener((holder, position) -> {
            Vibrator vibrator = (Vibrator) requireActivity().getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(70);
            if (position != mItems.size() - 1) {
                itemTouchHelper.startDrag(holder);
            }
        });

        mBinding.fragmentGirlRv.setAdapter(mAdapter);
        itemTouchHelper.attachToRecyclerView(mBinding.fragmentGirlRv);

    /*    mBinding.fragmentGirlRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    isScrolling = true;
                    Glide.with(requireActivity()).pauseRequests();
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isScrolling) {
                        Glide.with(requireActivity()).resumeRequests();
                    }
                    isScrolling = false;
                }
            }
        });*/

    }

    private void getData(boolean isShowDialog, int page) {
        App.me.getApi().getGirl(page).compose(this.thread()).subscribe(new BaseObserver<List<GirlInfo>>(requireActivity(), isShowDialog) {
            @Override
            protected void onSuccess(BaseEntity<List<GirlInfo>> listBaseEntity) {
                finishRefreshLoadMore(true);
                MyLog.log(TAG,"girls:" + listBaseEntity);
                List<GirlInfo> data = listBaseEntity.getData();
                if (data != null && data.size() > 0) {
                    if (page == 1) mItems.clear();
                    mItems.addAll(data);
                    hasLoadMore = true;
                } else {
                    hasLoadMore = false;
                }
                mAdapter.notifyDataSetChanged();

                String s = new Gson().toJson(mItems);
                ACache.get(requireActivity()).put("girls",s);

              //  MyLog.log(TAG,"jsonArrayString:" +  s);
            }

            @Override
            protected void onFail(Throwable e, boolean isNetworkException) {
                finishRefreshLoadMore(false);
                ToastUtil.networkErrorShowMessage(requireActivity(),isNetworkException,e.toString());
            }

            @Override
            protected void onStartSubscribe(Disposable disposable) {
                mDisposable = disposable;
            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (isLoadMore) return;
        isLoadMore = true;
        mRefreshLayout = refreshLayout;
        if (hasLoadMore) {
            mPage++;
            getData(false, mPage);
        } else {
            ToastUtil.showToast(requireActivity(),R.string.no_more_data);
            finishRefreshLoadMore(true);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (isRefresh) return;
        isRefresh = true;
        mRefreshLayout = refreshLayout;
        mPage = 1;
        getData(false,mPage);
    }

    private void finishRefreshLoadMore(boolean success) {
        if (null != mRefreshLayout) {
            if (isRefresh) {
                mRefreshLayout.finishRefresh(success);
                isRefresh = false;
            }
            if (isLoadMore) {
                mRefreshLayout.finishLoadMore(success);
                isLoadMore = false;
            }
            if (!success) {
                mPage --;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finishRefreshLoadMore(true);
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    private ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            //控制拖拽的方向（一般是上下左右）
            int dragFlags= ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            //控制快速滑动的方向（一般是左右）
            int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, swipeFlags);//计算movement flag值
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            // 拖拽时，每移动一个位置就会调用一次。
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter == null){
                return false;
            }

            if (mItems != null && mItems.size() > 0) {
                //获取被拖拽的Item的Position
                int from = viewHolder.getBindingAdapterPosition();
                //获取目标Item的Position
                int endPosition = target.getBindingAdapterPosition();
                //交换List集合中两个元素的位置
                Collections.swap(mItems, from, endPosition);
                //交换界面上两个Item的位置
                adapter.notifyItemMoved(from, endPosition);
            }
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //滑动处理
            int position = viewHolder.getBindingAdapterPosition();
            if(mItems != null && mItems.size() > 0){
                //删除List中对应的数据
                mItems.remove(position);
                RecyclerView.Adapter adapter = mBinding.fragmentGirlRv.getAdapter();
                if (adapter == null){
                    return;
                }
                //刷新页面
                adapter.notifyItemRemoved(position);

            }
        }
    };
}
