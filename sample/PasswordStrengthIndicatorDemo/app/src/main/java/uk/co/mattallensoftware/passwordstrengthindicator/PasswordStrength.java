package uk.co.mattallensoftware.passwordstrengthindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Matt Allen
 * 01/07/14
 * http://www.mattallensoftware.co.uk
 * mattallen092@gmail.com
 *
 * <p>
 * This View is designed to indicate how secure a user-entered password is in a visual way to
 * relay to the user if they need to make it stronger. The strength of the password can be set
 * at creation (or after) which will decide whether their password is strong enough.
 * </p>
 *
 * <p>
 * The password strength is decided by an index of 20. The minimum score needed to pass is 10
 * which means the String has met the conditions imposed by the strength test, but can be improved.
 * If the password scores 10-19 it is considered weak, and only if it scores 20 will it be
 * considered strong.
 * </p>
 */
public class PasswordStrength extends View {

    private final int               COLOR_FAIL = Color.parseColor("#e74c3c");
    private final int 				COLOR_WEAK = Color.parseColor("#e67e22");
    private final int 				COLOR_STRONG = Color.parseColor("#2ecc71");

    private Paint                   mTextPaint, mIndicatorPaint;
    private float 					mTextHeight;
    private int 					mIndicatorHeight, mIndicatorWidth, mCurrentScore;

    private boolean                 mShowGuides = true;

    /**
     * Used to define that the indicator should only be looking
     * for a weak password. The bare minimum is used here to let
     * the user continue.
     */
    public static final int STRENGTH_WEAK = 0;

    /**
     * A fairly strict rule for generating a password. It encourages a password that is
     * less easy to crack.
     */
    public static final int STRENGTH_MEDIUM = 1;

    /**
     * A strong algorithm that encourages very strong passwords that should be fairly long, with
     * non-alphanumeric, numbers, and upper case.
     */
    public static final int STRENGTH_STRONG = 2;

    public static final int VIEW_ROUND = 10;
    public static final int VIEW_LINE = 11;

    private int mStrengthRequirement = -1;

    private int mSelectedShape = -1;

    private String mPassword;

    public PasswordStrength(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray style = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PasswordStrength,
                0, 0);

        try {
            mStrengthRequirement = style.getInteger(R.styleable.PasswordStrength_strength, STRENGTH_MEDIUM);
            mSelectedShape = style.getInteger(R.styleable.PasswordStrength_shape, VIEW_LINE);
            mShowGuides = style.getBoolean(R.styleable.PasswordStrength_showGuides, true);
        } catch (Exception e){
            e.printStackTrace();
        }

        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * This view can determine if the password entered by the user is acceptable for
     * use by your use case. This is based on the strength requirement you have set.
     * @return True if requirement has been met
     */
    public boolean isStrengthRequirementMet() {
        return (mCurrentScore >= 10);
    }

    /**
     * Change the strength requirement of the password entered by the user. This will also
     * re-check the password already entered against these new requirements.
     * @param requiredStrength Use the public constants of this class to set
     */
    public void setStrengthRequirement(int requiredStrength) {
        if(requiredStrength >= 0 && requiredStrength <= 2){
            mStrengthRequirement = requiredStrength;
            if (mPassword != null && mPassword.length() > 0) {
                generatePasswordScore();
                // Update view with new score
                invalidate();
                requestLayout();
            }
        } else {
            throw new IndexOutOfBoundsException("Input out of expected range");
        }
    }

    /**
     * Update the password string to check strength of
     * @param passwordString String representation of user-input
     */
    public void setPassword(String passwordString) {
        if(passwordString != null && passwordString.length() > 0) {
            mPassword = passwordString;
            generatePasswordScore();
            // Update view with new score
            invalidate();
            requestLayout();
        } else {
            mPassword = "";
            mCurrentScore = 0;
            invalidate();
            requestLayout();
        }
    }

