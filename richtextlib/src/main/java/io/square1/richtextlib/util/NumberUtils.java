package io.square1.richtextlib.util;

/**
 * Created by roberto on 30/06/15.
 */
public class NumberUtils {

    public static final int INVALID = -1;

    public static int parseImageDimension(String in, int maxSize){
        try {
            if( in.indexOf(in.length() - 1 , '%') >= 0){
                //percentage
                float perc = Float.parseFloat(in.substring(0,in.length() - 2));
                return (int) ((float)maxSize / 100.0f * perc);

            }
            return Integer.parseInt(in);
        }catch (Exception e){
            return INVALID;
        }
    }

}
