package io.square1.richtextlib.v2.parser;

import android.text.TextUtils;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarkupTag {

         public final String tag;
         public  boolean closeOnEnd;
         public  boolean duplicateOnStart;
         public final Attributes attributes;
         public final List<String> elementClasses;

    private TagHandler mTagHandler;

         public MarkupTag(String tag, Attributes attributes){
             this.tag = tag;
             this.closeOnEnd = true;
             this.duplicateOnStart = true;
             this.attributes = new AttributesImpl(attributes);
             String attributeClass = attributes.getValue("class");
             this.elementClasses = parseClassAttribute(attributeClass);
         }

    public void setTagHandler(TagHandler handler){
        mTagHandler = handler;
    }

    public TagHandler getTagHandler(){
        return mTagHandler;
    }

         @Override
         public String toString() {
             return "MarkupTag{"+this.tag+"}";
         }

       public static List<String> parseClassAttribute(String classAttribute){
           if(TextUtils.isEmpty(classAttribute)){
               return  new ArrayList<>();
           }
            classAttribute = classAttribute.trim().replaceAll(" +", " ");
            return Arrays.asList(classAttribute.split(" "));
       }
    }