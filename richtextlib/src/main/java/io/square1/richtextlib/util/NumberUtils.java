package io.square1.richtextlib.util;

/**
 * Created by roberto on 30/06/15.
 */
public class NumberUtils {

    public static final int INVALID = -1;

    public static int parseImageDimension(String in){
        try {
            return Integer.parseInt(in);
        }catch (Exception e){
            return INVALID;
        }
    }

}
