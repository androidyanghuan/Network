package com.example.classroom.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.classroom.R;
import com.example.classroom.activitys.AboutActivity;
import com.example.classroom.databinding.FragmentMeBinding;
import com.example.classroom.databinding.FragmentMeSwitchIconBinding;
import com.example.classroom.utils.EmptyUtil;
import com.example.classroom.utils.FileProviderCompat;
import com.example.classroom.utils.GlideUtil;
import com.example.classroom.utils.MyLog;
import com.example.classroom.utils.SPUtil;
import com.example.classroom.utils.UriUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.permissionx.guolindev.PermissionX;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

/**
 * Version: v1.0
 * Author: YangHuan
 * Date: 2020/8/17
 * Description 我的页面
 */
public class MeFragment extends BaseFragment {
    private static final String TAG = "MeFragment";

    private FragmentMeBinding mBinding;
    private BottomSheetDialog mBottomSheetDialog;

    public static final int REQUEST_CAMERA = 100;
    public static final int REQUEST_ALBUM = 200;
    public static final int CROP_PICTURE = 300;

    private Uri cropImageUri = null;
    private File photoFile = null;

    public static MeFragment newInstance(String title) {
        MeFragment mf = new MeFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        mf.setArguments(args);
        return mf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_me,container,false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mBinding.setTitle(bundle.getString("title",requireActivity().getString(R.string.main_me_page)));
        }
        String userPhoto = SPUtil.getInstance(requireActivity()).getUserPhoto();
        if (!EmptyUtil.isEmpty(userPhoto)) {
            GlideUtil.getInstance().loadImage(requireActivity(),Uri.parse(userPhoto),mBinding.fragmentMeIcon,"#3700B3");
        } else {
            GlideUtil.getInstance().loadImageWithLocation(requireActivity(), R.mipmap.ic_launcher, mBinding.fragmentMeIcon, "#3700B3");
        }
        mBinding.fragmentMeSetting.setOnClickListener(this::onItemClick);
        mBinding.fragmentMeAbout.setOnClickListener(this::onItemClick);
        mBinding.fragmentMeIcon.setOnClickListener(this::onItemClick);
        return mBinding.getRoot();
    }


    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_me_setting:
                startActivity(new Intent(Settings.ACTION_SETTINGS));
                break;

            case R.id.fragment_me_about:
                startActivity(new Intent(requireActivity(), AboutActivity.class));
                break;

            case R.id.fragment_me_icon:
                if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) {
                    cancelBottomSheet();
                } else {
                    showBottomSheet();
                }
                break;

            case R.id.fragment_me_photograph:
                cancelBottomSheet();
                checkPermission(1,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
                break;

            case R.id.fragment_me_album:
                cancelBottomSheet();
                checkPermission(2,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
                break;

            case R.id.fragment_me_cancel:
                cancelBottomSheet();
                break;
        }
    }

    private void checkPermission(int type, String ... permission) {
        PermissionX.init(this)
                .permissions(permission)
                .explainReasonBeforeRequest()
                .onExplainRequestReason((scope, deniedList) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "我已明白"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        if (type == 1) {
                            openCamera();
                        } else{
                            openAlbum();
                        }
                    } else {
                        if (type == 1) {
                            Snackbar.make(mBinding.fragmentMeIcon, R.string.grant_camera_permission, Snackbar.LENGTH_LONG)
                                    .setActionTextColor(ContextCompat.getColor(requireActivity(), android.R.color.white)).show();
                        } else {
                            Snackbar.make(mBinding.fragmentMeIcon,R.string.grant_storage_permission,Snackbar.LENGTH_LONG)
                                    .setActionTextColor(ContextCompat.getColor(requireActivity(),android.R.color.white)).show();
                        }

                    }
                });
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent,getString(R.string.please_select_album)),REQUEST_ALBUM);
    }

    private void openCamera() {
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date()) + ".png";
     //   photoFile = new File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),fileName);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            photoFile = new File(Environment.getExternalStorageDirectory() + "/Information",fileName);
        else
            photoFile = new File(requireActivity().getExternalCacheDir(),fileName);
        if (!photoFile.getParentFile().exists())
            photoFile.getParentFile().mkdirs();

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProviderCompat.getUriForFile(requireActivity(),photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }
        else
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    private void showBottomSheet() {
        mBottomSheetDialog = new BottomSheetDialog(requireActivity(), R.style.BottomSheetTheme);
        FragmentMeSwitchIconBinding binding = DataBindingUtil.inflate(LayoutInflater.from(requireActivity()), R.layout.fragment_me_switch_icon, null, false);
        mBottomSheetDialog.setContentView(binding.getRoot());
        binding.fragmentMePhotograph.setOnClickListener(this::onItemClick);
        binding.fragmentMeAlbum.setOnClickListener(this::onItemClick);
        binding.fragmentMeCancel.setOnClickListener(this::onItemClick);
       // BottomSheetBehavior.from((View) binding.getRoot().getParent()).setPeekHeight(getPeekHeight());
        mBottomSheetDialog.setCanceledOnTouchOutside(true);
       // mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.show();
    }

    private int getPeekHeight() {
        int heightPixels = requireActivity().getResources().getDisplayMetrics().heightPixels;
        return (int) (heightPixels * 0.5);
    }

    private void cancelBottomSheet() {
        if (mBottomSheetDialog != null ) {
            mBottomSheetDialog.cancel();
            mBottomSheetDialog = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == Activity.RESULT_OK && photoFile.exists()) {
                  //  MyLog.log(TAG, "photoFile:" + photoFile.getAbsolutePath()); //photoFile:/storage/emulated/0/Information/20200818180518.png
                 //   startPhotoZoom(FileProviderCompat.getUriForFile(requireActivity(), photoFile));
                    startPhotoZoom(UriUtil.getImageContentUri(requireActivity(),photoFile));
                }
                break;
            case REQUEST_ALBUM:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    //  MyLog.log(TAG,"data1111111111:" + data.getData()); //content://media/external/images/media/21631
                    startPhotoZoom(data.getData());
                }
                break;

            case CROP_PICTURE:
                if (resultCode == Activity.RESULT_OK && cropImageUri != null) {
                    GlideUtil.getInstance().loadImage(requireActivity(), cropImageUri, mBinding.fragmentMeIcon, "#3700B3");
                    SPUtil.getInstance(getActivity()).saveUserPhoto(cropImageUri.toString());
                }
                if (photoFile != null && photoFile.exists()) {
                    photoFile.delete();
                }
                break;

        }
    }

    private void startPhotoZoom(Uri uri) {
        if (uri == null) return;
        MyLog.log(TAG,"================" + uri.toString());
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date()) + ".png";
     //   File cropFile = new File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
        File cropFile = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            cropFile = new File(Environment.getExternalStorageDirectory() + "/Information",fileName);
        else
            cropFile = new File(requireActivity().getExternalCacheDir(),fileName);
        if (!cropFile.getParentFile().exists())
            cropFile.getParentFile().mkdirs();

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri,"image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        intent.putExtra("return-data", false);
        cropImageUri = Uri.parse("file:///" + cropFile.getAbsolutePath());
        intent.putExtra(MediaStore.EXTRA_OUTPUT,cropImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, CROP_PICTURE);

    }
}
