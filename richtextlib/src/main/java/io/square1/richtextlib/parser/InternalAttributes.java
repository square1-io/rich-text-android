package io.square1.richtextlib.parser;

import android.util.SparseArray;

import org.xml.sax.Attributes;

import java.util.HashMap;

class InternalAttributes implements Attributes {

        HashMap<String,Attribute> mValues;
        SparseArray<Attribute> mOrderedValues;

        public InternalAttributes(){
            mValues = new HashMap<>();
            mOrderedValues = new SparseArray<>();
        }

        public void add(Attribute attribute){
            mValues.put(attribute.localName,attribute);
            mOrderedValues.append(mOrderedValues.size(),attribute);
        }


        @Override
        public int getLength() {
            return mValues.size();
        }

        @Override
        public String getURI(int index) {
            return mOrderedValues.valueAt(index).uri;
        }

        @Override
        public String getLocalName(int index) {
            return mOrderedValues.valueAt(index).localName;
        }

        @Override
        public String getQName(int index) {
            return mOrderedValues.valueAt(index).localName;
        }

        @Override
        public String getType(int index) {
            return "";
        }

        @Override
        public String getValue(int index) {
            return mOrderedValues.valueAt(index).value;
        }

        @Override
        public int getIndex(String uri, String localName) {
            return 0;
        }

        @Override
        public int getIndex(String qName) {
            return 0;
        }

        @Override
        public String getType(String uri, String localName) {
            return null;
        }

        @Override
        public String getType(String qName) {
            return null;
        }

        @Override
        public String getValue(String uri, String localName) {
            return getValue(localName);
        }

        @Override
        public String getValue(String localName) {
            return mValues.get(localName).value;
        }
    }
