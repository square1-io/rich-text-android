package io.square1.richtextlib.spans;

import android.net.Uri;

/**
 * Created by roberto on 30/06/15.
 */
public interface UrlBitmapDownloader {
    void downloadImage(RemoteBitmapSpan urlBitmapSpan, Uri image);
}
