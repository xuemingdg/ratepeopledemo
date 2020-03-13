package com.example.helloworld.adapter;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalLayoutManager extends LinearLayoutManager implements RecyclerView.SmoothScroller.ScrollVectorProvider  {

    private OnSwipeListener listener;
    private static final String TAG = "HorizontalLayoutManager";

    public void setListener(OnSwipeListener listener) {
        this.listener = listener;
    }

    public HorizontalLayoutManager(Context context) {
        super(context);
    }

    public interface OnSwipeListener{
        void onSwipeLeft();
        void onSwipeRight();
    }

    @Nullable
    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        if (getChildCount() == 0) {
            return null;
        }
        int firstChildPos = getPosition(getChildAt(0));
        int direction = 0;
        if (targetPosition < firstChildPos)
            direction = -1;
        else
            direction = 1;
        return new PointF(direction, 0f);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

//    @Override
//    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//        detachAndScrapAttachedViews(recycler);
//
//        if (getItemCount() == 0) {
//            return;
//        }
//        //横向绘制子View,则需要知道 X轴的偏移量
//        int offsetX = 0;
//
//        //绘制并添加view
//        for (int i = 0; i<getItemCount();i++) {
//            View view = recycler.getViewForPosition(i);
//            addView(view);
//
//            measureChildWithMargins(view, 0, 0);
//            int viewWidth = getDecoratedMeasuredWidth(view);
//            int viewHeight = getDecoratedMeasuredHeight(view);
//            layoutDecorated(view, offsetX, 0, offsetX + viewWidth, viewHeight);
//            offsetX += viewWidth;
//
//            if (offsetX > getWidth()) {
//                break;
//            }
//        }
//
//    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        recycleViews(dx, recycler);
        fill(dx, recycler);
        offsetChildrenHorizontal(dx * -1);
        return dx;
    }

    private void fill(int dx,  RecyclerView.Recycler recycler){
        //左滑
        if (dx > 0) {

            while (true) {
                //得到当前已添加（可见）的最后一个子View
                View lastVisibleView = getChildAt(getChildCount() - 1) ;
                if(lastVisibleView==null)
                    break;
                //如果滑动过后，View还是未完全显示出来就 不进行绘制下一个View
                if (lastVisibleView.getRight() - dx > getWidth())
                    break;

                            //得到View对应的位置
                int layoutPosition = getPosition(lastVisibleView);
                View nextView;
                if (layoutPosition == getItemCount() - 1) {
                    nextView = recycler.getViewForPosition(0);
                } else {
                    nextView = recycler.getViewForPosition(layoutPosition + 1);
                }

                addView(nextView);
                measureChildWithMargins(nextView, 0, 0);
                int viewWidth = getDecoratedMeasuredWidth(nextView);
                int viewHeight = getDecoratedMeasuredHeight(nextView);
                int offsetX = lastVisibleView.getRight()+lastVisibleView.getLeft();
                int offsetY = lastVisibleView.getTop();
                layoutDecorated(nextView, offsetX, offsetY, offsetX + viewWidth,offsetY+ viewHeight);
            }
        } else { //右滑
            while (true) {
                View firstVisibleView = getChildAt(0) ;
                if(firstVisibleView==null)
                    break;

                if (firstVisibleView.getLeft() - dx < 0)
                    break;

                int layoutPosition = getPosition(firstVisibleView);
                /**
                 * 如果当前第一个可见View为第0个，则左侧显示第20个View 如果不是，下一个就显示前一个
                 */
                View nextView;
                if (layoutPosition == 0) {
                    nextView = recycler.getViewForPosition(getItemCount() - 1);
                } else {
                    nextView = recycler.getViewForPosition(layoutPosition - 1);
                }

                addView(nextView, 0);
                measureChildWithMargins(nextView, 0, 0);
                int viewWidth = getDecoratedMeasuredWidth(nextView);
                int viewHeight = getDecoratedMeasuredHeight(nextView);
                int offsetX = firstVisibleView.getLeft()-firstVisibleView.getRight();
                int offsetY = firstVisibleView.getTop();
                layoutDecorated(nextView, offsetX-viewWidth, offsetY, offsetX, offsetY+viewHeight);
            }
        }

    }

    private void recycleViews(int dx,  RecyclerView.Recycler recycler) {
        for (int i = 0 ; i< getItemCount(); i++) {
            View childView = getChildAt(i) ;
            if(childView==null) {
                return;
            }
            //左滑
            if (dx > 0) {

                //移除并回收 原点 左侧的子View
                if (childView.getRight() - dx < 0) {

                    removeAndRecycleViewAt(i, recycler);

                }
            } else { //右滑
                //移除并回收 右侧即RecyclerView宽度之以外的子View
                if (childView.getLeft() + dx > getWidth()) {

                    removeAndRecycleViewAt(i, recycler);

                }
            }
        }
    }


}
