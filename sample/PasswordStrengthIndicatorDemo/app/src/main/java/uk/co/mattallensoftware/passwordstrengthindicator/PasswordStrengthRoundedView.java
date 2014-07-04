package uk.co.mattallensoftware.passwordstrengthindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by matt on 03/07/2014.
 */
public class PasswordStrengthRoundedView extends PasswordStrengthView {

    private RectF mRect;

    public PasswordStrengthRoundedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Change the width and height values to scale the view properly
        mMinWidth = 150;
        mMinHeight = 150;
        mRect = new RectF(
                getPaddingLeft(),
                getPaddingTop(),
                getPaddingLeft()+mMinWidth,
                getPaddingTop()+mMinHeight
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        generateIndicatorColor();
        // Get the size of the arc to display based on our score. Default to full size.
        int arcSize = 360;
        if (mCurrentScore < 20) arcSize = (360/20)*mCurrentScore;
        canvas.drawArc(mRect, 180, arcSize, true, mIndicatorPaint);
    }
}
