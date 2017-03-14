package com.shyky.library.adapter.base;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shyky.library.BaseApplication;
import com.shyky.library.R;
import com.shyky.library.util.ResourceUtil;
import com.shyky.util.ObjectUtil;
import com.shyky.util.RandomUtil;

import java.util.HashMap;

import static com.socks.library.KLog.d;

/**
 * 具有显示、加载及缓存图片功能的RecyclerView Adapter
 *
 * @param <ENTITY> 泛型参数，数据源集合中的实体
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.5
 * @date 2016/6/3
 * @email sj1510706@163.com
 * @since 1.0
 */
public abstract class BaseRecyclerViewImageAdapter<ENTITY> extends BaseRecyclerViewAdapter<ENTITY> {
    /**
     * 应用程序上下文
     */
    protected Context context;
    private HashMap<Integer, Float> indexMap;
    /**
     * Item布局高度状态
     */
    private final int SIZE_SCALE_01 = 1;
    private final int SIZE_SCALE_02 = 2;

    /**
     * 构造方法
     *
     * @param context 应用程序上下文
     */
    protected BaseRecyclerViewImageAdapter(@NonNull Context context) {
        super(context);
        this.context = context;
        indexMap = new HashMap<>();
    }

    /**
     * 获取RequestManager对象
     *
     * @return 成功返回RequestManager对象，否则返回null
     */
    private RequestManager glide() {
        return Glide.with(BaseApplication.getContext());
    }

    /**
     * 获取RequestManager对象
     *
     * @param context 应用程序上下文
     * @return 成功返回RequestManager对象，否则返回null
     */
    private RequestManager glide(@NonNull Context context) {
        return Glide.with(context);
    }

    /**
     * 获取DrawableRequestBuilder对象
     *
     * @param path 图片的url或本地路径
     * @return 成功返回DrawableRequestBuilder对象，否则返回null
     */
    private DrawableRequestBuilder<String> load(@NonNull String path) {
        // 开启磁盘缓存
        return glide().load(path).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false);
    }

    /**
     * @param context 应用程序上下文
     * @param path    图片的url或本地路径
     * @return 成功返回DrawableRequestBuilder对象，否则返回null
     */
    private DrawableRequestBuilder<String> load(@NonNull Context context, @NonNull String path) {
        // 开启磁盘缓存
        return glide(context).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false);
    }

    /**
     * 显示并缓存图片
     *
     * @param path      图片的URL或本地路径
     * @param imageView ImageView控件
     */
    public void showImage(@NonNull String path, @NonNull ImageView imageView) {
        if (ObjectUtil.notNull(imageView)) {
            if (context == null)
                load(path).into(imageView);
            else
                load(context, path).into(imageView);
        }
    }

    /**
     * 显示并缓存图片
     *
     * @param path       图片的URL或本地路径
     * @param viewHolder BaseRecyclerViewHolder对象
     * @param viewType   item view布局类型
     * @param viewResId  ImageView控件资源文件ID
     */
    public void showImage(@NonNull String path, @NonNull BaseRecyclerViewHolder viewHolder, int viewType, @IdRes int viewResId) {
        showImage(path, (ImageView) viewHolder.getView(viewType, viewResId));
    }

    /**
     * 显示并缓存图片
     *
     * @param path                 图片的URL或本地路径
     * @param viewHolder           BaseRecyclerViewHolder对象
     * @param viewType             item view布局类型
     * @param viewResId            ImageView控件资源文件ID
     * @param loadingDrawableResId 加载中图片资源文件ID
     * @param failureDrawableResId 加载失败图片资源文件ID
     */
    public final void showImage(@NonNull String path, @NonNull BaseRecyclerViewHolder viewHolder, int viewType, @IdRes int viewResId, @DrawableRes int loadingDrawableResId, @DrawableRes int failureDrawableResId) {
        showImage(path, (ImageView) viewHolder.getView(viewType, viewResId), loadingDrawableResId, failureDrawableResId);
    }

    /**
     * 显示并缓存图片
     *
     * @param path                 图片的URL或本地路径
     * @param imageView            ImageView控件
     * @param loadingDrawableResId 加载中图片资源文件ID
     * @param failureDrawableResId 加载失败图片资源文件ID
     */
    public final void showImage(@NonNull String path, @NonNull ImageView imageView, @DrawableRes int loadingDrawableResId, @DrawableRes int failureDrawableResId) {
        if (ObjectUtil.notNull(imageView)) {
            if (context == null)
                load(path).placeholder(loadingDrawableResId).error(failureDrawableResId).crossFade().dontTransform().into(imageView);
            else
                load(context, path).placeholder(loadingDrawableResId).error(failureDrawableResId).crossFade().dontTransform().into(imageView);
        }
    }

    /**
     * 显示并缓存图片
     *
     * @param path      图片的URL或本地路径
     * @param imageView ImageView控件
     * @param width     宽度
     * @param height    高度
     */
