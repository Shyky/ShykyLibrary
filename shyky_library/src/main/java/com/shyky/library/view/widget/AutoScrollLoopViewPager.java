package com.shyky.library.view.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * 自动轮播切换的ViewPager
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.3
 * @email sj1510706@163.com
 * @date 2016/7/18
 * @since 1.0
 */
public final class AutoScrollLoopViewPager extends SmartViewPager {
    public static final int DEFAULT_INTERVAL = 3000;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    /**
     * do nothing when sliding at the last or first item *
     */
    public static final int SLIDE_BORDER_MODE_NONE = 0;
    /**
     * deliver event to parent when sliding at the last or first item *
     */
    public static final int SLIDE_BORDER_MODE_TO_PARENT = 1;
    /**
     * auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL} *
     */
    private long interval = DEFAULT_INTERVAL;
    /**
     * auto scroll direction, default is {@link #RIGHT} *
     */
    private int direction = RIGHT;
    /**
     * whether automatic cycle when auto scroll reaching the last or first item,
     * default is true
     */
    private boolean isCycle = true;
    /**
     * whether stop auto scroll when touching, default is true *
     */
    private boolean stopScrollWhenTouch = true;
    /**
     * how to process when sliding at the last or first item, default is
     * {@link #SLIDE_BORDER_MODE_NONE}
     */
    private int slideBorderMode = SLIDE_BORDER_MODE_NONE;
    /**
     * scroll factor for auto scroll animation, default is 1.0 *
     */
    private double autoScrollFactor = 1.0;
    /**
     * scroll factor for swipe scroll animation, default is 1.0 *
     */
    private double swipeScrollFactor = 1.0;
    private Handler handler;
    private boolean isAutoScroll = false;
    private boolean isStopByTouch = false;
    private float touchX = 0f, downX = 0f;
    private CustomDurationScroller scroller = null;
    public static final int SCROLL_WHAT = 0;
    private float startX;
    private float startY;
    private float moveX;
    private float moveY;

    public AutoScrollLoopViewPager(Context paramContext) {
        this(paramContext, null);
    }

    public AutoScrollLoopViewPager(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    private void init() {
        handler = new MyHandler(this);
        setViewPagerScroller();
        super.setOnPageChangeListener(onPageChangeListener);
    }

    /**
     * start auto scroll, first scroll delay time is {@link #getInterval()}
     */
    public void startAutoScroll() {
        isAutoScroll = true;
        sendScrollMessage((long) (interval + scroller.getDuration() / autoScrollFactor * swipeScrollFactor));
    }

    /**
     * start auto scroll
     *
     * @param delayTimeInMills first scroll delay time
     */
    public void startAutoScroll(int delayTimeInMills) {
        isAutoScroll = true;
        sendScrollMessage(delayTimeInMills);
    }

    /**
     * stop auto scroll
     */
    public void stopAutoScroll() {
        isAutoScroll = false;
        handler.removeMessages(SCROLL_WHAT);
    }

    public boolean isAutoScroll() {
        return isAutoScroll;
    }

    /**
     * set the factor by which the duration of sliding animation will change
     * while swiping
     */
    public void setSwipeScrollDurationFactor(double scrollFactor) {
        swipeScrollFactor = scrollFactor;
    }

    /**
     * set the factor by which the duration of sliding animation will change
     * while auto scrolling
     */
    public void setAutoScrollDurationFactor(double scrollFactor) {
        autoScrollFactor = scrollFactor;
    }

    private void sendScrollMessage(long delayTimeInMills) {
        /** remove messages before, keeps one message is running at most **/
        handler.removeMessages(SCROLL_WHAT);
        handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
    }

    /**
     * set ViewPager scroller to change animation duration when sliding
     */
    private void setViewPagerScroller() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = ViewPager.class
                    .getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);

