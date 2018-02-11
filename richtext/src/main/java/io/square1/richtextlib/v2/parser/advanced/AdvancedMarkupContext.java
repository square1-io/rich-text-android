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

package io.square1.richtextlib.v2.parser.advanced;


import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.parser.advanced.facebook.FacebookDivTagHandler;
import io.square1.richtextlib.v2.parser.advanced.instagram.InstagramQuoteTagHandler;
import io.square1.richtextlib.v2.parser.advanced.twitter.TwitterQuoteTagHandler;


/**
 * Created by roberto on 13/12/2015.
 *
 * Etends the basic context and performs extracting of
 * tweets and instagrams tags from embedded quotes tags
 */
public class AdvancedMarkupContext extends MarkupContext {


    public static final String BLOCKQUOTE_CLASS_TWEET = "twitter-tweet";
    public static final String BLOCKQUOTE_CLASS_INSTAGRAM = "instagram-media";

    public static final String DIV_CLASS_FACEBOOK_POST  = "fb-post";
    public static final String DIV_CLASS_FACEBOOK_VIDEO  = "fb-video";



    public AdvancedMarkupContext() {
        super();

    }


    @Override
    public TagHandler getTagHandler(MarkupTag tag) {

        String elementClass = tag.getElementClass();

        if("div".equalsIgnoreCase(tag.tag)){

            if(DIV_CLASS_FACEBOOK_POST.equalsIgnoreCase(elementClass) ||
                    DIV_CLASS_FACEBOOK_VIDEO.equalsIgnoreCase(elementClass)){

                FacebookDivTagHandler handler = new FacebookDivTagHandler();
                handler.replaceContext(this);
                tag.setTagHandler(handler);
                return handler;
            }
        }
        else if("blockquote".equalsIgnoreCase(tag.tag)){

            if(BLOCKQUOTE_CLASS_TWEET.equalsIgnoreCase(elementClass) == true){
                TwitterQuoteTagHandler handler = new TwitterQuoteTagHandler();
                handler.replaceContext(this);
                tag.setTagHandler(handler);
                return handler;
            }
            else if(BLOCKQUOTE_CLASS_INSTAGRAM.equalsIgnoreCase(elementClass) == true){
                InstagramQuoteTagHandler handler = new InstagramQuoteTagHandler();
                handler.replaceContext(this);
                tag.setTagHandler(handler);
                return handler;
            }
        }

        return super.getTagHandler(tag);
    }

}
