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

package io.square1.richtextlib.v2.parser.handlers;

import android.text.Spannable;
import android.text.TextUtils;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;
import io.square1.richtextlib.v2.utils.SpannedBuilderUtils;

/**
 * Created by roberto on 19/08/15.
 */
public class PHandler extends TagHandler {



    public void onTagOpenAfterSplit(MarkupContext context, MarkupTag tag , RichTextDocumentElement out) {

        //we don't want any extra lines here
        String current = out.contentString();

        Markers.P marker = new Markers.P();
        SpannedBuilderUtils.startSpan(out, marker);
        SpannedBuilderUtils.trimTrailNewlines(out, 0);
        marker.newLinesAtStart = out.contentString().length();
//        if(TextUtils.getTrimmedLength(current) > 0 ) {
//
//        }else {
//            marker.newLinesAtStart = 1;
//            ///just start with a new line should be fine
//            out.append('\n');
//        }

    }

    @Override
    public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        String current = out.contentString();

        Markers.P marker = new Markers.P();
        SpannedBuilderUtils.startSpan(out, marker);

        if(TextUtils.getTrimmedLength(current) > 0 ) {
          marker.newLinesAtStart = SpannedBuilderUtils.ensureAtLeastThoseNewLines(out, 2);
        }else {
            marker.newLinesAtStart = 1;
            ///just start with a new line should be fine
            out.append('\n');
        }
    }

    @Override
    public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        int len = out.length();
        Markers.P obj = out.getLastSpan(Markers.P.class);
        int where = out.getSpanStart(obj);

        out.removeSpan(obj);

        if (len > obj.newLinesAtStart) {
            SpannedBuilderUtils.ensureAtLeastThoseNewLines(out,1);
           // text.setSpan(repl, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else if(obj.newLinesAtStart > 0) {
            out.delete(where,where + obj.newLinesAtStart );
        }

    }

    //don't add extra new lines when an OEMbed was found and the page continues
    public boolean openWhenSplitting(){
        return true;
    }

    public boolean closeWhenSplitting(){
        return false;
    }




}
