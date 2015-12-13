package io.square1.richtextlib.v2.parser;


import io.square1.richtextlib.v2.content.RichTextDocumentElement;

/**
 * Created by roberto on 19/08/15.
 */
public abstract class TagHandler {

    private MarkupContext mInitialContext;



    public TagHandler(){

    }

    /**
     * when splitting the string because of an embed was found
     * we have to make sure we are not leaving certain spans open. see onEmbedFound in RichTextV2
     * @return true is the span has to be closed
     */
    public boolean closeWhenSplitting(){
        return true;
    }

    /**
     * when splitting the string because of an embed was found
     * we have to make sure we apply  certain formatting spans on the new string. see onEmbedFound in RichTextV2
     * @return true is the span has to be closed
     */

    public boolean openWhenSplitting(){
        return true;
    }


    public abstract void onTagOpen(MarkupContext context, MarkupTag tag , RichTextDocumentElement out);
    public abstract void onTagClose(MarkupContext context, MarkupTag tag , RichTextDocumentElement out);

    public final MarkupContext replaceContext(MarkupContext context){
        mInitialContext = context;
        return getReplacementContext();
    }

    //return the initial context this handler was created in
    public  MarkupContext getInitialContext(){
        return mInitialContext;
    }


    public  MarkupContext getReplacementContext(){
        return mInitialContext;
    }


    public boolean processContent() {
        return true;
    }
}
