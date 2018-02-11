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

import java.lang.ref.WeakReference;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.util.Size;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 23/06/15.
 */
public class UrlBitmapSpan extends ReplacementSpan implements RemoteBitmapSpan,  UpdateAppearance, RichTextSpan {

    public static final Creator<UrlBitmapSpan> CREATOR = DynamicParcelableCreator.getInstance(UrlBitmapSpan.class);
    public static final int TYPE = UniqueId.getType();

    public static final double RATIO =  3.0 / 4.0;

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


    private Uri mImage;
    private Size mProvidedSize;
    private Size mActualSize;
    private Drawable mBitmap;

    public UrlBitmapSpan() {
        mVerticalAlignment = ALIGN_BOTTOM;
    }

    public UrlBitmapSpan(Uri image, int imageWidth, int imageHeight, int maxImageWidth) {
        this(null, image, imageWidth, imageHeight, maxImageWidth, ALIGN_BOTTOM);

    }

    public UrlBitmapSpan(Bitmap bitmap,
                         Uri image,
                         int imageWidth,
                         int imageHeight,
                         int maxImageWidth,
                         int alignment) {

        super();
        mImage = image;
        mMaxImageWidth = maxImageWidth;

        mProvidedSize = new Size(imageWidth, imageHeight);
        mActualSize = new Size(500,500);/// pass a default size while we load
        mVerticalAlignment = alignment;

        if (bitmap != null) {
            mBitmap = new BitmapDrawable(bitmap);
            mActualSize = new Size(mBitmap.getIntrinsicWidth(),
                    mBitmap.getIntrinsicHeight());
            mBitmap.setBounds(0, 0, mActualSize.getWidth(), mActualSize.getHeight());
        }

    }


    public Uri getUri() {
        return mImage;
    }

    @Override
    public int getType() {
        return TYPE;
    }


    @Override
    public void readFromParcel(Parcel src) {

        mImage = src.readParcelable(Uri.class.getClassLoader());
        mMaxImageWidth = src.readInt();
        mProvidedSize = src.readParcelable(Size.class.getClassLoader());

    }

    WeakReference<RichContentView> mRef;

    RichContentView getCurrentRichContentView() {

        if (mRef == null) {
            return null;
        }
        return mRef.get();
    }

    @Override
    public void onSpannedSetToView(RichContentView view) {

        RichContentView current = getCurrentRichContentView();

        if (current != null) {
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
        dest.writeParcelable(mImage, 0);
        dest.writeInt(mMaxImageWidth);
        dest.writeParcelable(mProvidedSize, 0);


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

    protected Rect getImageBounds(){

        final int maxAvailableWidth = containerViewHasMeasure();

        //we force the aspect ratio and size if the size is hard coded
        if(Size.valid(mProvidedSize) == true){

            if(maxAvailableWidth == NumberUtils.INVALID) {
                return mProvidedSize.bounds();
            }
            else if(mProvidedSize.getWidth() > maxAvailableWidth) {
                // what if we exceed the available size ?
                mProvidedSize = new Size(maxAvailableWidth, (int)(maxAvailableWidth * mProvidedSize.getRatio()));
            }
            return mProvidedSize.bounds();
        }

        if(Size.valid(mActualSize) == true){// we have the image size

            if(maxAvailableWidth == NumberUtils.INVALID){// container not measured
                return new Rect(0, 0, mMaxImageWidth, (int)(mMaxImageWidth * mActualSize.getRatio()));
            }else {
                return new Rect(0, 0, maxAvailableWidth, (int)(maxAvailableWidth * mActualSize.getRatio()));
            }
        }else { // we need to wait for the image to be loaded to know its size
            if(maxAvailableWidth == NumberUtils.INVALID){// container not measured
                return new Rect(0, 0, mMaxImageWidth, (int)(mMaxImageWidth * RATIO));
            }else {
                return new Rect(0, 0, maxAvailableWidth, (int)(maxAvailableWidth * RATIO));
            }
        }
    }


    @Override
    public int getSize(Paint paint, CharSequence text,
                       int start, int end,
                       Paint.FontMetricsInt fm) {

        Rect rect = getImageBounds();

        if (fm != null) {
            fm.ascent = -rect.bottom;
            fm.descent = 0;

            fm.top = fm.ascent;
            fm.bottom = 0;
        }

        mRect = rect;

        return rect.right;
    }

    public Drawable getBitmap() {
        return mBitmap;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

        if (mBitmap == null) {
            return;
        }

        final Rect bitmapBounds = getImageBounds();

        int transY = bottom - bitmapBounds.bottom;

        canvas.save();
        //center
        int containerViewMeasure = mRef.get().getMeasuredWidth();

        x = x + (containerViewMeasure - bitmapBounds.width()) / 2;
        x = x - mRef.get().getPaddingLeft();
        canvas.translate(x, transY);

        if (mBitmap != null) {
            mBitmap.setBounds(bitmapBounds);
            mBitmap.draw(canvas);
        }

        canvas.restore();

    }

    private Rect mRect = null;

    private boolean mLoading = false;

    @Override
    public void updateBitmap(Context context, Drawable bitmap) {

        mLoading = !(bitmap != null);

        mBitmap = bitmap;

        mActualSize = new Size(bitmap.getIntrinsicWidth(),
                bitmap.getIntrinsicHeight());

        mBitmap.setBounds(0, 0, mActualSize.getWidth(), mActualSize.getHeight());

        final RichContentView view = mRef.get();

        ensureDrawableIsAttached();

        if (view != null) {
            view.mediaSizeUpdated();
        }

    }

    private void ensureDrawableIsAttached() {

        final RichContentView viewDisplay = mRef.get();

        if (viewDisplay == null ||
                viewDisplay.viewAttachedToWindow() == false) {
            return;
        }

        if (mBitmap != null) {
            mBitmap.setCallback(viewDisplay);
            mBitmap.invalidateSelf();
            if (mBitmap instanceof Animatable) {
                Animatable animatable = (Animatable) mBitmap;
                animatable.start();
            }
        }
    }

    @Override
    public Rect getPossibleSize() {

        return getImageBounds();
    }

    private void loadImage() {

        if (mLoading == false && mBitmap == null) {
            mLoading = true;
            UrlBitmapDownloader downloader = SpanUtil.get(mRef);
            if (downloader != null) {
                //acquire one from the view
                downloader.downloadImage(this, mImage);
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(View view) {

        loadImage();
        ensureDrawableIsAttached();

        if (mBitmap != null) {
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
