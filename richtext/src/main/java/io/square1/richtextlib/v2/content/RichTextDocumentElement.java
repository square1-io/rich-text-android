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

package io.square1.richtextlib.v2.content;

import android.os.Parcel;
import android.text.GetChars;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.spans.RichTextSpan;


/**
 * This is the class for text whose content and markup can both be changed.
 */
public class RichTextDocumentElement extends DocumentElement implements CharSequence, GetChars, Spannable, Appendable {



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RichTextDocumentElement that = (RichTextDocumentElement) o;

        if(!contentString().equals(that.contentString())) return false;

        RichTextSpan[] richTextSpans = getSpans();
        RichTextSpan[] thatRichTextSpans = that.getSpans();

        if (richTextSpans == thatRichTextSpans) {
            return true;
        }
        if (richTextSpans == null ||
                thatRichTextSpans == null ||
                richTextSpans.length != thatRichTextSpans.length) {
            return false;
        }
        for (int i = 0; i < richTextSpans.length; i++) {

            RichTextSpan e1 = richTextSpans[i];
            RichTextSpan e2 = thatRichTextSpans[i];

            if(e1.getClass().equals(e2.getClass()) == false) {
                return false;
            }

        }
        return true;


    }

    @Override
    public int hashCode() {
        return mSpannableString.hashCode();
    }

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
        return mSpannableString.getSpans(start, end, type);
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

    public <T> T[]  getSpans(Class<T> kind) {
        return mSpannableString.getSpans(0, length(), kind);
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
            int end = start + count;
            try {
                mSpannableString.delete(start, end);
            }catch (Exception exc){}
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


    @Override
    public int describeContents() {
        return 0;
    }


}