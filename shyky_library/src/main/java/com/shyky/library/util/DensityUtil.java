package com.shyky.library.util;

/**
 * 密度、像素单位转换工具类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/5/30
 * @since 1.0
 */
public final class DensityUtil {
    /**
     * 屏幕密度
     */
    private static float density = ResourceUtil.getDisplayMetrics().density;
    /**
     * 屏幕按比例缩小的密度
     */
    private static float scaledDensity = ResourceUtil.getDisplayMetrics().scaledDensity;

    /**
     * 构造方法
     */
    private DensityUtil() {

    }

    /**
     * 获取屏幕密度
     *
     * @return 屏幕密度
     */
    public static float getDisplayMetricsDensity() {
        return density;
    }

    /**
     * 获取屏幕按比例缩小的密度
     *
     * @return 按比例缩小的密度
     */
    public static float getDisplayMetricsScaledDensity() {
        return scaledDensity;
    }

    /**
     * dp转换成px
     *
     * @param dp 独立像素单位
     * @return dp对应的像素
     */
    public static int dp2px(int dp) {
        return (int) (dp * density + .5f);
    }

    /**
     * dip转换成px
     *
     * @param dip dip单位
     * @return dp对应的像素
     */
    public static int dip2px(int dip) {
        return dp2px(dip);
    }

    /**
     * px转换成dp
     *
     * @param px 像素单位
     * @return 像素px对应的独立像素dp
     */
    public static int px2dp(int px) {
        return (int) (px / density + .5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dip dip单位
     * @return dp对应的像素单位px
     */
    public static int dip2px(float dip) {
        return (int) (dip * density + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param px 像素单位
     * @return 像素px对应的独立像素dp
     */
    public static int px2dip(float px) {
        return (int) (px / density + 0.5f);
    }

    /**
     * px值转换为sp值，保证文字大小不变
     *
     * @param px 像素单位
     * @return px对应的字体大小单位
     */
    public static int px2sp(float px) {
        return (int) (px / scaledDensity + 0.5f);
    }

    /**
     * sp值转换为px值，保证文字大小不变
     *
     * @param sp 字体大小单位
     * @return sp对应的像素
     */
    public static int sp2px(float sp) {
        return (int) (sp * scaledDensity + 0.5f);
    }
}