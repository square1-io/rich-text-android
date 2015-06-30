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

    public YouTubeSpan(String youtubeId, UrlBitmapDownloader downloader){
        super();
        mUrlBitmapDownloader = downloader;
        mImage = Uri.parse(EmbedUtils.getYoutubeThumbnailUrl(youtubeId));
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
        int measured =  mRef.get().getMeasuredWidth();
        double height = (double)measured * (double)9 / (double)16;
        return new Rect(0, 0, measured, (int)height);
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
        mAttachedToWindow = view.isAttachedToWindow();
        mRef = new WeakReference(view);
        if(mYoutubeIcon == null){
            mYoutubeIcon = view.getResources().getDrawable(R.drawable.youtube_play);
        }
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

        return rect.width();
    }



    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

        final Rect bitmapBounds = getBitmapSize();

        int transY = bottom - bitmapBounds.bottom;
        transY -= paint.getFontMetricsInt().descent;

        canvas.save();
        mRect = getBitmapSize();
        //center
        x = (mRef.get().getMeasuredWidth() - mRect.width()) / 2;
        canvas.translate(x, transY);
        if(mBitmap != null) {
            mBitmap.draw(canvas);
        }
        canvas.restore();

        if(mYoutubeIcon != null){
            mYoutubeIcon.draw(canvas);
        }

    }

    private Rect mRect = null;

    private boolean mLoading = false;
    private boolean mAttachedToWindow = false;


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
