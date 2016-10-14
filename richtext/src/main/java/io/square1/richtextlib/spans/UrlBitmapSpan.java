/*
 * Copyright (c) 2015. Roberto  Prato <https://github.com/robertoprato>
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


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;

import android.text.style.ReplacementSpan;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.widget.Toast;


import java.lang.ref.WeakReference;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 23/06/15.
 */
public class UrlBitmapSpan extends ReplacementSpan implements RemoteBitmapSpan, ClickableSpan, UpdateAppearance, RichTextSpan   {

    public static final Creator<UrlBitmapSpan> CREATOR  = DynamicParcelableCreator.getInstance(UrlBitmapSpan.class);
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

    protected final int mVerticalAlignment;

     int mMaxImageWidth;
     int mImageWidth;
     int mImageHeight;

     Uri mImage;

     Drawable mBitmap;

    public UrlBitmapSpan(){
        mVerticalAlignment = ALIGN_BOTTOM;
    }

    public UrlBitmapSpan(Uri image,  int imageWidth, int imageHeight, int maxImageWidth){
        this(null,image, imageWidth,imageHeight,maxImageWidth,ALIGN_BOTTOM);

    }
    public UrlBitmapSpan(Bitmap bitmap,
                         Uri image,
                         int  imageWidth,
                         int imageHeight,
                         int maxImageWidth,
                         int alignment){
        super();


        mImage = image;
        mMaxImageWidth = maxImageWidth;
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;

        mVerticalAlignment = alignment;

        if(bitmap != null) {
            mBitmap = new BitmapDrawable(bitmap);
            mBitmap.setBounds(estimateSize());
        }


    }


    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void readFromParcel(Parcel src) {
        mImage = src.readParcelable(Uri.class.getClassLoader());
        mMaxImageWidth = src.readInt();
        mImageWidth = src.readInt();
        mImageHeight = src.readInt();
    }

    WeakReference<RichContentView> mRef;

    RichContentView getCurrentRichContentView(){

        if(mRef == null){
            return null;
        }
        return mRef.get();
    }

    @Override
    public void onSpannedSetToView(RichContentView view) {

        RichContentView current = getCurrentRichContentView();

        if(current != null){
            current.removeOnAttachStateChangeListener(this);
        }

        view.addOnAttachStateChangeListener(this);
        mRef = new WeakReference(view);

        ensureDrawableIsAttached();
    }

    @Override
    public void onAttachedToWindow(RichContentViewDisplay view) {
        onViewAttachedToWindow(getCurrentRichContentView());
    }

    @Override
    public void onDetachedFromWindow(RichContentViewDisplay view) {
        onViewDetachedFromWindow(getCurrentRichContentView());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        DynamicParcelableCreator.writeType(dest, this);
        dest.writeParcelable(mImage,0);
        dest.writeInt(mMaxImageWidth);
        dest.writeInt(mImageWidth);
        dest.writeInt(mImageHeight);

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

    private Rect estimateSize() {

        int maxAvailableWidth = containerViewHasMeasure();

        //taking a guess here
        if(maxAvailableWidth == NumberUtils.INVALID) {
            maxAvailableWidth = mMaxImageWidth;
        }

        //we know the image size
        if(mImageWidth  !=  NumberUtils.INVALID){

            int imageHeight = mImageHeight != NumberUtils.INVALID ?
                    mImageHeight : (int)(mImageWidth*0.3);

            double rate = (double)maxAvailableWidth / (double)mImageWidth;
            return new Rect(0, 0, maxAvailableWidth, (int)(imageHeight * rate));
        }

        return new Rect(0, 0, maxAvailableWidth, (int)(maxAvailableWidth * 0.3));
    }

    private  Rect evaluateBitmapBounds(int bitmabW, int bitmapH) {

        int maxAvailableWidth = containerViewHasMeasure();

        //taking a guess here
        if(maxAvailableWidth == NumberUtils.INVALID) {
            maxAvailableWidth = mMaxImageWidth;
        }

        //we know the image size
        if(bitmabW  !=  NumberUtils.INVALID){

            int imageHeight = bitmapH != NumberUtils.INVALID ?
                    bitmapH : (int)(bitmabW*0.3);

            double rate = (double)maxAvailableWidth / (double)bitmabW;
            return new Rect(0, 0, maxAvailableWidth, (int)(imageHeight * rate));
        }

        return new Rect(0, 0, maxAvailableWidth, (int)(maxAvailableWidth * 0.3));
    }

    private Rect getBitmapBounds(){

        if(mBitmap == null) {
            return estimateSize();
        }else{
           return evaluateBitmapBounds(mBitmap.getIntrinsicWidth(),
                   mBitmap.getIntrinsicHeight());
        }
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

    Drawable getBitmap(){
        return mBitmap;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

        if(mBitmap == null) return;

        final Rect bitmapBounds = getBitmapBounds();

        int transY = bottom - bitmapBounds.bottom;



        canvas.save();
        //center
        int containerViewMeasure = mRef.get().getMeasuredWidth();

        x = x + (containerViewMeasure - bitmapBounds.width()) / 2;
        x = x - mRef.get().getPaddingLeft();
        canvas.translate(x, transY);

        if(mBitmap != null) {
            mBitmap.setBounds(bitmapBounds);
            mBitmap.draw(canvas);
        }

        canvas.restore();

    }

    private Rect mRect = null;

    private boolean mLoading = false;



    @Override
    public void updateBitmap(Context context, Drawable bitmap){

        mLoading = !(bitmap != null);


        boolean bitmapUpdated = ((mBitmap == null) || mBitmap != bitmap);
        mBitmap = bitmap;

        mImageWidth = bitmap.getIntrinsicWidth();
        mImageHeight = bitmap.getIntrinsicHeight();

        mBitmap.setBounds(0,0,mImageWidth,mImageHeight);

        final RichContentView view = mRef.get();
        Rect newRect = getBitmapBounds();
        mBitmap.setBounds(newRect);

        boolean needsLayout = (newRect.equals(mRect) == false);

        ensureDrawableIsAttached();

        if(view != null){



//            if(needsLayout == true || bitmapUpdated){
//                view.performLayout();
//            }else {
//                view.invalidate();
//            }

            view.spanUpdated(this);
        }

    }

    private void ensureDrawableIsAttached(){

        final RichContentView viewDisplay = mRef.get();

        if(viewDisplay == null ||
                viewDisplay.viewAttachedToWindow() == false){
            return;
        }

        if(mBitmap != null) {
            mBitmap.setCallback(viewDisplay);
            mBitmap.invalidateSelf();
            if(mBitmap instanceof Animatable){
                Animatable animatable = (Animatable)mBitmap;
                animatable.start();
            }
        }
    }


    @Override
    public Rect getPossibleSize() {
        return getBitmapBounds();
    }


    private void loadImage(){

        if(mLoading == false && mBitmap == null){
            mLoading = true;
            UrlBitmapDownloader downloader = SpanUtil.get(mRef);
            if(downloader != null){
                //acquire one from the view
                downloader.downloadImage(this, mImage);
            }
        }
    }


    @Override
    public void onViewAttachedToWindow(View view) {


        loadImage();
        ensureDrawableIsAttached();

        //redownloaded from earlier
        if(mBitmap != null){
            Drawable cached = mBitmap;
            mBitmap = null;
            updateBitmap(view.getContext(), cached);
        }
    }

    @Override
    public void onViewDetachedFromWindow(View viewDisplay) {

          if (mBitmap != null && mBitmap.getCallback() == viewDisplay) {
              mBitmap.setCallback(null);
          }
    }

}
