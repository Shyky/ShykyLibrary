package com.shyky.library.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shyky.library.util.ImageUtil;
import com.shyky.library.util.ResourceUtil;
import com.shyky.util.ListUtil;
import com.shyky.util.ObjectUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 通用的用于展示网络或本地图片的ViewPager适配器
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/9/13
 * @since 1.0
 */
public final class PagerAdapter extends android.support.v4.view.PagerAdapter {
    private Context context;
    private List<String> imgUrls;
    private int layoutResId;
    private int imageViewResId;
    private int loadingDrawableResId;
    private int failureDrawableResId;
    private ViewPager viewPager;
    private OnItemClickListener onItemClickListener;
    private int currentPosition = -1;

    public interface OnItemClickListener {
        void onClick(@NonNull View view, int position);
    }

    /**
     * 构造方法
     *
     * @param context 应用程序上下文
     */
    public PagerAdapter(@NonNull Context context) {
        this.context = context;
    }

    public PagerAdapter(@NonNull Context context, @NonNull List<String> imgUrls) {
        this(context);
        this.imgUrls = imgUrls;
    }

    public PagerAdapter(@NonNull Context context, @NonNull String[] imgUrls) {
        this(context);
        if (ObjectUtil.notNull(imgUrls)) {
            this.imgUrls = Arrays.asList(imgUrls);
        }
    }

    public PagerAdapter(@NonNull Context context, @LayoutRes int layoutResId) {
        this.context = context;
        this.layoutResId = layoutResId;
    }

    public PagerAdapter(@NonNull Context context, @LayoutRes int layoutResId, @NonNull List<String> imgUrls) {
        this(context, layoutResId);
        this.imgUrls = imgUrls;
    }

    public PagerAdapter(@NonNull Context context, @LayoutRes int layoutResId, @DrawableRes int loadingDrawableResId, @DrawableRes int failureDrawableResId, @NonNull List<String> imgUrls) {
        this(context, layoutResId);
        this.loadingDrawableResId = loadingDrawableResId;
        this.failureDrawableResId = failureDrawableResId;
        this.imgUrls = imgUrls;
    }

    public PagerAdapter(@NonNull Context context, @LayoutRes int layoutResId, @IdRes int imageViewResId, @DrawableRes int loadingDrawableResId, @DrawableRes int failureDrawableResId, @NonNull List<String> imgUrls) {
        this(context, layoutResId);
        this.imageViewResId = imageViewResId;
        this.loadingDrawableResId = loadingDrawableResId;
        this.failureDrawableResId = failureDrawableResId;
        this.imgUrls = imgUrls;
    }

    public PagerAdapter(@NonNull Context context, @LayoutRes int layoutResId, @DrawableRes int loadingDrawableResId, @DrawableRes int failureDrawableResId) {
        this(context, layoutResId);
        this.loadingDrawableResId = loadingDrawableResId;
        this.failureDrawableResId = failureDrawableResId;
    }

    public PagerAdapter(@NonNull Context context, @LayoutRes int layoutResId, @IdRes int imageViewResId, @DrawableRes int loadingDrawableResId, @DrawableRes int failureDrawableResId) {
        this(context, layoutResId);
        this.imageViewResId = imageViewResId;
        this.loadingDrawableResId = loadingDrawableResId;
        this.failureDrawableResId = failureDrawableResId;
    }

    public PagerAdapter(@NonNull Context context, @LayoutRes int layoutResId, @IdRes int imageViewResId, @DrawableRes int loadingDrawableResId, @DrawableRes int failureDrawableResId, ViewPager viewPager) {
        this(context, layoutResId);
        this.imageViewResId = imageViewResId;
        this.loadingDrawableResId = loadingDrawableResId;
        this.failureDrawableResId = failureDrawableResId;
        this.viewPager = viewPager;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(@NonNull List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public void setOnItemClickListener(@NonNull OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return ListUtil.isEmpty(imgUrls) ? 0 : imgUrls.size();
    }

    public String getItem(int position) {
        return getCount() == 0 ? null : imgUrls.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (currentPosition == position)
            return;
        currentPosition = position;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        if (layoutResId > 0) {
            final View inflateView = LayoutInflater.from(context).inflate(layoutResId, container, false);
            inflateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ObjectUtil.notNull(onItemClickListener)) {
                        onItemClickListener.onClick(v, position);
                    }
                }
            });
            if (inflateView instanceof ImageView) {
                final ImageView imageView = (ImageView) inflateView;
                final String imgUrl = getItem(position);
                if (loadingDrawableResId > 0 && failureDrawableResId > 0) {
                    ImageUtil.showImage(imgUrl, imageView, loadingDrawableResId, failureDrawableResId);

//                            new GlideDrawableImageViewTarget(imageView) {
//                        @Override
//                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                            super.onResourceReady(resource, animation);
//                            Rect rect = resource.getBounds();
//                            int height = rect.height();
//                            int width = rect.width();
//                            if (viewPager != null) {
//                                int limitHeight = viewPager.getMeasuredHeight();
//                                int limitWidth = width * limitHeight / height;
//                                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
//                                d("limitHeightAndlimitWidth:" + limitHeight + ", " + limitWidth + ", " + height + ", " + width);
//                                lp.width = limitWidth;
//                                lp.height = limitHeight;
//                                imageView.setLayoutParams(lp);
//                            }
//                        }
//                    });
                } else {
                    ImageUtil.showImage(imgUrl, imageView);
                }
            } else {
                final ImageView imageView = (ImageView) inflateView.findViewById(imageViewResId);
                if (ObjectUtil.isNull(imageView))
                    throw new NullPointerException("没有在布局文件：R.layout." + ResourceUtil.getNameWithResId(layoutResId) + ".xml中找到ImageView控件。");
                final String imgUrl = getItem(position);
                if (loadingDrawableResId > 0 && failureDrawableResId > 0) {
                    ImageUtil.showImage(imgUrl, imageView, ImageView.ScaleType.FIT_CENTER, loadingDrawableResId, failureDrawableResId);

//                            new GlideDrawableImageViewTarget(imageView) {
//                        @Override
//                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                            super.onResourceReady(resource, animation);
//                            Rect rect = resource.getBounds();
//                            int width = rect.width();
//                            int height = rect.height();
//                            if (viewPager != null) {
//                                int limitHeight = viewPager.getMeasuredHeight();
//                                int limitWidth = width * limitHeight / height;
//                                d("limitHeightAndlimitWidth:" + limitHeight + ", " + limitWidth);
//                                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
//                                lp.width = limitWidth;
//                                lp.height = limitHeight;
//                                imageView.setLayoutParams(lp);
//                            }
//                        }
//                    });
//                    ImageUtil.showImage(imgUrl, imageView, loadingDrawableResId, failureDrawableResId);
                } else {
                    ImageUtil.showImage(imgUrl, imageView);
                }
            }
            container.addView(inflateView);
            return inflateView;
        } else {
            final ImageView imageView = new ImageView(context);
            final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            final String imgUrl = getItem(position);
            if (loadingDrawableResId > 0 && failureDrawableResId > 0) {
                ImageUtil.showImage(imgUrl, imageView, loadingDrawableResId, failureDrawableResId);
            } else {
                ImageUtil.showImage(imgUrl, imageView);
            }
            container.addView(imageView);
            return imageView;
        }
    }
}