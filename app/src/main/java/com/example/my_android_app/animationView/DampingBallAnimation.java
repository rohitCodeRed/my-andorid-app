package com.example.my_android_app.animationView;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.BounceInterpolator;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import com.example.my_android_app.R;

public class DampingBallAnimation extends View {

    public String TAG="my app";
    private float centerXStart,centerYStart;
    private int mRadius ,mRotate;
    private Paint mCirclePaint,mLinePaint, mPiePaint;
    private RectF mBounds;
    private int paddingTop,paddingStart;
    private int xAxisChange,yAxisChange;
    private ValueAnimator animator,animatorX,animatorY;
    private int changedVal;
    private static Display mDisp;
    private Context context;
    private float[] pts;
    private Path mPath;
    private static boolean endInd;

    private static List<PointsXY> pointObj;

    private int maxX,maxY;


    public class PointsXY {
        public int x,y;
    }

    public DampingBallAnimation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init(){
        paddingTop = (int)convertDpToPixel(0);
        paddingStart = (int) convertDpToPixel(8);
        centerXStart = convertDpToPixel(50);
        mRotate=0;

        xAxisChange=0;

        centerYStart = convertDpToPixel(50);
        yAxisChange=0;
        mRadius = (int)convertDpToPixel(30);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //mCirclePaint.setColor(0xff101010);
        mCirclePaint.setStyle(Paint.Style.FILL);
        int[] rainbow = getRainbowColors();
        Shader shader = new LinearGradient(0, 0, 200, 200, rainbow,
                null, Shader.TileMode.MIRROR);

        //mCirclePaint.set
        mCirclePaint.setShader(shader);

        mLinePaint = new Paint();

        mPath = new Path();
        mPath.moveTo(centerXStart,centerYStart);

        mLinePaint.setStrokeWidth(6f);
        int color = ContextCompat.getColor(context, R.color.grey_500);
        mLinePaint.setColor(color);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setPathEffect(new CornerPathEffect(100));


        pointObj = new ArrayList<>();

        endInd = false;

        mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setStyle(Paint.Style.FILL);
        mPiePaint.setColor(0xff101010);

        mBounds = new RectF(centerXStart-100, centerYStart-100,centerXStart+100, centerYStart+100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.lineTo(centerXStart+xAxisChange,centerYStart+yAxisChange);

        //if(endInd){
            canvas.drawPath(mPath,mLinePaint);
        //}

        canvas.rotate(mRotate, centerXStart+xAxisChange, centerYStart+yAxisChange);
        //mPath.quadTo(centerXStart+xAxisChange,centerYStart+yAxisChange,centerXStart+xAxisChange,centerYStart+yAxisChange);
        canvas.drawCircle(centerXStart+xAxisChange, centerYStart+yAxisChange, mRadius, mCirclePaint);

        mBounds.set(centerXStart+xAxisChange -40,centerYStart+yAxisChange-40,centerXStart+xAxisChange+40,centerYStart+yAxisChange+40);
        canvas.drawArc(mBounds,
                0,
                30,
                true, mPiePaint);

        canvas.drawArc(mBounds,
                90,
                30,
                true, mPiePaint);
        canvas.drawArc(mBounds,
                180,
                30,
                true, mPiePaint);

        canvas.drawArc(mBounds,
                270,
                30,
                true, mPiePaint);


    }

    public void updateMaxScreenCoordinates(Display disp, float density){
        mDisp = disp;
        Point coordinates = screenCoordinate();
        this.maxX = coordinates.x;
        this.maxY = coordinates.y - (int)convertDpToPixel(50);
        Log.d(TAG, "updateMaxScreenCoordinates: "+maxX+" :"+maxY);

    }
    public Point screenCoordinate(){
        //Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mDisp.getSize(mdispSize);
        return mdispSize;
    }

    public  float convertDpToPixel(float dp){
        Resources resources = this.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Log.v("Chart onMeasure w", MeasureSpec.toString(widthMeasureSpec));
//        Log.v("Chart onMeasure h", MeasureSpec.toString(heightMeasureSpec));
//
//        int desiredWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
//        int desiredHeight = getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom();
//
//        setMeasuredDimension(resolveSizeAndState(desiredWidth, widthMeasureSpec,0),
//                resolveSizeAndState(desiredHeight, heightMeasureSpec,0));
//    }


    public void startAnimation(){
        init();
        startAnimationX();
        startAnimationY();
        animatorX.start();
        animatorY.start();

    }

    private void startAnimationX(){
        PropertyValuesHolder xAxis = PropertyValuesHolder.ofInt("MOVEx", 0,maxX-(int)centerXStart*2);
        //PropertyValuesHolder yAxis = PropertyValuesHolder.ofInt("MOVEy", 0,maxY-(int)centerYStart*2);
        PropertyValuesHolder propertyRotate = PropertyValuesHolder.ofInt("pRotate", 0, 720);

        animatorX = new ValueAnimator();
        animatorX.setValues(xAxis);
        animatorX.setDuration(3000);
        //animatorX.setInterpolator(new AccelerateDecelerateInterpolator());
        //animatorX.setInterpolator(new BounceInterpolator());
        animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                xAxisChange =  (int)animation.getAnimatedValue("MOVEx");
                addPoints((int)(xAxisChange+centerXStart),(int)(yAxisChange+centerYStart));
                //Log.d(TAG, "startAnimation: clicked...   :"+ (xAxisChange)+":"+yAxisChange);
                //mRotate = (int) animation.getAnimatedValue("pRotate");
                invalidate();
                requestLayout();
            }
        });
        //animator.start();
    }

    private void startAnimationY(){
        //PropertyValuesHolder xAxis = PropertyValuesHolder.ofInt("MOVEx", 0,maxX-(int)centerXStart*2);
        PropertyValuesHolder yAxis = PropertyValuesHolder.ofInt("MOVEy", 0,maxY-(int)centerYStart*2 -28);
        PropertyValuesHolder propertyRotate = PropertyValuesHolder.ofInt("pRotate", 0, 360);

        animatorY = new ValueAnimator();
        animatorY.setValues(yAxis,propertyRotate);
        animatorY.setDuration(3000);
        //animatorY.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorY.setInterpolator(new BounceInterpolator());
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //xAxisChange =  (int)animation.getAnimatedValue("MOVEx");
                yAxisChange = (int)animation.getAnimatedValue("MOVEy");
                addPoints((int)(xAxisChange+centerXStart),(int)(yAxisChange+centerYStart));
                //Log.d(TAG, "startAnimation: clicked...   :"+ (xAxisChange)+":"+yAxisChange);
                mRotate = (int) animation.getAnimatedValue("pRotate");
                invalidate();
                requestLayout();
            }
        });

        animatorY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                endInd = true;
                //invalidate();
                //requestLayout();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        //animatorY.a
    }


    private void addPoints(int x,int y){
        PointsXY temp = new PointsXY();
        temp.x = x;
        temp.y = y;
        pointObj.add(temp);
    }

    public Path returnPath(){
        Path path = new Path();
        path.moveTo(centerXStart,centerYStart);
        int length = pointObj.size();
        for(int i=1;i<length;i++){
            PointsXY temp = pointObj.get(i);
            path.lineTo(temp.x,temp.y);
        }
        //path.close();


        return path;
    }

    private int[] getRainbowColors() {
        return new int[] {
                getResources().getColor(R.color.red_500),
                getResources().getColor(R.color.yellow_400),
                getResources().getColor(R.color.green_500)

        };
    }

}
