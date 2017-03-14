package com.shyky.library.bean;

import android.os.Parcel;

import com.shyky.library.bean.base.BaseBean;

/**
 * @Author: Created by Shyky on 2016/5/9.
 * @Email: sj1510706@163.com
 */
public class PhotoBean extends BaseBean {
    private String imagePath;
    private boolean checked;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}