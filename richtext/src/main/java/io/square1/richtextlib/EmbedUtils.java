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

package io.square1.richtextlib;

import android.net.Uri;
import android.text.TextUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by roberto on 12/06/15.
 */
public class EmbedUtils {


    public final static String SOUND_CLOUD_CLIENT_ID = "1f6456941b1176c22d44fb16ec2015a2";
    public static final String OEMBED_API_INSTAGRAM = "https://api.instagram.com/oembed";
    public static final String OEMBED_API_VINE = "https://vine.co/oembed.json";
    public static final String OEMBED_API_VIMEO = "https://vimeo.com/api/oembed.json";

    public static String oembedRequestUrl(TEmbedType type) {

        switch (type){
            case EInstagram:
                return OEMBED_API_INSTAGRAM;
            case EVimeo:
                return OEMBED_API_VIMEO;
            case EVine:
                return OEMBED_API_VINE;
            default:
                return "";
        }

    }


    public interface ParseLinkCallback {
         void onLinkParsed(Object callingObject, String result, TEmbedType type);
    }

   public enum TEmbedType {
        EYoutube,
        EGfycat,
        ESoundCloud,
        ETwitter,
        EVimeo,
        EVine,
        EInstagram,
       EFacebook,
       EAudio,
       EUnsupported
    }



    public static boolean parseLink( Object calling , String link , ParseLinkCallback callback ) {



        final Uri uri = Uri.parse(link);

        String result = getTweetId(link);
        if(TextUtils.isEmpty(result) == false){
             callback.onLinkParsed(calling, result, TEmbedType.ETwitter);
             return true;
        }

        result = getYoutubeVideoId(link);
        if(TextUtils.isEmpty(result) == false){
            callback.onLinkParsed(calling, result, TEmbedType.EYoutube);
            return true;
        }

        result = getGfycatId(link);
        if(TextUtils.isEmpty(result) == false){
            callback.onLinkParsed(calling, result, TEmbedType.EGfycat);
            return true;
        }

        result = parseSoundCloud(link);
        if(TextUtils.isEmpty(result) == false){
            result = getSoundCloudStreamFromTrackId(result,SOUND_CLOUD_CLIENT_ID);
            callback.onLinkParsed(calling, result, TEmbedType.ESoundCloud);
            return true;
        }

        result = getVimeoId(link);
        if(TextUtils.isEmpty(result) == false){
            callback.onLinkParsed(calling, result, TEmbedType.EVimeo);
            return true;
        }

        result = getVineId(link);
        if(TextUtils.isEmpty(result) == false){
            callback.onLinkParsed(calling, result, TEmbedType.EVine);
            return true;
        }

        result = getInstagramId(link);
        if(TextUtils.isEmpty(result) == false){
            callback.onLinkParsed(calling, result, TEmbedType.EInstagram);
            return true;
        }


        return false;
       // callback.onLinkParsed(link,TLinkType.EUnsupported);

    }

    /**
     *
     * @param baseURL
     * @param key
     * @return the soundcloud track id
     */
    public static String parseSoundCloud(String baseURL){

        Uri parsed = Uri.parse(baseURL);
        String authority = parsed.getAuthority();
        String trackURL = null;
        if(TextUtils.isEmpty(authority)){
            return null;
        }
        if(authority.indexOf("api.soundcloud") >= 0 ){
            trackURL = baseURL;
        }else if(authority.indexOf("soundcloud") >= 0){
            Uri parsedURI = Uri.parse(baseURL);
            try {
                trackURL =  parsedURI.getQueryParameter("url");
            }catch (Exception exc){

            }
        }

        if(TextUtils.isEmpty(trackURL) == true){
            return null;
        }

        Uri base = Uri.parse(trackURL);

        List<String> segments =  base.getPathSegments();
        for(String segment : segments){
            if(TextUtils.isDigitsOnly(segment) == true){
                return segment;// String.format("https://api.soundcloud.com/tracks/%1$s/stream?consumer_key=%2$s",segment,key);
            }
        }

        return "";
    }

    public static String getTweetId(String in){


        if(TextUtils.isEmpty(in)){
            return null;
        }



        if(in.indexOf("twitter") >= 0 || in.indexOf("t.co") >=0 ){

            Uri tweet = Uri.parse(in);

            String lastPath = tweet.getLastPathSegment();
            if( TextUtils.isEmpty(lastPath) == false
                    && TextUtils.isDigitsOnly(lastPath) ){
                return lastPath;
            }
        }
        return null;
    }


    public static String getGfycatId(String in){

        Uri uri = Uri.parse(in);

        final String host = uri.getHost();

        if( TextUtils.isEmpty(host) == false &&
                host.endsWith("gfycat.com") == true){
            List<String> paths =  uri.getPathSegments();
            if(paths.size() == 1){
                return paths.get(0);
            }
            else if(paths.size() == 2){
                return paths.get(1);
            }
        }

        return null;
    }

    public static String getSoundCloudStream(String baseUrl,String clientId){
        //196567484
        final Uri uri = Uri.parse(baseUrl);
        final List<String> segments = uri.getPathSegments();
        String id = null;
        for(int index = segments.size() - 1; index >= 0; index --){

            if(TextUtils.isDigitsOnly(segments.get(index))){
                id = segments.get(index);
                break;
            }
        }


        return getSoundCloudStreamFromTrackId(id, clientId);
    }

    public static String getSoundCloudStreamFromTrackId(String trackId, String clientId){
        //196567484

        if(TextUtils.isEmpty(trackId) == false){
            return String.format("https://api.soundcloud.com/tracks/%1$s/stream?consumer_key=%2$s", trackId, clientId);
        }

        return null;
    }

    public static String getYoutubeVideoId(String in){

        //*EDIT* - fixed to hopefully support more recent youtube link styles/formats:
        final String pattern = "(?<=watch\\?v=|/videos/|/embed/|youtu.be/)[^&#?]*";
        final String pattern2 = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";


        Pattern compiledPattern = Pattern.compile(pattern2, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(in);

        if(matcher.find()){
            return matcher.group(1);
        }

        return null;
    }

    public static TEmbedType getOembedType(String link){


        String result = getVimeoId(link);
        if(TextUtils.isEmpty(result) == false){
            return TEmbedType.EVimeo;
        }

        result = getVineId(link);
        if(TextUtils.isEmpty(result) == false){
            return TEmbedType.EVine;
        }

        result = getInstagramId(link);
        if(TextUtils.isEmpty(result) == false){
            return TEmbedType.EInstagram;
        }

        return TEmbedType.EUnsupported;

    }

    public static String getVineId(String in){

        Uri uri = Uri.parse(in);

        final String host = uri.getHost();

        if( TextUtils.isEmpty(host) == false &&
                host.indexOf("vine") >= 0){
            return in;
        }

        return null;
    }

    public static String getVimeoId(String in){

        Uri uri = Uri.parse(in);

        final String host = uri.getHost();

        if( TextUtils.isEmpty(host) == false &&
                host.indexOf("vimeo") >= 0){
            return in;
        }

        return null;
    }

    public static String getInstagramId(String in){

        Uri uri = Uri.parse(in);

        final String host = uri.getHost();

        if( TextUtils.isEmpty(host) == false &&
                host.indexOf("instagram") >= 0){
            return in;
        }

        return null;
    }


    public static String getYoutubeThumbnailUrl(String videoId){
        return  String.format("http://img.youtube.com/vi/%s/hqdefault.jpg", videoId);
    }


}
