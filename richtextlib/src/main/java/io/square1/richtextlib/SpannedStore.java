package io.square1.richtextlib;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

import io.square1.richtextlib.style.P2ParcelUtils;
import io.square1.richtextlib.style.P2ParcelableSpan;


/**
 * Created by roberto on 12/06/15.
 */
public class SpannedStore implements Parcelable {


    private  String mText;
    private int[] mSpanStart;
    private int[] mSpanEnd;
    private P2ParcelableSpan[] mSpans;

    public SpannedStore(){}

    public SpannedStore(Spannable spanned){

        mText = spanned.toString();

        P2ParcelableSpan[] spans = spanned.getSpans(0,
                spanned.length(),
                P2ParcelableSpan.class);

        mSpans = new P2ParcelableSpan[spans.length];

        mSpanStart = new int[mSpans.length];
        mSpanEnd = new int[mSpans.length];


        for(int index = 0; index < spans.length; index ++){

            P2ParcelableSpan span = spans[index];
            mSpanStart[index] = spanned.getSpanStart(span);
            mSpanEnd[index] = spanned.getSpanEnd(span);
            mSpans[index] = span;

        }
    }

    public SpannableStringBuilder build(){

        SpannableStringBuilder builder = new SpannableStringBuilder(mText);

        for(int index = 0; index < mSpans.length; index ++){

            builder.setSpan(mSpans[index],
                    mSpanStart[index],
                    mSpanEnd[index],
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return builder;
    }


    public static final Creator<SpannedStore> CREATOR  = new Creator<SpannedStore>() {

        public SpannedStore createFromParcel(Parcel in) {
            return new SpannedStore(in);
        }

        public SpannedStore[] newArray(int size) {
            return new SpannedStore[size];
        }
    };

    private SpannedStore(Parcel in) {
       readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel in){
        mText = in.readString();
        mSpanStart = in.createIntArray();
        mSpanEnd = in.createIntArray();
        mSpans = in.createTypedArray(P2ParcelUtils.CREATOR);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mText);
        dest.writeIntArray(mSpanStart);
        dest.writeIntArray(mSpanEnd);
        dest.writeTypedArray(mSpans, 0);

    }
}
