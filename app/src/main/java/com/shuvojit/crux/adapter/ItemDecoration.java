package com.shuvojit.crux.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by SHOBOJIT on 12/29/2017.
 */

public class ItemDecoration extends RecyclerView.ItemDecoration {
    private final int left;
    private final int top;
    private final int right;
    private final int bottom;

    public ItemDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(left,top,right,bottom);
    }
}
