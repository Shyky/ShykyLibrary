package com.shyky.library.event;

/**
 * 列表滚动状态消息事件，用于发送列表滑动消息事件
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/6/17
 * @since 1.0
 */
public final class ListScrollStateEvent {
    public int orientation;
    public int position;
    public int currentIndex;

    public ListScrollStateEvent(int position) {
        this.position = position;
    }

    public ListScrollStateEvent(int position, int currentIndex) {
        this.position = position;
        this.currentIndex = currentIndex;
    }
}