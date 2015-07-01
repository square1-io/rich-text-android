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

import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.R;
import io.square1.richtextlib.ui.RichTextView;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 23/06/15.
 */
public class YouTubeSpan extends ReplacementSpan implements RemoteBitmapSpan, ClickableSpan, UpdateAppearance, P2ParcelableSpan {

    public static final Creator<YouTubeSpan> CREATOR  = P2ParcelableCreator.get(YouTubeSpan.class);
    public static final int TYPE = UniqueId.getType();


    private Uri mImage;
    private UrlBitmapDownloader mUrlBitmapDownloader;
    private Drawable mBitmap;
    private Drawable mYoutubeIcon;
    private int mImageHeight;
    private int mImageWidth;
    private int mMaxImageWidth;

    private String mYoutubeId;


    public String getYoutubeId(){
        return mYoutubeId;
    }

    public YouTubeSpan(){}

    public YouTubeSpan(String youtubeId, int maxWidth, UrlBitmapDownloader downloader){
        super();
        mYoutubeId = youtubeId;
        mImageWidth = mImageHeight = NumberUtils.INVALID;
        mMaxImageWidth = maxWidth;
        mUrlBitmapDownloader = downloader;

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


        int measured = mMaxImageWidth;

        double rate = (double)measured / (double)mImageWidth;
        return new Rect(0, 0, measured, (int)(mImageHeight * rate));


    }


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
        if(mYoutubeIcon == null){
            mYoutubeIcon = view.getContext().getResources().getDrawable(R.drawable.youtube_play);
            mYoutubeIcon.setBounds(0,0,mYoutubeIcon.getIntrinsicWidth(),mYoutubeIcon.getIntrinsicHeight());
        }
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


        mRect = getBitmapSize();;

        int transY = bottom - mRect.bottom;
        transY -= paint.getFontMetricsInt().descent;


        canvas.save();

        //center
        x = (mRef.get().getMeasuredWidth() - mRect.width()) / 2;
        canvas.translate(x, transY);
        if(mBitmap != null) {
            mBitmap.draw(canvas);
        }
        canvas.restore();

        drawIcon(canvas);

    }

    private void drawIcon(Canvas c){
        c.save();

        Rect rect = mYoutubeIcon.copyBounds();
        int x = (mRef.get().getMeasuredWidth() - rect.width()) / 2;
        c.translate(x, (mRef.get().getMeasuredHeight() - rect.height()) / 2);
        mYoutubeIcon.draw(c);
        c.restore();
    }

    private Rect mRect = null;

    private boolean mLoading = false;
    private boolean mAttachedToWindow = false;


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


    private void loadImage(){
        if(mAttachedToWindow == true && mLoading == false){
            mLoading = true;
            mUrlBitmapDownloader.downloadImage(this,mImage);
        }
    }


}
