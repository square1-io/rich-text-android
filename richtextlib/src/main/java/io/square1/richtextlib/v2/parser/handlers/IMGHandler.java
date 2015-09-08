package io.square1.richtextlib.v2.parser.handlers;

import android.net.Uri;
import android.text.Spannable;

import org.xml.sax.Attributes;

import io.square1.richtextlib.ParcelableSpannedBuilder;
import io.square1.richtextlib.style.UrlBitmapSpan;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 04/09/15.
 */
public class IMGHandler extends TagHandler {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, ParcelableSpannedBuilder out) {

        //buildNewSpannable();
        SpannedBuilderUtils.ensureAtLeastThoseNewLines(out,1);

        Attributes attributes = tag.attributes;
        String src = attributes.getValue("", "src");


        int maxSize = context.getStyle().maxImageWidth();
        UrlBitmapSpan imageDrawable = new UrlBitmapSpan(Uri.parse(src),
                null,
                NumberUtils.parseImageDimension(attributes.getValue("width"), maxSize),
                NumberUtils.parseImageDimension(attributes.getValue("height"),0),
                context.getStyle().maxImageWidth() );

        int len = out.length();
        out.append(SpannedBuilderUtils.NO_SPACE);

        out.setSpan(imageDrawable,
                len,
                out.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, ParcelableSpannedBuilder out) {

    }
}
