

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

package io.square1.richtextlib.v2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;


import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.v2.content.WebDocumentElement;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.content.DocumentElement;
import io.square1.richtextlib.v2.content.OembedDocumentElement;
import io.square1.richtextlib.v2.content.RichDocument;
import io.square1.richtextlib.v2.parser.InternalContentHandler;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.spans.*;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * This class processes HTML strings into displayable styled text.
 * Not all HTML tags are supported.
 */
public class RichTextV2 {

    public static final String TAG = "RICHTXT";


    public static class DefaultStyle implements  Style {

        private static final float[] HEADER_SIZES = {
                1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1f,
        };

        private Context mApplicationContext;

        private int mMaxImageWidth;

        public DefaultStyle(Context context){
            mApplicationContext = context.getApplicationContext();
            Resources resources = context.getResources();

            mMaxImageWidth = resources.getDisplayMetrics().widthPixels;
        }

        @Override
        public Context getApplicationContext(){
            return mApplicationContext;
        }


        @Override
        public int headerColor() {
            return Color.BLACK;
        }

        @Override
        public int backgroundColor() {
            return Color.WHITE;
        }

        @Override
        public int maxImageWidth() {
            return mMaxImageWidth;
        }

        @Override
        public int maxImageHeight() {
            return Style.USE_DEFAULT;
        }

        @Override
        public float headerIncrease(int headerLevel) {
            return HEADER_SIZES[headerLevel];
        }

        @Override
        public float smallTextReduce() {
            return 0.8f;
        }

        @Override
        public boolean parseWordPressTags(){
            return true;
        }

        @Override
        public boolean treatAsHtml(){
            return true;
        }

        @Override
        public boolean extractImages(){
            return false;
        }

        @Override
        public boolean extractVideos(){
            return true;
        }

        @Override
        public boolean extractEmbeds(){
            return true;
        }
    }


    private static class HtmlParser {
        private static final HTMLSchema schema = new HTMLSchema();
    }



    private ArrayList<DocumentElement> mResult = new ArrayList<>();
    private Stack<MarkupTag> mStack = new Stack<>();
    private ArrayList<MarkupTag> mHistory = new ArrayList<>();
    private Stack<MarkupContext> mMarkupContextStack = new Stack<>();
    private RichTextDocumentElement mOutput;
    private MarkupContext mCurrentContext;


    private int mOembedCount;


    private RichTextV2(Context context) {
        init();
        setupContext(new MarkupContext());
    }
    private RichTextV2(MarkupContext markupContext) {
        init();
        setupContext(markupContext);
    }

    private void setupContext(MarkupContext markupContext){
        mCurrentContext = markupContext;
        mCurrentContext.setRichText(this);
        mMarkupContextStack.push(mCurrentContext);
    }

    private void init(){
        mOembedCount = 0;
        mOutput = new RichTextDocumentElement();
        mResult = new ArrayList<>();
    }


    public Style getCurrentStyle(){
        return mCurrentContext.getStyle();
    }

    public RichTextDocumentElement getCurrentOutput(){
        return mOutput;
    }


    public static RichTextDocumentElement textFromHtml(Context context, String source) {

        RichDocument document = fromHtmlImpl(context, source, null, new DefaultStyle(context){

            @Override
            public boolean extractVideos(){
                return false;
            }

            @Override
            public boolean extractEmbeds(){
                return false;
            }
        } );
        ArrayList<DocumentElement> elements = document.getElements();

        for(DocumentElement element : elements){
            if(element instanceof RichTextDocumentElement){
                return (RichTextDocumentElement)element;
            }
        }
        return null;

    }


    public static RichDocument fromHtml(Context context, String source) {

       return fromHtmlImpl(context, source, null, null );

    }

    public static  RichDocument  fromHtml(Context context,
                                          String source,
                                          Style style) {

       return fromHtmlImpl(context, source, null, style);

    }