            scroller = new CustomDurationScroller(getContext(),
                    (Interpolator) interpolatorField.get(null));
            scrollerField.set(this, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * scroll only once
     */
    public void scrollOnce() {
        PagerAdapter adapter = getAdapter();
        int count = 0;
        if (adapter != null) {
            count = adapter.getCount();
            if (count <= 1) {
                return;
            }
        }
        int currentItem = getCurrentItem();
        int nextItem = (direction == LEFT) ? --currentItem : ++currentItem;
        if (nextItem >= count) {
            nextItem = 0;
        }
        /*the real position is Handled in LoopPagerAdapterWrapper */
        setCurrentItem(nextItem);
    }

    /**
     * <ul>
     * if stopScrollWhenTouch is true
     * <li>if event is down, stop auto scroll.</li>
     * <li>if event is up, start auto scroll again.</li>
     * </ul>
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);

        if (stopScrollWhenTouch) {
            if ((action == MotionEvent.ACTION_DOWN) && isAutoScroll) {
                isStopByTouch = true;
                stopAutoScroll();
            } else if (ev.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
                startAutoScroll();
            }
        }

        if (slideBorderMode == SLIDE_BORDER_MODE_TO_PARENT) {
            int currentItem = getCurrentItem();
            PagerAdapter adapter = getAdapter();
            int pageCount = adapter == null ? 0 : adapter.getCount();
            if (currentItem == 0 || currentItem == pageCount - 1) {
                touchX = ev.getX();
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    downX = touchX;
                }
                /**
                 * current index is first one and slide to right or current index is
                 * last one and slide to left.<br/>
                 * if slide border mode is to parent, then
                 * requestDisallowInterceptTouchEvent false.<br/>
                 * else scroll to last one when current item is first one, scroll to
                 * first one when current item is last one.
                 */
                if ((currentItem == 0 && downX < touchX) || (currentItem == pageCount - 1 && downX > touchX)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
        } else {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    private static class MyHandler extends Handler {

        private final WeakReference<AutoScrollLoopViewPager> autoScrollViewPager;

        public MyHandler(AutoScrollLoopViewPager autoScrollViewPager) {
            this.autoScrollViewPager = new WeakReference<>(
                    autoScrollViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SCROLL_WHAT:
                    AutoScrollLoopViewPager pager = this.autoScrollViewPager.get();
                    if (pager != null) {
                        pager.scroller.setScrollDurationFactor(pager.autoScrollFactor);
                        pager.scrollOnce();
                        pager.scroller.setScrollDurationFactor(pager.swipeScrollFactor);
                        pager.sendScrollMessage(pager.interval + pager.scroller.getDuration());
                        if (pager.onLoopBannerChangeTimeListener != null) {
                            pager.onLoopBannerChangeTimeListener.onChangeTime(pager.interval);
                        }
                    }
                default:
                    break;
            }
        }
    }

    /**
     * get auto scroll time in milliseconds, default is
     * {@link #DEFAULT_INTERVAL}
     *
     * @return the interval
     */
    public long getInterval() {
        return interval;
    }

    /**
     * set auto scroll time in milliseconds, default is
     * {@link #DEFAULT_INTERVAL}
     *
     * @param interval the interval to set
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     * get auto scroll direction
     *
     * @return {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
     */
    public int getDirection() {
        return (direction == LEFT) ? LEFT : RIGHT;
    }

    /**
     * set auto scroll direction
     *
     * @param direction {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * whether automatic cycle when auto scroll reaching the last or first item,
     * default is true
     *
     * @return the isCycle
     */
    public boolean isCycle() {
        return isCycle;
    }

    /**
     * set whether automatic cycle when auto scroll reaching the last or first
     * item, default is true
     *
     * @param isCycle the isCycle to set
     */
    public void setCycle(boolean isCycle) {
        boolean tmp = this.isCycle;
        this.isCycle = isCycle;
        if (mAdapter != null) {
            mAdapter.setCycle(isCycle);
            if (tmp && !isCycle) setCurrentItem(0, false);
        }
    }

    /**
     * whether stop auto scroll when touching, default is true
     *
     * @return the stopScrollWhenTouch
     */
    public boolean isStopScrollWhenTouch() {
        return stopScrollWhenTouch;
    }

    /**
     * set whether stop auto scroll when touching, default is true
     *
     * @param stopScrollWhenTouch
     */
    public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
        this.stopScrollWhenTouch = stopScrollWhenTouch;
    }

    /**
     * get how to process when sliding at the last or first item
     *
     * @return the slideBorderMode {@link #SLIDE_BORDER_MODE_NONE},
     * {@link #SLIDE_BORDER_MODE_TO_PARENT},
     * default is
     * {@link #SLIDE_BORDER_MODE_NONE}
     */
    public int getSlideBorderMode() {
        return slideBorderMode;
    }

    /**
     * set how to process when sliding at the last or first item
     *
     * @param slideBorderMode {@link #SLIDE_BORDER_MODE_NONE},
     *                        {@link #SLIDE_BORDER_MODE_TO_PARENT},
     *                        default is
     *                        {@link #SLIDE_BORDER_MODE_NONE}
     */
    public void setSlideBorderMode(int slideBorderMode) {
        this.slideBorderMode = slideBorderMode;
    }

    public class CustomDurationScroller extends Scroller {

        private double scrollFactor = 1;

        public CustomDurationScroller(Context context) {
            this(context, null);
        }

        public CustomDurationScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        /**
         * not exist in android 2.3
         *
         * @param context
         * @param interpolator
         * @param flywheel
         */
        /**
         * Set the factor by which the duration will change
         */
        public void setScrollDurationFactor(double scrollFactor) {
            this.scrollFactor = scrollFactor;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy,
                                int duration) {
            super.startScroll(startX, startY, dx, dy,
                    (int) (duration * scrollFactor));
        }
    }

    /**
     * *************************************************** Looping
     * *************************************
     */
    OnPageChangeListener mOuterPageChangeListener;
    private LoopPagerAdapterWrapper mAdapter;

    /**
     * helper function which may be used when implementing FragmentPagerAdapter
     *
     * @param position
     * @param count
     * @return (position-1)%count
     */
    public static int toRealPosition(int position, int count) {
        position = position - 1;
        if (position < 0) {
            position += count;
        } else {
            position = position % count;
        }
        return position;
    }

    /**
     * If set to true, the boundary views (i.e. first and last) will never be
     * destroyed This may help to prevent "blinking" of some views
     *
     * @param mBoundaryCaching
     */
    public void setBoundaryCaching(boolean mBoundaryCaching) {
        if (mAdapter != null) {
            mAdapter.setBoundaryCaching(mBoundaryCaching);
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        mAdapter = new LoopPagerAdapterWrapper(adapter);
        super.setAdapter(mAdapter);
        setCurrentItem(0, false);
    }

    @Override
    public PagerAdapter getAdapter() {
        return mAdapter != null ? mAdapter.getRealAdapter() : mAdapter;
    }

    @Override
    public int getCurrentItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super
                .getCurrentItem()) : 0;
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        int realItem = mAdapter.toInnerPosition(item);
        super.setCurrentItem(realItem, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOuterPageChangeListener = listener;
    }

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        private float mPreviousOffset = -1;
        private float mPreviousPosition = -1;

        @Override
        public void onPageSelected(int position) {

            int realPosition = mAdapter.toRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
                if (mOuterPageChangeListener != null) {
                    mOuterPageChangeListener.onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            int realPosition = position;
            if (mAdapter != null) {
                realPosition = mAdapter.toRealPosition(position);

                if (positionOffset == 0
                        && mPreviousOffset == 0
                        && (position == 0 || position == mAdapter.getCount() - 1)) {
                    if (!isCycle && realPosition != position) {
                        mOuterPageChangeListener.onPageScrolled(realPosition,
                                positionOffset, positionOffsetPixels);
                    } else {
                        setCurrentItem(realPosition, false);
                    }
                }
            }

            mPreviousOffset = positionOffset;
            if (mOuterPageChangeListener != null) {
                if (mAdapter != null && realPosition != mAdapter.getRealCount() - 1) {
                    mOuterPageChangeListener.onPageScrolled(realPosition,
                            positionOffset, positionOffsetPixels);
                } else {
                    if (positionOffset > .5) {
                        mOuterPageChangeListener.onPageScrolled(0, 0, 0);
                    } else {
                        mOuterPageChangeListener.onPageScrolled(realPosition,
                                0, 0);
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mAdapter != null) {
                int position = AutoScrollLoopViewPager.super.getCurrentItem();
                int realPosition = mAdapter.toRealPosition(position);
                if (state == ViewPager.SCROLL_STATE_IDLE
                        && (position == 0 || position == mAdapter.getCount() - 1)) {
                    if (!isCycle && realPosition != position) {
                        mOuterPageChangeListener
                                .onPageScrollStateChanged(state);
                    } else {
                        setCurrentItem(realPosition, false);
                    }
                }
            }
            if (mOuterPageChangeListener != null) {
                mOuterPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    };

    /**
     * A PagerAdapter wrapper responsible for providing a proper page to
     * LoopViewPager
     * <p/>
     * This class shouldn't be used directly
     */
    public static class LoopPagerAdapterWrapper extends PagerAdapter {
        private PagerAdapter adapter;
        private SparseArray<ToDestroy> mToDestroy = new SparseArray<>();
        private boolean mBoundaryCaching = false;
        private boolean isCycle = true;

        void setBoundaryCaching(boolean mBoundaryCaching) {
            this.mBoundaryCaching = mBoundaryCaching;
            super.notifyDataSetChanged();
        }

        void setCycle(boolean isCycle) {
            this.isCycle = isCycle;
            super.notifyDataSetChanged();
        }

        LoopPagerAdapterWrapper(PagerAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void notifyDataSetChanged() {
            mToDestroy = new SparseArray<>();
            super.notifyDataSetChanged();
        }

        int toRealPosition(int position) {
            int realPosition = position;
            int realCount = getRealCount();
            if (realCount == 0)
                return 0;
            if (isCycle) {
                realPosition = (position - 1) % realCount;
                if (realPosition < 0) {
                    realPosition += realCount;
                }
            }
            return realPosition;
        }

        public int toInnerPosition(int realPosition) {
            return isCycle ? realPosition + 1 : realPosition;
        }

        private int getRealFirstPosition() {
            return isCycle ? 1 : 0;
        }

        private int getRealLastPosition() {
            int count = getRealCount();
            return isCycle ? count : count - 1;
        }

        @Override
        public int getCount() {
            int count = getRealCount();
            return isCycle ? count + 2 : count;
        }

        public int getRealCount() {
            return adapter.getCount();
        }

        public PagerAdapter getRealAdapter() {
            return adapter;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int realPosition = (adapter instanceof FragmentPagerAdapter || adapter instanceof FragmentStatePagerAdapter) ? position
                    : toRealPosition(position);
            if (mBoundaryCaching) {
                ToDestroy toDestroy = mToDestroy.get(position);
                if (toDestroy != null) {
                    mToDestroy.remove(position);
                    return toDestroy.object;
                }
            }
            return adapter.instantiateItem(container, realPosition);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            int realFirst = getRealFirstPosition();
            int realLast = getRealLastPosition();
            int realPosition = (adapter instanceof FragmentPagerAdapter || adapter instanceof FragmentStatePagerAdapter) ? position
                    : toRealPosition(position);

            if (mBoundaryCaching
                    && (position == realFirst || position == realLast)) {
                mToDestroy.put(position, new ToDestroy(container, realPosition,
                        object));
            }
            adapter.destroyItem(container, realPosition, object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            adapter.finishUpdate(container);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return adapter.isViewFromObject(view, object);
        }

        @Override
        public void restoreState(Parcelable bundle, ClassLoader classLoader) {
            adapter.restoreState(bundle, classLoader);
        }

        @Override
        public Parcelable saveState() {
            return adapter.saveState();
        }

        @Override
        public void startUpdate(ViewGroup container) {
            adapter.startUpdate(container);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            adapter.setPrimaryItem(container, position, object);
        }
    }

    /**
     * Container class for caching the boundary views
     */
    public static class ToDestroy {
        ViewGroup container;
        int position;
        Object object;

        public ToDestroy(ViewGroup container, int position, Object object) {
            this.container = container;
            this.position = position;
            this.object = object;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.stopAutoScroll();
    }

    private OnLoopBannerChangeTimeListener onLoopBannerChangeTimeListener;

    public void setOnLoopBannerChangeTimeListener(OnLoopBannerChangeTimeListener onLoopBannerChangeTimeListener) {
        this.onLoopBannerChangeTimeListener = onLoopBannerChangeTimeListener;
    }

    public interface OnLoopBannerChangeTimeListener {
        void onChangeTime(long time);
    }

    float interceptStartX = 0;
    float interceptStartY = 0;
    float interceptMoveX = 0;
    float interceptMoveY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                interceptStartX = ev.getX();
                interceptStartY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                interceptMoveX = ev.getX();
                interceptMoveY = ev.getY();
                if (Math.abs(interceptMoveX - interceptStartX) < Math.abs(interceptMoveY - interceptStartY)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
                interceptStartX = interceptMoveX;
                interceptStartY = interceptMoveY;
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = ev.getX();
                moveY = ev.getY();
                if (Math.abs(moveX - startX) < Math.abs(moveY - startY)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
                startX = moveX;
                startY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.onTouchEvent(ev);
    }
}