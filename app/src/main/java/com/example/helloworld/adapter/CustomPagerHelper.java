package com.example.helloworld.adapter;

import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CustomPagerHelper extends PagerSnapHelper {

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        int position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
        if (position >= layoutManager.getItemCount()) {
            return 0;
        } else {
            return super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
        }
    }

}
