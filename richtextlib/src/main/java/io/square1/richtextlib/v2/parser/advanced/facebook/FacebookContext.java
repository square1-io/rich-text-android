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

import io.square1.richtextlib.spans.Style;
import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.parser.handlers.AppendContentHandler;
import io.square1.richtextlib.v2.parser.handlers.IgnoreContentHandler;

/**
 * Created by roberto on 17/12/2015.
 */
public class FacebookContext extends MarkupContext {

    public enum FBPostType {
        EUnknown,
        EPost,
        EVideo
    }


    private String mFacebookPostUrl;
    private FBPostType mType;

    public FacebookContext(RichTextV2 richText, Style style) {
        super();
        mType = FBPostType.EUnknown;
        setRichText(richText);
        setStyle(style);
    }

    public void setType(FBPostType type){
        mType = type;
    }

    public FBPostType getType(){
        return mType;
    }


    public void setFacebookPostUrl(String postUrl){
        mFacebookPostUrl = postUrl;
    }

    public String getFacebookPostUrl(){
        return mFacebookPostUrl;
    }

//    public RichTextDocumentElement replaceBuilder(RichTextDocumentElement in){
//        if(mInternalContent == null){
//            mInternalContent = new RichTextDocumentElement();
//        }
//        return mInternalContent;
//    }


    @Override
    public TagHandler getTagHandler(MarkupTag tag) {

        TagHandler handler = tag.getTagHandler();

        if(handler == null) {
            if("a".equalsIgnoreCase(tag.tag) == false) {
                handler = new AppendContentHandler();
                handler.replaceContext(this);
                tag.setTagHandler(handler);
            }else {
                return super.getTagHandler(tag);
            }
        }

        return handler;
    }

}
