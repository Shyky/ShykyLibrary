package com.shyky.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;

import java.io.File;

import static com.socks.library.KLog.d;

/**
 * 使用Glide加载、显示图片
 *
 * @author Shyky
 * @version 1.1
 * @date 2016/12/29
 * @since 1.0
 */
public final class ImageLoader {
    //默认配置(此处请勿修改setDiskCacheStrategy，使用SOURCE可以避免图片底部显示兰绿色，如需求需要，请自定义ImageLoadConfig)
    private final static LoadConfig DEFAULT_CONFIG = new LoadConfig.Builder().
            setCropType(LoadConfig.CENTER_INSIDE).
            setAsBitmap(true).
            setPlaceHolderResId(R.mipmap.ic_loading).
            setErrorResId(R.mipmap.ic_loading_fail).
            setDiskCacheStrategy(LoadConfig.DiskCache.SOURCE).
            setPrioriy(LoadConfig.LoadPriority.HIGH).build();

    public interface LoadListener {
        void onSuccess(@NonNull Bitmap bitmap);

        void onFailure();
    }

    /**
     * 图片最终显示在ImageView上的宽高像素
     * Created by mChenys on 2016/4/29.
     */
    public static class OverrideSize {
        private final int width;
        private final int height;

        public OverrideSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public static class LoadConfig {
        public static final int CENTER_CROP = 11;
        public static final int FIT_CENTER = 12;
        public static final int CENTER_INSIDE = 13;

        /**
         * 硬盘缓存策略
         */
        public enum DiskCache {
            NONE(DiskCacheStrategy.NONE),//跳过硬盘缓存
            SOURCE(DiskCacheStrategy.SOURCE),//仅仅保存原始分辨率的图片
            RESULT(DiskCacheStrategy.RESULT),//仅仅缓存最终分辨率的图像(降低分辨率后的或者转换后的)
            ALL(DiskCacheStrategy.ALL);//缓存所有版本的图片,默认行为
            private DiskCacheStrategy strategy;

            DiskCache(DiskCacheStrategy strategy) {
                this.strategy = strategy;
            }

            public DiskCacheStrategy getStrategy() {
                return strategy;
            }
        }

        /**
         * 加载优先级策略
         */
        public enum LoadPriority {
            LOW(Priority.LOW),
            NORMAL(Priority.NORMAL),
            HIGH(Priority.HIGH),
            IMMEDIATE(Priority.IMMEDIATE),;
            Priority priority;

            LoadPriority(Priority priority) {
                this.priority = priority;
            }

            public Priority getPriority() {
                return priority;
            }
        }

        private LoadConfig(@NonNull Builder builder) {

        }

        public static class Builder {
            public static final int CENTER_CROP = 0;
            public static final int FIT_CENTER = 1;
            public static final int CENTER_INSIDE = 2;
            private Integer placeHolderResId; //默认占位资源
            private Integer errorResId; //错误时显示的资源
            private boolean crossFade; //是否淡入淡出动画
            private int crossDuration; //淡入淡出动画持续的时间
            private OverrideSize size; //图片最终显示在ImageView上的宽高度像素
            private int CropType = CENTER_CROP; //裁剪类型,默认为中部裁剪
            private boolean asGif; //true,强制显示的是gif动画,如果url不是gif的资源,那么会回调error()
            private boolean asBitmap;//true,强制显示为常规图片,如果是gif资源,则加载第一帧作为图片
            private boolean skipMemoryCache;//true,跳过内存缓存,默认false
            private DiskCache diskCacheStrategy; //硬盘缓存,默认为all类型
            private LoadPriority prioriy;
            private float thumbnail;//设置缩略图的缩放比例0.0f-1.0f,如果缩略图比全尺寸图先加载完，就显示缩略图，否则就不显示
            private String thumbnailUrl;//设置缩略图的url,如果缩略图比全尺寸图先加载完，就显示缩略图，否则就不显示
            private SimpleTarget<Bitmap> simpleTarget; //指定simpleTarget对象,可以在Target回调方法中处理bitmap,同时该构造方法中还可以指定size
            private ViewTarget<? extends View, GlideDrawable> viewTarget;//指定viewTarget对象,可以是自定义View,该构造方法传入的view和通配符的view是同一个
            private NotificationTarget notificationTarget; //设置通知栏加载大图片的target;
            private AppWidgetTarget appWidgetTarget;//设置加载小部件图片的target
            private Integer animResId;//图片加载完后的动画效果,在异步加载资源完成时会执行该动画。
            private ViewPropertyAnimation.Animator animator; //在异步加载资源完成时会执行该动画。可以接受一个Animator对象
            private boolean cropCircle;//圆形裁剪
            private boolean roundedCorners;//圆角处理
            private boolean grayscale;//灰度处理
            private boolean blur;//高斯模糊处理
            private boolean rotate;//旋转图片
            private int rotateDegree;//默认旋转°
            private String tag; //唯一标识

            public Builder() {

            }

            public Builder setPlaceHolderResId(Integer placeHolderResId) {
                this.placeHolderResId = placeHolderResId;
                return this;
            }

            public Builder setErrorResId(Integer errorResId) {
                this.errorResId = errorResId;
                return this;
            }

            public Builder setCrossFade(boolean crossFade) {
                this.crossFade = crossFade;
                return this;
            }

            public Builder setCrossDuration(int crossDuration) {
                this.crossDuration = crossDuration;
                return this;
            }

            public Builder setSize(OverrideSize size) {
                this.size = size;
                return this;
            }

            public Builder setCropType(int cropType) {
                CropType = cropType;
                return this;
            }

            public Builder setAsGif(boolean asGif) {
                this.asGif = asGif;
                return this;
            }

            public Builder setAsBitmap(boolean asBitmap) {
                this.asBitmap = asBitmap;
                return this;
            }

            public Builder setSkipMemoryCache(boolean skipMemoryCache) {
                this.skipMemoryCache = skipMemoryCache;
                return this;
            }

            public Builder setDiskCacheStrategy(DiskCache diskCacheStrategy) {
                this.diskCacheStrategy = diskCacheStrategy;
                return this;
            }

            public Builder setPrioriy(LoadPriority prioriy) {
                this.prioriy = prioriy;
                return this;
            }

            public Builder setThumbnail(float thumbnail) {
                this.thumbnail = thumbnail;
                return this;
            }

            public Builder setThumbnailUrl(String thumbnailUrl) {
                this.thumbnailUrl = thumbnailUrl;
                return this;
            }

            public Builder setSimpleTarget(SimpleTarget<Bitmap> simpleTarget) {
                this.simpleTarget = simpleTarget;
                return this;
            }

            public Builder setViewTarget(ViewTarget<? extends View, GlideDrawable> viewTarget) {
                this.viewTarget = viewTarget;
                return this;
            }

            public Builder setNotificationTarget(NotificationTarget notificationTarget) {
                this.notificationTarget = notificationTarget;
                return this;
            }

            public Builder setAppWidgetTarget(AppWidgetTarget appWidgetTarget) {
                this.appWidgetTarget = appWidgetTarget;
                return this;
            }

            public Builder setAnimResId(Integer animResId) {
                this.animResId = animResId;
                return this;
            }

            public Builder setAnimator(ViewPropertyAnimation.Animator animator) {
                this.animator = animator;
                return this;
            }

            public Builder setCropCircle(boolean cropCircle) {
                this.cropCircle = cropCircle;
                return this;
            }

            public Builder setRoundedCorners(boolean roundedCorners) {
                this.roundedCorners = roundedCorners;
                return this;
            }

            public Builder setGrayscale(boolean grayscale) {
                this.grayscale = grayscale;
                return this;
            }

            public Builder setBlur(boolean blur) {
                this.blur = blur;
                return this;
            }

            public Builder setRotate(boolean rotate) {
                this.rotate = rotate;
                return this;
            }

            public Builder setRotateDegree(int rotateDegree) {
                this.rotateDegree = rotateDegree;
                return this;
            }

            public Builder setTag(String tag) {
                this.tag = tag;
                return this;
            }

            public LoadConfig build() {
                return new LoadConfig(this);
            }
        }
    }

