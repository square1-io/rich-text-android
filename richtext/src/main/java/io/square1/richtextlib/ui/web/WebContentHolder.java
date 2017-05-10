/*
 * Copyright (c) 2017. Roberto  Prato <https://github.com/robertoprato>
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

package io.square1.richtextlib.ui.web;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import io.square1.richtextlib.R;
import io.square1.richtextlib.ui.AspectRatioFrameLayout;
import io.square1.richtextlib.v2.content.WebDocumentElement;

/**
 * Created by roberto on 24/04/2017.
 */

public class WebContentHolder {

    private class WebContentClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (Uri.parse(url).getHost().equals("www.example.com")) {
//                // This is my web site, so do not override; let my WebView load the page
//                return false;
//            }
//            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//
            return true;
        }
    }

    private WrapContentWebView mWebView;
    private FrameLayout mContainer;

    public WebContentHolder(View view){
        view.setTag(this);
        mContainer = (FrameLayout)view;
        mWebView = (WrapContentWebView) view.findViewById(R.id.webView);
       // mWebView.setWebChromeClient(new WebContentClient());
        mWebView.setWebViewClient(new WebContentClient());


    }


    public void setWebContent(WebDocumentElement item) {
        //mContainer.setRatio(item.getWidth(), item.getHeight());
        if(item.getType() == WebDocumentElement.ContentType.EHtml ) {
            mWebView.loadData(item.getContent(), "text/html", "utf-8");
        }else {
            mWebView.loadUrl(item.getContent());
        }
        mWebView.setEnableResize(true);
    }
}
