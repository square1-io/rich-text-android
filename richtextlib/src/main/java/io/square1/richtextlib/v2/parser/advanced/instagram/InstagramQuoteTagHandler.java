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

package io.square1.richtextlib.v2.parser.advanced.instagram;

import android.text.TextUtils;

import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupContext;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.v2.parser.TagHandler;


public  class InstagramQuoteTagHandler extends TagHandler {


        private InstagramContext mInstagramContext;

        @Override
        public void onTagClose(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

            if(TextUtils.isEmpty(mInstagramContext.getInstagramId()) == false){

                context.getRichText().onEmbedFound(EmbedUtils.TEmbedType.EInstagram,
                        mInstagramContext.getInstagramId());

            }
        }


        @Override
        public MarkupContext getReplacementContext(){

            if(mInstagramContext == null){

                mInstagramContext = new InstagramContext(getInitialContext().getRichText(),
                        getInitialContext().getStyle());
            }
            return mInstagramContext;
        }

        public  MarkupContext getInitialContext(){
            return super.getInitialContext();
        }

        @Override
        public boolean closeWhenSplitting() {
            return false;
        }

        @Override
        public boolean openWhenSplitting() {
            return false;
        }

        @Override
        public void onTagOpen(MarkupContext context, MarkupTag tag, RichTextDocumentElement out) {

        }

        @Override
        public boolean processContent() {
            return false;
        }
    }