    public static void showImage(@NonNull ImageView imageView, @NonNull String imageUrlOrFileName) {
        showImage(imageView, imageUrlOrFileName, null);
    }

    public static void showImage(@NonNull ImageView imageView, @NonNull String imageUrlOrFileName, @NonNull LoadConfig config) {
        showImage(imageView, imageUrlOrFileName, config, null);
    }

    public static void showImage(@NonNull ImageView imageView, @NonNull String imageUrlOrFileName, @NonNull LoadConfig config, @NonNull LoadListener listener) {
        load(imageView.getContext(), imageView, imageUrlOrFileName, config, listener);
    }

    public static void showImage(@NonNull ImageView imageView, @DrawableRes int resId) {
        showImage(imageView, resId, null);
    }

    public static void showImage(@NonNull ImageView imageView, @DrawableRes int resId, @NonNull LoadConfig config) {
        showImage(imageView, resId, config, null);
    }

    public static void showImage(@NonNull ImageView imageView, @DrawableRes int resId, @NonNull LoadConfig config, @NonNull LoadListener listener) {
        load(imageView.getContext(), imageView, resId, config, listener);
    }

    public static void showImage(@NonNull ImageView imageView, @NonNull File file) {
        showImage(imageView, file, null);
    }

    public static void showImage(@NonNull ImageView imageView, @NonNull File file, @NonNull LoadConfig config) {
        showImage(imageView, file, config, null);
    }

