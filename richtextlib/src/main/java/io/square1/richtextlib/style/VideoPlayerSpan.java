package io.square1.richtextlib.style;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Parcel;
import android.text.style.ReplacementSpan;
import android.text.style.UpdateAppearance;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.VideoView;

import java.lang.ref.WeakReference;

import javax.microedition.khronos.egl.EGL10;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.R;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 23/06/15.
 */
public class VideoPlayerSpan extends ReplacementSpan implements ClickableSpan, UpdateAppearance, P2ParcelableSpan {

    public static final Creator<VideoPlayerSpan> CREATOR  = DynamicParcelableCreator.getInstance(VideoPlayerSpan.class);
    public static final int TYPE = UniqueId.getType();


    private Uri mVideoUri;
    private int mImageHeight;
    private int mImageWidth;
    private int mMaxImageWidth;

    private VideoView mVideoView;



    public VideoPlayerSpan(){}

    public VideoPlayerSpan(String videoUrl, int maxWidth){
        super();


        mImageWidth = mImageHeight = NumberUtils.INVALID;
        mMaxImageWidth = maxWidth;
        mVideoUri = Uri.parse(videoUrl);

    }


    private boolean imageSizeKnown(){
        return (mImageWidth != NumberUtils.INVALID );
    }


    public Rect getBitmapSize(){

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
        mVideoUri = src.readParcelable(Uri.class.getClassLoader());
        mImageWidth = src.readInt();
        mImageHeight = src.readInt();
        mMaxImageWidth = src.readInt();
    }

    WeakReference<RichContentViewDisplay> mRef;

    @Override
    public void onSpannedSetToView(RichContentViewDisplay view) {
        mRef = new WeakReference(view);
    }

    @Override
    public void onAttachedToView(RichContentViewDisplay view) {



    }

    @Override
    public void onDetachedFromView(RichContentViewDisplay view) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        P2ParcelUtils.writeType(dest,this);
        dest.writeParcelable(mVideoUri,0);
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




        if(mVideoView == null){

            final Rect bitmapBounds = getBitmapSize();

            int transY = bottom - bitmapBounds.bottom;
            transY -= paint.getFontMetricsInt().descent;

            canvas.save();

            //center
            x = x + (mRef.get().getMeasuredWidth() - bitmapBounds.width()) / 2;
            canvas.translate(x, transY);

            mVideoView = new VideoView(mRef.get().getContext());
            mRef.get().addSubView(mVideoView);
            FrameLayout.LayoutParams params =
                    new FrameLayout.LayoutParams(bitmapBounds.width(), bitmapBounds.height());
            params.topMargin = canvas.getClipBounds().top;
            mVideoView.setLayoutParams(params);
            mVideoView.setVideoURI(mVideoUri);
            mVideoView.start();

            canvas.restore();
        }



    }



}
