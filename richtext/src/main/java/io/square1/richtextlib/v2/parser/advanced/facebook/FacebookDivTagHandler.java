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

package io.square1.richtextlib.v2.parser.advanced.facebook;

import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextUtils;

import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.spans.QuoteSpan;
import io.square1.richtextlib.spans.RichAlignmentSpan;
import io.square1.richtextlib.spans.StrikethroughSpan;
import io.square1.richtextlib.spans.Style;
import io.square1.richtextlib.spans.StyleSpan;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.parser.advanced.AdvancedMarkupContext;
import io.square1.richtextlib.v2.parser.handlers.Markers;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;


public class FacebookDivTagHandler extends TagHandler {

    private FacebookContext mFacebookContext;

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {


            Style style = mFacebookContext.getStyle();
            SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 2);
            QuoteSpan quoteSpan = new QuoteSpan();
            SpannedBuilderUtils.endSpan(out, Markers.Blockquote.class, quoteSpan);

        if (TextUtils.isEmpty(mFacebookContext.getFacebookPostUrl()) == false) {

            String message = "full post on Facebook here";
            if(mFacebookContext.getType() == FacebookContext.FBPostType.EVideo){
                message = "see video on Facebook here";
            }

            SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 2);
            SpannedBuilderUtils.makeUnsupported(mFacebookContext.getFacebookPostUrl(), message, out);

        }
    }


    @Override
    public MarkupContext getReplacementContext() {

        if (mFacebookContext == null) {

            mFacebookContext = new FacebookContext(getInitialContext().getRichText(),
                    getInitialContext().getStyle());
        }
        return mFacebookContext;
    }

    public MarkupContext getInitialContext() {
        return super.getInitialContext();
    }

    @Override
    public boolean closeWhenSplitting() {
        return false;
    }

    @Override
    public boolean openWhenSplitting() {
        return false;
    }

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        //data-href="https://www.facebook.com/elizabeth.holland.1/posts/921366354625100"

        final String elementClass = tag.getElementClass();

        if(AdvancedMarkupContext.DIV_CLASS_FACEBOOK_VIDEO
                .equalsIgnoreCase(elementClass)){
            mFacebookContext.setType(FacebookContext.FBPostType.EVideo);
        }
        else if(AdvancedMarkupContext.DIV_CLASS_FACEBOOK_POST
                .equalsIgnoreCase(elementClass)){
            mFacebookContext.setType(FacebookContext.FBPostType.EPost);
        }

        String postHref = tag.attributes.getValue("data-href");
        mFacebookContext.setFacebookPostUrl(postHref);

        //SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 2);
        SpannedBuilderUtils.startSpan(out, new Markers.Blockquote(null));
    }

    @Override
    public boolean processContent() {
        return false;
    }
}