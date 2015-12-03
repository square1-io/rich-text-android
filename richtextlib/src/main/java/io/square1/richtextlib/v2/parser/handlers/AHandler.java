package io.square1.richtextlib.v2.parser.handlers;

import android.text.Spannable;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.spans.URLSpan;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;

/**
 * Created by roberto on 04/09/15.
 */
public class AHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {
        String href = tag.attributes.getValue("", "href");
        int len = out.length();
        Markers.Href h = new Markers.Href(href);
        out.setSpan(h, len, len, Spannable.SPAN_MARK_MARK);
    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        int len = out.length();
        Object obj = out.getLastSpan(Markers.Href.class);
        int where = out.getSpanStart(obj);
        out.removeSpan(obj);

        if (where != len) {

            if(obj == null){
                return;
            }

            Markers.Href h = (Markers.Href) obj;

            if (h.mHref != null) {
//TODO if in noscript tag we should update the context and supply a different class for href
              //  if(isWithinTag( stack, "noscript") != null){
              //      out.setSpan(new UnsupportedContentSpan(h.mHref), where, len,
              //              Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
              //  }else {
                    out.setSpan(new URLSpan(h.mHref), where, len,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
             //   }
            }
        }

    }
}
