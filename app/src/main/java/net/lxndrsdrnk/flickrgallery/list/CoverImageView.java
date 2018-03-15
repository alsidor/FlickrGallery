package net.lxndrsdrnk.flickrgallery.list;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by alsidor on 15/03/2018.
 */

public class CoverImageView extends SquareImageView {
    public CoverImageView(Context context) {
        super(context);
    }

    public CoverImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CoverImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public Matrix getImageMatrix() {
        return super.getImageMatrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Drawable drawable = getDrawable();

        if(drawable != null) {

            int displayWidth = getMeasuredWidth();
            int displayHeight = getMeasuredHeight();
            int imageWidth = drawable.getIntrinsicWidth();
            int imageHeight = drawable.getIntrinsicHeight();

            // Compute the scale to choose (this works)
            float scaleX = (float) displayWidth / (float) imageWidth;
            float scaleY = (float) displayHeight / (float) imageHeight;
            float minScale = Math.min(scaleX, scaleY);

            // tx, ty should be the translation to take the image back to the screen center
            float tx = Math.max(0,
                    0.5f * ((float) displayWidth - (minScale * imageWidth)));
            float ty = Math.max(0,
                    0.5f * ((float) displayHeight - (minScale * imageHeight)));

            // Compute the matrix
            Matrix m = new Matrix();
            m.reset();

            // Middle of the image should be the scale pivot
            m.postScale(minScale, minScale, imageWidth / 2f, imageHeight / 2f);

            // Translate
            m.postTranslate(tx, ty);

            setImageMatrix(m);
        }
    }
}