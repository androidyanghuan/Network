package com.example.classroom.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.example.classroom.R;
import com.example.classroom.utils.glide.GlideCircleTransform;
import com.example.classroom.utils.glide.GlideCircleWithBorder;
import com.example.classroom.utils.glide.RotateTransformation;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import io.reactivex.Observable;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class GlideUtil {
    private volatile static GlideUtil instance;
    //加载bitmap，如果是GIF则显示第一帧
    private static String LOAD_BITMAP = "GLIDEUTILS_GLIDE_LOAD_BITMAP";
    //加载gif动画
    private static String LOAD_GIF = "GLIDEUTILS_GLIDE_LOAD_GIF";


    private GlideUtil() {

    }

    public static GlideUtil getInstance() {
        if (instance == null) {
            synchronized (GlideUtil.class) {
                if (instance == null) {
                    instance = new GlideUtil();
                }
            }
        }
        return instance;
    }

    /** 加载本来资源文件 */
    public void glideLocalDrawable(View view, int resId, ImageView imageView) {
        Glide.with(view).load(resId).into(imageView);
    }

    /** 加载网络图片不缓存 */
    public void glideLoaderNoCache(Context context, String url, ImageView imageView){
      //  MyLog.log("*************","url:" + url);
    /*    RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(context).load(url).apply(options).error(R.drawable.icon_loading).into(imageView);*/
        Glide.with(context).load(url + "?key=" + Math.random()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(imageView);
    }

    /** 加载网络大长图片 */
/*    public void glideBigPicture(Context context, String url, final SubsamplingScaleImageView imageView){
        Glide.with(context.getApplicationContext()).downloadOnly().load(url).listener(new RequestListener<File>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                ToastUtil.showToast(context, "图片加载失败!");
                return false;
            }

            @Override
            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                showImage(imageView,resource);
                return false;
            }
        }).preload();
    }*/

/*    private void showImage(SubsamplingScaleImageView imageView, File resource) {
        try {
            FileInputStream fis = new FileInputStream(resource);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
            if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
            int rw = options.outWidth;
            int rh = options.outHeight;
            int height = imageView.getHeight();
            if (rh >= height && rh / rw >= 3) {
                imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                imageView.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(1.0f, new PointF(0, 0), 0));
            } else {
                imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
                imageView.setImage(ImageSource.uri(Uri.fromFile(resource)));
                imageView.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 带2dp边框的圆形图片
     * @param context 上下文
     * @param url 图片的url地址
     * @param imageView 加载的控件
     * @param colorId 边框的颜色
     */
    public void loadBroadImage(Context context, String url, ImageView imageView, int colorId) {
        Glide.with(context).asBitmap().placeholder(R.drawable.placeholder)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .transform(new GlideCircleWithBorder(context, 2, ContextCompat.getColor(context,colorId)))
                .error(R.drawable.placeholder)
                .into(imageView);
    }

    /**
     * 加载圆形图片
     * @param context 上下文
     * @param url 图片的地址
     * @param imageView 加载的控件
     */
    public void loadCircleImage(Context context, String url, ImageView imageView) {
        Glide.with(context).asBitmap().placeholder(R.drawable.placeholder)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);

    }


    /**
     * 加载网络图片
     * @param context 上下文
     * @param path 图片的地址
     * @param imageview 加载的控件
     */
    public void loadImage(Context context, String path, ImageView imageview) {
        Glide.with(context)
                .load(path)
                .thumbnail(0.5f)
              //  .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imageview);
    }

    /**
     * 加载本地图片uri
     * @param context 上下文
     * @param uri 图片的uri
     * @param imageview 加载的控件
     */
    public void loadImage(Context context, Uri uri, ImageView imageview, String color) {
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .thumbnail(0.5f)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .transform(new GlideCircleWithBorder(context, 2, Color.parseColor(color)))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageview);
    }

    /**
     * 加载用户网络图片
     * @param context 上下文
     * @param path 图片的地址
     * @param imageview 加载的控件
     */
    public void loadUserIcon(Context context, String path, ImageView imageview) {
        Glide.with(context).load(path).error(R.drawable.placeholder).into(imageview);
    }


    /**
     * 加载网络图片
     * @param context 上下文
     * @param path 图片的地址
     * @param imageview 加载的控件
     */
    public void loadMasterImage(Context context, String path, ImageView imageview) {
        Glide.with(context).load(path).error(R.drawable.placeholder).into(imageview);
    }

    /**
     * 加载网络缩略图
     * @param context 上下文
     * @param path 图片的地址
     * @param thumbnail 先加载thumbnail/10尺寸的缩略图然后再加载全图
     * @param imageview 加载的控件
     */
    public void loadThumbnailImage(Context context, String path, float thumbnail, ImageView imageview) {
        Glide.with(context).load(path).centerCrop().thumbnail(thumbnail).into(imageview);
    }

    /**
     * 加载file图片
     * @param context 上下文
     * @param file 图片的地址
     * @param imageview 加载的控件
     */
    public void loadImage(Context context, File file, ImageView imageview) {
        String updateTime = String.valueOf(System.currentTimeMillis()); // 在需要重新获取更新的图片时调用
         Glide.with(context).load(file).transition(withCrossFade())
                 .signature(new ObjectKey(updateTime))
                 .into(imageview);
    }

    /**
     * 加载带尺寸的图片
     * @param context 上下文
     * @param path 图片地址
     * @param Width 图片宽
     * @param Height 图片高
     * @param imageview 要加载的图片控件
     */
    public void loadImageWithSize(Context context, String path,
                                  int Width, int Height, ImageView imageview) {
        Glide.with(context).load(path).override(Width, Height)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageview);
    }

    /**
     * 加载带边框的圆形本地图片
     * @param context 上下文
     * @param path 图片路径
     * @param imageview 加载图片控件
     */
    public void loadImageWithLocation(Context context, Integer path, ImageView imageview, String color) {
        Glide.with(context)
                .asBitmap()
                .load(path)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .transform(new GlideCircleWithBorder(context, 2, Color.parseColor(color)))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageview);
    }

    public void loadImageAndHiddenProgressBar(FragmentActivity activity, ImageView imageView, String url, ProgressBar progressBar) {
        Glide.with(imageView.getContext())
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        activity.supportStartPostponedEnterTransition();
                        new Handler().postDelayed(() -> {
                            ToastUtil.showToast(activity,R.string.network_error);
                            activity.supportFinishAfterTransition();
                        },5000);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        activity.supportStartPostponedEnterTransition();
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
             //   .apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
            /*    .into(new DrawableImageViewTarget(imageView){
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        activity.supportStartPostponedEnterTransition();
                        progressBar.setVisibility(View.GONE);
                    }
                });*/
    }


    public void loadBitmap(Context context, String path, ImageView imageView) {
        loadContextBitmap(context, path, imageView, 0, 0, null);
    }

    /**
     * 加载指定圆角的图片
     */
    public void loadRoundCornersImage(Context context, String path, int roundingRadius, ImageView imageView) {
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(roundingRadius);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions override = RequestOptions.bitmapTransform(roundedCorners).override(300,300);
        Glide.with(context)
                .asBitmap()
                .load(path)
                .thumbnail(0.5f)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .apply(override)
                .into(imageView);
    }

    /**
     * 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制
     * 使用activity 会受到Activity生命周期控制
     * 使用FragmentActivity 会受到FragmentActivity生命周期控制
     * @param path 图片路径
     * @param imageView 图片控件
     * @param placeid     占位
     * @param errorid     错误
     * @param bitmapOrgif 加载普通图片 或者GIF图片 ，GIF图片设置bitmap显示第一帧
     */
    private void loadContextBitmap(Context context, String path, ImageView imageView, int placeid, int errorid, String bitmapOrgif) {
        if (context != null) {
            if (bitmapOrgif == null || bitmapOrgif.equals(LOAD_BITMAP)) {
                RequestOptions options = new RequestOptions().centerCrop().placeholder(placeid).priorityOf(Priority.HIGH).error(errorid);
                Glide.with(context).load(path).apply(options).into(imageView);
            } else if (bitmapOrgif.equals(LOAD_GIF)) {
                RequestOptions options = new RequestOptions().centerCrop().placeholder(placeid).error(errorid).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                Glide.with(context).load(path).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).apply(options).into(imageView);
            }
        }
    }

    /**
     * Glide请求图片，会受到support.v4.app.Fragment生命周期控制。
     * @param fragment Fragment
     * @param path 图片路径
     * @param imageView 图片控件
     * @param placeid 占位符
     * @param errorid 加载失败显示图
     * @param bitmapOrgif 加载普通图片 或者GIF图片 ，GIF图片设置bitmap显示第一帧
     */
    public void loadSupportv4FragmentBitmap(Fragment fragment, String path, ImageView imageView, int placeid, int errorid, String bitmapOrgif) {
        RequestOptions options = new RequestOptions().placeholder(placeid).error(errorid).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        if (bitmapOrgif == null || bitmapOrgif.equals(LOAD_BITMAP)) {
            Glide.with(fragment).load(path).transition(withCrossFade()).apply(options).into(imageView);
        } else if (bitmapOrgif.equals(LOAD_GIF)) {
            Glide.with(fragment).load(path).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).apply(options).into(imageView);
        }
    }

    /**
     * 加载设置圆形图片
     * 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制
     * <BR/>使用activity 会受到Activity生命周期控制
     * <BR/>使用FragmentActivity 会受到FragmentActivity生命周期控制
     * @param context 上下文
     * @param path 图片路径
     * @param imageView 图片控件
     */
    @SuppressWarnings("unchecked")
    public void loadContextCircleBitmap(Context context, String path, ImageView imageView) {
        RequestOptions options = new RequestOptions().priority(Priority.HIGH).bitmapTransform(new GlideCircleTransform()).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(path).apply(options).into(imageView);
    }

    /**
     * 旋转图片
     * 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制
     * <BR/>使用activity 会受到Activity生命周期控制
     * <BR/>使用FragmentActivity 会受到FragmentActivity生命周期控制
     * @param context 上下文
     * @param path 图片路径
     * @param imageView 图片控件
     * @param rotateRotationAngle 旋转角度
     */
    @SuppressWarnings("unchecked")
    public void loadContextRotateBitmap(Context context, String path, ImageView imageView, Float rotateRotationAngle) {
        RequestOptions options = new RequestOptions().priority(Priority.HIGH).bitmapTransform(new RotateTransformation(rotateRotationAngle)).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(path).apply(options).into(imageView);
    }

    /**
     * Glide 加载旋转图片 会受到Fragment生命周期控制
     * @param fragment Fragment
     * @param path 图片路径
     * @param imageView 图片控件
     * @param rotateRotationAngle 旋转角度
     */
    @SuppressWarnings("unchecked")
    public void loadFragmentRotateBitmap(Fragment fragment, String path, ImageView imageView, Float rotateRotationAngle) {
        RequestOptions options = new RequestOptions().priority(Priority.HIGH).bitmapTransform(new RotateTransformation(rotateRotationAngle)).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(fragment).load(path).apply(options).into(imageView);
    }

}
