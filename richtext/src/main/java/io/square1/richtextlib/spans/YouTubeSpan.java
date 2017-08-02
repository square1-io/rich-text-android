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


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Parcel;

import java.lang.ref.WeakReference;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.R;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 23/06/15.
 */
public class YouTubeSpan extends UrlBitmapSpan {

    public static final Creator<YouTubeSpan> CREATOR  = DynamicParcelableCreator.getInstance(YouTubeSpan.class);
    public static final int TYPE = UniqueId.getType();

    private Bitmap mYoutubeIcon;
    private String mYoutubeId;

    public String getYoutubeId(){
        return mYoutubeId;
    }

    public YouTubeSpan(){}

    public YouTubeSpan(String youtubeId, int maxWidth){
        super(Uri.parse(EmbedUtils.getYoutubeThumbnailUrl(youtubeId)),480,360,maxWidth);
        mYoutubeId = youtubeId;
    }



    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void readFromParcel(Parcel src) {
        super.readFromParcel(src);
        mYoutubeId = src.readString();

    }

    WeakReference<RichContentViewDisplay> mRef;

    @Override
    public void onSpannedSetToView(RichContentView view) {

        if(mYoutubeIcon == null){
            mYoutubeIcon = BitmapFactory.decodeResource(view.getContext().getResources(),
                    R.drawable.youtube_play);; //view.getContext().getResources().getDrawable(R.drawable.youtube_play);
        }
        super.onSpannedSetToView(view);
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
//        DynamicParcelableCreator.writeType(dest, this);
//        dest.writeParcelable(mImage,0);
        dest.writeString(mYoutubeId);
//        dest.writeInt(mImageWidth);
//        dest.writeInt(mImageHeight);
//        dest.writeInt(mMaxImageWidth);
    }






    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        super.draw(canvas,text,start,end,x,top,y,bottom,paint);
        if(getBitmap() == null) return;
        final Rect bitmapBounds = getBitmap().getBounds();
        drawBitmap(canvas, mYoutubeIcon, bitmapBounds, start, end, x, top, y, bottom, paint);
    }




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





}
