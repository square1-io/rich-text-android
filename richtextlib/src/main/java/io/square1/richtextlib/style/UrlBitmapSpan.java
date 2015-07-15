package io.square1.richtextlib.style;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;

import android.text.style.ReplacementSpan;
import android.text.style.UpdateAppearance;


import java.lang.ref.WeakReference;

import io.square1.richtextlib.ui.RichTextView;
import io.square1.richtextlib.util.NumberUtils;
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
    private int mImageWidth;
    private int mImageHeight;

    private Uri mImage;
    private UrlBitmapDownloader mUrlBitmapDownloader;
    private Drawable mBitmap;

    public UrlBitmapSpan(){
        mVerticalAlignment = ALIGN_BASELINE;
    }

    public UrlBitmapSpan(Uri image, UrlBitmapDownloader downloader, int imageWidth, int imageHeight, int maxImageWidth){
        this(null,downloader,image, imageWidth,imageHeight,maxImageWidth,ALIGN_BOTTOM);

    }
    public UrlBitmapSpan(Bitmap bitmap,
                         UrlBitmapDownloader downloader,
                         Uri image,
                         int  imageWidth,
                         int imageHeight,
                         int maxImageWidth,
                         int alignment){
        super();

        mUrlBitmapDownloader = downloader;
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

    private boolean imageSizeKnown(){
        return (mImageWidth != NumberUtils.INVALID );
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
        P2ParcelUtils.writeType(dest, this);
        dest.writeParcelable(mImage,0);
        dest.writeInt(mMaxImageWidth);
        dest.writeInt(mImageWidth);
        dest.writeInt(mImageHeight);

    }


    private int containerViewHasMeasure(){
        if(mRef != null && mRef.get() != null){
            int measured = mRef.get().getMeasuredWidth();
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
                    mImageHeight : (int)(mImageWidth*0.3);

            double rate = (double)maxAvailableWidth / (double)mImageWidth;
            return new Rect(0, 0, maxAvailableWidth, (int)(imageHeight * rate));
        }

        return new Rect(0, 0, maxAvailableWidth, (int)(maxAvailableWidth * 0.3));
    }

    private  Rect evaluateBitmapBounds(int bitmabW, int bitmapH) {

        int maxAvailableWidth = containerViewHasMeasure();

        //taking a guess here
        if(maxAvailableWidth == NumberUtils.INVALID)
            maxAvailableWidth = mMaxImageWidth;

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
           return evaluateBitmapBounds(mBitmap.getIntrinsicWidth(),mBitmap.getIntrinsicHeight());
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


    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

        if(mBitmap == null) return;

        final Rect bitmapBounds = mBitmap.getBounds();

        int transY = bottom - bitmapBounds.bottom;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        }


        canvas.save();

        //center
        x = (mRef.get().getMeasuredWidth() - bitmapBounds.width()) / 2;
        canvas.translate(x, transY);
        if(mBitmap != null) {
            mBitmap.draw(canvas);
        }

        canvas.restore();

    }

    private Rect mRect = null;

    private boolean mLoading = false;
    private boolean mAttachedToWindow = false;


    @Override
    public void updateBitmap(Context context, Drawable bitmap){
        mBitmap = bitmap;

        mImageWidth = bitmap.getIntrinsicWidth();
        mImageHeight = bitmap.getIntrinsicHeight();

        mBitmap.setBounds(0,0,mImageWidth,mImageHeight);
        final RichTextView view = mRef.get();
        Rect newRect = getBitmapBounds();
        mBitmap.setBounds(newRect);

        boolean needsLayout = (newRect.equals(mRect) == false);

        if(view != null && mAttachedToWindow){
            mBitmap.setCallback(view);
            mBitmap.invalidateSelf();

            if(needsLayout == true){
                view.requestLayout();
            }
            view.invalidate();
        }
    }

    @Override
    public Rect getPossibleSize() {
        return getBitmapBounds();
    }


    private void loadImage(){
        if(mAttachedToWindow == true && mLoading == false){
            mLoading = true;
            if(mUrlBitmapDownloader == null){
                //acquire one from the view
               mUrlBitmapDownloader =  mRef.get().getDownloader();
            }
            mUrlBitmapDownloader.downloadImage(this,mImage);
        }
    }



}
