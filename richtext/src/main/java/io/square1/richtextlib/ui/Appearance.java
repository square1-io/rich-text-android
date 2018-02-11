/*
 * Copyright (c) 2016. Roberto  Prato <https://github.com/robertoprato>
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

package io.square1.richtextlib.ui;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.TypedValue;

import io.square1.richtextlib.R;

/**
 * Created by roberto on 18/09/2016.
 */
public class Appearance {

    /**
     * main color for the text
     */
    private Integer textColor;


    /**
     * text color for links
     */
    private Integer linkColor;

    /**
     *  font for the main text
     */
    private Typeface textTypeFace;

    /**
     *  font for the link text
     */
    private Typeface linkTypeFace;


    /**
     * font size for text
     */
    private float textFontSize = 0;

    /**
     * font size for the links
     */
    private float linkFontSize = 0;



    /**
     *  padding between quote sign and the left margin
     */
    private float quoteSignLeftPadding = 0;

    /**
     *  padding between quote sign and the text
     */
    private float quoteSignRightPadding = 0;

    /**
     *  padding between quote sign and the top pf the quotation box
     *  negative values supported
     */
    private float quoteSignTopPadding = 0;

    /**
     *  background color for quotes
     */
    private  int quoteBackgroundColor = 0;

    /**
     * a bitmap to be displayed on the top left corner of a quote
     */
    private Drawable quoteSign;

    /**
     * text color for the quote
     */
    private Integer textQuoteColor;

    /**
     *  text size for the quote
     */
    private float textQuoteFontSize;

    /**
     *  font for the quote text
     */
    private Typeface textQuoteTypeFace;


    /**
     * text color for the quote
     */
    private Integer textHeaderColor;


    private float spacingMult = 1.0f;

    private float spacingAdd = 0.0f;

    private Context applicationContext;


    public Appearance(Context context){

        applicationContext = context.getApplicationContext();
        textTypeFace = null;
        linkTypeFace = null;
        textQuoteTypeFace = null;

        textColor = null;
        linkColor = null;
        textQuoteColor = null;

        quoteBackgroundColor = Color.TRANSPARENT;

        final Resources res = applicationContext.getResources();

        quoteSign = res.getDrawable(R.drawable.quote);

        quoteSignLeftPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5,
                res.getDisplayMetrics());

        quoteSignRightPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5,
                res.getDisplayMetrics());

        quoteSignTopPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                -5,
                res.getDisplayMetrics());


        textFontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                15,
                res.getDisplayMetrics());

        textQuoteFontSize = linkFontSize = textFontSize;

    }

    public void setTextTypeFace(Typeface textTypeFace){
        this.textTypeFace = textTypeFace;
    }

    public void setTextFontSize(float size){
        textFontSize = size;
    }

    public float getTextFontSize(){
        return textFontSize;
    }

    public void setTextColor(int color){
        this.textColor = color;
    }

    public void setLinkColor(int linkColor){
        this.linkColor = linkColor;
    }

    public void setLinkFontSize(int size){
        linkFontSize = size;
    }


    public final TextPaint textPaint(TextPaint input){

        TextPaint textPaint = (input == null) ? defaultTextPaint() : input;

        if(textTypeFace != null){
            textPaint.setTypeface(textTypeFace);
        }

        if(textFontSize > 0){
            textPaint.setTextSize(textFontSize);
        }

        if(textColor != null) {
            textPaint.setColor(textColor);
        }

        if(linkColor != null) {
            textPaint.linkColor = linkColor;
        }


        return textPaint;
    }

    public final TextPaint linkTextPaint(TextPaint input){

        TextPaint textPaint = textPaint(input);

        if(linkTypeFace != null){
            textPaint.setTypeface(linkTypeFace);
        }

        if(linkFontSize > 0){
            textPaint.setTextSize(linkFontSize);
        }

        if(linkColor != null) {
            textPaint.setColor(linkColor);
        }

        return textPaint;
    }


    public int getQuoteBackgroundColor() {
        return quoteBackgroundColor;
    }

    public void setQuoteBackgroundColor(int color){
        quoteBackgroundColor = color;
    }

    public Drawable getQuoteSign(){
        return quoteSign;
    }

    public void setQuoteSign(Drawable quoteSign){
         this.quoteSign = quoteSign;
    }

    public float getQuoteSignLeftPadding() {
        return quoteSignLeftPadding;
    }

    public void setQuoteSignLeftPadding(float quoteSignLeftPadding) {
        this.quoteSignLeftPadding = quoteSignLeftPadding;
    }

    public float getQuoteSignRightPadding() {
        return quoteSignRightPadding;
    }

    public void setQuoteSignRightPadding(float quoteSignRightPadding) {
        this.quoteSignRightPadding = quoteSignRightPadding;
    }

    public float getQuoteSignTopPadding() {
        return quoteSignTopPadding;
    }

    public void setQuoteSignTopPadding(float quoteSignTopPadding) {
        this.quoteSignTopPadding = quoteSignTopPadding;
    }

    private TextPaint defaultTextPaint(){

        final Resources res = applicationContext.getResources();
        TextPaint defaultTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        defaultTextPaint.linkColor = Color.BLUE;
        defaultTextPaint.density = res.getDisplayMetrics().density;
        defaultTextPaint.setTextSize(this.textFontSize);

        return defaultTextPaint;
    }

    public float getTextQuoteFontSize() {
        return textQuoteFontSize;
    }

    public void setTextQuoteFontSize(float textQuoteFontSize) {
        this.textQuoteFontSize = textQuoteFontSize;
    }

    public Typeface getTextQuoteTypeFace() {
        return textQuoteTypeFace;
    }

    public void setTextQuoteTypeFace(Typeface textQuoteTypeFace) {
        this.textQuoteTypeFace = textQuoteTypeFace;
    }

    public int getTextQuoteColor() {
        return textQuoteColor;
    }

    public void setTextQuoteColor(int textQuoteColor) {
        this.textQuoteColor = textQuoteColor;
    }

    public TextPaint getQuoteTextPaint() {

        TextPaint quoteTextPaint = textPaint(null);

        if(textQuoteTypeFace != null){
            quoteTextPaint.setTypeface(textQuoteTypeFace);
        }

        if(textQuoteFontSize > 0){
            quoteTextPaint.setTextSize(textQuoteFontSize);
        }

        if(textQuoteColor != null) {
            quoteTextPaint.setColor(textQuoteColor);
        }

        if(linkColor != null) {
            quoteTextPaint.linkColor = linkColor;
        }

        return quoteTextPaint;
    }

    public float getSpacingMult(){
        return spacingMult;
    }

    public void setSpacingMult(float spacingMult){
        this.spacingMult = spacingMult;
    }

    public float getLineSpacingAdd(){
        return spacingAdd;
    }

    public void setLineSpacingAdd(float spacingAdd){
        this.spacingAdd = spacingAdd;
    }



    public Integer getTextHeaderColor() {
        return textHeaderColor;
    }

    public void setTextHeaderColor(Integer textHeaderColor) {
        this.textHeaderColor = textHeaderColor;
    }

    private static final float[] HEADER_SIZES = {
            1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1f,
    };

    public TextPaint textPaintForHeader(final int header){

       final TextPaint textPaint = textPaint(null);

        if(header >= 0 &&  header <= 6) {
            float mx = HEADER_SIZES[header];
            textPaint.setTypeface(Typeface.create(textPaint.getTypeface(), Typeface.BOLD));
            textPaint.setTextSize(textPaint.getTextSize() * mx);
        }

        if(textHeaderColor != null){
            textPaint.setColor(textHeaderColor);
        }

        if(linkColor != null) {
            textPaint.linkColor = linkColor;
        }

        return textPaint;
    }
}
