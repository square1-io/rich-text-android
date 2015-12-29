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

import android.text.Spannable;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.spans.URLSpan;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;

/**
 * Created by roberto on 04/09/15.
 */
public class AHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        String href = tag.attributes.getValue("", "href");
        int len = out.length();
        Markers.Href h = new Markers.Href(href);
        out.setSpan(h, len, len, Spannable.SPAN_MARK_MARK);
    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        int len = out.length();
        Object obj = out.getLastSpan(Markers.Href.class);
        int where = out.getSpanStart(obj);
        out.removeSpan(obj);

        if (where != len) {

            if(obj == null){
                return;
            }

            Markers.Href h = (Markers.Href) obj;

            if (h.mHref != null) {
//TODO if in noscript tag we should update the context and supply a different class for href
              //  if(isWithinTag( stack, "noscript") != null){
              //      out.setSpan(new UnsupportedContentSpan(h.mHref), where, len,
              //              Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
              //  }else {
                    out.setSpan(new URLSpan(h.mHref), where, len,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
             //   }
            }
        }

    }
}
