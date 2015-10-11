package io.square1.richtextlib.style;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import android.graphics.drawable.Animatable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Parcel;
import android.text.style.ReplacementSpan;
import android.text.style.UpdateAppearance;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.VideoView;

import java.lang.ref.WeakReference;
import java.util.HashMap;


import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.ui.video.ScrollableVideoView;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 23/06/15.
 */
public class VideoPlayerSpan extends ReplacementSpan implements ClickableSpan, UpdateAppearance, P2ParcelableSpan,  Animatable {

    public static final Creator<VideoPlayerSpan> CREATOR  = DynamicParcelableCreator.getInstance(VideoPlayerSpan.class);
    public static final int TYPE = UniqueId.getType();


    private Uri mVideoUri;
    private int mImageHeight;
    private int mImageWidth;
    private int mMaxImageWidth;

    private ScrollableVideoView mPlayer;

    private boolean mAttachedToWindow;



    public VideoPlayerSpan(){
        super();
    }

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
        mAttachedToWindow = true;

    }

    @Override
    public void onDetachedFromView(RichContentViewDisplay view) {
        mAttachedToWindow = false;
        if (mPlayer != null){
            mPlayer.stopPlayback();
            mPlayer = null;
        }
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



    int mStart;
    int mEnd;
    float mX;
    int mTop;
    int mY;
    int mBottom;
    int mTransY;

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

         mStart = start;
         mEnd = end;
         mX = x;
         mTop = top;
         mY = y;
         mBottom = bottom;


        final Rect bitmapBounds = getBitmapSize();

        mTransY = bottom - bitmapBounds.bottom;
        mTransY -= paint.getFontMetricsInt().descent;

        prepareVideoView();

        }



    private void prepareVideoView(){

        RichContentView viewDisplay = (RichContentView)mRef.get();
        if(mPlayer == null) {
            mPlayer = new ScrollableVideoView(mRef.get().getContext());
            viewDisplay.addSubView(mPlayer);
            mPlayer.setVideoURI(mVideoUri);
        }
        Point point = new Point((int) mX, mTransY);
        mPlayer.setLayoutParams(viewDisplay.generateDefaultLayoutParams(point, getBitmapSize().width(), getBitmapSize().height()));
    }

    @Override
    public void start() {
        if(mPlayer != null) {
            mPlayer.start();
        }


    }

    @Override
    public void stop() {
        if(mPlayer != null){
            mPlayer.pause();
        }
    }

    @Override
    public boolean isRunning() {
        if(mPlayer != null){
            return mPlayer.isPlaying();
        }
        return false;
    }

//    private Bitmap getVideoFrame() {
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        try {
//            retriever.setDataSource(mVideoUri.toString(), new HashMap<String, String>());
//            return retriever.getFrameAtTime();
//        } catch (IllegalArgumentException ex) {
//            ex.printStackTrace();
//        } catch (RuntimeException ex) {
//            ex.printStackTrace();
//        } finally {
//            try {
//                retriever.release();
//            } catch (RuntimeException ex) {
//            }
//        }
//        return null;
//    }
}
