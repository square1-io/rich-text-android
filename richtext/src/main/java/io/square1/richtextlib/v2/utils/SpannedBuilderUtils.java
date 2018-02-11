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

package io.square1.richtextlib.v2.utils;

import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ParagraphStyle;

import io.square1.richtextlib.spans.RelativeSizeSpan;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.spans.URLSpan;
import io.square1.richtextlib.spans.UnsupportedContentSpan;
import io.square1.richtextlib.spans.YouTubeSpan;

/**
 * Created by roberto on 04/09/15.
 */
public class SpannedBuilderUtils {

    public final static String NO_SPACE = "\uFFFC";
    public static final String TAB = "\t";
    public static final String BULLET = "•";
    public static final String SPACE = " ";

    public static void trimLeadingNewlines(RichTextDocumentElement text) {

        int len = text.length();
        int index = 0;
        boolean found = false;
        for (index = 0; index < len; index++) {

            if (text.charAt(index) != '\n') {
                break;
            }
            found = true;
        }

        if (found == true) {
            text.delete(0, index);
        }

    }

    public static void trimTrailNewlines(RichTextDocumentElement text, int newLinesCountAfter) {

        int len = text.length();

        int currentNewLines = 0;

        while (len > 0) {
            if (text.charAt(len - 1) == '\n') {
                currentNewLines++;
            }
            else {
                break;
            }
            len--;
        }

        ///at the end of the process the string must end with
        //at max newLinesCount
        // if there are more new lines than what we want
        if (newLinesCountAfter < currentNewLines) {
            text.trim(currentNewLines - newLinesCountAfter);
        }

    }

    public static int ensureAtLeastThoseNewLines(RichTextDocumentElement text, int newLines) {

        int len = text.length();

        int currentNewLines = 0;

        while (len > 0) {
            if (text.charAt(len - 1) == '\n') {
                currentNewLines++;
            }
            else {
                break;
            }
            len--;
        }

        newLines = newLines - currentNewLines;

        for (int index = 0; index < newLines; index++) {
            text.append('\n');
        }

        return newLines;
    }

    public static int ensureBeginsWithAtLeastThoseNewLines(StringBuilder text, int newLines) {

        int len = text.length();

        int currentNewLines = 0;

        for(int index = 0; index < len; index ++){

            if (text.charAt(index) == '\n') {
                currentNewLines++;
            }else {
                break;
            }

            if(currentNewLines == newLines){
                break;
            }

        }

        newLines = newLines - currentNewLines;

        for (int index = 0; index < newLines; index++) {
            text.insert(0, '\n');
        }

        return newLines;
    }

    public static int ensureAtLeastThoseNewLines(StringBuilder text, int newLines) {

        int len = text.length();

        int currentNewLines = 0;

        while (len > 0) {
            if (text.charAt(len - 1) == '\n') {
                currentNewLines++;
            }
            else {
                break;
            }
            len--;
        }

        newLines = newLines - currentNewLines;

        for (int index = 0; index < newLines; index++) {
            text.append('\n');
        }

        return newLines;
    }

    public static void makeYoutube(String youtubeId, int maxImageWidth, RichTextDocumentElement builder) {

        ensureAtLeastThoseNewLines(builder, 1);
        int len = builder.length();
        builder.append(NO_SPACE);
        builder.setSpan(new YouTubeSpan(youtubeId, maxImageWidth),
                len,
                builder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    }

    public static void makeYoutube(String youtubeId,int width, int height, int maxImageWidth, RichTextDocumentElement builder) {

        ensureAtLeastThoseNewLines(builder, 1);
        int len = builder.length();
        builder.append(NO_SPACE);
        builder.setSpan(new YouTubeSpan(youtubeId, width,  height, maxImageWidth),
                len,
                builder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    }

    public static void makeUnsupported(String link, String text, RichTextDocumentElement builder) {
        //clean the link:
        if (link.indexOf("//") == 0) {
            link = "http:" + link;
        }
        if (TextUtils.isEmpty(text) == true) {
            text = link;
        }
        int len = builder.length();
        builder.append(text);
        builder.setSpan(new UnsupportedContentSpan(link), len, len + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void makeLink(String link, String text, RichTextDocumentElement builder) {
        //clean the link:
        if (link.indexOf("//") == 0) {
            link = "http:" + link;
        }
        if (TextUtils.isEmpty(text) == true) {
            text = link;
        }
        int len = builder.length();
        builder.append(text);
        builder.setSpan(new URLSpan(link), len, len + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void startSpan(Spannable text, Object mark) {

        int len = text.length();
        text.setSpan(mark, len, len, Spannable.SPAN_MARK_MARK);
    }

    public static void endSpan(RichTextDocumentElement text, Class kind, Object repl) {

        int len = text.length();
        Object obj = text.getLastSpan(kind);
        int where = text.getSpanStart(obj);

        text.removeSpan(obj);

        if (where != len) {
            text.setSpan(repl, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public static void fixFlags(Spannable builder) {

        // Fix flags and range for paragraph-type markup.
        ParagraphStyle[] obj = builder.getSpans(0, builder.length(), ParagraphStyle.class);
        for (int i = 0; i < obj.length; i++) {
            int start = builder.getSpanStart(obj[i]);
            int end = builder.getSpanEnd(obj[i]);

            // If the last line of the range is blank, back off by one.
            if (end - 2 >= 0) {
                if (builder.charAt(end - 1) == '\n' &&
                        builder.charAt(end - 2) == '\n') {
                    end--;
                }
            }

            if (end == start) {
                builder.removeSpan(obj[i]);
            }
            else {

                try {
                    builder.setSpan(obj[i], start, end, Spannable.SPAN_PARAGRAPH);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
