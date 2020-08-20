package com.example.classroom.activitys;

import android.Manifest;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.classroom.R;
import com.example.classroom.databinding.ActivityImageBrowserBinding;
import com.example.classroom.utils.EmptyUtil;
import com.example.classroom.utils.FileUtil;
import com.example.classroom.utils.GlideUtil;
import com.example.classroom.utils.ToastUtil;
import com.google.android.material.snackbar.Snackbar;
import com.permissionx.guolindev.PermissionX;

import java.io.File;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ImageBrowserActivity extends BaseActivity {
    private static final String TAG = "ImageBrowserActivity";

    private ActivityImageBrowserBinding mBinding;
    private String mImageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_image_browser);
        supportPostponeEnterTransition();
        // 分解式
         //   getWindow().setEnterTransition(new Explode());
         //   getWindow().setExitTransition(new Explode());
        // 滑动进入
         //   getWindow().setEnterTransition(new Slide());
         //   getWindow().setExitTransition(new Slide());
        // 淡入淡出
         //  getWindow().setEnterTransition(new Fade());
        //   getWindow().setExitTransition(new Fade());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String desc = bundle.getString("desc", "");
            mImageUrl = bundle.getString("url", "");
            mBinding.setTitle(desc);
            if (!EmptyUtil.isEmpty(mImageUrl)) {
                GlideUtil.getInstance().loadImageAndHiddenProgressBar(this,mBinding.activityImageBrowsePv, mImageUrl, mBinding.activityImageBrowsePb);
            }
        }
        mBinding.activityImageBrowsePv.enable();

        mBinding.activityImageBrowseFab.setOnClickListener(view ->  {
            checkPermission();
        });

    }

    private void checkPermission() {
        PermissionX.init(this)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .explainReasonBeforeRequest()
                .onExplainRequestReason((scope, deniedList) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "我已明白"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        downloadImage(mImageUrl);
                    } else {
                        Snackbar.make(mBinding.activityImageBrowsePv,R.string.grant_storage_permission,Snackbar.LENGTH_LONG)
                                .setActionTextColor(ContextCompat.getColor(ImageBrowserActivity.this,android.R.color.white)).show();
                    }
                });
    }


    private void downloadImage(String url) {
        if (EmptyUtil.isEmpty(url)) {
            ToastUtil.showToast(this, R.string.save_failed);
            return;
        }
        Disposable subscribe = Observable.create((ObservableOnSubscribe<File>) e -> {
            e.onNext(Glide.with(this)
                    .load(mImageUrl)
                    .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get());
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::saveImage);
    }

    private void saveImage(File file) {
        FileUtil.saveImage(this,mImageUrl,file, new FileUtil.SaveResultCallback() {
            @Override
            public void onSavedSuccess() {
                Snackbar.make(mBinding.activityImageBrowsePv,R.string.save_success,Snackbar.LENGTH_LONG)
                        .setActionTextColor(ContextCompat.getColor(ImageBrowserActivity.this,android.R.color.white)).show();
            }

            @Override
            public void onSavedFailed(final String error) {
                if (error.contains("open failed: ENOENT (No such file or directory)"))
                    Snackbar.make(mBinding.activityImageBrowsePv,R.string.grant_storage_permission,Snackbar.LENGTH_LONG)
                            .setActionTextColor(ContextCompat.getColor(ImageBrowserActivity.this,android.R.color.white)).show();
                else
                    Snackbar.make(mBinding.activityImageBrowsePv,R.string.save_failed,Snackbar.LENGTH_LONG)
                            .setActionTextColor(ContextCompat.getColor(ImageBrowserActivity.this,android.R.color.white)).show();
            }
        });
    }

}