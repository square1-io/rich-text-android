package io.square1.richtextlib.spans;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Browser;
import android.text.Html;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.util.UniqueId;

public class URLSpan extends ClickableSpan implements io.square1.richtextlib.spans.ClickableSpan, RichTextSpan {

    public static final Parcelable.Creator<URLSpan> CREATOR  = DynamicParcelableCreator.getInstance(URLSpan.class);

    public static final int TYPE = UniqueId.getType();



    @Override
    public int getType() {
        return TYPE;
    }

    private  String mURL;

    public URLSpan(){}

    public URLSpan(String url) {

        mURL = url;
        if(TextUtils.isEmpty(mURL) == true){
            mURL = Uri.EMPTY.toString();
        }
    }

    public URLSpan(Parcel src) {
        readFromParcel(src);
    }
    

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        DynamicParcelableCreator.writeType(dest, this);
        dest.writeString(mURL);
    }

    public String getURL() {
        return mURL;
    }

    @Override
    public void onClick(View view) {
        Uri uri = Uri.parse(getURL());
        Context context = view.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        context.startActivity(intent);
    }

    @Override
    public void readFromParcel(Parcel in) {
        mURL = in.readString();
    }

    @Override
    public void onAttachedToView(RichContentViewDisplay view) {

    }

    @Override
    public void onDetachedFromView(RichContentViewDisplay view) {

    }

    @Override
    public void onSpannedSetToView(RichContentViewDisplay view){

    }
}