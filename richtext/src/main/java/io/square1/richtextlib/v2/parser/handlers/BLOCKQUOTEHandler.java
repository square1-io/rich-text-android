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

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.spans.QuoteSpan;
import io.square1.richtextlib.spans.Style;
import io.square1.richtextlib.spans.StyleSpan;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 04/09/15.
 */
public class BLOCKQUOTEHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 2);
        SpannedBuilderUtils.startSpan(out, new Markers.Blockquote(tag.elementClasses));
       // handleP(spannable);
    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        //TODO handle Tweets
        Markers.Blockquote obj = out.getLastSpan(Markers.Blockquote.class);
        if(obj == null) return;

        SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 2);

        int len = out.length();
        int where = out.getSpanStart(obj);
        out.removeSpan(obj);

            if (where != len) {
                StyleSpan styleSpan = new StyleSpan(Typeface.ITALIC);
                out.setSpan(styleSpan, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                QuoteSpan quoteSpan = new QuoteSpan();
                out.setSpan(quoteSpan, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
    }
}
