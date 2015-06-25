package io.square1.richtextlib;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

import io.square1.richtextlib.style.P2ParcelUtils;
import io.square1.richtextlib.style.P2ParcelableSpan;


/**
 * Created by roberto on 12/06/15.
 */
public class SpannedStore implements Parcelable , Spanned {


    private  SpannableStringBuilder mText;
    private int[] mSpanStart;
    private int[] mSpanEnd;
    private P2ParcelableSpan[] mSpans;

    public SpannedStore(){
        mText = new SpannableStringBuilder();
    }

    public SpannedStore(Spannable spanned){
        extract(spanned);
    }

    private void extract(Spanned spanned){

        mText = new SpannableStringBuilder(spanned);

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
    private SpannableStringBuilder build(String text){

        SpannableStringBuilder builder = new SpannableStringBuilder(text);

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
        String text = in.readString();
        mSpanStart = in.createIntArray();
        mSpanEnd = in.createIntArray();
        mSpans = in.createTypedArray(P2ParcelUtils.CREATOR);
        mText = build(text);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //extract arrays
        extract(mText);

        dest.writeString(mText.toString());
        dest.writeIntArray(mSpanStart);
        dest.writeIntArray(mSpanEnd);
        dest.writeTypedArray(mSpans, 0);

    }

    @Override
    public <T> T[] getSpans(int start, int end, Class<T> type) {
        return mText.getSpans(start,end,type);
    }

    @Override
    public int getSpanStart(Object tag) {
        return mText.getSpanStart(tag);
    }

    @Override
    public int getSpanEnd(Object tag) {
        return mText.getSpanEnd(tag);
    }

    @Override
    public int getSpanFlags(Object tag) {
        return mText.getSpanFlags(tag);
    }

    @Override
    public int nextSpanTransition(int start, int limit, Class type) {
        return mText.nextSpanTransition(start,limit,type);
    }

    @Override
    public int length() {
        return mText.length();
    }

    @Override
    public char charAt(int index) {
        return mText.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return mText.subSequence(start, end);
    }
}
