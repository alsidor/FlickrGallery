package net.lxndrsdrnk.flickrgallery.list;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by alsidor on 15/03/2018.
 */

public class SquareImageView extends android.support.v7.widget.AppCompatImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = Math.max( getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }

}