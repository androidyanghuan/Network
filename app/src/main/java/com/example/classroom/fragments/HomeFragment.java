package com.example.classroom.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.classroom.R;
import com.example.classroom.adapters.BannerAdapter;
import com.example.classroom.adapters.ClassifyAdapter;
import com.example.classroom.adapters.ClassifyBottomSheetAdapter;
import com.example.classroom.adapters.HeaderAndFooterWrapper;
import com.example.classroom.apis.BaseObserver;
import com.example.classroom.app.App;
import com.example.classroom.constants.Constant;
import com.example.classroom.databinding.ClassifyBottomSheetBinding;
import com.example.classroom.databinding.ClassifyHeadItemBinding;
import com.example.classroom.databinding.FragmentHomeBinding;
import com.example.classroom.entitys.BannerInfo;
import com.example.classroom.entitys.BaseEntity;
import com.example.classroom.entitys.ClassifyBottomSheetInfo;
import com.example.classroom.entitys.ClassifyInfo;
import com.example.classroom.utils.ACache;
import com.example.classroom.utils.DimenUtils;
import com.example.classroom.utils.EmptyUtil;
import com.example.classroom.utils.MyLog;
import com.example.classroom.utils.ToastUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.to.aboomy.pager2banner.IndicatorView;
import com.to.aboomy.pager2banner.ScaleInTransformer;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/11
 * Description
 */

