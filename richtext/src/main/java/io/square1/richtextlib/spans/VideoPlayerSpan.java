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
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Parcel;
import android.text.style.ReplacementSpan;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.ui.video.RichVideoView;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.util.Size;
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

    String mVideoPath;
    Size mVideoSize;
    RichVideoView mVideoPlayer;

    boolean mSizeHasChanged;

    public static final double RATIO =  9.0 / 16.0;

    public VideoPlayerSpan(){

    }

    public VideoPlayerSpan(String video, int imageWidth, int imageHeight, int maxImageWidth){
        this(video, imageWidth,imageHeight,maxImageWidth, ALIGN_BASELINE);

    }
    public VideoPlayerSpan(String image, int  imageWidth, int imageHeight, int maxImageWidth, int alignment){
        super();
        mSizeHasChanged = true;
        mVideoPath = image;
        mMaxImageWidth = maxImageWidth;
        mVideoSize = new Size(imageWidth, imageHeight);

    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void readFromParcel(Parcel src) {
        mVideoPath = src.readString();
        mMaxImageWidth = src.readInt();
        mVideoSize = src.readParcelable(Size.class.getClassLoader());
    }

    WeakReference<RichContentView> mRef;

    @Override
    public void onSpannedSetToView(RichContentView view) {
        mRef = new WeakReference(view);
        mAttachedToWindow = view.viewAttachedToWindow();



    }

    @Override
    public void onAttachedToWindow(RichContentViewDisplay view) {

        if(mVideoPlayer == null){
            mVideoPlayer = new RichVideoView(view.getContext());
            mVideoPlayer.setRichVideoViewListener(this);
        }
        ensureViewIsAddedToParent(mVideoPlayer, mRef.get());
        mVideoPlayer.setData(mVideoPath);
    }

    @Override
    public void onDetachedFromWindow(RichContentViewDisplay view) {

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
        dest.writeParcelable(mVideoSize, 0);


    }


    private int containerViewHasMeasure(){
        if(mRef != null && mRef.get() != null){
            RichContentView display = mRef.get();

            int measured = display.getMeasuredWidth() -
                    display.getPaddingLeft() -
                    display.getPaddingRight();

            if(measured > 0) return measured;
        }

        return NumberUtils.INVALID;
    }

    private Rect getVideoPlayerBounds(){

        final int maxAvailableWidth = containerViewHasMeasure();
        final Size videoSize = mVideoPlayer.getVideoSize();

        if(videoSize == null){
            if(maxAvailableWidth == NumberUtils.INVALID){
                // still measuring the container view
                return new Rect(0, 0, mMaxImageWidth, (int)(mMaxImageWidth * RATIO));
            }else {// container is measured , video is not return a initial size
                return new Rect(0, 0, maxAvailableWidth, (int)(maxAvailableWidth * RATIO));
            }
        }else {// we have the size for the video
            if (maxAvailableWidth == NumberUtils.INVALID) { //but the container has not been fully measured yet
                return new Rect(0, 0, mMaxImageWidth, (int) (mMaxImageWidth * RATIO));
            } else {// we have the measure of the container
                double videoRatio = videoSize.getRatio();
                return new Rect(0, 0, maxAvailableWidth, (int) (((double)maxAvailableWidth * videoRatio)));
            }
        }
    }

    @Override
    public int getSize(Paint paint, CharSequence text,
                       int start, int end,
                       Paint.FontMetricsInt fm) {

        Rect rect = getVideoPlayerBounds();


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
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x,
                     int top, int y, int bottom, Paint paint) {

        if(mVideoPlayer == null ) return;

        final Rect videoPlayerBounds = getVideoPlayerBounds();
        int transY = bottom - videoPlayerBounds.bottom;
;

        //center
        x = x + (mRef.get().getMeasuredWidth() - videoPlayerBounds.width()) / 2;
        x = x - mRef.get().getPaddingLeft();

//        debug code
//        canvas.save();
//        canvas.translate(x, transY);
//        // border
//        paint.setStyle(Paint.Style.FILL_AND_STROKE);
//        paint.setColor(Color.GREEN);
//        canvas.drawRect(videoPlayerBounds, paint);
//        canvas.restore();

        ensureViewIsAddedToParent(mVideoPlayer, mRef.get());

        if(mSizeHasChanged == true) {
            RichContentView view = (RichContentView) mVideoPlayer.getParent();

            FrameLayout.LayoutParams current = (FrameLayout.LayoutParams) mVideoPlayer.getLayoutParams();
            FrameLayout.LayoutParams newParams = view.generateDefaultLayoutParams(new Point((int) x, transY),
                    videoPlayerBounds.width(),
                    videoPlayerBounds.height());

            if (view.areLayoutParamsDifferent(current, newParams) == true) {
                mVideoPlayer.setLayoutParams(newParams);
                view.performLayout();
            }

            mSizeHasChanged = false;
        }

    }

    private Rect mRect = null;

    private boolean mAttachedToWindow = false;



    @Override
    public void onVideoReady(RichVideoView videoView) {

    }

    @Override
    public void onVideoSizeAvailable(RichVideoView videoView) {

        mVideoSize = videoView.getVideoSize();
        mSizeHasChanged = true;
        final RichContentView view = mRef.get();
        Rect newRect = getVideoPlayerBounds();

        boolean needsLayout = (newRect.equals(mRect) == false);

        if(view != null){
            if(needsLayout == true){
                view.mediaSizeUpdated();
            }else {
                view.invalidate();
            }
        }

    }

    private boolean ensureViewIsAddedToParent(View view, ViewGroup parent){

        if(view == null || parent == null) {
            return false;
        }

        if(view.getParent() != parent){
            ViewGroup currentParent = (ViewGroup)view.getParent();
            if(currentParent!= null)
                currentParent.removeView(view);
            parent.addView(view);
            return true;
        }

        return false;

    }


    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {

    }

    @Override
    public String getAction() {
        return mVideoPath;
    }

}
