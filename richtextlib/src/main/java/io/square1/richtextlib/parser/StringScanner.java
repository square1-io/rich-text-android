package io.square1.richtextlib.parser;

import android.text.TextUtils;

/**
 * Created by roberto on 14/04/15.
 */
public class StringScanner {

   public static class EndReachedException extends Exception {

   }

   final String mString;
   public int mPosition;

   public StringScanner(String string){

       mString = string;
       if(TextUtils.isEmpty(mString)){
           throw new IllegalArgumentException(" Sting must not be empty ");
       }
       mPosition = -1;
   }

   public char next() throws EndReachedException{
       mPosition ++;
       return charAt(mPosition);
   }

    public boolean hasNext(){

        int next = mPosition + 1;
        return next < mString.length();
    }

    public char afterNext(){

        int afterNext = mPosition + 1;
        try {
            return charAt(afterNext);
        }catch (Exception exc){
            return ' ';
        }

    }

   public char charAt(int position) throws EndReachedException{

       if(position >= mString.length()){
           throw new EndReachedException();
       }
       return mString.charAt(position);
   }




}
