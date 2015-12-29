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

package io.square1.richtextlib.ui;

import android.text.Layout;
import android.text.TextPaint;

/**
 * Created by roberto on 25/09/15.
 */
public class RichTextLayout extends Layout {

    /**
     * Subclasses of Layout use this constructor to set the display text,
     * width, and other standard properties.
     *
     * @param text        the text to render
     * @param paint       the default paint for the layout.  Styles can override
     *                    various attributes of the paint.
     * @param width       the wrapping width for the text.
     * @param align       whether to left, right, or center the text.  Styles can
     *                    override the alignment.
     * @param spacingMult factor by which to scale the font size to get the
     *                    default line spacing
     * @param spacingAdd  amount to add to the default line spacing
     */
    protected RichTextLayout(CharSequence text,
                             TextPaint paint,
                             int width,
                             Alignment align,
                             float spacingMult,
                             float spacingAdd) {

        super(text, paint, width, align, spacingMult, spacingAdd);
    }

    @Override
    public int getLineCount() {
        return 0;
    }

    @Override
    public int getLineTop(int line) {
        return 0;
    }

    @Override
    public int getLineDescent(int line) {
        return 0;
    }

    @Override
    public int getLineStart(int line) {
        return 0;
    }

    @Override
    public int getParagraphDirection(int line) {
        return 0;
    }

    @Override
    public boolean getLineContainsTab(int line) {
        return false;
    }

    @Override
    public Directions getLineDirections(int line) {
        return null;
    }

    @Override
    public int getTopPadding() {
        return 0;
    }

    @Override
    public int getBottomPadding() {
        return 0;
    }

    @Override
    public int getEllipsisStart(int line) {
        return 0;
    }

    @Override
    public int getEllipsisCount(int line) {
        return 0;
    }
}