    public static  RichDocument  fromHtml(Context context,
                                          String source,
                                          MarkupContext markupContext,
                                          Style style) {

        return fromHtmlImpl(context, source, markupContext, style);

    }

    final static String SOUND_CLOUD = "\\[soundcloud (.*?)/?\\]";
    final static String SOUND_CLOUD_REPLACEMENT = "<soundcloud $1 />";

    final static String INTERACTION = "\\[interaction (.*?)/?\\]";
    final static String INTERACTION_REPLACEMENT = "";


    private static RichDocument fromHtmlImpl(Context context,
                                                         String source,
                                             MarkupContext markupContext,
                                                         Style style)  {


        if(style == null){
            style = new DefaultStyle(context);
        }


        try {

            XMLReader reader = new Parser();
            reader.setProperty(Parser.schemaProperty, HtmlParser.schema);

            if(style.parseWordPressTags() == true) {


                source = source.replaceAll(SOUND_CLOUD,
                        SOUND_CLOUD_REPLACEMENT);

                source = source.replaceAll(INTERACTION,
                        INTERACTION_REPLACEMENT);

            }



            RichTextV2 richText = markupContext == null ?
                    new RichTextV2(context) :
                    new RichTextV2(markupContext);


            richText.mCurrentContext.setStyle(style);

            reader.setContentHandler(new InternalContentHandler(richText));
            reader.parse(new InputSource(new StringReader(source)));
            richText.appendRemainder();
            RichDocument out = new RichDocument("",richText.mResult);
            return out;

        } catch (Exception e) {
            Log.e("HTML", "error");
            e.printStackTrace();
        }

        return RichDocument.EMPTY;
    }



    public void startElement(String uri, String localName, Attributes atts, String textContent) {

        //a new tag is starting here , this is text from previous tag should we process ?
        MarkupTag parentTag = getParent();

        //MarkupTag last = mStack.peek();
        MarkupTag tag = new MarkupTag(localName,atts);
        if(parentTag != null){
            parentTag.addChild(tag);
        }

        if( parentTag == null || (!parentTag.ignoreTag() && parentTag.getTagHandler().processContent())) {
            processAccumulatedTextContent(textContent);
        }

        mStack.push(tag);
        mCurrentContext = mCurrentContext.onTagOpen(tag, mOutput, false);
        mHistory.add(tag);
        mMarkupContextStack.push( mCurrentContext );
    }

    public void endElement(String uri, String localName, String textContent) {

        MarkupTag tag = mStack.peek();

         boolean allowedByParent =  mCurrentContext.tagAllowedByParent(tag);
        if( !tag.ignoreTag() && tag.getTagHandler().processContent() == true && allowedByParent) {
            processAccumulatedTextContent(textContent);
        }

        if(tag.tag.equalsIgnoreCase(localName) == true) {
            if(tag.discardOnClosing == false) {
                mCurrentContext =  mCurrentContext.onTagClose(tag, mOutput, false);
            }
        }

        mStack.pop();

        //mCurrentContext = mMarkupContextStack.pop();
    }


