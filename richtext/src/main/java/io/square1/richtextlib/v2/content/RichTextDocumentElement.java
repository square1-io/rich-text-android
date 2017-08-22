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

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.ColorInt;
import android.text.GetChars;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TtsSpan;


import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.spans.BackgroundColorSpan;
import io.square1.richtextlib.spans.BoldSpan;
import io.square1.richtextlib.spans.ForegroundColorSpan;
import io.square1.richtextlib.spans.ItalicSpan;
import io.square1.richtextlib.spans.RelativeSizeSpan;
import io.square1.richtextlib.spans.RichAlignmentSpan;
import io.square1.richtextlib.spans.RichTextSpan;
import io.square1.richtextlib.spans.StrikethroughSpan;
import io.square1.richtextlib.spans.StyleSpan;
import io.square1.richtextlib.spans.TypefaceSpan;
import io.square1.richtextlib.spans.URLSpan;
import io.square1.richtextlib.spans.UnderlineSpan;
import io.square1.richtextlib.spans.UrlBitmapSpan;
import io.square1.richtextlib.spans.VideoPlayerSpan;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;


/**
 * This is the class for text whose content and markup can both be changed.
 */
public class RichTextDocumentElement extends DocumentElement implements CharSequence, GetChars, Spannable, Appendable {

    private static final class StringSpans {

        private HashMap<Class,RichTextSpan> mSpans;
        private String mString;

        private StringSpans(String string){
            mString = string;
            mSpans = new HashMap<>();
        }

        public void addSpan(RichTextSpan span){
            mSpans.put(span.getClass(), span);
        }

        public void removeSpan(Class span){
            mSpans.remove(span);
        }

        private RichTextDocumentElement append(RichTextDocumentElement text){

            int bounds[] = text.appendText(mString);

            for(RichTextSpan span : mSpans.values()){

                text.setSpan(span, bounds[0],
                        bounds[1], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            return text;
        }



    }

    public static final class TextBuilder {

        private ArrayList<StringSpans> mSpans;

        public TextBuilder(String append){
            mSpans = new ArrayList<>();
            mSpans.add( new StringSpans(append));
        }

        public TextBuilder append(String text){
            mSpans.add( new StringSpans(text));
            return this;
        }

        public TextBuilder newLine(){
           return append("\n");
        }


        public TextBuilder paragraph(String text) {
            StringBuilder builder = new StringBuilder(text);
            SpannedBuilderUtils.ensureBeginsWithAtLeastThoseNewLines(builder, 1);
            SpannedBuilderUtils.ensureAtLeastThoseNewLines(builder, 1);
            mSpans.add( new StringSpans(builder.toString()));
            return this;
        }

        public TextBuilder click(String action) {
            getCurrent().addSpan(new URLSpan(action));
            return this;
        }



        private StringSpans getCurrent(){
            return mSpans.get(mSpans.size() - 1);
        }

        public TextBuilder strikethrough(boolean set){
            if(set == true) {
                getCurrent().addSpan( new StrikethroughSpan());
            }else {
                getCurrent().removeSpan(StrikethroughSpan.class);
            }
            return this;
        }

        public TextBuilder underline(boolean set){
            if(set == true) {
                getCurrent().addSpan( new UnderlineSpan());
            }else {
                getCurrent().removeSpan(UnderlineSpan.class);
            }
            return this;
        }

        public TextBuilder background(@ColorInt int color){
            getCurrent().addSpan( new BackgroundColorSpan(color));
            return this;
        }

        public TextBuilder color(@ColorInt int color){
            getCurrent().addSpan(  new ForegroundColorSpan(color));
            return this;
        }

        public TextBuilder center(){
            getCurrent().addSpan(  new RichAlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER));
            return this;
        }

        public TextBuilder left(){
            getCurrent().addSpan(  new RichAlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL));
            return this;
        }

        public TextBuilder right(){
            getCurrent().addSpan(  new RichAlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE));
            return this;
        }

        public TextBuilder bold() {
            getCurrent().addSpan( new BoldSpan());
            return this;
        }

        public TextBuilder italic() {
            getCurrent().addSpan( new ItalicSpan());
            return this;
        }

        public TextBuilder sizeChange(float change){
            getCurrent().addSpan(new RelativeSizeSpan(change));
            return this;
        }

        public TextBuilder image(String imageUrl){
            return image(imageUrl,NumberUtils.INVALID, NumberUtils.INVALID );
        }

        public TextBuilder image(String imageUrl, int w, int h){
            append(SpannedBuilderUtils.NO_SPACE);
            getCurrent().addSpan( new UrlBitmapSpan(Uri.parse(imageUrl),
                    w, h, w));
            return this;
        }


        public TextBuilder video(String videoUrl) {
            append(SpannedBuilderUtils.NO_SPACE);
            getCurrent().addSpan( new VideoPlayerSpan(videoUrl,
                    NumberUtils.INVALID, NumberUtils.INVALID,
                    NumberUtils.INVALID));
            return this;
        }

        public TextBuilder font(String family) {
            getCurrent().addSpan( new TypefaceSpan(family) );
            return this;
        }


        public RichTextDocumentElement build(RichTextDocumentElement textDocumentElement){

            if(textDocumentElement == null){
                textDocumentElement = new RichTextDocumentElement();
            }

            for(StringSpans span : mSpans){
                span.append(textDocumentElement);
            }

            return textDocumentElement;
        }

