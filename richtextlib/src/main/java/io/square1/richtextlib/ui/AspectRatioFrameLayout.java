package io.square1.richtextlib.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * Created by roberto on 12/10/15.
 */
public class AspectRatioFrameLayout extends FrameLayout {

    private Double mRatio = Double.NaN;

    public AspectRatioFrameLayout(Context context) {
        super(context);
    }

    public AspectRatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectRatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public AspectRatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setRatio(int width, int height){
        setRatio((double) width / (double)height);
    }

    public void setRatio(double scalingFactor){
        if(mRatio != scalingFactor){
            mRatio = scalingFactor;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(mRatio.isNaN()){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec) -
                (getPaddingLeft() + getPaddingRight());

        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec) -
                (getPaddingTop() + getPaddingBottom());

        double current = (double)measuredWidth / (double) measuredHeight;

        //if not right just yet
        if(mRatio - current != 0){
            measuredHeight = (int)(measuredWidth / mRatio) + getPaddingTop() + getPaddingBottom();
            measuredWidth = measuredWidth + getPaddingLeft() + getPaddingRight();
            setMeasuredDimension(measuredWidth,measuredHeight);

        }else {// nothing to do
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
