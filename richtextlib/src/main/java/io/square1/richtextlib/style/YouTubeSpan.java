package io.square1.richtextlib.style;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.text.style.ReplacementSpan;
import android.text.style.UpdateAppearance;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.R;
import io.square1.richtextlib.ui.RichTextView;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.util.UniqueId;
import io.square1.richtextlib.v2.utils.SpanUtils;

/**
 * Created by roberto on 23/06/15.
 */
public class YouTubeSpan extends ReplacementSpan implements RemoteBitmapSpan, ClickableSpan, UpdateAppearance, P2ParcelableSpan {

    public static final Creator<YouTubeSpan> CREATOR  = P2ParcelableCreator.get(YouTubeSpan.class);
    public static final int TYPE = UniqueId.getType();


    private Uri mImage;
    private Drawable mBitmap;
    private Bitmap mYoutubeIcon;
    private int mImageHeight;
    private int mImageWidth;
    private int mMaxImageWidth;

    private String mYoutubeId;


    public String getYoutubeId(){
        return mYoutubeId;
    }

    public YouTubeSpan(){}

    public YouTubeSpan(String youtubeId, int maxWidth){
        super();
        mYoutubeId = youtubeId;
        mImageWidth = mImageHeight = NumberUtils.INVALID;
        mMaxImageWidth = maxWidth;

        mImage = Uri.parse(EmbedUtils.getYoutubeThumbnailUrl(youtubeId));

    }


    private boolean imageSizeKnown(){
        return (mImageWidth != NumberUtils.INVALID );
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

       // mRef.get().getPaddingLeft()
//        if(mBitmap != null){
//         int measured = mMaxImageWidth;
//         double rate = (double)measured / (double)mBitmap.getIntrinsicWidth();
//         return new Rect(0, 0, measured, (int)(mBitmap.getIntrinsicHeight() * rate));
//        }

        if(mRef != null &&
                mRef.get() != null &&
                mRef.get().getMeasuredWidth() != 0){

            final RichTextView view = mRef.get();
            double availableWidth =  (double)(view.getMeasuredWidth());

            double availableHeight = availableWidth / 16 * 9;
            return new Rect(0,0,(int)availableWidth, (int)availableHeight);
        }

        double measure =  mMaxImageWidth;
        double height = measure / 16 * 9;
        return new Rect(0,0,(int)measure,(int)height);
    }


    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void readFromParcel(Parcel src) {
        mImage = src.readParcelable(Uri.class.getClassLoader());
        mYoutubeId = src.readString();
        mImageWidth = src.readInt();
        mImageHeight = src.readInt();
        mMaxImageWidth = src.readInt();
    }

    WeakReference<RichTextView> mRef;

    @Override
    public void onSpannedSetToView(RichTextView view) {

        if(mYoutubeIcon == null){
            mYoutubeIcon = BitmapFactory.decodeResource(view.getContext().getResources(),
                    R.drawable.youtube_play);; //view.getContext().getResources().getDrawable(R.drawable.youtube_play);
        }

        mAttachedToWindow = view.viewAttachedToWindow();
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
        dest.writeString(mYoutubeId);
        dest.writeInt(mImageWidth);
        dest.writeInt(mImageHeight);
        dest.writeInt(mMaxImageWidth);
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

        mRect = getBitmapSize();

        //drawIcon(x,canvas);

        if(mBitmap != null){

            if(mBitmap instanceof BitmapDrawable){
                Bitmap bmp = ((BitmapDrawable)mBitmap).getBitmap();
                drawBitmap(canvas,bmp,mRect,start,end,x,top,y,bottom,paint);
            }else{
                int transY = bottom - mRect.bottom;
                transY -= paint.getFontMetricsInt().descent;
                canvas.save();
                canvas.translate(x, transY);
                mBitmap.setBounds(mRect);
                mBitmap.draw(canvas);
                canvas.restore();
            }

        }

        drawBitmap(canvas, mYoutubeIcon, mRect, start, end, x, top, y, bottom, paint);
    }

//    private void drawIcon(float offset , Canvas c){
//        c.save();
//
//        Rect rect = mYoutubeIcon.copyBounds();
//        float x = (mRef.get().getMeasuredWidth() - rect.width()) / 2 + offset;
//        c.translate(x, (mRef.get().getMeasuredHeight() - rect.height()) / 2);
//        mYoutubeIcon.draw(c);
//        c.restore();
//    }

    private Rect mRect = null;

    private boolean mLoading = false;
    private boolean mAttachedToWindow = false;

    private static void drawBitmap(Canvas canvas,
                                   Bitmap bitmap,
                                   Rect bounds,
                                   int start,
                                   int end,
                                   float x,
                                   int top,
                                   int y,
                                   int bottom,
                                   Paint paint){


        int transY = bottom - bounds.bottom;
        transY -= paint.getFontMetricsInt().descent;

        canvas.save();
        canvas.translate(x, transY);
        bitmap.getWidth();

        int offsetX = (bounds.width() - bitmap.getWidth()) / 2 ;
        int offsetY = (bounds.height() - bitmap.getHeight()) / 2 ;
        canvas.drawBitmap(bitmap,offsetX,offsetY, paint);
        canvas.restore();
    }

    @Override
    public void updateBitmap(Context context, Drawable bitmap){
        mBitmap = bitmap;
        boolean needsLayout = false;
        if(imageSizeKnown() == false) {
            needsLayout = true;
            mImageWidth = bitmap.getIntrinsicWidth();
            mImageHeight = bitmap.getIntrinsicHeight();
        }
        mBitmap.setBounds(getBitmapSize());
        final RichTextView view = mRef.get();

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
        return getBitmapSize();
    }



    private void loadImage(){
        if(mAttachedToWindow == true && mLoading == false && mRef != null){
            mLoading = true;
            if(mRef.get() == null){
                return;
            }

            UrlBitmapDownloader downloader = SpanUtils.getDownloader(mRef.get());
            if(downloader != null) {
                downloader.downloadImage(this, mImage);
            }
        }
    }



}