//    public final void showImage(@NonNull String path, @NonNull ImageView imageView, int width, int height) {
//        // 非空判断
//        if (!TextUtil.isEmptyAndNull(path) && imageView != null)
//            load(path).override(width, height).into(imageView);
//    }

    /**
     * 显示并缓存图片并重新设置宽度和高度
     *
     * @param path      图片的URL或本地路径
     * @param resId     默认显示图片
     * @param imageView ImageView控件
     */
    public final void showImage(@NonNull String path, @DrawableRes int resId, @NonNull ImageView imageView) {
        if (ObjectUtil.notNull(imageView)) {
            if (resId > 0) {
                load(path).placeholder(resId).into(imageView);
            } else {
                load(path).into(imageView);
            }
        }
    }

    /**
     * 显示并缓存图片并重新设置宽度和高度
     *
     * @param path      图片的URL或本地路径
     * @param imageView ImageView控件
     * @param position  item position
     */
    public final void showImage(@NonNull String path, @NonNull ImageView imageView, int position) {
        if (ObjectUtil.notNull(imageView)) {
            showImage(path, imageView, position, R.dimen.dp_176, R.dimen.dp_258);
        }
    }

    /**
     * 显示图片并重新设置宽度和高度
     *
     * @param path      图片的URL或本地路径
     * @param imageView ImageView控件
     * @param position  item position
     * @param width     宽度
     * @param height    高度
     */
    public final void showImage(@NonNull String path, @NonNull ImageView imageView, int position, int width, int height) {
        if (ObjectUtil.notNull(imageView)) {
            showImage(path, imageView);
            resizeItemView(imageView, getScaleType(position), width, height);
        }
    }

    /**
     * 获取保存起来的item的高度的状态
     *
     * @param position item的position位置
     * @return 直接返回相应scaleType的状态
     */
    private float getScaleType(int position) {
        if (!indexMap.containsKey(position)) {
            float scaleType;
            if (position == 0) {
                scaleType = SIZE_SCALE_01;
            } else if (position == 1) {
                scaleType = SIZE_SCALE_02;
            } else {
                scaleType = RandomUtil.getRandomInt(100) % 2 == 0 ? SIZE_SCALE_01 : SIZE_SCALE_02;
            }
            indexMap.put(position, scaleType);
        }
        return indexMap.get(position);
    }

    /**
     * 重新设置图片宽度和高度
     *
     * @param frontCoverImage
     * @param scaleType
     * @param width
     * @param height
     */
    private void resizeItemView(@NonNull ImageView frontCoverImage, float scaleType, int width, int height) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) frontCoverImage.getLayoutParams();
        if (scaleType == SIZE_SCALE_01) {
            params.width = ResourceUtil.getDimens(width);
            params.height = ResourceUtil.getDimens(width);
        } else if (scaleType == SIZE_SCALE_02) {
            params.width = ResourceUtil.getDimens(width);
            params.height = ResourceUtil.getDimens(height);
        }
        d("width", params.width);
        d("height", params.height);
        frontCoverImage.setLayoutParams(params);
    }
}