package io.square1.richtextlib.v2.parser.handlers;

import android.net.Uri;
import android.text.Spannable;

import org.xml.sax.Attributes;

import io.square1.richtextlib.style.UrlBitmapSpan;
import io.square1.richtextlib.style.VideoPlayerSpan;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 04/09/15.
 */
public class VIDEOHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 1);

        Attributes attributes = tag.attributes;
        String src = attributes.getValue("", "src");


        VideoPlayerSpan videoPlayerSpan = new VideoPlayerSpan(src,context.getStyle().maxImageWidth());

        int len = out.length();
        out.append(SpannedBuilderUtils.NO_SPACE);

        out.setSpan(videoPlayerSpan,
                len,
                out.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

    }

    public boolean processContent() {
        return false;
    }
}
