package uk.co.mattallensoftware.passwordstrengthindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Rounded Password Strength Indicator
 *
 * Subs the PasswordStrengthIndicator and implements its own drawing and measuring
 * to show a rounded view when drawn.
 */
public class PasswordStrengthRoundedView extends PasswordStrengthView {

    private RectF mRect, mInnerRect;

    public PasswordStrengthRoundedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Change the width and height values to scale the view properly
        mMinWidth = 150;
        mMinHeight = 150;
        mRect = new RectF();
        mInnerRect = new RectF();
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

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Calculate bounds and set the draw area
        mRect.set(
                getPaddingLeft(),
                getPaddingTop(),
                getPaddingLeft()+mIndicatorWidth,
                getPaddingTop()+mIndicatorHeight
        );
        mInnerRect.set(
                getPaddingLeft() + (mIndicatorWidth/3),
                getPaddingTop() + (mIndicatorHeight/3),
                getPaddingLeft() + ((mIndicatorWidth/3)*2),
                getPaddingTop() + ((mIndicatorHeight/3)*2)
        );
    }
}
