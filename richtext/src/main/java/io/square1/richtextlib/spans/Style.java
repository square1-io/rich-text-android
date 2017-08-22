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

package io.square1.richtextlib.spans;

import android.content.Context;
import android.graphics.Bitmap;




/**
 * Created by roberto on 17/06/15.
 */
public interface Style {


    static final int NOT_SET = Integer.MIN_VALUE;
    static final int USE_DEFAULT = Integer.MIN_VALUE;

    Context getApplicationContext();




    /**
     * text color to be used for H1... headers
     */
     int headerColor();

    /**
     * The color to be used as the main background
     * @return
     */
    int backgroundColor();

    /**
     * the max width for images to be displayed
     * @return
     */
    int maxImageWidth();

    int maxImageHeight();

    /**
     * The multiplier for the text increase for Headers
     *
     * @param headerLevel from 1 to 6
     * @return 1.2 , 1.3 ecc ecc
     *
     */
    float headerIncrease(int headerLevel);

    /**
     * the text reduction to apply when the <SMALL></SMALL> tag is encountered.
     * @return < 1
     */
    float  smallTextReduce();

    /**
     * will make an attempt to resolve Wordpress tags such as [soundcloud /]
     */
    boolean parseWordPressTags();

    /**
     * when parsing pure HTML white spaces and new lines have to be considered differently
     * @return
     */
    boolean treatAsHtml();

    /**
     * @return true if the parser should extract images. False is images are treated inside the content
     */
    boolean extractImages();

    /**
     * @return true if the parser should extract videos. False is videos are treated inside the content
     */
    boolean extractVideos();

    /**
     * @return true if the parser should parse embeds or not
     */
    boolean extractEmbeds();


}
