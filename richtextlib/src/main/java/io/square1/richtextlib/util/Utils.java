package io.square1.richtextlib.util;

import android.os.Looper;
import android.util.Log;

/**
 * Created by roberto on 04/10/15.
 */
public class Utils {

    public static <T extends Object> T newInstance(String className){

        try {
            Class<T> currentClass = (Class<T>) Class.forName(className);
            T item = currentClass.newInstance();
            Log.i("CLASS", className);
            return item;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}

