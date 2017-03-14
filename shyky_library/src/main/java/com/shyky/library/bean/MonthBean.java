package com.shyky.library.bean;

import android.os.Parcel;

import com.shyky.library.bean.base.BaseBean;

/**
 * @author Shyky
 * @version 1.1
 * @date 2016/6/23
 * @since 1.0
 */
public class MonthBean extends BaseBean {
    private String dayOfMonth;
    private int day;
    private boolean checked;

    public MonthBean(int day, boolean checked) {
        this.day = day;
        this.checked = checked;
    }

    public MonthBean(String dayOfMonth, boolean checked) {
        this.dayOfMonth = dayOfMonth;
        this.checked = checked;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public String toString() {
        return "MonthBean{" +
                "dayOfMonth='" + dayOfMonth + '\'' +
                ", day=" + day +
                ", checked=" + checked +
                '}';
    }
}