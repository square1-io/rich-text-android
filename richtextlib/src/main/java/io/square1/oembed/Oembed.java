package io.square1.oembed;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by roberto on 30/09/15.
 */
public class Oembed implements Parcelable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Oembed oembed = (Oembed) o;
        return mMainUrl.equals(oembed.mMainUrl);
    }

    @Override
    public int hashCode() {
        return mMainUrl.hashCode();
    }

    private static final  String JSON_MAIN_URL = "JSON_MAIN_URL.dsa";

    public  String mMainUrl;
    private JSONObject mData;

    public Oembed(String url, JSONObject data){
        mMainUrl = url;
        mData = data;

        try {
            mData.putOpt(JSON_MAIN_URL, mMainUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Oembed(String data){
        try {
            mData = new JSONObject(data);
            mMainUrl = mData.optString(JSON_MAIN_URL);
        }catch (Exception e){

        }

    }

    protected Oembed(Parcel in) {
        try {
            mData = new JSONObject(in.readString());
            mMainUrl = mData.optString(JSON_MAIN_URL);
        }catch (Exception ex){
            throw new IllegalArgumentException();
        }
    }


    public static final Creator<Oembed> CREATOR = new Creator<Oembed>() {
        @Override
        public Oembed createFromParcel(Parcel in) {
            return new Oembed(in);
        }

        @Override
        public Oembed[] newArray(int size) {
            return new Oembed[size];
        }
    };

    public String getThumbnailUrl(){
        return mData.optString("thumbnail_url");
    }

    public String getThumbNailWidth(){
        return mData.optString("thumbnail_width");
    }

    public String getThumbNailHeight(){
        return mData.optString("thumbnail_height");
    }

    public String getHtml(){
        return mData.optString("html");
    }

    public String getMainUrl(){
        return mMainUrl;
    }

    public String getProviderName(){
        return mData.optString("provider_name");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mData.toString());
    }

    @Override
    public String toString(){
        return mData.toString();
    }
}
