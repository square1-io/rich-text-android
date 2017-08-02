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

package io.square1.richtext.io.square1.richtext.sample;


import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.square1.parcelable.ParcelableUtil;
import io.square1.richtext.R;
import io.square1.richtextlib.spans.ClickableSpan;
import io.square1.richtextlib.spans.RemoteBitmapSpan;
import io.square1.richtextlib.spans.UrlBitmapDownloader;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.content.RichDocument;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;

/**
 * A simple {@link Fragment} subclass.
 */
public class HtmlParserExampleFragment extends Fragment {


    private  RichContentView mContentView;

    public HtmlParserExampleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text_builder_example, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContentView = (RichContentView)view.findViewById(R.id.richTextView);

        mContentView.setOnSpanClickedObserver(new RichContentViewDisplay.OnSpanClickedObserver() {
            @Override
            public boolean onSpanClicked(ClickableSpan span) {
                String action = span.getAction();
                action = TextUtils.isEmpty(action) ? " no action" : action;
                Toast.makeText(getContext(), action, Toast.LENGTH_LONG).show();
                return true;
            }
        });

        mContentView.setUrlBitmapDownloader(new UrlBitmapDownloader() {

            @Override
            public void downloadImage(RemoteBitmapSpan urlBitmapSpan, Uri image) {
                    Glide.with(getActivity())
                            .load(image)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(new GlideTarget(getActivity(),urlBitmapSpan));
                }

        });

        Uri html5File = new Uri.Builder()
                .scheme("file")
                .appendPath(NavigationDrawerFragment.SAMPLES_FOLDER)
                .appendEncodedPath("html5.html")
                .build();

        new ParseContentTask(getContext()).execute(html5File);
    }

    private class ParseContentTask extends AsyncTask<Uri,Integer, RichTextDocumentElement> {

        private Context mApplicationContext;
        private String mHtml;

        public ParseContentTask(Context context){
            super();
            mApplicationContext = context.getApplicationContext();
        }

        @Override
        protected RichTextDocumentElement doInBackground(Uri... sampleFileName) {
            mHtml = Utils.readFromfile(mApplicationContext, sampleFileName[0]);
            RichTextDocumentElement result = RichTextV2.textFromHtml(mApplicationContext, mHtml);
            return result;
        }

        @Override
        protected void onPostExecute(RichTextDocumentElement content) {
            mContentView.setText(content);
        }
    }
}
