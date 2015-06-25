package io.square1.richtextlib.parser;

class Attribute {

        public String uri;
        public String localName;
        public String value;

        Attribute(String uri, String localName, Object value){
            this.localName = localName;
            this.uri = uri;
            this.value = String.valueOf(value);
        }

    }