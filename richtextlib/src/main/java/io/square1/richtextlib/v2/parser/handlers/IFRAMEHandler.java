package io.square1.richtextlib.v2.parser.handlers;

import java.util.HashMap;

import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.ParcelableSpannedBuilder;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;


/**
 * Created by roberto on 04/09/15.
 */
public class IFRAMEHandler extends TagHandler  {

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, final ParcelableSpannedBuilder out) {

        String href = tag.attributes.getValue("", "src");

        if( EmbedUtils.parseLink(context, href, new EmbedUtils.ParseLinkCallback() {

            @Override
            public void onLinkParsed(Object callingObject, String result, EmbedUtils.TEmbedType type) {
                MarkupContext context = (MarkupContext)callingObject;
                if(type == EmbedUtils.TEmbedType.EYoutube){
                    SpannedBuilderUtils.makeYoutube(result,context.getStyle().maxImageWidth(), out);
                    return;
                }
                context.getRichText().onEmbedFound(type,result);
                //we have to remove embeds from quotes tags
               // context.buildNewSpannable();
               // HashMap<String,Object> attrs = new HashMap<>();
               // attrs.put(RichText.EMBED_TYPE,type);
               // mCallback.onElementFound(RichText.TNodeType.EEmbed, result, attrs);

            }

        }) == false) {
            SpannedBuilderUtils.makeUnsupported(href, null, out);
        }


    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, ParcelableSpannedBuilder out) {

    }


    @Override
    public boolean closeWhenSplitting(){
        return false;
    }


    @Override
    public boolean openWhenSplitting(){
        return false;
    }

}
