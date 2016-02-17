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


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Parcel;
import android.text.style.ReplacementSpan;
import android.text.style.UpdateAppearance;

import java.lang.ref.WeakReference;


import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.ui.video.RichVideoView;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 23/06/15.
 */
public class VideoPlayerSpan extends ReplacementSpan implements ClickableSpan, UpdateAppearance, RichTextSpan,  Animatable {

    public static final Creator<VideoPlayerSpan> CREATOR  = DynamicParcelableCreator.getInstance(VideoPlayerSpan.class);
    public static final int TYPE = UniqueId.getType();


    private Uri mVideoUri;


    private RichVideoView mPlayer;



    public VideoPlayerSpan(){

    }

    public VideoPlayerSpan(String videoUrl, int maxWidth){
        super();


        mVideoUri = Uri.parse(videoUrl);

    }



    public Rect getBitmapSize(){

        RichContentView viewDisplay = (RichContentView)mRef.get();
        int viewDisplayWidth = viewDisplay.getMeasuredWidth() -
                viewDisplay.getPaddingRight() -
                viewDisplay.getPaddingRight();

        double measure =  viewDisplayWidth;
        double height = measure / 16 * 9;
        return new Rect(0,0,(int)measure,(int)height);
    }


    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void readFromParcel(Parcel src) {
        String s = src.readString();
        mVideoUri = Uri.parse(s);
    }

    WeakReference<RichContentViewDisplay> mRef;

    @Override
    public void onSpannedSetToView(RichContentViewDisplay view) {
        mRef = new WeakReference(view);
    }

    @Override
    public void onAttachedToWindow(RichContentViewDisplay view) {


    }

    @Override
    public void onDetachedFromWindow(RichContentViewDisplay view) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        DynamicParcelableCreator.writeType(dest, this);
        dest.writeString(mVideoUri.toString());
       // dest.writeParcelable(mVideoUri,0);
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
            mPlayer = new RichVideoView(mRef.get().getContext());
            viewDisplay.addSubView(mPlayer);
            mPlayer.setData(mVideoUri);
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

}