    /**
     * Private convenience method for adding to the password score
     * @param score Amount to be added to current score
     */
    private void addToPasswordScore(int score) {
        // Limit max score
        if ((mCurrentScore + score) > 20){
            mCurrentScore = 20;
        } else {
            mCurrentScore = mCurrentScore + score;
        }
    }

    /**
     * Call this to determine the current strength requirement set on the algorithm
     * @return Int representation of the current strength set for the indicator
     */
    public int getStrengthRequirement() {
        return mStrengthRequirement;
    }

    /**
     * Generate a score based on the password. The password will already need to be stored
     * as a class member before running this.
     */
    private void generatePasswordScore() {
        mCurrentScore = 0;
        int upperCase = getUppercaseCount(mPassword);
        int nonAlpha = getNonAlphanumericCount(mPassword);
        int numbers = getNumberCount(mPassword);
        addToPasswordScore(upperCase);
        addToPasswordScore(nonAlpha*2);
        addToPasswordScore(numbers);
        switch (mStrengthRequirement){
            case STRENGTH_WEAK:
                addToPasswordScore(mPassword.length()*2);
                break;

            case STRENGTH_MEDIUM:
                addToPasswordScore((mPassword.length()));
                break;

            case STRENGTH_STRONG:
                addToPasswordScore(mPassword.length()/2);
                // Cut the score in half to make this a very high requirement
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        int paddingX = getPaddingLeft();
        int paddingY = getPaddingTop();
        mIndicatorHeight = h - paddingY;
        mIndicatorWidth = w - paddingX;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minw = getPaddingLeft() + getPaddingRight() + 300;
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);
        int minh = 80 + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(minh, heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int color = COLOR_FAIL;
        int indWidth = (mIndicatorWidth / 20) * mCurrentScore;
        if (mCurrentScore >= 18) {
            color = COLOR_STRONG;
        }
        else if (mCurrentScore >= 10) {
            color = COLOR_WEAK;
        }
        mIndicatorPaint.setColor(color);
        if (mSelectedShape == VIEW_ROUND) {

        }
        else if (mSelectedShape == VIEW_LINE) {
            canvas.drawRect(
                    getPaddingLeft(),
                    getPaddingTop(),
                    indWidth,
                    mIndicatorHeight,
                    mIndicatorPaint
            );
        }
    }

    /**
     * Quick method to determine how many of the characters in a given string are upper case
     * @param stringToCheck The string to examine
     * @return Number of upper case characters
     */
    private int getUppercaseCount(String stringToCheck) {
        int score = 0;
        int loops = stringToCheck.length()-1;
        for (int i=0;i<=loops;i++){
            if(Character.isUpperCase(stringToCheck.charAt(i))) {
                score++;
            }
        }
        return score;
    }

    private int getNonAlphanumericCount(String stringToCheck) {
        int score = 0;
        int loops = stringToCheck.length()-1;
        for (int i=0;i<=loops;i++) {
            if(!Character.isLetter(stringToCheck.charAt(i)) &&
                    !Character.isDigit(stringToCheck.charAt(i))){
                score++;
            }
        }
        return score;
    }

    private int getNumberCount(String stringToCheck) {
        int score = 0;
        int loops = stringToCheck.length()-1;
        for (int i=0;i<=loops;i++) {
            if(Character.isDigit(stringToCheck.charAt(i))) {
                score++;
            }
        }
        return score;
    }

    /**
     * Set the guides to show on the view.<br />
     * On the line style, the guides will show underneath<br />
     * On the rounded style, the guides will be shown on the outer edges.<br />
     * The view will be redrawn after the method is called.
     * @param showGuides True if you want the guides to be shown
     */
    public void setShowGuides(boolean showGuides) {
        mShowGuides = showGuides;
        if (mPassword != null && mPassword.length() > 0) {
            generatePasswordScore();
            invalidate();
            requestLayout();
        }
    }

    /**
     * Determine whether the view is showing the guides for the password score
     * @return True if the guides are being shown
     */
    public boolean isShowingGuides() {
        return mShowGuides;
    }
}