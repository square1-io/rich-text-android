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

package io.square1.richtext.io.square1.richtext.sample;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bumptech.glide.Glide;

import io.square1.richtext.R;
import io.square1.richtextlib.spans.RemoteBitmapSpan;
import io.square1.richtextlib.spans.UrlBitmapDownloader;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.content.RichDocument;

/**
 * A placeholder fragment containing a simple view.
 */
public  class ContentFragment extends Fragment implements UrlBitmapDownloader {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SAMPLE_FILE_NAME = "sample_file_name";

    private ListView mListView;
    private ContentAdapter mContentAdapter;


    public ContentFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ContentFragment newInstance(String sampleFileName) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SAMPLE_FILE_NAME, sampleFileName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView)rootView.findViewById(R.id.list);
        mListView.setAdapter(mContentAdapter);
        mListView.setScrollingCacheEnabled(false);
        mListView.setDrawingCacheEnabled(false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContentAdapter = new ContentAdapter(this);
        String sampleFileName = getArguments().getString(ARG_SAMPLE_FILE_NAME);
        ((MainActivity) activity).onSectionAttached(sampleFileName);
         new ParseContentTask(activity).execute(sampleFileName);

    }

    @Override
    public void downloadImage(RemoteBitmapSpan urlBitmapSpan, Uri image) {
        Glide.with(getActivity())
                .load(image)
                .into(new GlideTarget(getActivity(),urlBitmapSpan));
    }


    private class ParseContentTask extends AsyncTask<String,Integer,RichDocument> {

        private Context mApplicationContext;
        private String mHtml;

        public ParseContentTask(Context context){
            super();
            mApplicationContext = context.getApplicationContext();
        }

        @Override
        protected RichDocument doInBackground(String... sampleFileName) {
            mHtml = Utils.readFromfile(mApplicationContext, sampleFileName[0]);
            RichDocument result = RichTextV2.fromHtml(mApplicationContext, mHtml);
            return result;
        }

        @Override
        protected void onPostExecute(RichDocument richDocument) {
            super.onPostExecute(richDocument);
            mContentAdapter.setDocument(richDocument);
        }
    }


}