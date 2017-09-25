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

package io.square1.richtextlib.v2.parser;

import android.text.TextUtils;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarkupTag {

    public static final String ATT_IGNORE_TAG = "app-ignore";

    public final String tag;
    public boolean closeOnEnd;
    public boolean duplicateOnStart;
    public final Attributes attributes;
    public final List<String> elementClasses;
    public boolean discardOnClosing;

    private MarkupTag mParent;
    private ArrayList<MarkupTag> mChildren;

    private TagHandler mTagHandler;

    public MarkupTag(String tag, Attributes attributes) {
        mChildren = new ArrayList<>();
        this.tag = tag;
        this.discardOnClosing = false;
        this.closeOnEnd = true;
        this.duplicateOnStart = true;
        this.attributes = new AttributesImpl(attributes);
        String attributeClass = attributes.getValue("class");
        this.elementClasses = parseClassAttribute(attributeClass);
    }

    public void setParent(MarkupTag parent){
        mParent = parent;
    }
    public void addChild(MarkupTag tag){
        tag.setParent(this);
        mChildren.add(tag);
    }

    public final String getElementClass(){
        return attributes.getValue("class");
    }

    public void setTagHandler(TagHandler handler) {
        mTagHandler = handler;
    }

    public TagHandler getTagHandler() {
        return mTagHandler;
    }

    @Override
    public String toString() {
        return "MarkupTag{" + this.tag + "}";
    }

    public static List<String> parseClassAttribute(String classAttribute) {
        if (TextUtils.isEmpty(classAttribute)) {
            return new ArrayList<>();
        }
        classAttribute = classAttribute.trim().replaceAll(" +", " ");
        return Arrays.asList(classAttribute.split(" "));
    }

    public MarkupTag getParent() {
        return mParent;
    }

    /**
     * the ignore flag is either set on the current tag
     * or it is set on the parent !
     *
     * @return true or false
     */
    public boolean ignoreTag(){

        boolean ignore = this.attributes.getIndex("", ATT_IGNORE_TAG) > -1;

        if(ignore == false && mParent != null ){
            ignore = mParent.ignoreTag();
        }

        return ignore;
    }
}