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