        public RichTextDocumentElement build(){
            return build(null);
        }


    }


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


    /**
     * @param fontFamily The font family for this typeface.  Examples include "monospace", "serif", and "sans-serif".
     * @param start the starting point in the current string
     * @param length how long is the portion of string that should receive the new style
     */
    public RichTextDocumentElement setFontFamily(String fontFamily, int start, int length){
        if (TextUtils.isEmpty(fontFamily) == false) {
            mSpannableString.setSpan(new TypefaceSpan(fontFamily), start, length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    /**
     * Set the color of the text for the range specified between start and length
     * @param color
     * @param start the starting point in the current string
     * @param length how long is the portion of string that should receive the new style
     */
    public RichTextDocumentElement setFontColor(@ColorInt int color, int start, int length){
        mSpannableString.setSpan(new ForegroundColorSpan(color | 0xFF000000),
                start, length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
     }

    /**
     *  apply a size change to the current string as a % of the current sie
     * @param relativeSizeChange
     * @param start
     * @param length
     * @return
     */
    public RichTextDocumentElement setFontSizeChange(float relativeSizeChange, int start, int length){
        mSpannableString.setSpan(new RelativeSizeSpan(relativeSizeChange),
                start, length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * Set the color of the background for the range specified between start and length
     * @param color
     * @param start the starting point in the current string
     * @param length how long is the portion of string that should receive the new style
     */
    public RichTextDocumentElement setBackgroundColor(@ColorInt int color, int start, int length){
        mSpannableString.setSpan(new BackgroundColorSpan(color | 0xFF000000),
                start, length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }



    /**
     *  Underline the text in the specified range
     * @param start
     * @param length
     * @return
     */
     public RichTextDocumentElement setUnderline(int start, int length){
             mSpannableString.setSpan(new UnderlineSpan(),
                     start,
                     length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
         return this;
     }

    /**
     *  change the font size by a factor specified in change
     * @param start the starting point for the change
     * @param length how long is the portion of string that should receive the new style
     * @param change float to specify the text change , for example 1.1 = increase font by 10%
     * @return
     */
    public RichTextDocumentElement setRelativeTextSize(int start, int length, float change){
        mSpannableString.setSpan(new RelativeSizeSpan(change),
                start,
                length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     *  set to bold the text in the specified range
     * @param start the starting point for the change
     * @param length how long is the portion of string that should receive the new style
     * @return
     */

    public RichTextDocumentElement setBoldText(int start, int length){
        mSpannableString.setSpan(new StyleSpan(Typeface.BOLD),
                start,
                length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }


    public RichTextDocumentElement setStrikethrough(int start, int length){

        mSpannableString.setSpan(new StrikethroughSpan(),
                start,
                length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }


    public RichTextDocumentElement setAlignment(int start, int length, Layout.Alignment alignment){

        mSpannableString.setSpan(new RichAlignmentSpan.Standard(alignment),
                start,
                length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     *
     * @param imageUri
     * @param width
     * @param height
     * @return
     */
     public RichTextDocumentElement appendImage(Uri imageUri, int width, int height){
         UrlBitmapSpan imageDrawable = new UrlBitmapSpan(imageUri, width, height, width);
         int[] bounds =  appendText(SpannedBuilderUtils.NO_SPACE);
         mSpannableString.setSpan(imageDrawable,
                 bounds[0],
                 bounds[1],
                 Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
         SpannedBuilderUtils.ensureAtLeastThoseNewLines(this, 1);
         return this;
     }

    /**
     * Appends a string and make it bold.
     * @param text the string to be appended
     * @return
     */
     public RichTextDocumentElement appendBold(CharSequence text){
         int[] bounds =  appendText(text);
         return setBoldText(bounds[0], bounds[1]);
     }

    /**
     * Appends a string and make it underlined.
     * @param text the string to be appended
     * @return
     */
    public RichTextDocumentElement appendUnderlined(CharSequence text){
        int[] bounds =  appendText(text);
        return setUnderline(bounds[0], bounds[1]);
    }


    /**
     * append a string and makes the text of the specified color
     * @param text
     * @param color
     * @return
     */
    public RichTextDocumentElement appendColored(CharSequence text, @ColorInt int color){
        int[] bounds =  appendText(text);
        return setFontColor(color, bounds[0], bounds[1]);
    }

    /**
     * append a string and makes the text of the specified color
     * @param text
     * @param color
     * @return
     */
    public RichTextDocumentElement appendBackground(CharSequence text, @ColorInt int color){
        int[] bounds =  appendText(text);
        return setBackgroundColor(color, bounds[0], bounds[1]);
    }

    public RichTextDocumentElement appendFontSizeChange(CharSequence text, int color){
        int[] bounds =  appendText(text);
        return setFontColor(color, bounds[0], bounds[1]);
    }

    public RichTextDocumentElement appendStrikethrough(CharSequence text){
        int[] bounds =  appendText(text);
        return setStrikethrough(bounds[0], bounds[1]);
    }

     private int[] appendText(CharSequence sequence){
         int[] startEnd = new int[2];
         startEnd[0] = mSpannableString.length();
         mSpannableString.append(sequence);
         startEnd[1] = mSpannableString.length();
         return startEnd;
     }


}