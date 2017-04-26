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

package io.square1.richtextlib.ui.iframe;

import android.view.View;
import android.webkit.WebView;

import io.square1.richtextlib.R;
import io.square1.richtextlib.v2.content.IframeDocumentElement;
import io.square1.richtextlib.v2.parser.handlers.Markers;

/**
 * Created by roberto on 24/04/2017.
 */

public class IframeHolder {

    private WrapContentWebView mWebView;

    public IframeHolder(View view){
        view.setTag(this);
        mWebView = (WrapContentWebView) view.findViewById(R.id.webView);
    }


    public void setIframe(IframeDocumentElement item) {
        mWebView.loadUrl(item.getIframeURL());
    }
}
