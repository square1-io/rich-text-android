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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.style.ReplacementSpan;
import android.view.View;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 23/06/15.
 */
public class BitmapSpan extends ReplacementSpan implements RichTextSpan, ClickableSpan {



    public static final Parcelable.Creator<BitmapSpan> CREATOR  = DynamicParcelableCreator.getInstance(BitmapSpan.class);
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

    private Bitmap mBitmap;

    public BitmapSpan(Bitmap bitmap){
       this(bitmap,ALIGN_BOTTOM);
    }

    public BitmapSpan(Bitmap bitmap, int alignment){
        super();
        mVerticalAlignment = alignment;
        mBitmap = bitmap;
    }

    public Rect getBitmapSize(){
        return  new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void readFromParcel(Parcel src) {
        mBitmap = src.readParcelable(Bitmap.class.getClassLoader());
    }


    @Override
    public void onAttachedToWindow(RichContentViewDisplay view) {

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
        dest.writeParcelable(mBitmap,0);
    }


    @Override
    public int getSize(Paint paint, CharSequence text,
                       int start, int end,
                       Paint.FontMetricsInt fm) {

        Rect rect = getBitmapSize();

        if (fm != null) {
            fm.ascent = -rect.bottom;
            fm.descent = 0;

            fm.top = fm.ascent;
            fm.bottom = 0;
        }

        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

        final Rect bitmapBounds = getBitmapSize();

        int transY = bottom - bitmapBounds.bottom;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        }

        canvas.save();
        canvas.translate(x, transY);
        canvas.drawBitmap(mBitmap,null,getBitmapSize(),paint);
        canvas.restore();

        drawBitmap(canvas, mBitmap, bitmapBounds, start, end, x, top, y, bottom,mVerticalAlignment, paint);
    }

    private static void drawBitmap(Canvas canvas,
                                   Bitmap bitmap,
                                   Rect bounds,
                                   int start,
                                   int end,
                                   float x,
                                   int top,
                                   int y,
                                   int bottom,
                                   int verticalAlignment,
                                   Paint paint){


        int transY = bottom - bounds.bottom;
        if (verticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        }

        canvas.save();
        canvas.translate(x, transY);
        canvas.drawBitmap(bitmap, null, bounds, paint);
        canvas.restore();
    }


//    private void loadImage(){
//        if(mAttachedToWindow == true && mVideoPlayer == null){
//            Glide.with(mViewRef.getInstance().getContext()).load(mImageUri).asBitmap().into(mSimpleTarget);
//        }
//    }

    @Override
    public void onSpannedSetToView(RichContentView view){

    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {

    }

    @Override
    public String getAction() {
        return "";
    }

}
