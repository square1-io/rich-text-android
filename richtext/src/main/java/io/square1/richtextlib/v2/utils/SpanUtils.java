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

package io.square1.richtextlib.v2.utils;

import io.square1.richtextlib.spans.UrlBitmapDownloader;
import io.square1.richtextlib.ui.RichContentViewDisplay;

/**
 * Created by roberto on 08/09/15.
 */
public class SpanUtils {

    public static UrlBitmapDownloader getDownloader(RichContentViewDisplay view){

        if(view == null) return null;

        if(view instanceof RichContentViewDisplay){
            return ((RichContentViewDisplay)view).getDownloader();
        }

        return null;
    }

}
