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
    private int textColor;


    /**
     * text color for links
     */
    private int linkColor;

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
     *
     */
    /**
     *  background color for quotes
     */
    private  int quoteBackgroundColor = 0;

    /**
     * a bitmap to be displayed on the top left corner of a quote
     */
    private Bitmap quoteSign;

    private Context applicationContext;


    public Appearance(Context context){

        applicationContext = context.getApplicationContext();
        textTypeFace = null;
        linkTypeFace = null;

        textColor = -1;
        linkColor = -1;

        quoteBackgroundColor = Color.TRANSPARENT;

        final Resources res = applicationContext.getResources();

        quoteSign = BitmapFactory.decodeResource(res, R.drawable.quote);

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

        linkFontSize = textFontSize;

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

        if(textColor > 0) {
            textPaint.setColor(textColor);
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

        if(linkColor > 0) {
            textPaint.setColor(linkColor);
        }

        return textPaint;
    }


    public int getQuoteBackgroundColor() {
        return Color.RED;//quoteBackgroundColor;
    }

    public void setQuoteBackgroundColor(int color){
        quoteBackgroundColor = color;
    }

    public Bitmap getQuoteSign(){
        return quoteSign;
    }

    public void setQuoteSign(Bitmap quoteSign){
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

}
