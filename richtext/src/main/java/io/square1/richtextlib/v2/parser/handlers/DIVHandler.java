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
import android.net.Uri;
import android.text.Spannable;
import android.text.TextUtils;

import org.xml.sax.Attributes;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.spans.StyleSpan;
import io.square1.richtextlib.spans.UnsupportedContentSpan;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;

/**
 * Created by roberto on 04/09/15.
 */
public class DIVHandler extends TagHandler {

    private boolean mProcessContent = true;

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        Attributes attributes = tag.attributes;
        String elementClass = attributes.getValue("", "class");

        if("pb_feed".equalsIgnoreCase(elementClass)){
            mProcessContent = false;
            String dataGame  = attributes.getValue("", "data-game");

            if(TextUtils.isEmpty(dataGame) == true) return;

            int where = out.length();
            String message = " TAKE  THE  QUIZ HERE!" ;

            out.append(message);
            String url = Uri.parse("http://www.playbuzz.com")
                    .buildUpon()
                    .encodedPath(dataGame)
                    .build()
                    .toString();


            out.setSpan( new StyleSpan(Typeface.BOLD), where, where + message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            UnsupportedContentSpan span = new UnsupportedContentSpan(url);
            out.setSpan(span, where,where + message.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        else if("fb-video".equalsIgnoreCase(elementClass)){
            mProcessContent = false;
            String url = attributes.getValue("","data-href");
            if(TextUtils.isEmpty(url) == false){



                int where = out.length();
                String message = " See Facebook Video Here " ;
                out.append(message);
                UnsupportedContentSpan span = new UnsupportedContentSpan(url);
                out.setSpan(span, where,where + message.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

    }

    public boolean processContent() {
        return mProcessContent;
    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

    }
}
