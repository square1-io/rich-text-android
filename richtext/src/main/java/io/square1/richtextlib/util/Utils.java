/*
 * Copyright (c) 2015. Roberto  Prato <https://github.com/robertoprato>
 *
 *  *
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

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
            return newInstance(currentClass);
        }catch (Exception e){
            Log.e("CLASS" , className);
            e.printStackTrace();
        }

        return null;
    }

    public static <T extends Object> T newInstance(Class<T> currentClass){

        try {
            T item = currentClass.newInstance();
            Log.i("CLASS", currentClass.getName());
            return item;
        }catch (Exception e){
            Log.e("CLASS" , currentClass.getName());
            e.printStackTrace();
        }

        return null;
    }

}

