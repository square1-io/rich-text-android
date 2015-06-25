package io.square1.richtextlib.style;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.style.ReplacementSpan;

import io.square1.richtextlib.ui.RichTextView;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 23/06/15.
 */
public class BitmapSpan extends ReplacementSpan implements P2ParcelableSpan {



    public static final Parcelable.Creator<BitmapSpan> CREATOR  = P2ParcelableCreator.get(BitmapSpan.class);
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
    public void onSpannedSetToView(RichTextView view) {

    }

    @Override
    public void onAttachedToView(RichTextView view) {

    }

    @Override
    public void onDetachedFromView(RichTextView view) {

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

    }


//    private void loadImage(){
//        if(mAttachedToWindow == true && mBitmap == null){
//            Glide.with(mViewRef.get().getContext()).load(mImageUri).asBitmap().into(mSimpleTarget);
//        }
//    }

}
