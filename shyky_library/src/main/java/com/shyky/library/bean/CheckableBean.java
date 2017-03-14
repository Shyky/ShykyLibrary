package com.shyky.library.bean;

import com.shyky.library.bean.base.BaseBean;

/**
 * 具有选中和反选的实体类
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/9/7
 * @since 1.0
 */
public abstract class CheckableBean extends BaseBean {
    private boolean checked;

    public final boolean isChecked() {
        return checked;
    }

    public final void setChecked(boolean checked) {
        this.checked = checked;
    }
}