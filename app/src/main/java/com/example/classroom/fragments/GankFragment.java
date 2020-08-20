package com.example.classroom.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.classroom.BR;
import com.example.classroom.R;
import com.example.classroom.adapters.HeaderAndFooterWrapper;
import com.example.classroom.adapters.RecycleAdapter;
import com.example.classroom.apis.BaseObserver;
import com.example.classroom.app.App;
import com.example.classroom.databinding.FragmentGankBinding;
import com.example.classroom.entitys.BaseEntity;
import com.example.classroom.entitys.HotInfo;
import com.example.classroom.utils.ACache;
import com.example.classroom.utils.EmptyUtil;
import com.example.classroom.utils.MyLog;
import com.example.classroom.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.disposables.Disposable;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/17
 * Description 干货
 */
public class GankFragment extends BaseFragment implements OnRefreshListener {
    private static final String TAG = "GankFragment";

    private List<HotInfo> mHotInfos = new ArrayList<>();
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private View mFootView;
    private Disposable mDisposable;
    private RefreshLayout mRefreshLayout;

    public static GankFragment newInstance(String title) {
        GankFragment gf = new GankFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        gf.setArguments(args);
        return gf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentGankBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gank,container,false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            binding.setTitle(bundle.getString("title",requireActivity().getString(R.string.main_books_page)));
        }
        initViews(binding);
        String hot = ACache.get(requireActivity()).getAsString("hot");
        if (!EmptyUtil.isEmpty(hot)) {
            List<HotInfo> hotInfos = new Gson().fromJson(hot, new TypeToken<List<HotInfo>>() {
            }.getType());
            if (hotInfos != null && hotInfos.size() > 0) {
                mHotInfos.addAll(hotInfos);
                mHeaderAndFooterWrapper.showFootView(mFootView);
                mHeaderAndFooterWrapper.notifyDataSetChanged();
            } else {
                getData(true);
            }
        } else {
            getData(true);
        }
        return binding.getRoot();
    }

    private void getData(boolean isShowDialog) {
        App.me.getApi().getHot().compose(this.thread()).subscribe(new BaseObserver<List<HotInfo>>(requireActivity(),isShowDialog){

            @Override
            protected void onSuccess(BaseEntity<List<HotInfo>> listBaseEntity) {
                finishRefresh(true);
                List<HotInfo> data = listBaseEntity.getData();
                MyLog.log(TAG,"hotInfo:" + data);
                if (data != null && data.size() > 0) {
                    mHotInfos.addAll(data);
                    mHeaderAndFooterWrapper.showFootView(mFootView);
                    mHeaderAndFooterWrapper.notifyDataSetChanged();

                    ACache.get(requireActivity()).put("hot",new Gson().toJson(mHotInfos));
                }
            }

            @Override
            protected void onFail(Throwable e, boolean isNetworkException) {
                finishRefresh(false);
                ToastUtil.networkErrorShowMessage(requireActivity(),isNetworkException,e.toString());
            }

            @Override
            protected void onStartSubscribe(Disposable disposable) {
                mDisposable = disposable;
            }
        });
    }

    private void initViews(FragmentGankBinding binding) {
        binding.fragmentGankRv.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(new RecycleAdapter(mHotInfos,R.layout.fragment_gank_item, BR.hotInfo));
     //   mHeadView = DataBindingUtil.inflate(LayoutInflater.from(requireActivity()), R.layout.classify_head_item, null, false);
        mFootView = LayoutInflater.from(requireActivity()).inflate(R.layout.classify_foot_item, null, false);
        binding.fragmentGankRv.setAdapter(mHeaderAndFooterWrapper);

        binding.fragmentGankSrl.setEnableLoadMore(false);
        binding.fragmentGankSrl.setOnRefreshListener(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
        finishRefresh(true);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
        getData(false);
    }

    private void finishRefresh(boolean success) {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh(success);
        }
    }
}
