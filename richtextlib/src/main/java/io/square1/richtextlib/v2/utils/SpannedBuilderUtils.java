package io.square1.richtextlib.v2.utils;

import android.text.Spannable;

import io.square1.richtextlib.ParcelableSpannedBuilder;

/**
 * Created by roberto on 04/09/15.
 */
public class SpannedBuilderUtils {

    public final static String NO_SPACE = "\uFFFC";
    public static final String TAB = "\t";
    public static final String BULLET = "•";
    public static final String SPACE = " ";



    public static void ensureAtLeastThoseNewLines(ParcelableSpannedBuilder text, int newLines){

        int len = text.length();

        int currentNewLines = 0;

        while(len > 0){
            if( text.charAt(len - 1) == '\n'){
                currentNewLines ++;
            }else{
                break;
            }
            len --;
        }

        newLines = newLines - currentNewLines;


        for(int index = 0; index < newLines; index ++){
            text.append('\n');
        }


    }


    public static void startSpan(ParcelableSpannedBuilder text, Object mark) {
        int len = text.length();
        text.setSpan(mark, len, len, Spannable.SPAN_MARK_MARK);
    }

    public static void endSpan(ParcelableSpannedBuilder text, Class kind, Object repl) {

        int len = text.length();
        Object obj = text.getLastSpan(kind);
        int where = text.getSpanStart(obj);

        text.removeSpan(obj);

        if (where != len) {
            text.setSpan(repl, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}
