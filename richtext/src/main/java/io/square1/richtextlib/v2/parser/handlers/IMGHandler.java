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

package io.square1.richtextlib.v2.parser.handlers;

import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.util.DisplayMetrics;

import org.xml.sax.Attributes;

import io.square1.richtextlib.v2.content.ImageDocumentElement;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.spans.UrlBitmapSpan;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 04/09/15.
 */
public class IMGHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        Attributes attributes = tag.attributes;
        String src = attributes.getValue("", "src");

        // translate those values based on screen density
        Context applicationContext = context.getRichText().getCurrentStyle().getApplicationContext();

        DisplayMetrics displayMetrics = applicationContext.getResources().getDisplayMetrics();

        int w = NumberUtils.parseAttributeDimension(attributes.getValue("width"),0);
        int h = NumberUtils.parseAttributeDimension(attributes.getValue("height"),0);

        if(w != NumberUtils.INVALID) {
            w = (int)(w * displayMetrics.density);
        }

        if(h != NumberUtils.INVALID) {
            h = (int)(h * displayMetrics.density);
        }

        if(context.getStyle().extractImages() == true){
            SpannedBuilderUtils.trimTrailNewlines(out, 0);
            context.getRichText().splitDocument(ImageDocumentElement.newInstance(src,null,w,h));
            return;
        }


        SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 1);
        UrlBitmapSpan imageDrawable = new UrlBitmapSpan(Uri.parse(src), w, h, context.getStyle().maxImageWidth() );

        int len = out.length();
        out.append(SpannedBuilderUtils.NO_SPACE);
        out.setSpan(imageDrawable,
                len,
                out.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 1);

    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

    }


    @Override
    public boolean closeWhenSplitting(){
        return false;
    }


    @Override
    public boolean openWhenSplitting(){
        return false;
    }
}
