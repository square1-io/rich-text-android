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


import java.util.TreeSet;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;

/**
 * Created by roberto on 19/08/15.
 */
public abstract class TagHandler {

    private MarkupContext mInitialContext;

    /**
     * somethimes if we are inside a particular tag we want to ensure only the
     * content of certain other tags is taken into account
     * for examlpe an audio tag containing source and an <a></a> tag. in this case we want to ingnore the a tags.
     */
    private TreeSet<String> mAllowedTags;

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

    public  void onTagOpenAfterSplit(MarkupContext context, MarkupTag tag , RichTextDocumentElement out) {
        onTagOpen(context, tag, out);
    }

    public  void onTagCloseAfterSplit(MarkupContext context, MarkupTag tag , RichTextDocumentElement out) {
        onTagClose(context, tag, out);
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



    public boolean childAllowed(MarkupTag childTag){
        return true;
    }
}
