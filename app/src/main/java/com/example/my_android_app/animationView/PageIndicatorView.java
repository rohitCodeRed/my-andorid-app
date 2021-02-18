package com.example.my_android_app.animationView;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.my_android_app.R;

public class PageIndicatorView extends View {

    public String TAG="hi";

    private Boolean mShowText;
    private Paint mTextPaint,mPiePaint,mShadowPaint;
    private RectF mShadowBounds,mBounds;
    private Shader mShader;
    private ValueAnimator animator;
    private int mRadius ,mRotate;

    private int mTextHeight,mTextWidth,mTextColor;

    private int mTextPos;
    public PageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PieChart,
                0, 0);

        try {
            mShowText = a.getBoolean(R.styleable.PieChart_showText, false);
            mTextPos = a.getInteger(R.styleable.PieChart_labelPosition, 0);
            init();
        } finally {
            a.recycle();
        }

    }

    private void init() {
        mRadius = (int)convertDpToPixel(100);
        mRotate =0;
        mTextWidth = (int)convertDpToPixel(100);
        mTextHeight = (int) convertDpToPixel(100);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(0xff101010);
        mTextPaint.setTextSize(convertDpToPixel(18));
//        if (mTextHeight == 0) {
//            mTextHeight = (int)mTextPaint.getTextSize();
//        } else {
//            mTextPaint.setTextSize(100);
//        }

        mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setStyle(Paint.Style.FILL);
        mPiePaint.setTextSize(convertDpToPixel(20));
        mPiePaint.setColor(0xff101010);

        mShadowPaint = new Paint(0);
        mShadowPaint.setColor(0xff101010);
        //mShadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

        mShadowBounds = new RectF();
        mBounds = new RectF(convertDpToPixel(100), convertDpToPixel(100),convertDpToPixel(300), convertDpToPixel(300));

        mShader = new Shader();
        //startAnimation();

    }


    public boolean isShowText(){
        return mShowText;
    }

    public void setShowText(boolean showText) {
        mShowText = showText;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = MeasureSpec.getSize(w) - (int)mTextWidth + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(MeasureSpec.getSize(w) - (int)mTextWidth, heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(mRotate,mBounds.centerX(),mBounds.centerY());
        canvas.drawArc(mBounds,
                0,
                90,
                true, mPiePaint);


        //mBounds.set

        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), mRadius, mTextPaint);


    }

    public  float convertDpToPixel(float dp){
        Resources resources = this.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }




    public void startAnimation(){
        //Log.d(TAG, "startAnimation: clicked...");
        PropertyValuesHolder propertyRadius = PropertyValuesHolder.ofInt("pRadius", 0,100);
        PropertyValuesHolder propertyRotate = PropertyValuesHolder.ofInt("pRotate", 0, 360);

        animator = new ValueAnimator();
        animator.setValues(propertyRadius,propertyRotate);
        animator.setDuration(3000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRadius = (int)convertDpToPixel(100) - (int) animation.getAnimatedValue("pRadius");
                //Log.d(TAG, "startAnimation: clicked...   :"+ animation.getAnimatedValue(pRotated));
                mRotate = (int) animation.getAnimatedValue("pRotate");
                invalidate();
                //requestLayout();
            }
        });
        animator.start();
    }
}
