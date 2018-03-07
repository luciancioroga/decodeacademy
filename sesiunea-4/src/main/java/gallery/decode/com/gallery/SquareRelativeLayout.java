package gallery.decode.com.gallery;

/**
 * Created by lucian.cioroga on 3/7/2018.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SquareRelativeLayout extends RelativeLayout {
    public SquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        int newHeight = MeasureSpec.makeMeasureSpec((int) Math.ceil(w), MeasureSpec.getMode(heightMeasureSpec));

        if (h != 0) {
            int newWidth = MeasureSpec.makeMeasureSpec((int) Math.ceil(h), MeasureSpec.getMode(heightMeasureSpec));

            if (heightMeasureSpec > newHeight)
                super.onMeasure(widthMeasureSpec, newHeight);
            else
                super.onMeasure(newWidth, heightMeasureSpec);
        } else
            super.onMeasure(widthMeasureSpec, newHeight);
    }
}

