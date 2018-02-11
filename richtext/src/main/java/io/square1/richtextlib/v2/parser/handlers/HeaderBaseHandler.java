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

import android.graphics.Typeface;
import android.text.Spannable;

import io.square1.richtextlib.spans.HeaderSpan;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.spans.ForegroundColorSpan;
import io.square1.richtextlib.spans.RelativeSizeSpan;
import io.square1.richtextlib.spans.StyleSpan;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 04/09/15.
 */
public abstract class HeaderBaseHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        String tagName = tag.tag;
        //don't add new lines if we are at the top of the document
        if(out.length() > 0) {
            SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 2);
        }
        char value =  tagName.charAt(1);
        SpannedBuilderUtils.startSpan(out, new Markers.Header( value - '1'));

    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        int len = out.length();
        Markers.Header header = out.getLastSpan(Markers.Header.class);

        int where = out.getSpanStart(header);
        out.removeSpan(header);

        // Back off not to change only the text, not the blank line.
        while (len > where && out.charAt(len - 1) == '\n') {
            len--;
        }

        if (where != len) {

            out.setSpan(new HeaderSpan(header.level),
                    where,
                    len,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//            int color = context.getStyle().headerColor();
//
//            out.setSpan(new ForegroundColorSpan(color),
//                    where,
//                    len,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            out.setSpan(new RelativeSizeSpan(context.getStyle().headerIncrease(header.level)),
//                    where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            out.setSpan(new StyleSpan(Typeface.BOLD),
//                    where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 2);
        }

    }
}
