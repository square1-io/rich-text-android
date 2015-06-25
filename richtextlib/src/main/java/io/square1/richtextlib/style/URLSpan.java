package io.square1.richtextlib.style;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Browser;
import android.text.style.ClickableSpan;
import android.view.View;

import io.square1.richtextlib.ui.RichTextView;
import io.square1.richtextlib.util.UniqueId;

public class URLSpan extends ClickableSpan implements P2ParcelableSpan {

    public static final Parcelable.Creator<URLSpan> CREATOR  = P2ParcelableCreator.get(URLSpan.class);

    public static final int TYPE = UniqueId.getType();



    @Override
    public int getType() {
        return TYPE;
    }

    private  String mURL;

    public URLSpan(String url) {
        mURL = url;
    }

    public URLSpan(Parcel src) {
        readFromParcel(src);
    }
    

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        P2ParcelUtils.writeType(dest,this);
        dest.writeString(mURL);
    }

    public String getURL() {
        return mURL;
    }

    @Override
    public void onClick(View widget) {
        Uri uri = Uri.parse(getURL());
        Context context = widget.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        context.startActivity(intent);
    }

    @Override
    public void readFromParcel(Parcel in) {
        mURL = in.readString();
    }

    @Override
    public void onAttachedToView(RichTextView view) {

    }

    @Override
    public void onDetachedFromView(RichTextView view) {

    }

    @Override
    public void onSpannedSetToView(RichTextView view){

    }
}