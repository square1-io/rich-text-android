package io.square1.richtextlib.style;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;

import android.text.style.ReplacementSpan;
import android.text.style.UpdateAppearance;
import android.view.View;


import java.lang.ref.WeakReference;

import io.square1.richtextlib.ui.RichTextView;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 23/06/15.
 */
public class UrlBitmapSpan extends ReplacementSpan implements RemoteBitmapSpan, ClickableSpan, UpdateAppearance, P2ParcelableSpan {



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
    private UrlBitmapDownloader mUrlBitmapDownloader;
    private Drawable mBitmap;

    public UrlBitmapSpan(Uri image, UrlBitmapDownloader downloader, int imageWidth, int imageHeight, int maxImageWidth){
        this(null,downloader,image, imageWidth,imageHeight,maxImageWidth,ALIGN_BOTTOM);

    }
    public UrlBitmapSpan(Bitmap bitmap, UrlBitmapDownloader downloader, Uri image, int imageWidth, int imageHeight, int maxImageWidth, int alignment){
        super();
        mUrlBitmapDownloader = downloader;
        mImage = image;
        mMaxImageWidth = maxImageWidth;
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;

        mVerticalAlignment = alignment;

        if(bitmap != null) {
            mBitmap = new BitmapDrawable(bitmap);
            mBitmap.setBounds(getBitmapSize());
        }

        ensureNotNullPlaceHolder();
    }

    private void ensureNotNullPlaceHolder(){
        if(mBitmap == null){

            Rect size = getBitmapSize();

            mBitmap = new BitmapDrawable( Bitmap.createBitmap(size.width(),
                    size.height(),
                    Bitmap.Config.ALPHA_8));
            mBitmap.setBounds(getBitmapSize());
        }
    }

    public Rect getBitmapSize(){


        int measured = mMaxImageWidth;

       // if(mImageWidth > measured) {
            double rate = (double)measured / (double)mImageWidth;
            return new Rect(0, 0, measured, (int)(mImageHeight * rate));
       // }

      //  return new Rect(0, 0, mImageWidth, mImageHeight);
    }

//    public Rect getAvailableSize(){
//
//        if(mImageWidth > mMaxImageWidth) {
//            double rate = mMaxImageWidth / mImageWidth;
//            return new Rect(0, 0, mRef.get().getMeasuredWidth(), (int)(mImageHeight * rate));
//        }
//
//        return new Rect(0, 0, mRef.get().getMeasuredWidth() , mImageHeight);
//    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void readFromParcel(Parcel src) {
        mImage = src.readParcelable(Uri.class.getClassLoader());
    }

    WeakReference<RichTextView> mRef;
    @Override
    public void onSpannedSetToView(RichTextView view) {
        mAttachedToWindow = view.isAttachedToWindow();
        mRef = new WeakReference(view);
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
        dest.writeParcelable(mImage,0);
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
        mRect = getBitmapSize();
        //center
        x = (mRef.get().getMeasuredWidth() - mRect.width()) / 2;
        canvas.translate(x, transY);
        if(mBitmap != null) {
            mBitmap.draw(canvas);
        }
        //canvas.drawBitmap(mBitmap,null,getBitmapSize(),null);
        canvas.restore();

    }

    private Rect mRect = null;

    private boolean mLoading = false;
    private boolean mAttachedToWindow = false;


    @Override
    public void updateBitmap(Context context, Drawable bitmap){
        mBitmap = bitmap;
        mBitmap.setBounds(getBitmapSize());
        final RichTextView view = mRef.get();
        if(view != null && mAttachedToWindow){
            mBitmap.setCallback(view);
            mBitmap.invalidateSelf();
            view.invalidate();
        }
    }


    private void loadImage(){
        if(mAttachedToWindow == true && mLoading == false){
            mLoading = true;
            mUrlBitmapDownloader.downloadImage(this,mImage);
        }
    }



}
