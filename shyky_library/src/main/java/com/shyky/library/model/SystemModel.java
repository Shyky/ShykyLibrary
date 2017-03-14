package com.shyky.library.model;

import android.support.annotation.NonNull;

import com.shyky.library.constant.Constant;
import com.shyky.library.model.impl.ISystemModel;
import com.shyky.library.util.SharedPreferencesUtil;

/**
 * 系统业务，提供一些基础的系统业务功能，如判断是否第一次使用、是否第一次判断等功能
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/5/10
 * @since 1.0
 */
public class SystemModel implements ISystemModel {
    @Override
    public boolean isFirstUse() {
        return SharedPreferencesUtil.getBoolean(Constant.SHARED_KEY.IS_FIRST_USE);
    }

    @Override
    public boolean writeFirstUse() {
        return SharedPreferencesUtil.save(Constant.SHARED_KEY.IS_FIRST_USE, true);
    }

    @Override
    public void hotfix(@NonNull String version, @NonNull String patchFileName) {
//        // Initialize PatchManager
//        final PatchManager patchManager = new PatchManager(BaseApplication.getContext());
//        // current app version
//        patchManager.init(version);
//        // Load patch
//        patchManager.loadPatch();
//        // Add patch
//        try {
//            // path of the patch file that was downloaded
//            patchManager.addPatch(patchFileName);
//        } catch (IOException e) {
//            d("热修复失败，请检查本地是否有apk差分包 --> " + e.getMessage());
//        }
    }

    @NonNull
    @Override
    public String readUpdateApkPatchFileName() {
        return SharedPreferencesUtil.getString("patchFileName");
    }

    @Override
    public boolean writeUpdateApkPatchFileName(@NonNull String fileName) {
        return SharedPreferencesUtil.save("patchFileName", fileName);
    }
}