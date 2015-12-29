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

package io.square1.richtextlib.v2.parser.advanced.twitter;

import io.square1.richtextlib.spans.Style;
import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.parser.handlers.IgnoreContentHandler;

class TwitterContext extends MarkupContext {

    private String mTweetId;

    public void setTweetId(String tweetId){
        mTweetId = tweetId;
    }

    public TwitterContext(RichTextV2 richText, Style style) {
        super();
        setRichText(richText);
        setStyle(style);
    }

    public String getTweetId(){
        return mTweetId;
    }

    @Override
    public TagHandler getTagHandler(MarkupTag tag) {

        TagHandler handler = tag.getTagHandler();

        if(handler == null) {

            if ("a".equalsIgnoreCase(tag.tag) == true) {
                handler = new TwitterLinkTagHandler();
            } else {
                handler = new IgnoreContentHandler();
            }

            handler.replaceContext(this);
            tag.setTagHandler(handler);
        }

        return handler;
    }
}