    public static void showImage(@NonNull ImageView imageView, @NonNull File file, @NonNull LoadConfig config, @NonNull LoadListener listener) {
        load(imageView.getContext(), imageView, file, config, listener);
    }

    public static void showImage(@NonNull ImageView imageView, @NonNull Uri uri) {
        showImage(imageView, uri, null);
    }

    public static void showImage(@NonNull ImageView imageView, @NonNull Uri uri, @NonNull LoadConfig config) {
        showImage(imageView, uri, config, null);
    }

    public static void showImage(@NonNull ImageView imageView, @NonNull Uri uri, @NonNull LoadConfig config, @NonNull LoadListener listener) {
        load(imageView.getContext(), imageView, uri, config, listener);
    }

    public static void showImage(Context context, Object objUrl, LoadConfig config, final LoadListener listener) {
        load(context, null, objUrl, config, listener);
    }

    private static void load(Context context, ImageView view, Object objUrl, LoadConfig config, LoadListener listener) {

    }

    /**
     * 取消所有正在下载或等待下载的任务。
     */
    public static void cancelAllTasks(Context context) {
        Glide.with(context).pauseRequests();
    }

    /**
     * 恢复所有任务
     */
    public static void resumeAllTasks(Context context) {
        Glide.with(context).resumeRequests();
    }

    /**
     * 清除磁盘缓存
     *
     * @param context
     */
    public static void clearDiskCache(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }

    /**
     * 清除所有缓存
     *
     * @param context
     */
    public static void cleanAll(Context context) {
        clearDiskCache(context);
        Glide.get(context).clearMemory();
    }

    public static File getDiskCacheDir() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/YourCacheDirectory/";
        d("GlidePathUtil", "path-->" + path);
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/YourCacheDirectory/");
    }

    /**
     * 获取缓存大小
     *
     * @param context
     * @return
     */
    public static synchronized long getDiskCacheSize(@NonNull Context context) {
        long size = 0L;
        File cacheDir = getDiskCacheDir();
        if (cacheDir != null && cacheDir.exists()) {
            File[] files = cacheDir.listFiles();
            if (files != null) {
                File[] arr$ = files;
                int len$ = files.length;

                for (int i$ = 0; i$ < len$; ++i$) {
                    File imageCache = arr$[i$];
                    if (imageCache.isFile()) {
                        size += imageCache.length();
                    }
                }
            }
        }

        return size;
    }


    public static void clearTarget(View view) {
        Glide.clear(view);
    }

    /**
     * 清缓存
     *
     * @author dingpeihua
     * @date 2016/6/21 17:43
     * @version 1.0
     */
    public static void clear() {
        new Thread() {
            @Override
            public void run() {
                Glide.get(BaseApplication.getContext()).clearDiskCache();
            }
        }.start();
        Glide.get(BaseApplication.getContext()).clearMemory();
    }
}