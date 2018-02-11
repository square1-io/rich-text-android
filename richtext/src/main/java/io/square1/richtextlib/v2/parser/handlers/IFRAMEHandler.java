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

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.URLUtil;

import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.spans.YouTubeSpan;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.util.WebAddress;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;


/**
 * Created by roberto on 04/09/15.
 */
public class IFRAMEHandler extends TagHandler  {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, final RichTextDocumentElement out) {

        final String href = tag.attributes.getValue("", "src");

        SpannedBuilderUtils.trimTrailNewlines(out, 0);
        boolean embedFound = false;
        if(context.getStyle().extractEmbeds()  == true){

            embedFound = EmbedUtils.parseLink(context, href, new EmbedUtils.ParseLinkCallback() {

                @Override
                public void onLinkParsed(Object callingObject, String result, EmbedUtils.TEmbedType type) {
                    MarkupContext context = (MarkupContext) callingObject;
                    if (type == EmbedUtils.TEmbedType.EYoutube) {
                        SpannedBuilderUtils.makeYoutube(result, context.getStyle().maxImageWidth(), out);
                        return;
                    }
                    //remove new lines here as we are splitting content
                    context.getRichText().onEmbedFound(type, result);
                }
            });
        }

        if(embedFound == false){

            if(context.getStyle().extractEmbeds() == true) {
                // we use the webview cell
                WebAddress webAddress = WebAddress.parseWebAddress(href);
                if (webAddress != null) {
                    //ensure we have a scheme here
                    if (TextUtils.isEmpty(webAddress.getScheme())) {
                        webAddress.setScheme("http");
                    }
                    int w = NumberUtils.parseAttributeDimension(tag.attributes.getValue("width"), 16);
                    int h = NumberUtils.parseAttributeDimension(tag.attributes.getValue("height"), 9);
                    context.getRichText().onIframeFound(webAddress.toString(), w, h);
                }
            }else {
                String youtubeId = EmbedUtils.getYoutubeVideoId(href);
                if(TextUtils.isEmpty(youtubeId) == true) {
                    SpannedBuilderUtils.makeUnsupported(href, null, out);
                }else {
                    int w = NumberUtils.parseAttributeDimension(tag.attributes.getValue("width"), YouTubeSpan.DEFAULT_WIDTH);
                    int h = NumberUtils.parseAttributeDimension(tag.attributes.getValue("height"), YouTubeSpan.DEFAULT_HEIGHT);
                    SpannedBuilderUtils.makeYoutube(youtubeId, w, h, context.getStyle().maxImageWidth(), out);
                }
            }
        }

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
