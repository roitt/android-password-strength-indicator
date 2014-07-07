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
 * Created by matt on 03/07/2014.
 */
public class PasswordStrengthRoundedView extends PasswordStrengthView {

    private RectF mRect, mInnerRect;
    private Paint mInnerCircle;

    public PasswordStrengthRoundedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Change the width and height values to scale the view properly
        mMinWidth = 150;
        mMinHeight = 150;
        // Set PorterDuff mode on main indicator
        //mIndicatorPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST));
        // Create the inner circle and use PorterDuff to 'erase' the pixels
        mInnerCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCircle.setColor(Color.TRANSPARENT);
        mInnerCircle.setStyle(Paint.Style.FILL);
        mInnerCircle.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        generateIndicatorColor();
        // Get the size of the arc to display based on our score. Default to full size.
        int arcSize = 360;
        if (mCurrentScore < 20) arcSize = (360/20)*mCurrentScore;
        canvas.drawArc(mRect, 180, arcSize, true, mIndicatorPaint);
        canvas.drawArc(mInnerRect, 0, 360, true, mInnerCircle);
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Calculate bounds
        mRect = new RectF(
                getPaddingLeft(),
                getPaddingTop(),
                getPaddingLeft()+mIndicatorWidth,
                getPaddingTop()+mIndicatorHeight
        );
        mInnerRect = new RectF(
                getPaddingLeft() + (mIndicatorWidth/3),
                getPaddingTop() + (mIndicatorHeight/3),
                getPaddingLeft() + ((mIndicatorWidth/3)*2),
                getPaddingTop() + ((mIndicatorHeight/3)*2)
        );
    }
}
