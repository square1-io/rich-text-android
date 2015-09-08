package io.square1.richtextlib.v2.parser;


import io.square1.richtextlib.ParcelableSpannedBuilder;

/**
 * Created by roberto on 19/08/15.
 */
public abstract class TagHandler {


    public TagHandler(){

    }

    public abstract void onTagOpen(MarkupContext context, MarkupTag tag , ParcelableSpannedBuilder out);
    public abstract void onTagClose(MarkupContext context, MarkupTag tag , ParcelableSpannedBuilder out);

}
