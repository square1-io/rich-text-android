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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.square1.richtext.R;
import io.square1.richtextlib.spans.RemoteBitmapSpan;
import io.square1.richtextlib.spans.UrlBitmapDownloader;
import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.content.RichDocument;

/**
 * A simple {@link Fragment} subclass.
 */
public class HtmlParserSplitElementsExampleFragment extends Fragment {


    private ListView mListContentView;
    private ContentAdapter mAdapter;

    public HtmlParserSplitElementsExampleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mAdapter != null){
            mAdapter.destroy();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListContentView = (ListView) view.findViewById(R.id.list);

        mAdapter = new ContentAdapter(new UrlBitmapDownloader() {

            @Override
            public void downloadImage(RemoteBitmapSpan urlBitmapSpan, Uri image) {
                Glide.with(getActivity())
                        .load(image)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(new GlideTarget(getActivity(),urlBitmapSpan));
            }
        }, getContext());

        mListContentView.setAdapter(mAdapter);

        Uri html5File = new Uri.Builder()
                .scheme("file")
                .appendPath(NavigationDrawerFragment.SAMPLES_FOLDER)
                .appendEncodedPath("test.html")
                .build();

        new ParseContentTask(getContext()).execute(html5File);
    }

    private class ParseContentTask extends AsyncTask<Uri,Integer, RichDocument> {

        private Context mApplicationContext;
        private String mHtml;

        public ParseContentTask(Context context){
            super();
            mApplicationContext = context.getApplicationContext();
        }

        @Override
        protected RichDocument doInBackground(Uri... sampleFileName) {
            mHtml = Utils.readFromfile(mApplicationContext, sampleFileName[0]);

            RichDocument result = RichTextV2.fromHtml(mApplicationContext,
                    mHtml, new RichTextV2.DefaultStyle(mApplicationContext) {

                @Override
                public boolean extractImages(){
                    return true;
                }

            });

            return result;
        }

        @Override
        protected void onPostExecute(RichDocument content) {
            mAdapter.setDocument(content);
        }
    }
}
