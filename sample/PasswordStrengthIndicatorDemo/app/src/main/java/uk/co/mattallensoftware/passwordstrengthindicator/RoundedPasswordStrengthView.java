package uk.co.mattallensoftware.passwordstrengthindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by matt on 03/07/2014.
 */
public class RoundedPasswordStrengthView extends PasswordStrengthView {

    public RoundedPasswordStrengthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Change the width and height values to scale the view properly
        mMinWidth = 150;
        mMinHeight = 150;
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }
}
