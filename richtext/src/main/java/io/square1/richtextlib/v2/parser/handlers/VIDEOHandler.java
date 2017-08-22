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

import org.xml.sax.Attributes;

import io.square1.richtextlib.spans.VideoPlayerSpan;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.content.VideoDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 04/09/15.
 */
public class VIDEOHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        Attributes attributes = tag.attributes;
        String src = attributes.getValue("", "src");

        if(context.getStyle().extractVideos() == true){

            SpannedBuilderUtils.trimTrailNewlines(out, 0);
            int w =  NumberUtils.parseAttributeDimension(attributes.getValue("width"),0);
            int h =  NumberUtils.parseAttributeDimension(attributes.getValue("height"),0);
            context.getRichText().splitDocument(VideoDocumentElement.newInstance(src,w,h));
            return;
        }


        if(out.length() > 0) {
            SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 1);
        }


        VideoPlayerSpan videoPlayerSpan = new VideoPlayerSpan(src,
                NumberUtils.parseAttributeDimension(attributes.getValue("width"), NumberUtils.INVALID),
                NumberUtils.parseAttributeDimension(attributes.getValue("height"),NumberUtils.INVALID),
                context.getStyle().maxImageWidth() );

      //  URLSpan videoPlayerSpan = new URLSpan(src);

        int len = out.length();
       // out.append(SpannedBuilderUtils.NO_SPACE);
        out.append(src);

        out.setSpan(videoPlayerSpan,
                len,
                out.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

    }

    public boolean processContent() {
        return false;
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
