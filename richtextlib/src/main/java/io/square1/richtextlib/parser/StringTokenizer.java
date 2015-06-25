package io.square1.richtextlib.parser;
import android.text.TextUtils;
import android.util.SparseArray;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;

import java.io.IOException;
import java.io.Reader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by roberto on 14/04/15.
 */
public class StringTokenizer implements XMLReader {


    @Override
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return false;
    }

    @Override
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {

    }

    @Override
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return null;
    }

    @Override
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {

    }

    @Override
    public void setEntityResolver(EntityResolver resolver) {

    }

    @Override
    public EntityResolver getEntityResolver() {
        return null;
    }

    @Override
    public void setDTDHandler(DTDHandler handler) {

    }

    @Override
    public DTDHandler getDTDHandler() {
        return null;
    }

    @Override
    public void setContentHandler(ContentHandler handler) {
        mObserver = handler;
    }

    @Override
    public ContentHandler getContentHandler() {
        return mObserver;
    }

    @Override
    public void setErrorHandler(ErrorHandler handler) {

    }

    @Override
    public ErrorHandler getErrorHandler() {
        return null;
    }

    @Override
    public void parse(InputSource input) throws IOException, SAXException {
        mScanner = input.getCharacterStream();
        parse();
    }

    @Override
    public void parse(String systemId) throws IOException, SAXException {

    }

    public static class TagDelimiter {

        char openingChar;
        char closingChar;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TagDelimiter)) return false;

            TagDelimiter that = (TagDelimiter) o;

            if (closingChar != that.closingChar) return false;
            if (openingChar != that.openingChar) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (int) openingChar;
            result = 31 * result + (int) closingChar;
            return result;
        }
    }


   private ContentHandler mObserver;
   private Reader mScanner;
   private char[] mOpenTokenDelimiters;
   private char[] mCloseTokenDelimiters;


    public StringTokenizer(char[] openTokenDelimiters, char[] closeTokenDelimiter){
        mOpenTokenDelimiters = openTokenDelimiters;
        mCloseTokenDelimiters = closeTokenDelimiter;
    }


    private final void parse(){

        //initial state is nothing was opened yet, so we assume closed is = true
        boolean tagOpened = false;
        boolean tagClosed = true;

        StringBuilder currentToken = new StringBuilder();

        int current = -1;
        try { current = mScanner.read();}catch (Exception e){}

        while (current >= 0) {

                final char next = (char)current;
                try { current = mScanner.read();}catch (Exception e){}
                final char nextNext = (char)current;

                final boolean nextNextSpecial = isSpecialChar(nextNext);

                /// opening tag
                if(!nextNextSpecial && isOpenDelimiter(next) == true && tagClosed == true ){

                    //was there any text in between tags ?
                    final String text = currentToken.toString();

                    if(TextUtils.isEmpty(text) == false) {
                        char[] chars = text.toCharArray();
                        try{ mObserver.characters(chars,0,chars.length);} catch (Exception ex){};
                    }

                    tagClosed = false;
                    tagOpened = true;

                    currentToken.setLength(0);
                }
                else if(isClosingDelimiter(next) == true &&
                        tagOpened == true ){

                    tagClosed = true;
                    tagOpened = false;

                    InternalAttributes attr = new InternalAttributes();
                    String token = parseTokenAttributes(currentToken.toString(),attr);

                    try {
                        StringBuilder builder = new StringBuilder(token);
                        if(cleanClosingToken(builder) == true){
                            token = builder.toString();
                            mObserver.endElement(token, token, token);
                        }else {
                            mObserver.startElement(token, token, token, attr);
                        }
                    }catch (Exception e){}

                    currentToken.setLength(0);

                }else{
                    currentToken.append(next);
                }
        }

        final String text = currentToken.toString();
        if(TextUtils.isEmpty(text) == false) {
            char[] chars = text.toCharArray();
            try {
                mObserver.characters(chars, 0, chars.length);
            }catch (Exception e){}
        }

        try { mObserver.endDocument();}catch (Exception e){}
    }

    private String parseTokenAttributes(String string, HashMap<String, String> attrs) {
        String[] parts = string.split(" ");
        if(parts.length > 1){

            for(int index = 1; index < parts.length; index ++){
                String[] attr = parts[index].split("=");
                if(attr.length > 0) {
                    String key = attr[0];
                    String value = attr.length == 2 ?
                            TextUtils.isEmpty(attr[1]) ? "" : attr[1] : "";

                    attrs.put(key, removeQuotes(value));
                }
            }

        }
        return parts[0];
    }


    private String parseTokenAttributes(String string, InternalAttributes attrs) {
        String[] parts = string.split(" ");
        if(parts.length > 1){

            for(int index = 1; index < parts.length; index ++){
                String[] attr = parts[index].split("=");

                if(attr.length > 0) {

                    String key = attr[0];
                    String value = attr.length == 2 ?
                            TextUtils.isEmpty(attr[1]) ? "" : attr[1] : "";

                    attrs.add(new Attribute(key, key, removeQuotes(value)));
                }
            }

        }
        return parts[0];
    }



    private boolean isOpenDelimiter(char current){
        for(char c : mOpenTokenDelimiters){
            if(current == c){
                return true;
            }
        }
        return false;
    }

    private boolean isClosingDelimiter(char current){
        for(char c : mCloseTokenDelimiters){
            if(current == c){
                return true;
            }
        }
        return false;
    }

    boolean isSpecialChar(char current){
        return isClosingDelimiter(current) || isOpenDelimiter(current);
    }

    private String removeQuotes(String in){
        return  in.replaceAll("\"|\'","");
    }



    /**
     * get a stringbuilder containing a token, returns true if
     * the token starts with / .
     *  upon return the Stringbuilder contains the token with the removed / is present
     * @param token
     * @return
     */
    public boolean cleanClosingToken(StringBuilder token){
        char first = token.charAt(0);
        if(first == '/'){
            token.delete(0,1);
            return true;
        }
        return false;
    }


}
