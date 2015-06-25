package io.square1.richtextlib.util;

/**
 * Created by roberto on 25/06/15.
 */
public class UniqueId {

    private static int sSeed = 100;

    synchronized public static final int getType(){
        return sSeed ++;
    }
}
