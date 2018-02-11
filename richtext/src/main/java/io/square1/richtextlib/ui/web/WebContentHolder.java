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
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import io.square1.richtextlib.R;
import io.square1.richtextlib.ui.AspectRatioFrameLayout;
import io.square1.richtextlib.v2.content.WebDocumentElement;

/**
 * Created by roberto on 24/04/2017.
 */

public class WebContentHolder {

    private class WebContentClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);

            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);

            if (mProgressBar != null) {
                view.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
            //only if is enabled
            mWebView.resize();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            
            //let WebView load the page return false;

            if (!mShouldOverrideUrlLoading) {
                WebContentHolder.this.shouldOverrideUrlLoading(view, url);
            }

            return mShouldOverrideUrlLoading;
        }
    }

    protected void shouldOverrideUrlLoading(WebView view, String url) {

    }

    private WrapContentWebView mWebView;
    private ProgressBar mProgressBar;
    private Boolean mShouldOverrideUrlLoading;

    public WebContentHolder(View view) {

        view.setTag(this);
        mWebView = (WrapContentWebView) view.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mWebView.setWebViewClient(new WebContentClient());
        mShouldOverrideUrlLoading = true;
    }


    public final WrapContentWebView getWebView(){
        return mWebView;
    }


    public void setWebContent(WebDocumentElement item) {

        clearContent();

        if (item.getType() == WebDocumentElement.ContentType.EHtml) {
            mWebView.loadData(item.getContent(), "text/html", "utf-8");
        }
        else {
            mWebView.loadUrl(item.getContent());
        }
    }

    public void clearContent() {

        mWebView.loadUrl("about:blank");
    }

    public void resize() {

        mWebView.resize();
    }

    public void setShouldOverrideUrlLoading(boolean shouldOverrideUrlLoading) {

        mShouldOverrideUrlLoading = shouldOverrideUrlLoading;
    }

    public void setEnableResize(boolean enableResize) {

        mWebView.setEnableResize(enableResize);
    }

    public void reload() {
        mWebView.reload();
    }


}
