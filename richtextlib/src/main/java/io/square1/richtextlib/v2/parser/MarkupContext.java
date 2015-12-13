package io.square1.richtextlib.v2.parser;

import java.util.HashMap;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.spans.Style;
import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.parser.handlers.DefaultHandler;

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

            }catch (Exception e){}
        }

        try {

            TagHandler handler =  tagHandlerClass.newInstance();
            handler.replaceContext(this);
            tag.setTagHandler(handler);
            return handler;

        }catch (Exception e){
            Exception ex = e;
            TagHandler defaultHandler = new DefaultHandler();
            tag.setTagHandler(defaultHandler);
            return defaultHandler;
        }

    }

    public TagHandler tagHandlerInstance(MarkupTag tag){

        if(tag.getTagHandler() == null){

        }

        return tag.getTagHandler();
    }

    public final MarkupContext onTagOpen(MarkupTag current, RichTextDocumentElement builder, boolean newOutput) {
        TagHandler handler = getTagHandler(current);
        if( (newOutput && handler.openWhenSplitting()) || !newOutput ) {
            handler.onTagOpen(this, current, builder);
            return handler.replaceContext(this);
        }

        return this;
    }

    public final MarkupContext onTagClose(MarkupTag tag, RichTextDocumentElement builder, boolean newOutput) {
        TagHandler handler = tag.getTagHandler();
        //getTagHandler(tag);
        if( (newOutput && handler.closeWhenSplitting()) || !newOutput ) {
            handler.onTagClose(this, tag, builder);
            return handler.getInitialContext();
        }

        return this;
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
