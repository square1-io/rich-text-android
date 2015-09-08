package io.square1.richtextlib.v2.parser.handlers;

import android.graphics.Typeface;
import android.net.Uri;
import android.text.Spannable;
import android.text.TextUtils;

import org.xml.sax.Attributes;

import io.square1.richtextlib.ParcelableSpannedBuilder;
import io.square1.richtextlib.style.StyleSpan;
import io.square1.richtextlib.style.UnsupportedContentSpan;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;

/**
 * Created by roberto on 04/09/15.
 */
public class DIVHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, ParcelableSpannedBuilder out) {

        Attributes attributes = tag.attributes;
        String elementClass = attributes.getValue("", "class");

        if("pb_feed".equalsIgnoreCase(elementClass)){
            String dataGame  = attributes.getValue("", "data-game");

            if(TextUtils.isEmpty(dataGame) == true) return;

            int where = out.length();
            String message = " TAKE  THE  QUIZ HERE!" ;

            out.append(message);
            String url = Uri.parse("http://www.playbuzz.com")
                    .buildUpon()
                    .encodedPath(dataGame)
                    .build()
                    .toString();


            out.setSpan( new StyleSpan(Typeface.BOLD), where, where + message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            UnsupportedContentSpan span = new UnsupportedContentSpan(url);
            out.setSpan(span, where,where + message.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        else if("fb-video".equalsIgnoreCase(elementClass)){
            String url = attributes.getValue("","data-href");
            if(TextUtils.isEmpty(url) == false){



                int where = out.length();
                String message = " See Facebook Video Here " ;
                out.append(message);
                UnsupportedContentSpan span = new UnsupportedContentSpan(url);
                out.setSpan(span, where,where + message.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, ParcelableSpannedBuilder out) {

    }
}
