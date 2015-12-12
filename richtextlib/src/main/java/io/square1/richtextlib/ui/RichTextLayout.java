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
