package qs.com.myapplication;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

/**
 * Created by roberto on 03/12/2015.
 */
public class RichSpannable implements Spannable {

    public RichSpannable(String plainString){
        mStringStore = new SpannableStringBuilder(plainString);
        Html.fromHtml()
    }

    private SpannableStringBuilder mStringStore;

    @Override
    public void setSpan(Object what, int start, int end, int flags) {

    }

    @Override
    public void removeSpan(Object what) {

    }

    @Override
    public <T> T[] getSpans(int start, int end, Class<T> type) {
        return null;
    }

    @Override
    public int getSpanStart(Object tag) {
        return 0;
    }

    @Override
    public int getSpanEnd(Object tag) {
        return 0;
    }

    @Override
    public int getSpanFlags(Object tag) {
        return 0;
    }

    @Override
    public int nextSpanTransition(int start, int limit, Class type) {
        return 0;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return null;
    }
}
