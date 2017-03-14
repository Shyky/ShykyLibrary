package com.shyky.library.exception;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

/**
 * @Author: Created by Shyky on 2016/5/13.
 * @Email: sj1510706@163.com
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String detailMessage) {
        super(detailMessage);
    }

    public ResourceNotFoundException(@NonNull String resourceTypeName, @LayoutRes String resourceFileName) {
        super(String.format("没有在res/%s中没有找到%s资源文件", resourceTypeName, resourceFileName));
    }
}