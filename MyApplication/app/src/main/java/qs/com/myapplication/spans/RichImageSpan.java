package qs.com.myapplication.spans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.style.ImageSpan;

/**
 * Created by roberto on 03/12/2015.
 */
public class RichImageSpan extends android.text.style.ImageSpan implements RichSpan {

    public static Parcelable.Creator<RichImageSpan> CREATOR = new Parcelable.Creator<RichImageSpan>() {

        @Override
        public RichImageSpan createFromParcel(Parcel source) {
            RichImageSpan span = new RichImageSpan(null,(Bitmap)null,0);
            span.readFromParcel(source);
            return span;
        }

        @Override
        public RichImageSpan[] newArray(int size) {
            return new RichImageSpan[size];
        }
    };

    private Bitmap mBitmap;

    public RichImageSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
        mBitmap = b;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        mBitmap.


    }

    @Override
    public void readFromParcel(Parcel input) {

    }
}
