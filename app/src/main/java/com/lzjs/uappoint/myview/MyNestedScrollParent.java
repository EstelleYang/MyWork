package com.lzjs.uappoint.myview;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzjs.uappoint.bean.CollecteArticle;

public class MyNestedScrollParent extends LinearLayout implements NestedScrollingParent {
    private TextView tv;
    private MyNestedScrollChild nsc;
    private NestedScrollingParentHelper mParentHelper;
    private int tvHeight;

    public MyNestedScrollParent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyNestedScrollParent(Context context) {
        super(context);
        init();
    }

    private void init() {
        mParentHelper = new NestedScrollingParentHelper(this);
    }

    //获取子view
    @Override
    protected void onFinishInflate() {
        tv = (TextView) getChildAt(0);
        nsc = (MyNestedScrollChild) getChildAt(1);

        tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvHeight <= 0) {
                    tvHeight = tv.getMeasuredHeight();
                }
            }
        });
    }

//接口实现--------------------------------------------------

    //在此可以判断参数target是哪一个子view以及滚动的方向，然后决定是否要配合其进行嵌套滚动
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (target instanceof MyNestedScrollChild) {
            return true;
        }
        return false;
    }


    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        mParentHelper.onStopNestedScroll(target);
    }

    //先于child滚动
    //前3个为输入参数，最后一个是输出参数
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (showTitle(dy) || hideTitle(dy)) {//如果需要显示或隐藏图片，即需要自己(parent)滚动
            scrollBy(0, -dy);//滚动
            consumed[1] = dy;//告诉child我消费了多少
        }
    }

    //后于child滚动
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    //返回值：是否消费了fling
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    //返回值：是否消费了fling
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }

//--------------------------------------------------

    //下拉的时候是否要向下滚动以显示图片
    public boolean showTitle(int dy) {
        if (dy > 0) {
            if (getScrollY() > 0 && nsc.getScrollY() == 0) {
                return true;
            }
        }

        return false;
    }

    //上拉的时候，是否要向上滚动，隐藏图片
    public boolean hideTitle(int dy) {
        if (dy < 0) {
            if (getScrollY() < tvHeight) {
                return true;
            }
        }
        return false;
    }

    //scrollBy内部会调用scrollTo
    //限制滚动范围
    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > tvHeight) {
            y = tvHeight;
        }

        super.scrollTo(x, y);
    }
}