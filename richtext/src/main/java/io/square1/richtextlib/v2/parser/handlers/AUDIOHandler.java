/*
 * Copyright (c) 2017. Roberto  Prato <https://github.com/robertoprato>
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
import android.text.TextUtils;

import org.xml.sax.Attributes;

import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.spans.StyleSpan;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 13/09/2017.
 */

public class AUDIOHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        Markers.Audio audioMarker =  new Markers.Audio();
        Attributes attributes = tag.attributes;
        audioMarker.src = attributes.getValue("", "src");
        audioMarker.type = attributes.getValue("", "type");

        SpannedBuilderUtils.startSpan(out, audioMarker);
    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        Markers.MarkerWithSource source = out.getLastSpan(Markers.MarkerWithSource.class);
        if(source != null &&
                TextUtils.isEmpty(source.src) == false){

            SpannedBuilderUtils.trimTrailNewlines(out, 0);

            if(context.getStyle().extractEmbeds()  == true) {
                context.getRichText().onEmbedFound(EmbedUtils.TEmbedType.EAudio, source.src);
            }else {
                SpannedBuilderUtils.makeUnsupported(source.src, source.src, out);
            }
        }
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

    public boolean childAllowed(MarkupTag childTag){
        return "source".equalsIgnoreCase(childTag.tag);
    }
}
