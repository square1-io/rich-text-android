/*
 * Copyright (c) 2016. Roberto  Prato <https://github.com/robertoprato>
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

package io.square1.oembed;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.square1.oembed.Oembed;
import io.square1.richtextlib.EmbedUtils;

/**
 * Created by roberto on 17/04/15.
 */
public class OembedCache {

    public interface OembedDownloader {
        void downloadOembed(String mainUrl, String requestUrl, OembedCache cache);
    }

    public interface OembedCacheObserver {
        void OnOembedDownloaded(Oembed oembed);
    }

    public ArrayList<OembedCacheObserver> mObservers;
    private HashMap<String, Oembed> mCache;
    private OembedDownloader mOembedDownloader;

    public static OembedCache sInstance;

    public static final OembedCache initOembedCache(Context context, OembedDownloader downloader) {

        if (sInstance == null) {
            sInstance = new OembedCache(context, downloader);
        }

        return sInstance;

    }

    public static final synchronized OembedCache getInstance() {

        return sInstance;
    }


    OembedCache(Context context, OembedDownloader downloader) {
        mCache = new HashMap<>();
        mObservers = new ArrayList<>();
        mOembedDownloader = downloader;

    }

    public void addObserver(OembedCacheObserver observer) {
        synchronized (this) {
            mObservers.add(observer);
        }
    }

    public void removeObserver(OembedCacheObserver observer) {
        synchronized (this) {
            mObservers.remove(observer);
        }
    }

    public Oembed get(String url) {

        synchronized (mCache) {
            Oembed t = mCache.get(url);
            if (t == null) {
                t = getFromCache(url);
                if (t != null) {
                    mCache.put(url, t);
                }
            }

            return t;
        }
    }

    public Oembed loadOembed(Context context, String url) {

        Oembed oembed = get(url);

        if (oembed == null) {

            EmbedUtils.TEmbedType tEmbedType = EmbedUtils.getOembedType(url);

            if(tEmbedType != EmbedUtils.TEmbedType.EUnsupported) {
                String requestUrl = EmbedUtils.oembedRequestUrl(tEmbedType);
                mOembedDownloader.downloadOembed(url, requestUrl, this);
            }
        }

        return oembed;
    }

    public Oembed set(String url, Oembed embed) {

        synchronized (mCache) {
            mCache.put(url, embed);
            saveToFile(embed);
        }
        return embed;
    }


    private Oembed getFromCache(String url) {

//        try {
//            InputStream in = mDiskCache.getInputStream(url);
//            if(in == null) return null;
//            byte[] data = IOUtils.getAsByteArray(in);
//            in.close();
//            String json  = new String(data);
//            return new Oembed(json);
//        }catch (Exception exc){
//
//        }

        return null;
    }

    private void saveToFile(Oembed embed) {

        String data = embed.toString();
        try {
            //   mDiskCache.saveData(embed.getMainUrl(), data.getBytes());
        } catch (Exception exc) {

        }
    }


    public void oembedDowloaded(JSONObject json ,String mainUrl ) {

        Oembed oembed = new Oembed(mainUrl, json);
        set(oembed.getMainUrl(), oembed);
        synchronized (this){
            for(OembedCacheObserver observer : mObservers){
                observer.OnOembedDownloaded(oembed);
            }
        }
    }
}

