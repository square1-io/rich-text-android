package io.square1.richtextlib.util;

/**
 * Created by roberto on 19/06/15.
 */
public class CatchAndLog {


    public final static void catchAndLog(Runnable run){

        try{
            run.run();
        }catch (Exception exc){}


    }

}
