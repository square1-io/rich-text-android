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

    public interface ParseLinkCallback {
         void onLinkParsed(Object callingObject, String result, TEmbedType type);
    }

   public enum TEmbedType {
        EYoutube,
        EGfycat,
        ESoundCloud,
        ETwitter,
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

        return false;
       // callback.onLinkParsed(link,TLinkType.EUnsupported);

    }

    public static String parseSoundCloud(String baseURL , String key){

        Uri base = Uri.parse(baseURL);

        List<String> segments =  base.getPathSegments();
        for(String segment : segments){
            if(TextUtils.isDigitsOnly(segment) == true){
                return String.format("https://api.soundcloud.com/tracks/%1$s/stream?consumer_key=%2$s",segment,key);
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
        for(int index = segments.size() - 1; index == 0; index --){

            if(TextUtils.isDigitsOnly(segments.get(index))){
                id = segments.get(index);
                break;
            }
        }

        if(TextUtils.isEmpty(id) == false){
            return String.format("https://api.soundcloud.com/tracks/%1$s/stream?consumer_key=%2$s", id, clientId);
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


    public static String getYoutubeThumbnailUrl(String videoId){
        return  String.format("http://img.youtube.com/vi/%s/hqdefault.jpg", videoId);
    }


}
