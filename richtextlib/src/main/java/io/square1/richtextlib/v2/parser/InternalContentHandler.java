package io.square1.richtextlib.v2.parser;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.v2.RichTextV2;

/**
 * Created by roberto on 07/09/15.
 */
public class InternalContentHandler  implements ContentHandler, EmbedUtils.ParseLinkCallback {

    RichTextV2 mHandler;
    private StringBuilder mAccumulatedText;

    public InternalContentHandler(RichTextV2 context){
        mHandler = context;
        mAccumulatedText = new StringBuilder();

    }

    @Override
    public void setDocumentLocator(Locator locator) {

    }

    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {

    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        mHandler.startElement(uri, localName, atts, mAccumulatedText);
        mAccumulatedText.setLength(0);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        mHandler.endElement(uri, localName, mAccumulatedText);
        mAccumulatedText.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        /*
         * Ignore whitespace that immediately follows other whitespace;
         * newlines count as spaces.
         */

        if(mHandler.getCurrentStyle().treatAsHtml() == true) {


            for (int i = 0; i < length; i++) {
                char c = ch[i + start];

                if (c == ' ' || c == '\n') {

                    char pred;
                    int len = mAccumulatedText.length();

                    //no text yet in the accumulated buffer
                    if (len == 0) {

                        len = mHandler.getCurrentOutput().length();

                        if (len == 0) {
                            pred = '\n';
                        } else {
                            pred = mHandler.getCurrentOutput().charAt(len - 1);
                        }
                    } else {
                        pred = mAccumulatedText.charAt(len - 1);
                    }

                    if (pred != ' ' && pred != '\n') {
                        mAccumulatedText.append(' ');
                    }
                } else {
                    mAccumulatedText.append(c);
                }
            }
        }else {
            mAccumulatedText.append(ch, start, length);
        }


    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {

    }

    @Override
    public void skippedEntity(String name) throws SAXException {

    }

    @Override
    public void onLinkParsed(Object callingObject, String result, EmbedUtils.TEmbedType type) {

    }
}