    private RichTextDocumentElement processAccumulatedTextContent(String accumulatedText)  {

        if(TextUtils.isEmpty(accumulatedText)){
            return null;
        }

        Matcher m = Patterns.WEB_URL.matcher(accumulatedText);

        while (m.find()) {
            //link found
            int matchStart = m.start();
            int matchEnd = m.end();

            //any text in between ?
            StringBuffer buffer = new StringBuffer();
            m.appendReplacement(buffer, "");
            mOutput.append(buffer);

            CharSequence link = accumulatedText.subSequence(matchStart, matchEnd);
            boolean linkingToEmbed = getCurrentStyle().extractEmbeds();
            if( linkingToEmbed == true){
                // in case we find  embeds we add them as individual elements to the RichDocument
                // is the current link an embed ?
                linkingToEmbed = EmbedUtils.parseLink(accumulatedText, String.valueOf(link), new EmbedUtils.ParseLinkCallback() {

                    @Override
                    public void onLinkParsed(Object callingObject, String result, EmbedUtils.TEmbedType type) {
                        if(type == EmbedUtils.TEmbedType.EYoutube){
                            //we just take care of youtbe videos inline
                            SpannedBuilderUtils.makeYoutube(result, getCurrentStyle().maxImageWidth(), mOutput);
                        }else{
                            onEmbedFound(type, result);
                        }
                    }
                });
            }
            // the link wasn't a recognised embed or extractEmbeds = false
            if(linkingToEmbed == false) {

                //double check if it is a youtube video ?
                final String youtubeId = EmbedUtils.getYoutubeVideoId(String.valueOf(link));
                if(TextUtils.isEmpty(youtubeId) == false) {
                    SpannedBuilderUtils.makeYoutube(youtubeId, getCurrentStyle().maxImageWidth(), mOutput);
                }else {

                    //is there any white space here ??
                    Pattern pattern = Pattern.compile("\\s");
                    Matcher matcher = pattern.matcher(link);
                    boolean found = matcher.find();

                    // is not empty and the string doesn't contain empty spaces !
                    if (TextUtils.isEmpty(link) == false && found == false) {
                        SpannedBuilderUtils.makeLink(link.toString(), null, mOutput);
                    } else if (TextUtils.isEmpty(link) == false) {
                        mOutput.append(link);
                    }
                }
            }

        }

        StringBuffer buffer = new StringBuffer();
        m.appendTail(buffer);
        mOutput.append(buffer);

        return mOutput;
    }

    public void onEmbedFound(EmbedUtils.TEmbedType type, String content){
        splitDocument(OembedDocumentElement.newInstance(type, content));
    }

    public void onIframeFound(String url, int width, int height) {
        splitDocument(new WebDocumentElement(url,
                WebDocumentElement.ContentType.EUrl,
                width, height));
    }


    public void splitDocument( DocumentElement element){

        mOembedCount ++;

        RichTextDocumentElement newOut = new RichTextDocumentElement();
        /// close output
        if(mOutput != null &&
                mOutput.length() > 0){

            for(int index = (mStack.size() - 1); index >= 0 ; index --){
                MarkupTag tag = mStack.get(index);
                mCurrentContext.onTagClose(tag, mOutput, true);
                tag.discardOnClosing = true;
            }

            String text = mOutput.contentString();
            if(TextUtils.getTrimmedLength(text) > 0) {
                SpannedBuilderUtils.fixFlags(mOutput);
                if(mOembedCount > 1) {
                    SpannedBuilderUtils.trimLeadingNewlines(mOutput);
                }
                SpannedBuilderUtils.trimTrailNewlines(mOutput, 0);
                mResult.add(mOutput);
            }

            //create new Output
            for(int index = 0; index < mStack.size(); index ++){
                mCurrentContext.onTagOpenAfterSplit(mStack.get(index), newOut, true );
            }

            mOutput = newOut;
        }

        mResult.add(element);

    }

    public MarkupTag getParent(MarkupTag child, Class<? extends TagHandler> parentClass) {

        boolean childFound = false;

        for(int index = (mStack.size() - 1); index >= 0 ; index --){

            MarkupTag current = mStack.get(index);

            if(childFound == false){
                childFound = (current == child);
            }else {
                TagHandler handler = current.getTagHandler();
                if(parentClass.isAssignableFrom(handler.getClass())){
                    return current;
                }
            }
        }

        return null;
    }


    public final Stack<MarkupTag> getStack(){
        return mStack;
    }



    public final MarkupTag getParent(){

        if(mStack.size() > 0) {
            return mStack.peek();
        }
        return null;
    }

    private void appendRemainder(){
        if(mOutput != null &&
                TextUtils.getTrimmedLength(mOutput.contentString()) > 0){
            SpannedBuilderUtils.fixFlags(mOutput);
            mResult.add(mOutput);
        }
    }


}

