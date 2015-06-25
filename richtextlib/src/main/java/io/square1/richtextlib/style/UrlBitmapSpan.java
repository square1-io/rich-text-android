package io.square1.richtextlib.style;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Parcel;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ReplacementSpan;
import android.text.style.UpdateAppearance;
import android.util.Property;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.lang.ref.WeakReference;

import io.square1.richtextlib.ui.RichTextView;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 23/06/15.
 */
public class UrlBitmapSpan extends ReplacementSpan implements UpdateAppearance, P2ParcelableSpan {



    public static final Creator<UrlBitmapSpan> CREATOR  = P2ParcelableCreator.get(UrlBitmapSpan.class);
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

    private int mMaxImageWidth;
    private int mMaxImageHeight;

    private int mImageWidth;
    private int mImageHeight;

    private Uri mImage;

    private Bitmap mBitmap;

    public UrlBitmapSpan(Uri image, int imageWidth, int imageHeight, int maxImageWidth){
        this(null,image,imageWidth,imageHeight,maxImageWidth,ALIGN_BOTTOM);

    }
    public UrlBitmapSpan(Bitmap bitmap, Uri image, int imageWidth, int imageHeight, int maxImageWidth, int alignment){
        super();

        mImage = image;
        mMaxImageWidth = maxImageWidth;
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;

        mVerticalAlignment = alignment;
        mBitmap = bitmap;

        ensureNotNullPlaceHolder();
    }

    private void ensureNotNullPlaceHolder(){
        if(mBitmap == null){

            Rect size = getBitmapSize();

            mBitmap = Bitmap.createBitmap(size.width(),
                    size.height(),
                    Bitmap.Config.ALPHA_8);
        }
    }

    public Rect getBitmapSize(){

        if(mImageWidth > mMaxImageWidth) {
            double rate = mMaxImageWidth / mImageWidth;
            return new Rect(0, 0, mMaxImageWidth, (int)(mImageHeight * rate));
        }

        return new Rect(0, 0, mImageWidth, mImageHeight);
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void readFromParcel(Parcel src) {
        mBitmap = src.readParcelable(Bitmap.class.getClassLoader());
    }

    WeakReference<RichTextView> mRef;
    @Override
    public void onSpannedSetToView(RichTextView view) {
        mRef = new WeakReference<RichTextView>(view);
        loadImage();
    }

    @Override
    public void onAttachedToView(RichTextView view) {
        mAttachedToWindow = true;
        loadImage();
    }

    @Override
    public void onDetachedFromView(RichTextView view) {
        mAttachedToWindow  = false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        P2ParcelUtils.writeType(dest,this);
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

        return mMaxImageWidth;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

        final Rect bitmapBounds = getBitmapSize();

        int transY = bottom - bitmapBounds.bottom;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        }

        canvas.save();
        mRect = new Rect((int)x,top,y,bottom);
        //center
        x = (mRef.get().getMeasuredWidth() - mImageWidth) / 2;
        canvas.translate(x, transY);
        canvas.drawBitmap(mBitmap,null,getBitmapSize(),null);
        canvas.restore();

    }

    private Rect mRect = null;

    private boolean mLoading = false;
    private boolean mAttachedToWindow = false;

    private SimpleTarget<Bitmap> mSimpleTarget = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            mBitmap = resource;
            final RichTextView view = mRef.get();

            if(view != null){
                  Spannable span = view.getSpannable();
                  view.setText("");
                  //view.setText(span);
            }

//            if(mAttachedToWindow && mRef.get() != null && mRef.get().getSpans().length > 0){
//                Spannable current = (Spannable)mRef.get().getText();
//                int start = current.getSpanStart(UrlBitmapSpan.this);
//                int end = current.getSpanEnd(UrlBitmapSpan.this);
//                BitmapSpan bitmapSpan = new BitmapSpan(resource);
//                current.removeSpan(UrlBitmapSpan.this);
//                current.setSpan(bitmapSpan, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//            }
        }
    };


    private void loadImage(){
        if(mAttachedToWindow == true && mLoading == false){
            mLoading = true;
            Glide.with(mRef.get().getContext()).load(mImage).asBitmap().into(mSimpleTarget);
        }
    }



}
