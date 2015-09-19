package io.square1.richtextlib.v2.parser;

import java.util.HashMap;
import java.util.Stack;

import io.square1.richtextlib.ParcelableSpannedBuilder;
import io.square1.richtextlib.style.Style;
import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.parser.handlers.BRHandler;
import io.square1.richtextlib.v2.parser.handlers.DefaultHandler;
import io.square1.richtextlib.v2.parser.handlers.PHandler;

/**
 * Created by roberto on 19/08/15.
 */
public class MarkupContext {

    private HashMap<String,Object> mValues = new HashMap<>();

    private HashMap<String,Class<? extends TagHandler>> mHandlers;
    private String mHandlersPackage;
    private Style mStyle;
    private RichTextV2 mRichTextV2;

    public MarkupContext(RichTextV2 richText, Style style){
        mStyle = style;
        mRichTextV2 = richText;
        mHandlersPackage = DefaultHandler.class.getPackage().getName();
        mHandlers = new HashMap<>();
    }

    public RichTextV2 getRichText(){
        return mRichTextV2;
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

            }catch (Exception e){}
        }

        try {

            TagHandler handler =  tagHandlerClass.newInstance();
            tag.setTagHandler(handler);
            return handler;

        }catch (Exception e){
            Exception ex = e;
            return new DefaultHandler();
        }

    }

    public void onTagOpen(MarkupTag tag, ParcelableSpannedBuilder builder, boolean newOutput) {
        TagHandler handler = getTagHandler(tag);
        if( (newOutput && handler.openWhenSplitting()) || !newOutput ) {
            handler.onTagOpen(this, tag, builder);
        }
    }

    public void onTagClose(MarkupTag tag, ParcelableSpannedBuilder builder, boolean newOutput) {
        TagHandler handler = getTagHandler(tag);
        if( (newOutput && handler.closeWhenSplitting()) || !newOutput ) {
            handler.onTagClose(this, tag, builder);
        }
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

    public MarkupTag getParent(MarkupTag child) {
        return mRichTextV2.getParent(child);
    }
}
