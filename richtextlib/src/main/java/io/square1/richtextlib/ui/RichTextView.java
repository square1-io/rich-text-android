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

package io.square1.richtextlib.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;

import android.graphics.Movie;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Surface;
import android.widget.TextView;

import io.square1.richtextlib.R;


/**
 * Created by roberto on 24/06/15.
 */
@Deprecated
public class RichTextView extends TextView  {



    public RichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseCustomAttributes(context, attrs);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseCustomAttributes(context, attrs);
    }


    @TargetApi(21)
    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseCustomAttributes(context, attrs);
    }


    public RichTextView(Context context) {
        super(context);
    }


    private void parseCustomAttributes(Context ctx, AttributeSet attrs) {
       // TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.io_square1_richtextlib_ui_RichTextView);
       // String customFont = a.getString(R.styleable.io_square1_richtextlib_ui_RichTextView_fontName);
       // setCustomFont(ctx, customFont);
       // a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {

        if(TextUtils.isEmpty(asset)){
            return false;
        }

        try {

            Typeface tf  = Typeface.createFromAsset(ctx.getAssets(), asset);
            setTypeface(tf);

        } catch (Exception e) {

            return false;

        }

        return true;
    }

}