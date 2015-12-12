package qs.com.myapplication.spans;

import android.os.Parcel;
import android.text.style.URLSpan;

/**
 * Created by roberto on 03/12/2015.
 */
public class RichURLSpan extends URLSpan implements RichSpan {

    public RichURLSpan() {
        super("");
    }
    public RichURLSpan(String url) {
        super(url);
    }

    @Override
    public void readFromParcel(Parcel input) {

    }
}
