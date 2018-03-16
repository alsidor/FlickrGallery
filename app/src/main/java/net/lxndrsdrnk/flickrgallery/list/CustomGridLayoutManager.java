package net.lxndrsdrnk.flickrgallery.list;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Grid with always rectangular items.
 * Created by alsidor on 15/03/2018.
 */

public class CustomGridLayoutManager extends GridLayoutManager {

    public CustomGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public CustomGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void measureChild(View child, int widthUsed, int heightUsed) {
        super.measureChild(child, widthUsed, heightUsed);
    }

    @Override
    public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
        int childWidth = getWidth()/getSpanCount();
        android.view.ViewGroup.LayoutParams lp = child.getLayoutParams();
        lp.width = childWidth;
        lp.height = childWidth;
        child.setLayoutParams(lp);
    }

    @Override
    public int getDecoratedMeasuredHeight(View child) {
        return getWidth()/getSpanCount();
    }

    @Override
    public int getDecoratedMeasuredWidth(View child) {
        return getWidth()/getSpanCount();
    }
}
