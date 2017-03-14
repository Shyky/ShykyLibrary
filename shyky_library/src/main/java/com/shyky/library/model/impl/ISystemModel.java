package com.shyky.library.model.impl;

import android.support.annotation.NonNull;

/**
 * 系统业务接口
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/5/10
 * @since 1.0
 */
public interface ISystemModel {
    /**
     * 是否是第一次使用
     *
     * @return 是返回true，否则返回false
     */
    boolean isFirstUse();

    /**
     * 记录是第一次使用
     *
     * @return 成功返回true，失败返回false
     */
    boolean writeFirstUse();

    /**
     * APP在线热修复
     *
     * @param version       APP版本号
     * @param patchFileName 差分包文件名（全路径名称）
     */
    void hotfix(@NonNull String version, @NonNull String patchFileName);

    /**
     * 读取保存在共享配置文件中要更新的apk差分包文件名
     *
     * @return apk差分包文件名
     */
    @NonNull
    String readUpdateApkPatchFileName();

    /**
     * 保存下载完成后的要更新的apk差分包文件名到共享配置文件中
     *
     * @param fileName 本地文件名
     * @return 成功返回true，失败返回false
     */
    boolean writeUpdateApkPatchFileName(@NonNull String fileName);
}