public class HomeFragment extends BaseFragment implements OnRefreshLoadMoreListener {
    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding mBinding;
    private CompositeDisposable cd = new CompositeDisposable();
    private boolean isShowBanner;
    private List<BannerInfo> mBannerInfo = new ArrayList<>();
    private BannerAdapter mAdapter;
    private List<ClassifyInfo> mClassifyInfos = new ArrayList<>();
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private View mFootView;
    private String mClassifyType;
    private ClassifyHeadItemBinding mHeadView;
    private int mPage = 1;
    private RefreshLayout mRefreshLayout;
    private boolean isRefresh, isLoadMore,hasLoadMore;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String title){
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        String title = "";
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("title","");
        }
        mClassifyType = getString(R.string.all_str);
        mBinding.setTitle(title);
        MyLog.log(TAG,"Title:" + title);
        initViews();
        initBannerData();
        initListData(false);
        return mBinding.getRoot();
    }

    private void initBannerData() {
        String s = ACache.get(requireActivity()).getAsString("banner");
        if (!EmptyUtil.isEmpty(s)) {
            List<BannerInfo> bannerInfos = new Gson().fromJson(s,new TypeToken<List<BannerInfo>>(){}.getType());
            if (bannerInfos != null && bannerInfos.size() > 0) {
                mBannerInfo.addAll(bannerInfos);
                mAdapter.notifyDataSetChanged();
                getBannerData(false);
            } else {
                getBannerData(true);
            }
        } else {
            getBannerData(true);
        }
    }

    private void initViews() {
        isShowBanner = true;
     /*   Indicator indicator = new IndicatorView(requireActivity())
                .setIndicatorColor(Color.GRAY)
                .setIndicatorSelectorColor(Color.RED)
                .setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_DASH);*/

        mBinding.fragmentHomeBi.setIndicatorRatio(1f).setIndicatorRadius(2f).setIndicatorColor(Color.GRAY)
                .setIndicatorSelectedRatio(5f).setIndicatorSelectorColor(Color.RED).setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_DASH);

        //设置左右页面露出来的宽度及item与item之间的宽度
        mBinding.fragmentHomeBanner.setPageMargin(DimenUtils.dp2px(requireActivity(),20), DimenUtils.dp2px(requireActivity(),10));
        //内置ScaleInTransformer，设置切换缩放动画
        mBinding.fragmentHomeBanner.addPageTransformer(new ScaleInTransformer());
        mAdapter = new BannerAdapter(mBannerInfo);
        mBinding.fragmentHomeBanner.setIndicator(mBinding.fragmentHomeBi, false).setAdapter(mAdapter);


        mBinding.fragmentHomeRv.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(new ClassifyAdapter(mClassifyInfos));
        mHeadView = DataBindingUtil.inflate(LayoutInflater.from(requireActivity()), R.layout.classify_head_item, null, false);
        mFootView = LayoutInflater.from(requireActivity()).inflate(R.layout.classify_foot_item, null, false);
        mHeaderAndFooterWrapper.addHeaderView(mHeadView.getRoot());
        mBinding.fragmentHomeRv.setAdapter(mHeaderAndFooterWrapper);
        mHeadView.classifyHeadItemType.setText(mClassifyType);
        mHeadView.classifyHeadItemSelect.setOnClickListener(view -> showBottomSheet());

        mBinding.fragmentHomeSrl.setOnRefreshLoadMoreListener(this);
        mBinding.fragmentHomeSrl.setEnableAutoLoadMore(false);
    }

    private void getBannerData(boolean isShowDialog) {
        App.me.getApi().getBanner(Constant.BANNER_URL).compose(this.thread())
                .subscribe(new BaseObserver<List<BannerInfo>>(requireActivity(), isShowDialog) {
                    @Override
                    protected void onSuccess(BaseEntity<List<BannerInfo>> bannerInfoBaseEntity) {
                        MyLog.log(TAG,"entity:" + bannerInfoBaseEntity);
                        List<BannerInfo> data = bannerInfoBaseEntity.getData();
                        if (data != null && !data.isEmpty()) {
                            mBannerInfo.clear();
                            mBannerInfo.addAll(data);
                         //   mAdapter.notifyDataSetChanged();
                            ACache.get(requireActivity()).put("banner",new Gson().toJson(mBannerInfo));
                        }
                    }

                    @Override
                    protected void onFail(Throwable e, boolean isNetworkException) {
                        ToastUtil.networkErrorShowMessage(requireActivity(),isNetworkException,e.toString());
                    }

                    @Override
                    protected void onStartSubscribe(Disposable disposable) {
                        cd.add(disposable);
                    }
                });

    }

    private void initListData(boolean isShowDialog) {
        String s = ACache.get(requireActivity()).getAsString(mClassifyType);
        if (!EmptyUtil.isEmpty(s)) {
            List<ClassifyInfo> infos = new Gson().fromJson(s,new TypeToken<List<ClassifyInfo>>(){}.getType());
            if (infos != null && infos.size() > 0) {
                mClassifyInfos.addAll(infos);
                mHeaderAndFooterWrapper.notifyDataSetChanged();
            } else {
                getData(mClassifyType,mPage,isShowDialog);
            }
            hasLoadMore = true;
        } else {
            getData(mClassifyType,mPage,isShowDialog);
        }
    }

    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity(),R.style.BottomSheetTheme);
        ClassifyBottomSheetBinding binding = DataBindingUtil.inflate(LayoutInflater.from(requireActivity()),R.layout.classify_bottom_sheet,null,false);
        bottomSheetDialog.setContentView(binding.getRoot());
        binding.classifyBottomSheetRv.setLayoutManager(new LinearLayoutManager(requireActivity()));
        ClassifyBottomSheetAdapter adapter = new ClassifyBottomSheetAdapter(requireActivity());
        adapter.setOnItemClickListener(o -> {
            if (o instanceof ClassifyBottomSheetInfo) {
                ClassifyBottomSheetInfo cbsi = (ClassifyBottomSheetInfo) o;
                mClassifyType = cbsi.getName();
                mHeadView.classifyHeadItemType.setText(mClassifyType);
                bottomSheetDialog.cancel();

                mPage = 1;
                initListData(true);
            }
        });
        binding.classifyBottomSheetRv.setAdapter(adapter);
        BottomSheetBehavior.from((View) binding.getRoot().getParent()).setPeekHeight(getPeekHeight());
        bottomSheetDialog.show();
    }

    private int getPeekHeight() {
        int heightPixels = requireActivity().getResources().getDisplayMetrics().heightPixels;
        return (int) (heightPixels * 0.5);
    }

    private void getData(String type,int page, boolean isShowDialog) {
        App.me.getApi().getType("GanHuo",type,page,50).compose(this.thread())
                .subscribe(new BaseObserver<List<ClassifyInfo>>(requireActivity(), isShowDialog) {
                    @Override
                    protected void onSuccess(BaseEntity<List<ClassifyInfo>> listBaseEntity) {
                        finishRefreshLoadMore(true);
                        MyLog.log(TAG,"classify:" + listBaseEntity);
                        List<ClassifyInfo> data = listBaseEntity.getData();
                        if (data != null && data.size() > 0) {
                            if (page == 1) mClassifyInfos.clear();
                            mClassifyInfos.addAll(data);
                            //   mHeaderAndFooterWrapper.showFootView(mFootView);
                            mHeaderAndFooterWrapper.notifyDataSetChanged();
                            hasLoadMore = true;
                        } else {
                            hasLoadMore = false;
                        }
                        ACache.get(requireActivity()).put(type, new Gson().toJson(mClassifyInfos));
                    }

                    @Override
                    protected void onFail(Throwable e, boolean isNetworkException) {
                        finishRefreshLoadMore(false);
                        ToastUtil.networkErrorShowMessage(requireActivity(),isNetworkException,e.toString());
                    }

                    @Override
                    protected void onStartSubscribe(Disposable disposable) {
                        cd.add(disposable);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isShowBanner) {
            mBinding.fragmentHomeBanner.stopTurning();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isShowBanner) {
            mBinding.fragmentHomeBanner.startTurning();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finishRefreshLoadMore(true);
        if (isShowBanner) {
            mBinding.fragmentHomeBanner.stopTurning();
        }

        cd.clear();
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
        isLoadMore = true;
        if (hasLoadMore) {
            mPage ++;
            getData(mClassifyType,mPage,false);
        } else {
            ToastUtil.showToast(requireActivity(), R.string.no_more_data);
            refreshLayout.finishLoadMore(true);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
        isRefresh = true;
        mPage = 1;
        getData(mClassifyType,mPage,false);
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

}
