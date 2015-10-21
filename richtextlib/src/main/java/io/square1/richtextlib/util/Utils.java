package io.square1.richtextlib.util;

/**
 * Created by roberto on 04/10/15.
 */
public class Utils {

    public static <T extends Object> T newInstance(String className){

        try {
            Class<T> currentClass = (Class<T>) Class.forName(className);
            T item = currentClass.newInstance();

            return item;
        }catch (Exception e){}

        return null;
    }

}

