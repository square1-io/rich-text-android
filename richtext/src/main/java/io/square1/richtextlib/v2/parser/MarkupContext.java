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

package io.square1.richtextlib.v2.parser;

import java.util.HashMap;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.spans.Style;
import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.parser.handlers.DefaultHandler;
import io.square1.richtextlib.v2.parser.handlers.SCRIPTHandler;

/**
 * Created by roberto on 19/08/15.
 */
public class MarkupContext {

    private HashMap<String,Object> mValues = new HashMap<>();

    private HashMap<String,Class<? extends TagHandler>> mHandlers;
    private String mHandlersPackage;
    private Style mStyle;
    private RichTextV2 mRichTextV2;


    public MarkupContext(){
        mHandlersPackage = DefaultHandler.class.getPackage().getName();
        mHandlers = new HashMap<>();
    }

    public final  void setRichText(RichTextV2 richText){
        mRichTextV2 = richText;
    }


    public final RichTextV2 getRichText(){
        return mRichTextV2;
    }

    public void setStyle(Style style){
        mStyle = style;
    }

    public Style getStyle(){
        return mStyle;
    }

    public TagHandler getTagHandler(MarkupTag tag){

        Class<? extends TagHandler> tagHandlerClass = mHandlers.get(tag.tag);

        if(tagHandlerClass == null){

            String handlerClassName = mHandlersPackage + "." + tag.tag.toUpperCase() + "Handler";
            try {
                tagHandlerClass = (Class<? extends TagHandler>) Class.forName(handlerClassName);

                if(tagHandlerClass != null) {
                    mHandlers.put(tag.tag,tagHandlerClass);
                }

            }catch (Exception e){

            }
        }

        try {

            TagHandler handler =  tagHandlerClass.newInstance();
            handler.replaceContext(this);
            return handler;

        }catch (Exception e){
            Exception ex = e;
            TagHandler defaultHandler = new DefaultHandler();
            tag.setTagHandler(defaultHandler);
            return defaultHandler;
        }

    }

    public final TagHandler tagHandlerInstance(MarkupTag tag){

        TagHandler handler = getTagHandler(tag);
        tag.setTagHandler(handler);

        return handler;
    }

    public final TagHandler parentTagHandlerInstance(MarkupTag tag){

        MarkupTag parentTag = tag.getParent();
        if(parentTag != null){
            return parentTag.getTagHandler();
        }
        return null;
    }

    public boolean tagAllowedByParent(MarkupTag current){
        TagHandler parentHandler = parentTagHandlerInstance(current);
        return parentHandler != null ? parentHandler.childAllowed(current) : true;
    }

    public final MarkupContext onTagOpen(MarkupTag current, RichTextDocumentElement builder, boolean newOutput) {

        if(current.ignoreTag()){
            return this;
        }

        builder = replaceBuilder(builder);
        TagHandler handler = tagHandlerInstance(current);
        // the parent tag might decide that we don't need to process this child.
        // this case apply for example in <audio> that contain a message for the user when
        // the browser doesn't support the audio and we don't want to show the
        // message ( https://www.w3.org/wiki/HTML/Elements/audio#Examples )
        boolean allowedByParent = tagAllowedByParent(current);
        if( ((newOutput && handler.openWhenSplitting()) || !newOutput ) && allowedByParent ) {
            handler.onTagOpen(this, current, builder);
            return handler.replaceContext(this);
        }

        return this;
    }

    public final MarkupContext onTagOpenAfterSplit(MarkupTag current, RichTextDocumentElement builder, boolean newOutput) {

        if(current.ignoreTag()){
            return this;
        }

        builder = replaceBuilder(builder);
        TagHandler handler = tagHandlerInstance(current);
        if( (newOutput && handler.openWhenSplitting()) || !newOutput ) {
            handler.onTagOpenAfterSplit(this, current, builder);
            return handler.replaceContext(this);
        }

        return this;
    }

    public final MarkupContext onTagClose(MarkupTag tag, RichTextDocumentElement builder, boolean newOutput) {

        if(tag.ignoreTag()){
            return this;
        }

        builder = replaceBuilder(builder);
        TagHandler handler = tag.getTagHandler();
        //getTagHandler(tag);
        boolean allowedByParent = tagAllowedByParent(tag);
        if( ((newOutput && handler.closeWhenSplitting()) || !newOutput ) && allowedByParent ) {
            handler.onTagClose(this, tag, builder);
            return handler.getInitialContext();
        }

        return this;
    }

    public RichTextDocumentElement replaceBuilder(RichTextDocumentElement in){
        return in;
    }

    public void setValue(String key, Object value){
        if(value == null){
            mValues.remove(key);
        }else {
            mValues.put(key, value);
        }
    }

    public Object getValue(String key){
        return mValues.get(key);
    }

    public MarkupTag getParent(MarkupTag child, Class<? extends TagHandler> parentClass) {
        return mRichTextV2.getParent(child, parentClass);
    }
}
