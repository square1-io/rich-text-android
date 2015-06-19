package io.square1.richtextlib.parser;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by roberto on 14/04/15.
 */
public class StringTokenizer {

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

    public interface StringTokenizerObserver {
         boolean onTokenFound(String token, HashMap<String, String> attributes);
         void onTextFound(String text);

        public void endReached();
    }

   private StringTokenizerObserver mObserver;
   private StringScanner mScanner;
   private char[] mOpenTokenDelimiters;
   private char[] mCloseTokenDelimiters;


    StringTokenizer(String text, char[] openTokenDelimiters,
                    char[] closeTokenDelimiter,
                    StringTokenizerObserver observer){

        mObserver = observer;
        mScanner = new StringScanner(text);

        mOpenTokenDelimiters = openTokenDelimiters;
        mCloseTokenDelimiters = closeTokenDelimiter;
    }

    public final void parse(){


        //initial state is nothing was opened yet, so we assume closed is = true
        boolean tagOpened = false;
        boolean tagClosed = true;

        StringBuilder currentToken = new StringBuilder();

        while (mScanner.hasNext() == true) {

            try {

                final char next = mScanner.next();
                final char nextNext = mScanner.afterNext();
                final boolean nextNextSpecial = isSpecialChar(nextNext);

                /// opening tag
                if(!nextNextSpecial && isOpenDelimiter(next) == true && tagClosed == true ){

                    //was there any text in between tags ?
                    final String text = currentToken.toString();
                    if(TextUtils.isEmpty(text) == false) {
                        mObserver.onTextFound(text);
                    }

                    tagClosed = false;
                    tagOpened = true;

                    currentToken.setLength(0);
                }
                else if(isClosingDelimiter(next) == true &&
                        tagOpened == true ){

                    tagClosed = true;
                    tagOpened = false;
                    HashMap<String,String> attr = new HashMap<>();
                    String token = parseTokenAttributes(currentToken.toString(),attr);
                    mObserver.onTokenFound(token, attr);
                    currentToken.setLength(0);

                }else{

                    currentToken.append(next);

                }

            } catch (StringScanner.EndReachedException exc) {

            }
        }

        final String text = currentToken.toString();
        if(TextUtils.isEmpty(text) == false) {
            mObserver.onTextFound(text);
        }
        mObserver.endReached();
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





}
