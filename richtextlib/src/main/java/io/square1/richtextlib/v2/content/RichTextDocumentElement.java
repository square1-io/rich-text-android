package io.square1.richtextlib.v2.content;

import android.os.Parcel;
import android.text.GetChars;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import java.io.IOException;
import java.util.ArrayList;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.spans.RichTextSpan;


/**
 * This is the class for text whose content and markup can both be changed.
 */
public class RichTextDocumentElement extends DocumentElement implements CharSequence, GetChars, Spannable, Appendable {

    public static final Creator<RichTextDocumentElement> CREATOR = DynamicParcelableCreator.getInstance(RichTextDocumentElement.class);


    private SpannableStringBuilder mSpannableString;

    public RichTextDocumentElement(CharSequence string){
        mSpannableString = new SpannableStringBuilder(string);

    }


    public RichTextDocumentElement() {
        this("");
    }

    @Override
    public void write(Parcel dest, int flags) {

        dest.writeString(mSpannableString.toString());

        RichTextSpan[] spans =  getSpans();
        int[] spanStarts = new int[spans.length];
        int[] spanEnds = new int[spans.length];
        int[] spanFlags = new int[spans.length];

        for(int index = 0; index < spans.length; index ++){
            RichTextSpan currentSpan = spans[index];
            spanStarts[index] = mSpannableString.getSpanStart(currentSpan);
            spanEnds[index] = mSpannableString.getSpanEnd(currentSpan);
            spanFlags[index] = mSpannableString.getSpanFlags(currentSpan);
        }

        dest.writeIntArray(spanStarts);
        dest.writeIntArray(spanEnds);
        dest.writeIntArray(spanFlags);
        dest.writeTypedArray(spans, flags);
    }

    @Override
    public void readFromParcel(Parcel source) {

        String text = source.readString();

        mSpannableString = new SpannableStringBuilder(text);

        int[] spanStarts = source.createIntArray();
        int[] spanEnds = source.createIntArray();
        int[] spanFlags = source.createIntArray();

        RichTextSpan[] spans = source.createTypedArray(RichTextSpan.CREATOR);

        for(int index = 0; index < spans.length; index ++){

            mSpannableString.setSpan(spans[index],
                    spanStarts[index],
                    spanEnds[index],
                    spanFlags[index]);

        }

    }


    @Override
    public Appendable append(char c)  {
        mSpannableString =  mSpannableString.append(c);
        return this;
    }

    @Override
    public Appendable append(CharSequence csq) {
        mSpannableString = mSpannableString.append(csq);
        return this;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        mSpannableString = mSpannableString.append(csq, start, end);
        return mSpannableString;
    }

    @Override
    public void getChars(int start, int end, char[] dest, int destoff) {
        mSpannableString.getChars(start, end, dest, destoff);
    }

    @Override
    public void setSpan(Object what, int start, int end, int flags) {
        mSpannableString.setSpan(what, start, end, flags);

    }

    @Override
    public void removeSpan(Object what) {
        mSpannableString.removeSpan(what);
    }

    @Override
    public <T> T[] getSpans(int start, int end, Class<T> type) {
        return mSpannableString.getSpans(start,end,type);
    }

    @Override
    public int getSpanStart(Object span) {
        return mSpannableString.getSpanStart(span);
    }

    @Override
    public int getSpanEnd(Object span) {
        return mSpannableString.getSpanEnd(span);
    }

    @Override
    public int getSpanFlags(Object span) {
        return mSpannableString.getSpanFlags(span);
    }

    @Override
    public int nextSpanTransition(int start, int limit, Class type) {
        return mSpannableString.nextSpanTransition(start, limit, type);
    }

    @Override
    public int length() {
        return mSpannableString.length();
    }

    @Override
    public char charAt(int index) {
        return mSpannableString.charAt(index);
    }

    public void replaceAt(int index, String replacement){
        mSpannableString.replace(index,index + replacement.length(),replacement);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return mSpannableString.subSequence(start, end);
    }

    public RichTextSpan[] getSpans() {
        return mSpannableString.getSpans(0, length(), RichTextSpan.class);
    }

    public  <T>  T getLastSpan(Class<T> kind) {
        /*
         * This knows that the last returned object from getSpans()
         * will be the most recently added.
         */
        T[] objs = mSpannableString.getSpans(0, mSpannableString.length(), kind);

        if (objs.length == 0) {
            return null;
        } else {
            return objs[objs.length - 1];
        }
    }

    public void trim(int count) {
        if(count > 0) {
            int start = length() - count;
            int end = start + count - 1;
            mSpannableString.delete(start, end);
        }
    }

    public void delete(int start, int end) {
        try {
           // mSpannableString.replace(start, end, " ", 0, 0);
            mSpannableString = mSpannableString.delete(start, end);
        }catch (Exception e){

        }
    }

    @Override
    public String toString(){
        int len = mSpannableString.length();
        if(len == 0) return "<EMPTY>";
        if(len < 70) return mSpannableString.toString();
        return mSpannableString.subSequence(0,30).toString() +
                " ~...~ " +
                mSpannableString.subSequence(len-30, len - 1).toString();
    }

    public String contentString() {
        return mSpannableString == null ? "" : mSpannableString.toString();
    }

}