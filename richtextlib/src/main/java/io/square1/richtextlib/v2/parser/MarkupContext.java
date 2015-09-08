package io.square1.richtextlib.v2.parser;

import java.util.HashMap;
import java.util.Stack;

import io.square1.richtextlib.ParcelableSpannedBuilder;
import io.square1.richtextlib.style.Style;
import io.square1.richtextlib.v2.parser.handlers.BRHandler;
import io.square1.richtextlib.v2.parser.handlers.DefaultHandler;
import io.square1.richtextlib.v2.parser.handlers.PHandler;

/**
 * Created by roberto on 19/08/15.
 */
public class MarkupContext {




    private HashMap<String,Class<? extends TagHandler>> mHandlers;
    private String mHandlersPackage;
    private MarkupTag mLastClosedTag;
    private Style mStyle;

    public MarkupContext(Style style){
        mStyle = style;
        mHandlersPackage = DefaultHandler.class.getPackage().getName();
        mHandlers = new HashMap<>();
       // mHandlers.put("p", PHandler.class);
       // mHandlers.put("br", BRHandler.class);

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
            return handler;

        }catch (Exception e){
            Exception ex = e;
            return new DefaultHandler();
        }

    }


    public void onTagOpen(MarkupTag tag, ParcelableSpannedBuilder builder) {
        TagHandler handler = getTagHandler(tag);
        handler.onTagOpen(this, tag, builder);
    }
    public void onTagClose(MarkupTag tag, ParcelableSpannedBuilder builder) {
        TagHandler handler = getTagHandler(tag);
        handler.onTagClose(this, tag, builder);
        mLastClosedTag = tag;

    }
}
