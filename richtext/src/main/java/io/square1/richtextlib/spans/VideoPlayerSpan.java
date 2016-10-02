/*
 * Copyright (c) 2016. Roberto  Prato <https://github.com/robertoprato>
 *
 *  *
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package io.square1.richtextlib.spans;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Parcel;
import android.text.style.ReplacementSpan;
import android.text.style.UpdateAppearance;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.ui.video.RichVideoView;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 23/06/15.
 */
public class VideoPlayerSpan extends ReplacementSpan implements  ClickableSpan, UpdateAppearance, RichTextSpan , RichVideoView.RichVideoViewListener {

    public static final Creator<VideoPlayerSpan> CREATOR  = DynamicParcelableCreator.getInstance(VideoPlayerSpan.class);
    public static final int TYPE = UniqueId.getType();

    /**
     * A constant indicating that the bottom of this span should be aligned
     * with the bottom of the surrounding text, i.e., at the same level as the
     * lowest descender in the text.
     */
    public static final int ALIGN_BOTTOM = 0;

    /**
     * A constant indicating that the bottom of this span should be aligned
     * with the baseline of the surrounding text.
     */
    public static final int ALIGN_BASELINE = 1;



     int mMaxImageWidth;
     int mImageWidth;
     int mImageHeight;

     String mVideoPath;

    RichVideoView mVideoPlayer;

    public static final double RATIO =  9.0 / 16.0;

    public VideoPlayerSpan(){

    }

    public VideoPlayerSpan(String video, int imageWidth, int imageHeight, int maxImageWidth){
        this(video, imageWidth,imageHeight,maxImageWidth, ALIGN_BASELINE);

    }
    public VideoPlayerSpan(String image, int  imageWidth, int imageHeight, int maxImageWidth, int alignment){
        super();

        mVideoPath = image;
        mMaxImageWidth = maxImageWidth;
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;



    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void readFromParcel(Parcel src) {
        mVideoPath = src.readString();
        mMaxImageWidth = src.readInt();
        mImageWidth = src.readInt();
        mImageHeight = src.readInt();
    }

    WeakReference<RichContentViewDisplay> mRef;

    @Override
    public void onSpannedSetToView(RichContentViewDisplay view) {
        mRef = new WeakReference(view);
        mAttachedToWindow = view.viewAttachedToWindow();

    }

    @Override
    public void onAttachedToWindow(RichContentViewDisplay view) {
        mAttachedToWindow = true;
        if(mVideoPlayer == null){
            mVideoPlayer = new RichVideoView(view.getContext());
            ///view.addSubView(mVideoPlayer);
            mVideoPlayer.setRichVideoViewListener(this);
            mVideoPlayer.setData(mVideoPath);
        }

    }

    @Override
    public void onDetachedFromWindow(RichContentViewDisplay view) {
        mAttachedToWindow  = false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        DynamicParcelableCreator.writeType(dest, this);
        dest.writeString(mVideoPath);
        dest.writeInt(mMaxImageWidth);
        dest.writeInt(mImageWidth);
        dest.writeInt(mImageHeight);

    }


    private int containerViewHasMeasure(){
        if(mRef != null && mRef.get() != null){
            RichContentViewDisplay display = mRef.get();

            int measured = display.getMeasuredWidth() -
                    display.getPaddingLeft() -
                    display.getPaddingRight();

            if(measured > 0) return measured;
        }

        return NumberUtils.INVALID;
    }

    private Rect estimateSize() {

        int maxAvailableWidth = containerViewHasMeasure();
        //taking a guess here
        if(maxAvailableWidth == NumberUtils.INVALID)
            maxAvailableWidth = mMaxImageWidth;

        //we know the image size
        if(mImageWidth  !=  NumberUtils.INVALID){

            int imageHeight = mImageHeight != NumberUtils.INVALID ?
                    mImageHeight : (int)(mImageWidth*RATIO);

            double rate = (double)maxAvailableWidth / (double)mImageWidth;
            return new Rect(0, 0, maxAvailableWidth, (int)(imageHeight * rate));
        }

        return new Rect(0, 0, maxAvailableWidth, (int)(maxAvailableWidth * RATIO));
    }

    private  Rect evaluateBitmapBounds(int bitmabW, int bitmapH) {

        int maxAvailableWidth = containerViewHasMeasure();

        //taking a guess here
        if(maxAvailableWidth == NumberUtils.INVALID)
            maxAvailableWidth = mMaxImageWidth;

        //we know the image size
        if(bitmabW  !=  NumberUtils.INVALID){

            int imageHeight = bitmapH != NumberUtils.INVALID ?
                    bitmapH : (int)(bitmabW*RATIO);

            double rate = (double)maxAvailableWidth / (double)bitmabW;
            return new Rect(0, 0, maxAvailableWidth, (int)(imageHeight * rate));
        }

        return new Rect(0, 0, maxAvailableWidth, (int)(maxAvailableWidth * RATIO));
    }

    private Rect getBitmapBounds(){


        int maxAvailableWidth = containerViewHasMeasure();
        if(maxAvailableWidth == NumberUtils.INVALID){
            maxAvailableWidth = mMaxImageWidth;
        }
        return new Rect(0, 0, maxAvailableWidth, (int)(maxAvailableWidth * RATIO));

//        if(mVideoPlayer == null ||
//                mVideoPlayer.videSizeKnown() == false) {
//
//            return estimateSize();
//
//        }else{
//           return evaluateBitmapBounds(mVideoPlayer.getVideoWidth(),
//                   mVideoPlayer.getVideoWidth());
//        }
    }

    @Override
    public int getSize(Paint paint, CharSequence text,
                       int start, int end,
                       Paint.FontMetricsInt fm) {

        Rect rect = getBitmapBounds();


        if (fm != null) {
            fm.ascent = -rect.bottom;
            fm.descent = 0;

            fm.top = fm.ascent;
            fm.bottom = 0;
        }

        mRect = rect;

        return rect.right;
    }



    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

        if(mVideoPlayer == null) return;

        final Rect bitmapBounds = getBitmapBounds();
        int transY = bottom - bitmapBounds.bottom;
;

        //center
        x = x + (mRef.get().getMeasuredWidth() - bitmapBounds.width()) / 2;
        x = x - mRef.get().getPaddingLeft();


        canvas.save();
        canvas.translate(x, transY);
        // border
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.GREEN);
        canvas.drawRect(bitmapBounds, paint);
        canvas.restore();

        RichContentView view = (RichContentView)mVideoPlayer.getParent();
        if(view != null) {
            FrameLayout.LayoutParams current = (FrameLayout.LayoutParams) mVideoPlayer.getLayoutParams();
            FrameLayout.LayoutParams newParams = view.generateDefaultLayoutParams(new Point((int) x, transY),
                    bitmapBounds.width(),
                    bitmapBounds.height());

            if (view.areLayoutParamsDifferent(current, newParams) == true) {
                mVideoPlayer.setLayoutParams(newParams);
                view.performLayout();
            }
        }

    }

    private Rect mRect = null;

    private boolean mAttachedToWindow = false;



    @Override
    public void onVideoReady(RichVideoView videoView) {

        mVideoPlayer = videoView;

        mImageWidth = mVideoPlayer.getVideoWidth();
        mImageHeight = mVideoPlayer.getVideoHeight();

        final RichContentViewDisplay view = mRef.get();
        Rect newRect = getBitmapBounds();

        boolean needsLayout = (newRect.equals(mRect) == false);


        if(view != null && mAttachedToWindow){

            if(needsLayout == true){
                view.performLayout();
            }else {
                view.invalidate();
            }
        }

    }


}
